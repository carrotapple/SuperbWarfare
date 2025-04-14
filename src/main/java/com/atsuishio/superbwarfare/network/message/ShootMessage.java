package com.atsuishio.superbwarfare.network.message;

import com.atsuishio.superbwarfare.event.GunEventHandler;
import com.atsuishio.superbwarfare.init.ModItems;
import com.atsuishio.superbwarfare.init.ModPerks;
import com.atsuishio.superbwarfare.init.ModSounds;
import com.atsuishio.superbwarfare.init.ModTags;
import com.atsuishio.superbwarfare.item.gun.GunItem;
import com.atsuishio.superbwarfare.item.gun.data.GunData;
import com.atsuishio.superbwarfare.network.ModVariables;
import com.atsuishio.superbwarfare.perk.AmmoPerk;
import com.atsuishio.superbwarfare.perk.Perk;
import com.atsuishio.superbwarfare.tools.InventoryTool;
import com.atsuishio.superbwarfare.tools.ParticleTool;
import com.atsuishio.superbwarfare.tools.SoundTool;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ShootMessage {

    private final double spread;
    private final boolean zoom;

    public ShootMessage(double spread, boolean zoom) {
        this.spread = spread;
        this.zoom = zoom;
    }

    public static ShootMessage decode(FriendlyByteBuf buffer) {
        return new ShootMessage(buffer.readDouble(), buffer.readBoolean());
    }

    public static void encode(ShootMessage message, FriendlyByteBuf buffer) {
        buffer.writeDouble(message.spread);
        buffer.writeBoolean(message.zoom);
    }

    public static void handler(ShootMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            if (context.getSender() != null) {
                pressAction(context.getSender(), message.spread, message.zoom);
            }
        });
        context.setPacketHandled(true);
    }

    public static void pressAction(Player player, double spared, boolean zoom) {
        ItemStack stack = player.getMainHandItem();
        if (!(stack.getItem() instanceof GunItem)) return;
        var data = GunData.from(stack);
        var tag = data.data();

        if (stack.is(ModTags.Items.NORMAL_GUN)) {
            int projectileAmount = data.projectileAmount();

            if (data.ammo.get() > 0) {
                // 空仓挂机
                if (data.ammo.get() == 1) {
                    data.holdOpen.set(true);
                }

                if (stack.is(ModTags.Items.REVOLVER)) {
                    data.canImmediatelyShoot.set(true);
                }

                // 判断是否为栓动武器（BoltActionTime > 0），并在开火后给一个需要上膛的状态
                if (data.defaultActionTime() > 0 && data.ammo.get() > (stack.is(ModTags.Items.REVOLVER) ? 0 : 1)) {
                    data.bolt.needed.set(true);
                }

                data.ammo.set(data.ammo.get() - 1);
                data.isEmpty.set(true);

                if (stack.getItem() == ModItems.M_60.get() && data.ammo.get() <= 5) {
                    data.hideBulletChain.set(true);
                }

                if (stack.getItem() == ModItems.HOMEMADE_SHOTGUN.get()) {
                    stack.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(InteractionHand.MAIN_HAND));
                    if (player instanceof ServerPlayer serverPlayer && player.level() instanceof ServerLevel serverLevel) {
                        ParticleTool.sendParticle(serverLevel, ParticleTypes.CLOUD, player.getX() + 1.8 * player.getLookAngle().x, player.getY() + player.getBbHeight() - 0.1 + 1.8 * player.getLookAngle().y,
                                player.getZ() + 1.8 * player.getLookAngle().z, 30, 0.4, 0.4, 0.4, 0.005, true, serverPlayer);
                    }
                }

                if (stack.getItem() == ModItems.SENTINEL.get()) {
                    stack.getCapability(ForgeCapabilities.ENERGY).ifPresent(
                            iEnergyStorage -> iEnergyStorage.extractEnergy(3000, false)
                    );
                }

                var perk = data.perk.get(Perk.Type.AMMO);

                for (int index0 = 0; index0 < (perk instanceof AmmoPerk ammoPerk && ammoPerk.slug ? 1 : projectileAmount); index0++) {
                    GunEventHandler.gunShoot(player, data, spared, zoom);
                }

                GunEventHandler.playGunSounds(player, zoom);
            }
        } else if (stack.is(ModItems.MINIGUN.get())) {
            var cap = player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables());

            if (cap.rifleAmmo > 0 || InventoryTool.hasCreativeAmmoBox(player)) {
                tag.putDouble("heat", (tag.getDouble("heat") + 0.1));
                if (tag.getDouble("heat") >= 50.5) {
                    tag.putDouble("overheat", 40);
                    player.getCooldowns().addCooldown(stack.getItem(), 40);
                    if (!player.level().isClientSide() && player instanceof ServerPlayer serverPlayer) {
                        SoundTool.playLocalSound(serverPlayer, ModSounds.MINIGUN_OVERHEAT.get(), 2f, 1f);
                    }
                }

                var perk = data.perk.get(Perk.Type.AMMO);
                float pitch = tag.getDouble("heat") <= 40 ? 1 : (float) (1 - 0.025 * Math.abs(40 - tag.getDouble("heat")));

                if (!player.level().isClientSide() && player instanceof ServerPlayer) {
                    float soundRadius = (float) data.soundRadius();

                    player.playSound(ModSounds.MINIGUN_FIRE_3P.get(), soundRadius * 0.2f, pitch);
                    player.playSound(ModSounds.MINIGUN_FAR.get(), soundRadius * 0.5f, pitch);
                    player.playSound(ModSounds.MINIGUN_VERYFAR.get(), soundRadius, pitch);

                    if (perk == ModPerks.BEAST_BULLET.get()) {
                        player.playSound(ModSounds.HENG.get(), 4f, pitch);
                    }
                }

                GunEventHandler.gunShoot(player, data, spared, false);
                if (!InventoryTool.hasCreativeAmmoBox(player)) {
                    cap.rifleAmmo = cap.rifleAmmo - 1;
                    cap.sync(player);
                }
            }
        }
    }
}
