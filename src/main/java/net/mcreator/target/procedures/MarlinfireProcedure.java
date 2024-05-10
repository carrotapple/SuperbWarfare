package net.mcreator.target.procedures;

import net.mcreator.target.init.TargetModItems;
import net.mcreator.target.network.TargetModVariables;
import net.mcreator.target.tools.GunsTool;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class MarlinfireProcedure {
    public static void execute(Player player) {
        if (player.isSpectator()) return;

        ItemStack usehand = player.getMainHandItem();
        if (usehand.getOrCreateTag().getDouble("reloading") == 1 && usehand.getOrCreateTag().getDouble("prepare") == 0 && usehand.getOrCreateTag().getDouble("ammo") > 0) {
            usehand.getOrCreateTag().putDouble("forcestop", 1);
        }
        if (usehand.getItem() == TargetModItems.MARLIN.get() && usehand.getOrCreateTag().getDouble("reloading") == 0 && !player.getCooldowns().isOnCooldown(usehand.getItem())
                && usehand.getOrCreateTag().getDouble("ammo") > 0) {
            if ((player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).zooming) {
                player.getCooldowns().addCooldown(usehand.getItem(), 15);
                usehand.getOrCreateTag().putDouble("firing", 15);
                usehand.getOrCreateTag().putDouble("fastfiring", 0);
            } else {
                player.getCooldowns().addCooldown(usehand.getItem(), 10);
                usehand.getOrCreateTag().putDouble("fastfiring", 1);
                usehand.getOrCreateTag().putDouble("firing", 10);
            }
            GunsTool.spawnBullet(player);

            if (!player.level().isClientSide() && player.getServer() != null) {
                player.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, player.position(), player.getRotationVector(), (ServerLevel) player.level(), 4,
                        player.getName().getString(), player.getDisplayName(), player.level().getServer(), player), "playsound target:marlin_fire_1p player @s ~ ~ ~ 2 1");
                player.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, player.position(), player.getRotationVector(), (ServerLevel) player.level(), 4,
                        player.getName().getString(), player.getDisplayName(), player.level().getServer(), player), "playsound target:marlin_fire_3p player @a ~ ~ ~ 4 1");
                player.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, player.position(), player.getRotationVector(), (ServerLevel) player.level(), 4,
                        player.getName().getString(), player.getDisplayName(), player.level().getServer(), player), "playsound target:marlin_far player @s ~ ~ ~ 12 1");
                player.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, player.position(), player.getRotationVector(), (ServerLevel) player.level(), 4,
                        player.getName().getString(), player.getDisplayName(), player.level().getServer(), player), "playsound target:marlin_veryfar player @a ~ ~ ~ 24 1");
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
