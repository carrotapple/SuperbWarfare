package net.mcreator.target.procedures;

import net.mcreator.target.init.TargetModItems;
import net.mcreator.target.tools.GunsTool;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class KraberfireProcedure {
    public static void execute(Player player) {
        if (player.isSpectator()) return;

        ItemStack usehand = player.getMainHandItem();
        if (usehand.getItem() == TargetModItems.KRABER.get() && usehand.getOrCreateTag().getDouble("reloading") == 0 && !player.getCooldowns().isOnCooldown(usehand.getItem())
                && usehand.getOrCreateTag().getInt("ammo") > 0) {
            usehand.getOrCreateTag().putDouble("fireanim", 40);
            GunsTool.spawnBullet(player);
            player.getCooldowns().addCooldown(usehand.getItem(), 40);

            if (!player.level().isClientSide() && player.getServer() != null) {
                player.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, player.position(), player.getRotationVector(), (ServerLevel) player.level(), 4,
                        player.getName().getString(), player.getDisplayName(), player.level().getServer(), player), "playsound target:kraber_fire_1p player @s ~ ~ ~ 2 1");
                player.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, player.position(), player.getRotationVector(), (ServerLevel) player.level(), 4,
                        player.getName().getString(), player.getDisplayName(), player.level().getServer(), player), "playsound target:kraber_fire_3p player @a ~ ~ ~ 4 1");
                player.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, player.position(), player.getRotationVector(), (ServerLevel) player.level(), 4,
                        player.getName().getString(), player.getDisplayName(), player.level().getServer(), player), "playsound target:kraber_far player @a ~ ~ ~ 12 1");
                player.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, player.position(), player.getRotationVector(), (ServerLevel) player.level(), 4,
                        player.getName().getString(), player.getDisplayName(), player.level().getServer(), player), "playsound target:kraber_veryfar player @a ~ ~ ~ 24 1");
            }
            usehand.getOrCreateTag().putInt("ammo", (usehand.getOrCreateTag().getInt("ammo") - 1));
        }
    }
}
