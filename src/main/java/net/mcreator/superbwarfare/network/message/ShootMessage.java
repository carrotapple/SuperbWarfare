package net.mcreator.superbwarfare.network.message;

import net.mcreator.superbwarfare.init.ModItems;
import net.mcreator.superbwarfare.init.ModPerks;
import net.mcreator.superbwarfare.init.ModSounds;
import net.mcreator.superbwarfare.init.ModTags;
import net.mcreator.superbwarfare.network.ModVariables;
import net.mcreator.superbwarfare.perk.AmmoPerk;
import net.mcreator.superbwarfare.perk.Perk;
import net.mcreator.superbwarfare.perk.PerkHelper;
import net.mcreator.superbwarfare.tools.ParticleTool;
import net.mcreator.superbwarfare.tools.SoundTool;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

import static net.mcreator.superbwarfare.event.GunEventHandler.gunShoot;
import static net.mcreator.superbwarfare.event.GunEventHandler.playGunSounds;

public class ShootMessage {
    private final double spread;

    public ShootMessage(double spread) {
        this.spread = spread;
    }

    public static ShootMessage decode(FriendlyByteBuf buffer) {
        return new ShootMessage(buffer.readDouble());
    }

    public static void encode(ShootMessage message, FriendlyByteBuf buffer) {
        buffer.writeDouble(message.spread);
    }

    public static void handler(ShootMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            if (context.getSender() != null) {
                pressAction(context.getSender(), message.spread);
            }
        });
        context.setPacketHandled(true);
    }

    public static void pressAction(Player player, double spared) {
        Level level = player.level();

        if (!level.isLoaded(player.blockPosition())) {
            return;
        }

        ItemStack stack = player.getMainHandItem();
        if (stack.is(ModTags.Items.NORMAL_GUN)) {

            double rpm = stack.getOrCreateTag().getDouble("rpm") + stack.getOrCreateTag().getInt("customRpm");;

            int coolDownTick = (int) Math.ceil(20 / (rpm / 60));
            int mode = stack.getOrCreateTag().getInt("fire_mode");

            int projectileAmount = (int) stack.getOrCreateTag().getDouble("projectile_amount");

            if (stack.getOrCreateTag().getInt("ammo") > 0) {

                int singleInterval = 0;
                int burstCooldown = 0;

                if (mode == 0) {
                    singleInterval = coolDownTick;
                } else if (mode == 1) {
                    burstCooldown = stack.getOrCreateTag().getInt("burst_fire") == 0 ? coolDownTick + 3 : 0;
                }

                /*
                  空仓挂机
                 */
                if (stack.getOrCreateTag().getInt("ammo") == 1) {
                    stack.getOrCreateTag().putBoolean("HoldOpen", true);
                }

                /*
                  判断是否为栓动武器（bolt_action_time > 0），并在开火后给一个需要上膛的状态
                 */
                if (stack.getOrCreateTag().getDouble("bolt_action_time") > 0 && stack.getOrCreateTag().getInt("ammo") > 1) {
                    stack.getOrCreateTag().putBoolean("need_bolt_action", true);
                }

                stack.getOrCreateTag().putInt("ammo", (stack.getOrCreateTag().getInt("ammo") - 1));
                stack.getOrCreateTag().putInt("fire_animation", coolDownTick);
                stack.getOrCreateTag().putDouble("flash_time", 2);

                stack.getOrCreateTag().putDouble("empty", 1);

                if (stack.getItem() == ModItems.M_60.get()) {
                    stack.getOrCreateTag().putBoolean("bullet_chain", true);
                }

                if (stack.getItem() == ModItems.DEVOTION.get()) {
                    stack.getOrCreateTag().putInt("customRpm", Mth.clamp(stack.getOrCreateTag().getInt("customRpm") + 20, 0, 500));
                }

                if (stack.getItem() == ModItems.ABEKIRI.get()) {
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
                    stack.getOrCreateTag().putDouble("chamber_rot", 20);
                }

                int customCoolDown = 0;

                if (stack.getItem() == ModItems.MARLIN.get()) {
                    if ((player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables())).zoom) {
                        stack.getOrCreateTag().putDouble("marlin_animation_time", 15);
                        stack.getOrCreateTag().putBoolean("fastfiring", false);
                        customCoolDown = 5;
                    } else {
                        stack.getOrCreateTag().putDouble("marlin_animation_time", 10);
                        stack.getOrCreateTag().putBoolean("fastfiring", true);
                    }
                }

                int cooldown = burstCooldown + singleInterval + customCoolDown;
                player.getCooldowns().addCooldown(stack.getItem(), cooldown);

                var perk = PerkHelper.getPerkByType(stack, Perk.Type.AMMO);

                for (int index0 = 0; index0 < (perk instanceof AmmoPerk ammoPerk && ammoPerk.slug ? 1 : projectileAmount); index0++) {
                    gunShoot(player, spared);
                }

                playGunSounds(player);
            }
        } else if (stack.is(ModItems.MINIGUN.get())) {
            var tag = stack.getOrCreateTag();

            int projectileAmount = (int) tag.getDouble("projectile_amount");

            if ((player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables())).rifleAmmo > 0
                    || player.getInventory().hasAnyMatching(s -> s.is(ModItems.CREATIVE_AMMO_BOX.get()))) {
                tag.putDouble("heat", (tag.getDouble("heat") + 0.5));
                if (tag.getDouble("heat") >= 50.5) {
                    tag.putDouble("overheat", 40);
                    player.getCooldowns().addCooldown(stack.getItem(), 40);
                    if (!player.level().isClientSide() && player instanceof ServerPlayer serverPlayer) {
                        SoundTool.playLocalSound(serverPlayer, ModSounds.MINIGUN_OVERHEAT.get(), 2f, 1f);
                    }
                }
                var perk = PerkHelper.getPerkByType(stack, Perk.Type.AMMO);
                float pitch = tag.getDouble("heat") <= 40 ? 1 : (float) (1 - 0.025 * Math.abs(40 - tag.getDouble("heat")));

                if (!player.level().isClientSide() && player instanceof ServerPlayer serverPlayer) {
                    player.playSound(ModSounds.MINIGUN_FIRE_3P.get(), (float) stack.getOrCreateTag().getDouble("SoundRadius") * 0.2f, pitch);
                    player.playSound(ModSounds.MINIGUN_FAR.get(), (float) stack.getOrCreateTag().getDouble("SoundRadius") * 0.5f, pitch);
                    player.playSound(ModSounds.MINIGUN_VERYFAR.get(), (float) stack.getOrCreateTag().getDouble("SoundRadius"), pitch);

                    if (perk == ModPerks.BEAST_BULLET.get()) {
                        player.playSound(ModSounds.HENG.get(), 4f, pitch);
                    }
                }

                for (int index0 = 0; index0 < (perk instanceof AmmoPerk ammoPerk && ammoPerk.slug ? 1 : projectileAmount); index0++) {
                    gunShoot(player, spared);
                }

                if (!player.getInventory().hasAnyMatching(s -> s.is(ModItems.CREATIVE_AMMO_BOX.get()))) {
                    player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                        capability.rifleAmmo = player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables()).rifleAmmo - 1;
                        capability.syncPlayerVariables(player);
                    });
                }

                tag.putInt("fire_animation", 2);
            }
        }
    }
}
