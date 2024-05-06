package net.mcreator.target.procedures;

import net.mcreator.target.init.TargetModItems;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class Hk416fireProcedure {
    public static void execute(Entity entity) {
        if (entity == null) return;
        if (entity instanceof Player player && !player.isSpectator()) {
            ItemStack usehand = player.getMainHandItem();
            if (usehand.getItem() == TargetModItems.HK_416.get()) {
                if (usehand.getOrCreateTag().getDouble("firemode") == 0) {
                    if (usehand.getOrCreateTag().getDouble("reloading") == 0 && usehand.getOrCreateTag().getDouble("ammo") > 0 && !(entity instanceof Player _plrCldCheck6 && _plrCldCheck6.getCooldowns().isOnCooldown(usehand.getItem()))) {
                        if (entity instanceof Player _player)
                            _player.getCooldowns().addCooldown(usehand.getItem(), 2);
                        BulletFireNormalProcedure.execute(entity);
                        HkDsProcedure.execute(entity);

                        if (!entity.level().isClientSide() && entity.getServer() != null) {
                            entity.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, entity.position(), entity.getRotationVector(), entity.level() instanceof ServerLevel ? (ServerLevel) entity.level() : null, 4,
                                    entity.getName().getString(), entity.getDisplayName(), entity.level().getServer(), entity), "playsound target:hk416_fire1p player @s ~ ~ ~ 2 1");
                            entity.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, entity.position(), entity.getRotationVector(), entity.level() instanceof ServerLevel ? (ServerLevel) entity.level() : null, 4,
                                    entity.getName().getString(), entity.getDisplayName(), entity.level().getServer(), entity), "playsound target:hk416fire player @a ~ ~ ~ 4 1");
                            entity.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, entity.position(), entity.getRotationVector(), entity.level() instanceof ServerLevel ? (ServerLevel) entity.level() : null, 4,
                                    entity.getName().getString(), entity.getDisplayName(), entity.level().getServer(), entity), "stopsound @s player target:hk416fire");
                        }
                        usehand.getOrCreateTag().putDouble("fireanim", 2);
                        usehand.getOrCreateTag().putDouble("ammo", (usehand.getOrCreateTag().getDouble("ammo") - 1));
                    }
                } else if (usehand.getOrCreateTag().getDouble("firemode") == 2) {
                    entity.getPersistentData().putDouble("firing", 1);
                }
            }
        }
    }
}
