package net.mcreator.target.procedures;

import net.mcreator.target.entity.GunGrenadeEntity;
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

public class M79fireProcedure {
    public static void execute(Entity entity) {
        if (entity == null) return;
        if (entity instanceof Player player && !player.isSpectator()) {
            ItemStack usehand = player.getMainHandItem();
            if (usehand.getItem() == TargetModItems.M_79.get() && usehand.getOrCreateTag().getDouble("reloading") == 0 && !(entity instanceof Player _plrCldCheck4 && _plrCldCheck4.getCooldowns().isOnCooldown(usehand.getItem()))
                    && usehand.getOrCreateTag().getInt("ammo") > 0) {
                entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                    capability.recoilHorizon = Math.random() < 0.5 ? -1 : 1;
                    capability.recoil = 0.1;
                    capability.firing = 1;
                    capability.syncPlayerVariables(entity);
                });

                Level projectileLevel = entity.level();
                if (!projectileLevel.isClientSide()) {
                    Projectile _entityToSpawn = new Object() {
                        public Projectile getArrow(Level level, Entity shooter, float damage, int knockback) {
                            AbstractArrow entityToSpawn = new GunGrenadeEntity(TargetModEntities.GUN_GRENADE.get(), level);
                            entityToSpawn.setOwner(shooter);
                            entityToSpawn.setBaseDamage(damage);
                            entityToSpawn.setKnockback(knockback);
                            entityToSpawn.setSilent(true);
                            return entityToSpawn;
                        }
                    }.getArrow(projectileLevel, entity, (float) ((usehand.getOrCreateTag().getDouble("damage") / usehand.getOrCreateTag().getDouble("velocity")) * (1 + 0.05 * usehand.getOrCreateTag().getDouble("level"))), 0);
                    _entityToSpawn.setPos(entity.getX(), entity.getEyeY() - 0.1, entity.getZ());
                    _entityToSpawn.shoot(entity.getLookAngle().x, entity.getLookAngle().y, entity.getLookAngle().z, (float) usehand.getOrCreateTag().getDouble("velocity"),
                            (float) ((LivingEntity) entity).getAttribute(TargetModAttributes.SPREAD.get()).getBaseValue());
                    projectileLevel.addFreshEntity(_entityToSpawn);
                }

                if (!entity.level().isClientSide() && entity.getServer() != null) {
                    entity.getServer().getCommands().performPrefixedCommand(
                            new CommandSourceStack(CommandSource.NULL, entity.position(), entity.getRotationVector(), entity.level() instanceof ServerLevel ? (ServerLevel) entity.level() : null, 4, entity.getName().getString(), entity.getDisplayName(),
                                    entity.level().getServer(), entity),
                            ("particle minecraft:cloud" + (" " + (entity.getX() + 1.8 * entity.getLookAngle().x)) + (" " + (entity.getY() + entity.getBbHeight() - 0.1 + 1.8 * entity.getLookAngle().y))
                                    + (" " + (entity.getZ() + 1.8 * entity.getLookAngle().z)) + " 0.1 0.1 0.1 0.002 4 force @s"));
                }
                player.getCooldowns().addCooldown(usehand.getItem(), 15);

                if (!entity.level().isClientSide() && entity.getServer() != null) {
                    entity.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, entity.position(), entity.getRotationVector(), entity.level() instanceof ServerLevel ? (ServerLevel) entity.level() : null, 4,
                            entity.getName().getString(), entity.getDisplayName(), entity.level().getServer(), entity), "playsound target:m_79_fire_1p player @s ~ ~ ~ 2 1");
                    entity.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, entity.position(), entity.getRotationVector(), entity.level() instanceof ServerLevel ? (ServerLevel) entity.level() : null, 4,
                            entity.getName().getString(), entity.getDisplayName(), entity.level().getServer(), entity), "playsound target:m_79_fire_3p player @a ~ ~ ~ 4 1");
                    entity.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, entity.position(), entity.getRotationVector(), entity.level() instanceof ServerLevel ? (ServerLevel) entity.level() : null, 4,
                            entity.getName().getString(), entity.getDisplayName(), entity.level().getServer(), entity), "playsound target:m_79_far player @s ~ ~ ~ 6 1");
                    entity.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, entity.position(), entity.getRotationVector(), entity.level() instanceof ServerLevel ? (ServerLevel) entity.level() : null, 4,
                            entity.getName().getString(), entity.getDisplayName(), entity.level().getServer(), entity), "playsound target:m_79_veryfar player @a ~ ~ ~ 12 1");
                }
                usehand.getOrCreateTag().putDouble("fireanim", 2);
                usehand.getOrCreateTag().putInt("ammo", (usehand.getOrCreateTag().getInt("ammo") - 1));
            }
        }
    }
}
