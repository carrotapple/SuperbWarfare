package net.mcreator.superbwarfare.network.message;

import net.mcreator.superbwarfare.init.ModItems;
import net.mcreator.superbwarfare.init.ModTags;
import net.mcreator.superbwarfare.network.ModVariables;
import net.mcreator.superbwarfare.tools.ParticleTool;
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
    private final int type;

    public ShootMessage(int type) {
        this.type = type;
    }

    public static ShootMessage decode(FriendlyByteBuf buffer) {
        return new ShootMessage(buffer.readInt());
    }

    public static void encode(ShootMessage message, FriendlyByteBuf buffer) {
        buffer.writeInt(message.type);
    }

    public static void handler(ShootMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            if (context.getSender() != null) {
                pressAction(context.getSender(), message.type);
            }
        });
        context.setPacketHandled(true);
    }

    public static void pressAction(Player player, int type) {
        Level level = player.level();

        if (!level.isLoaded(player.blockPosition())) {
            return;
        }

        ItemStack stack = player.getMainHandItem();
        if (stack.is(ModTags.Items.NORMAL_GUN)) {

            double mode = stack.getOrCreateTag().getInt("fire_mode");
            int interval = stack.getOrCreateTag().getInt("fire_interval");

            if ((player.getPersistentData().getBoolean("holdFire") || stack.getOrCreateTag().getInt("burst_fire") > 0)
                    && !(stack.getOrCreateTag().getBoolean("is_normal_reloading") || stack.getOrCreateTag().getBoolean("is_empty_reloading"))
                    && !stack.getOrCreateTag().getBoolean("reloading")
                    && !stack.getOrCreateTag().getBoolean("charging")
                    && stack.getOrCreateTag().getInt("ammo") > 0
                    && !player.getCooldowns().isOnCooldown(stack.getItem())
                    && !stack.getOrCreateTag().getBoolean("need_bolt_action")) {

                if (mode == 0) {
                    player.getPersistentData().putBoolean("holdFire", false);
                }

                int burstCooldown = 0;
                if (mode == 1) {
                    player.getPersistentData().putBoolean("holdFire", false);
                    stack.getOrCreateTag().putInt("burst_fire", (stack.getOrCreateTag().getInt("burst_fire") - 1));
                    burstCooldown = stack.getOrCreateTag().getInt("burst_fire") == 0 ? interval + 4 : 0;
                }

                if (stack.getOrCreateTag().getDouble("animindex") == 1) {
                    stack.getOrCreateTag().putDouble("animindex", 0);
                } else {
                    stack.getOrCreateTag().putDouble("animindex", 1);
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
                stack.getOrCreateTag().putInt("fire_animation", interval);
                player.getPersistentData().putInt("noRun_time", interval + 2);
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

                int actionInterval = 0;

                if (stack.getItem() == ModItems.MARLIN.get() || stack.getItem() == ModItems.M_870.get()) {
                    actionInterval = stack.getOrCreateTag().getInt("fire_interval");
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

                int cooldown = burstCooldown + actionInterval + customCoolDown;
                player.getCooldowns().addCooldown(stack.getItem(), cooldown);

                for (int index0 = 0; index0 < (int) stack.getOrCreateTag().getDouble("projectile_amount"); index0++) {
                    gunShoot(player);
                }
                playGunSounds(player);

                stack.getOrCreateTag().putBoolean("shoot", true);

            }
        }
    }
}
