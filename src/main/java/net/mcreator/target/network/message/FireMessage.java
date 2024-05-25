package net.mcreator.target.network.message;

import net.mcreator.target.entity.BocekArrowEntity;
import net.mcreator.target.entity.ProjectileEntity;
import net.mcreator.target.init.TargetModItems;
import net.mcreator.target.init.TargetModSounds;
import net.mcreator.target.network.TargetModVariables;
import net.mcreator.target.procedures.PressFireProcedure;
import net.mcreator.target.tools.SoundTool;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class FireMessage {
    private final int type;

    public FireMessage(int type) {
        this.type = type;
    }

    public FireMessage(FriendlyByteBuf buffer) {
        this.type = buffer.readInt();
    }

    public static void buffer(FireMessage message, FriendlyByteBuf buffer) {
        buffer.writeInt(message.type);
    }

    public static void handler(FireMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            if (context.getSender() != null) {
                pressAction(context.getSender(), message.type);
            }
        });
        context.setPacketHandled(true);
    }

    public static void pressAction(Player player, int type) {
        Level world = player.level();

        if (!world.isLoaded(player.blockPosition())) {
            return;
        }

        if (type == 0) {
            PressFireProcedure.execute(player);
        } else if (type == 1) {
            player.getPersistentData().putBoolean("firing", false);
            player.getPersistentData().putDouble("minigun_firing", 0);
            player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                capability.bowPullHold = false;
                capability.syncPlayerVariables(player);
            });

            handleBowShoot(player);
        }
    }

    private static void handleBowShoot(Player player) {
        ItemStack stack = player.getMainHandItem();

        if (stack.getItem() != TargetModItems.BOCEK.get()) {
            return;
        }

        double power = stack.getOrCreateTag().getDouble("power");

        if (player instanceof ServerPlayer serverPlayer) {
            SoundTool.stopSound(serverPlayer, TargetModSounds.BOCEK_PULL_1P.getId(), SoundSource.PLAYERS);
            SoundTool.stopSound(serverPlayer, TargetModSounds.BOCEK_PULL_3P.getId(), SoundSource.PLAYERS);
        }

        if (stack.getOrCreateTag().getDouble("power") >= 6) {
            stack.getOrCreateTag().putDouble("speed", stack.getOrCreateTag().getDouble("power"));
            if ((player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).zooming) {
                Level level = player.level();
                if (!level.isClientSide()) {
                    float damage = (float) (0.02 * stack.getOrCreateTag().getDouble("damage") * (1 + 0.05 * stack.getOrCreateTag().getInt("level")));

                    BocekArrowEntity arrow = new BocekArrowEntity(player, level);
                    arrow.setBaseDamage(damage);
                    arrow.setKnockback(0);
                    arrow.setSilent(true);
                    arrow.setPierceLevel((byte) 2);
                    arrow.pickup = AbstractArrow.Pickup.ALLOWED;

                    arrow.setPos(player.getX(), player.getEyeY() - 0.1, player.getZ());
                    arrow.shoot(player.getLookAngle().x, player.getLookAngle().y, player.getLookAngle().z, (float) (4 * power), (float) 0.02);
                    level.addFreshEntity(arrow);
                }

                if (!player.level().isClientSide()) {
                    SoundTool.playLocalSound(player, TargetModSounds.BOCEK_ZOOM_FIRE_1P.get(), 10, 1);
                    player.playSound(TargetModSounds.BOCEK_ZOOM_FIRE_3P.get(), 2, 1);
                }
            } else {
                for (int index0 = 0; index0 < 10; index0++) {
                    spawnBullet(player);
                }

                if (!player.level().isClientSide() && player.getServer() != null) {
                    SoundTool.playLocalSound(player, TargetModSounds.BOCEK_SHATTER_CAP_FIRE_1P.get(), 10, 1);
                    player.playSound(TargetModSounds.BOCEK_SHATTER_CAP_FIRE_3P.get(), 2, 1);
                }
            }

            player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                capability.recoilHorizon = Math.random() < 0.5 ? -1 : 1;
                capability.recoil = 0.1;
                capability.firing = 1;
                capability.syncPlayerVariables(player);
            });

            player.getCooldowns().addCooldown(player.getMainHandItem().getItem(), 7);
            player.getMainHandItem().getOrCreateTag().putInt("arrow_empty", 7);
            player.getMainHandItem().getOrCreateTag().putDouble("power", 0);
            stack.getOrCreateTag().putInt("fire_animation", 2);

            if (!player.isCreative()) {
                player.getInventory().clearOrCountMatchingItems(p -> Items.ARROW == p.getItem(), 1, player.inventoryMenu.getCraftSlots());
            }
        }
    }

    private static void spawnBullet(Player player) {
        ItemStack heldItem = player.getMainHandItem();
        player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
            capability.recoilHorizon = Math.random() < 0.5 ? -1 : 1;
            capability.recoil = 0.1;
            capability.firing = 1;
            capability.syncPlayerVariables(player);
        });

        if (player.level().isClientSide()) return;

        CompoundTag tag = heldItem.getOrCreateTag();
        double damage;
        float headshot = (float) tag.getDouble("headshot");
        float velocity = 4 * (float) tag.getDouble("speed");

        var projectile = new ProjectileEntity(player.level())
                .shooter(player)
                .headShot(headshot);
        if (tag.getBoolean("beast")) {
            projectile.beast();
        }
        projectile.setPos(player.getX() - 0.1 * player.getLookAngle().x, player.getEyeY() - 0.1 - 0.1 * player.getLookAngle().y, player.getZ() + -0.1 * player.getLookAngle().z);

        damage = 0.008333333 * tag.getDouble("damage") * tag.getDouble("speed") * tag.getDouble("damageadd");
        projectile.shoot(player.getLookAngle().x, player.getLookAngle().y, player.getLookAngle().z, velocity, 2.5f);
        projectile.damage((float) damage);
        player.level().addFreshEntity(projectile);
    }

}
