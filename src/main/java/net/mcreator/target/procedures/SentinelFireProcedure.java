package net.mcreator.target.procedures;

import net.mcreator.target.init.TargetModItems;
import net.mcreator.target.network.TargetModVariables;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class SentinelFireProcedure {
    public static void execute(Player player) {
        if (player.isSpectator()) return;

        ItemStack usehand = player.getMainHandItem();
        if (usehand.getItem() == TargetModItems.SENTINEL.get() && usehand.getOrCreateTag().getDouble("reloading") == 0 && !player.getCooldowns().isOnCooldown(usehand.getItem())
                && usehand.getOrCreateTag().getDouble("ammo") > 0) {
            if (usehand.getOrCreateTag().getDouble("power") > 0) {
                if ((player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).zooming) {
                    usehand.getOrCreateTag().putDouble("zoomfiring", 24);
                } else {
                    usehand.getOrCreateTag().putDouble("firing", 24);
                }
                if (!player.level().isClientSide() && player.getServer() != null) {
                    player.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, player.position(), player.getRotationVector(), (ServerLevel) player.level(), 4,
                            player.getName().getString(), player.getDisplayName(), player.level().getServer(), player), "playsound target:sentinel_charge_fire_1p player @s ~ ~ ~ 2 1");
                    player.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, player.position(), player.getRotationVector(), (ServerLevel) player.level(), 4,
                            player.getName().getString(), player.getDisplayName(), player.level().getServer(), player), "playsound target:sentinel_charge_fire_3p player @a ~ ~ ~ 4 1");
                    player.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, player.position(), player.getRotationVector(), (ServerLevel) player.level(), 4,
                            player.getName().getString(), player.getDisplayName(), player.level().getServer(), player), "playsound target:sentinel_charge_far player @s ~ ~ ~ 12 1");
                    player.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, player.position(), player.getRotationVector(), (ServerLevel) player.level(), 4,
                            player.getName().getString(), player.getDisplayName(), player.level().getServer(), player), "playsound target:sentinel_charge_veryfar player @a ~ ~ ~ 24 1");
                }
                if (usehand.getOrCreateTag().getDouble("power") > 20) {
                    usehand.getOrCreateTag().putDouble("power", (usehand.getOrCreateTag().getDouble("power") - 20));
                } else {
                    usehand.getOrCreateTag().putDouble("power", 0);
                }
            } else {
                if ((player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).zooming) {
                    usehand.getOrCreateTag().putDouble("zoomfiring", 24);
                } else {
                    usehand.getOrCreateTag().putDouble("firing", 24);
                }
                if (!player.level().isClientSide() && player.getServer() != null) {
                    player.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, player.position(), player.getRotationVector(), (ServerLevel) player.level(), 4,
                            player.getName().getString(), player.getDisplayName(), player.level().getServer(), player), "playsound target:sentinel_fire_1p player @s ~ ~ ~ 2 1");
                    player.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, player.position(), player.getRotationVector(), (ServerLevel) player.level(), 4,
                            player.getName().getString(), player.getDisplayName(), player.level().getServer(), player), "playsound target:sentinel_fire_3p player @a ~ ~ ~ 4 1");
                    player.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, player.position(), player.getRotationVector(), (ServerLevel) player.level(), 4,
                            player.getName().getString(), player.getDisplayName(), player.level().getServer(), player), "playsound target:sentinel_far player @s ~ ~ ~ 12 1");
                    player.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, player.position(), player.getRotationVector(), (ServerLevel) player.level(), 4,
                            player.getName().getString(), player.getDisplayName(), player.level().getServer(), player), "playsound target:sentinel_veryfar player @a ~ ~ ~ 24 1");
                }
            }
            BulletFireNormalProcedure.execute(player);
            usehand.getOrCreateTag().putDouble("crot", 20);
            player.getCooldowns().addCooldown(usehand.getItem(), 23);
            usehand.getOrCreateTag().putDouble("ammo", (usehand.getOrCreateTag().getDouble("ammo") - 1));
            usehand.getOrCreateTag().putDouble("fireanim", 2);
        }
    }
}
