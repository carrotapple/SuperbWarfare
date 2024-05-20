package net.mcreator.target.procedures;

import net.mcreator.target.entity.TaserBulletProjectileEntity;
import net.mcreator.target.init.TargetModAttributes;
import net.mcreator.target.init.TargetModItems;
import net.mcreator.target.init.TargetModSounds;
import net.mcreator.target.network.TargetModVariables;
import net.mcreator.target.tools.SoundTool;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

// TODO 内联这个类
public class TaserfireProcedure {
    public static void execute(Entity entity) {
        if (entity == null) return;
        if (entity instanceof Player player && !player.isSpectator()) {
            ItemStack stack = player.getMainHandItem();
            if (stack.getItem() == TargetModItems.TASER.get() && !stack.getOrCreateTag().getBoolean("reloading")) {
                Player _plrCldCheck4 = (Player) entity;
                if (!_plrCldCheck4.getCooldowns().isOnCooldown(stack.getItem()) && stack.getOrCreateTag().getInt("ammo") > 0) {

                    entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                        capability.recoilHorizon = Math.random() < 0.5 ? -1 : 1;
                        capability.recoil = 0.1;
                        capability.firing = 1;
                        capability.syncPlayerVariables(entity);
                    });
                    player.getCooldowns().addCooldown(stack.getItem(), 5);

                    if (entity instanceof ServerPlayer serverPlayer) {
                        SoundTool.playLocalSound(serverPlayer, TargetModSounds.TASER_FIRE_1P.get(), 1, 1);
                        serverPlayer.level().playSound(null, serverPlayer.getOnPos(), TargetModSounds.TASER_FIRE_3P.get(), SoundSource.PLAYERS, 1, 1);
                    }

                    Level level = entity.level();
                    if (!level.isClientSide()) {
                        TaserBulletProjectileEntity taserBulletProjectile = new TaserBulletProjectileEntity(player, level, (float) stack.getOrCreateTag().getDouble("damage"));

                        taserBulletProjectile.setPos(entity.getX(), entity.getEyeY() - 0.1, entity.getZ());
                        taserBulletProjectile.shoot(entity.getLookAngle().x, entity.getLookAngle().y, entity.getLookAngle().z, (float) stack.getOrCreateTag().getDouble("velocity"),
                                (float) ((LivingEntity) entity).getAttribute(TargetModAttributes.SPREAD.get()).getBaseValue());
                        level.addFreshEntity(taserBulletProjectile);
                    }
                    stack.getOrCreateTag().putInt("fire_animation", 4);
                    stack.getOrCreateTag().putInt("ammo", (stack.getOrCreateTag().getInt("ammo") - 1));
                }
            }
        }
    }
}
