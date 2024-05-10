package net.mcreator.target.procedures;

import net.mcreator.target.entity.BocekarrowEntity;
import net.mcreator.target.init.TargetModEntities;
import net.mcreator.target.init.TargetModItems;
import net.mcreator.target.network.TargetModVariables;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class BowlooseProcedure {
    public static void execute(Player player) {
        ItemStack usehand = ItemStack.EMPTY;
        double power;
        power = player.getMainHandItem().getOrCreateTag().getDouble("power");
        if (!player.level().isClientSide() && player.getServer() != null) {
            player.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, player.position(), player.getRotationVector(), (ServerLevel) player.level(), 4,
                    player.getName().getString(), player.getDisplayName(), player.level().getServer(), player), "stopsound @a player target:bocek_pull_1p");
            player.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, player.position(), player.getRotationVector(), (ServerLevel) player.level(), 4,
                    player.getName().getString(), player.getDisplayName(), player.level().getServer(), player), "stopsound @a player target:bocek_pull_3p");
        }
        if (player.getMainHandItem().getItem() == TargetModItems.BOCEK.get()
                && player.getMainHandItem().getOrCreateTag().getDouble("power") >= 6) {
            player.getMainHandItem().getOrCreateTag().putDouble("speed",
                    (player.getMainHandItem().getOrCreateTag().getDouble("power")));
            if ((player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).zooming) {
                Level projectileLevel = player.level();
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
                    }.getArrow(projectileLevel, player, (float) (0.02 * player.getMainHandItem().getOrCreateTag().getDouble("damage") * (1 + 0.05 * player.getMainHandItem().getOrCreateTag().getDouble("level"))), 0, (byte) 2);
                    _entityToSpawn.setPos(player.getX(), player.getEyeY() - 0.1, player.getZ());
                    _entityToSpawn.shoot(player.getLookAngle().x, player.getLookAngle().y, player.getLookAngle().z, (float) (4 * power), (float) 0.02);
                    projectileLevel.addFreshEntity(_entityToSpawn);
                }
                if (!player.level().isClientSide() && player.getServer() != null) {
                    player.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, player.position(), player.getRotationVector(), (ServerLevel) player.level(), 4,
                            player.getName().getString(), player.getDisplayName(), player.level().getServer(), player), "playsound target:bocek_zoom_fire_1p player @s ~ ~ ~ 10 1");
                    player.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, player.position(), player.getRotationVector(), (ServerLevel) player.level(), 4,
                            player.getName().getString(), player.getDisplayName(), player.level().getServer(), player), "playsound target:bocek_zoom_fire_3p player @a ~ ~ ~ 2 1");
                }
            } else {
                for (int index0 = 0; index0 < 10; index0++) {
                    BulletFireNormalProcedure.execute(player);
                }

                if (!player.level().isClientSide() && player.getServer() != null) {
                    player.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, player.position(), player.getRotationVector(), (ServerLevel) player.level(), 4,
                            player.getName().getString(), player.getDisplayName(), player.level().getServer(), player), "playsound target:bocek_shatter_cap_fire_1p player @s ~ ~ ~ 10 1");
                    player.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, player.position(), player.getRotationVector(), (ServerLevel) player.level(), 4,
                            player.getName().getString(), player.getDisplayName(), player.level().getServer(), player), "playsound target:bocek_shatter_cap_fire_3p player @a ~ ~ ~ 2 1");
                }
            }
            player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                capability.recoilhorizon = Math.random() < 0.5 ? -1 : 1;
                capability.recoil = 0.1;
                capability.firing = 1;
                capability.syncPlayerVariables(player);
            });

            player.getCooldowns().addCooldown(player.getMainHandItem().getItem(), 7);
            player.getMainHandItem().getOrCreateTag().putDouble("arrowempty", 7);
            player.getMainHandItem().getOrCreateTag().putDouble("power", 0);
            usehand.getOrCreateTag().putDouble("fireanim", 2);

            if (!player.isCreative()) {
                player.getInventory().clearOrCountMatchingItems(p -> Items.ARROW == p.getItem(), 1, player.inventoryMenu.getCraftSlots());
            }
        }
    }
}
