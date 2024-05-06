package net.mcreator.target.procedures;

import net.mcreator.target.init.TargetModItems;
import net.mcreator.target.network.TargetModVariables;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class MarlinfireProcedure {
    public static void execute(Entity entity) {
        if (entity == null) return;
        if (entity instanceof Player player && !player.isSpectator()) {
            ItemStack usehand = player.getMainHandItem();
            if (usehand.getOrCreateTag().getDouble("reloading") == 1 && usehand.getOrCreateTag().getDouble("prepare") == 0 && usehand.getOrCreateTag().getDouble("ammo") > 0) {
                usehand.getOrCreateTag().putDouble("forcestop", 1);
            }
            if (usehand.getItem() == TargetModItems.MARLIN.get() && usehand.getOrCreateTag().getDouble("reloading") == 0 && !(entity instanceof Player _plrCldCheck8 && _plrCldCheck8.getCooldowns().isOnCooldown(usehand.getItem()))
                    && usehand.getOrCreateTag().getDouble("ammo") > 0) {
                if ((entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).zooming) {
                    if (entity instanceof Player _player)
                        _player.getCooldowns().addCooldown(usehand.getItem(), 15);
                    usehand.getOrCreateTag().putDouble("firing", 15);
                    usehand.getOrCreateTag().putDouble("fastfiring", 0);
                } else {
                    if (entity instanceof Player _player)
                        _player.getCooldowns().addCooldown(usehand.getItem(), 10);
                    usehand.getOrCreateTag().putDouble("fastfiring", 1);
                    usehand.getOrCreateTag().putDouble("firing", 10);
                }
                BulletFireNormalProcedure.execute(entity);

                if (!entity.level().isClientSide() && entity.getServer() != null) {
                    entity.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, entity.position(), entity.getRotationVector(), entity.level() instanceof ServerLevel ? (ServerLevel) entity.level() : null, 4,
                            entity.getName().getString(), entity.getDisplayName(), entity.level().getServer(), entity), "playsound target:marlin_fire1p player @s ~ ~ ~ 100 1");
                    entity.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, entity.position(), entity.getRotationVector(), entity.level() instanceof ServerLevel ? (ServerLevel) entity.level() : null, 4,
                            entity.getName().getString(), entity.getDisplayName(), entity.level().getServer(), entity), "playsound target:marlin_fire3p player @a ~ ~ ~ 4 1");
                    entity.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, entity.position(), entity.getRotationVector(), entity.level() instanceof ServerLevel ? (ServerLevel) entity.level() : null, 4,
                            entity.getName().getString(), entity.getDisplayName(), entity.level().getServer(), entity), "stopsound @s player target:marlin_fire3p");
                }
                usehand.getOrCreateTag().putDouble("ammo", (usehand.getOrCreateTag().getDouble("ammo") - 1));
                if (usehand.getOrCreateTag().getDouble("animindex") == 1) {
                    usehand.getOrCreateTag().putDouble("animindex", 0);
                } else {
                    usehand.getOrCreateTag().putDouble("animindex", 1);
                }
                usehand.getOrCreateTag().putDouble("fireanim", 2);
            }
        }
    }
}
