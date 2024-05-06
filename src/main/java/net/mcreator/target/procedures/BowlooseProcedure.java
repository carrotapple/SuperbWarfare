package net.mcreator.target.procedures;

import net.mcreator.target.entity.BocekarrowEntity;
import net.mcreator.target.entity.ProjectileEntity;
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
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class BowlooseProcedure {
    public static void execute(Entity entity) {
        if (entity == null) return;
        ItemStack usehand = ItemStack.EMPTY;
        double power;
        power = (entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getOrCreateTag().getDouble("power");
        if (!entity.level().isClientSide() && entity.getServer() != null) {
            entity.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, entity.position(), entity.getRotationVector(), entity.level() instanceof ServerLevel ? (ServerLevel) entity.level() : null, 4,
                    entity.getName().getString(), entity.getDisplayName(), entity.level().getServer(), entity), "stopsound @s player target:bowpull");
            entity.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, entity.position(), entity.getRotationVector(), entity.level() instanceof ServerLevel ? (ServerLevel) entity.level() : null, 4,
                    entity.getName().getString(), entity.getDisplayName(), entity.level().getServer(), entity), "stopsound @s player target:bowpull1p");
        }
        if ((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() == TargetModItems.BOCEK.get()
                && (entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getOrCreateTag().getDouble("power") >= 6) {
            (entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getOrCreateTag().putDouble("speed",
                    ((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getOrCreateTag().getDouble("power")));
            if ((entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).zooming) {
                Level projectileLevel = entity.level();
                if (!projectileLevel.isClientSide()) {
                    Projectile _entityToSpawn = new Object() {
                        public Projectile getArrow(Level level, Entity shooter, float damage, int knockback, byte piercing) {
                            AbstractArrow entityToSpawn = new BocekarrowEntity(TargetModEntities.BOCEKARROW.get(), level);
                            entityToSpawn.setOwner(shooter);
                            entityToSpawn.setBaseDamage(damage);
                            entityToSpawn.setKnockback(knockback);
                            entityToSpawn.setSilent(true);
                            entityToSpawn.setPierceLevel(piercing);
                            entityToSpawn.pickup = AbstractArrow.Pickup.ALLOWED;
                            return entityToSpawn;
                        }
                    }.getArrow(projectileLevel, entity, (float) (0.5 * (1 + 0.05 * usehand.getOrCreateTag().getDouble("level"))), 0, (byte) 2);
                    _entityToSpawn.setPos(entity.getX(), entity.getEyeY() - 0.1, entity.getZ());
                    _entityToSpawn.shoot(entity.getLookAngle().x, entity.getLookAngle().y, entity.getLookAngle().z, (float) (4 * power), (float) 0.01);
                    projectileLevel.addFreshEntity(_entityToSpawn);
                }
                if (!entity.level().isClientSide() && entity.getServer() != null) {
                    entity.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, entity.position(), entity.getRotationVector(), entity.level() instanceof ServerLevel ? (ServerLevel) entity.level() : null, 4,
                            entity.getName().getString(), entity.getDisplayName(), entity.level().getServer(), entity), "playsound target:bowfire1p player @s ~ ~ ~ 10 1");
                    entity.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, entity.position(), entity.getRotationVector(), entity.level() instanceof ServerLevel ? (ServerLevel) entity.level() : null, 4,
                            entity.getName().getString(), entity.getDisplayName(), entity.level().getServer(), entity), "playsound target:bowfire3p player @a ~ ~ ~ 2 1");
                    entity.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, entity.position(), entity.getRotationVector(), entity.level() instanceof ServerLevel ? (ServerLevel) entity.level() : null, 4,
                            entity.getName().getString(), entity.getDisplayName(), entity.level().getServer(), entity), "stopsound @s player target:bowfire3p");
                }
            } else {
                for (int index0 = 0; index0 < 10; index0++) {
                    BulletFireNormalProcedure.execute(entity);
                }

                if (!entity.level().isClientSide() && entity.getServer() != null) {
                    entity.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, entity.position(), entity.getRotationVector(), entity.level() instanceof ServerLevel ? (ServerLevel) entity.level() : null, 4,
                            entity.getName().getString(), entity.getDisplayName(), entity.level().getServer(), entity), "playsound target:shotfire player @s ~ ~ ~ 10 1");
                    entity.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, entity.position(), entity.getRotationVector(), entity.level() instanceof ServerLevel ? (ServerLevel) entity.level() : null, 4,
                            entity.getName().getString(), entity.getDisplayName(), entity.level().getServer(), entity), "playsound target:shotfire3p player @a ~ ~ ~ 2 1");
                    entity.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, entity.position(), entity.getRotationVector(), entity.level() instanceof ServerLevel ? (ServerLevel) entity.level() : null, 4,
                            entity.getName().getString(), entity.getDisplayName(), entity.level().getServer(), entity), "stopsound @s player target:shotfire3p");
                }
            }
            entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                capability.recoilhorizon = Math.random() < 0.5 ? -1 : 1;
                capability.recoil = 0.1;
                capability.firing = 1;
                capability.syncPlayerVariables(entity);
            });

            if (entity instanceof Player _player) {
                _player.getCooldowns().addCooldown(_player.getMainHandItem().getItem(), 7);
            }
            (entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getOrCreateTag().putDouble("arrowempty", 7);
            (entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getOrCreateTag().putDouble("power", 0);
            usehand.getOrCreateTag().putDouble("fireanim", 2);

            if (entity instanceof Player player && !player.isCreative()) {
                ItemStack _stktoremove = new ItemStack(Items.ARROW);
                player.getInventory().clearOrCountMatchingItems(p -> _stktoremove.getItem() == p.getItem(), 1, player.inventoryMenu.getCraftSlots());
            }
        }
    }
}
