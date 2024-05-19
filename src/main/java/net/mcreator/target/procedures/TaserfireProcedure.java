package net.mcreator.target.procedures;

import net.mcreator.target.entity.TaserBulletProjectileEntity;
import net.mcreator.target.init.TargetModAttributes;
import net.mcreator.target.init.TargetModEntities;
import net.mcreator.target.init.TargetModItems;
import net.mcreator.target.init.TargetModSounds;
import net.mcreator.target.network.TargetModVariables;
import net.mcreator.target.tools.SoundTool;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class TaserfireProcedure {
    public static void execute(Entity entity) {
        if (entity == null) return;
        if (entity instanceof Player player && !player.isSpectator()) {
            ItemStack usehand = player.getMainHandItem();
            if (usehand.getItem() == TargetModItems.TASER.get() && !usehand.getOrCreateTag().getBoolean("reloading")) {
                Player _plrCldCheck4 = (Player) entity;
                if (!_plrCldCheck4.getCooldowns().isOnCooldown(usehand.getItem()) && usehand.getOrCreateTag().getInt("ammo") > 0) {

                    entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                        capability.recoilHorizon = Math.random() < 0.5 ? -1 : 1;
                        capability.recoil = 0.1;
                        capability.firing = 1;
                        capability.syncPlayerVariables(entity);
                    });
                    player.getCooldowns().addCooldown(usehand.getItem(), 5);

                    if (entity instanceof ServerPlayer serverPlayer) {
                        SoundTool.playLocalSound(serverPlayer, TargetModSounds.TASER_FIRE_1P.get(), 1, 1);
                        serverPlayer.level().playSound(null, serverPlayer.getOnPos(), TargetModSounds.TASER_FIRE_3P.get(), SoundSource.PLAYERS, 1, 1);
                    }

                    Level projectileLevel = entity.level();
                    if (!projectileLevel.isClientSide()) {
                        Projectile _entityToSpawn = new Object() {
                            public Projectile getArrow(Level level, Entity shooter, float damage, int knockback) {
                                AbstractArrow entityToSpawn = new TaserBulletProjectileEntity(TargetModEntities.TASER_BULLET_PROJECTILE.get(), level);
                                entityToSpawn.setOwner(shooter);
                                entityToSpawn.setBaseDamage(damage);
                                entityToSpawn.setKnockback(knockback);
                                entityToSpawn.setSilent(true);
                                return entityToSpawn;
                            }
                        }.getArrow(projectileLevel, entity, (float) (usehand.getOrCreateTag().getDouble("damage") / usehand.getOrCreateTag().getDouble("velocity")), 0);
                        _entityToSpawn.setPos(entity.getX(), entity.getEyeY() - 0.1, entity.getZ());
                        _entityToSpawn.shoot(entity.getLookAngle().x, entity.getLookAngle().y, entity.getLookAngle().z, (float) usehand.getOrCreateTag().getDouble("velocity"),
                                (float) ((LivingEntity) entity).getAttribute(TargetModAttributes.SPREAD.get()).getBaseValue());
                        projectileLevel.addFreshEntity(_entityToSpawn);
                    }
                    usehand.getOrCreateTag().putInt("fire_animation", 4);
                    usehand.getOrCreateTag().putInt("ammo", (usehand.getOrCreateTag().getInt("ammo") - 1));
                }
            }
        }
    }
}
