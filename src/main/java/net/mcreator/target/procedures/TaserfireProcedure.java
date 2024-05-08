package net.mcreator.target.procedures;

import net.mcreator.target.entity.TaserBulletProjectileEntity;
import net.mcreator.target.init.TargetModAttributes;
import net.mcreator.target.init.TargetModEntities;
import net.mcreator.target.init.TargetModItems;
import net.mcreator.target.network.TargetModVariables;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerLevel;
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
            if (usehand.getItem() == TargetModItems.TASER.get() && usehand.getOrCreateTag().getDouble("reloading") == 0 && !(entity instanceof Player _plrCldCheck4 && _plrCldCheck4.getCooldowns().isOnCooldown(usehand.getItem()))
                    && usehand.getOrCreateTag().getDouble("ammo") > 0) {

                entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                    capability.recoilhorizon = Math.random() < 0.5 ? -1 : 1;
                    capability.recoil = 0.1;
                    capability.firing = 1;
                    capability.syncPlayerVariables(entity);
                });
                player.getCooldowns().addCooldown(usehand.getItem(), 5);

                if (!entity.level().isClientSide() && entity.getServer() != null) {
                    entity.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, entity.position(), entity.getRotationVector(), entity.level() instanceof ServerLevel ? (ServerLevel) entity.level() : null, 4,
                            entity.getName().getString(), entity.getDisplayName(), entity.level().getServer(), entity), "playsound target:taser_fire_1p player @s ~ ~ ~ 1 1");
                }
                {
                    Entity _ent = entity;
                    if (!_ent.level().isClientSide() && _ent.getServer() != null) {
                        _ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4,
                                _ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), "playsound target:taser_fire_3p player @a ~ ~ ~ 1 1");
                    }
                }
                {
                    Entity _ent = entity;
                    if (!_ent.level().isClientSide() && _ent.getServer() != null) {
                        _ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4,
                                _ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), "stopsound @s player target:taser_fire_3p");
                    }
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
                usehand.getOrCreateTag().putDouble("fireanim", 4);
                usehand.getOrCreateTag().putDouble("ammo", (usehand.getOrCreateTag().getDouble("ammo") - 1));
            }
        }
    }
}
