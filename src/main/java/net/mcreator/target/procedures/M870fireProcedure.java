package net.mcreator.target.procedures;

import net.mcreator.target.init.TargetModItems;
import net.mcreator.target.tools.GunsTool;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class M870fireProcedure {
    public static void execute(Player player) {
        if (player.isSpectator()) return;

        ItemStack usehand = player.getMainHandItem();
        if (usehand.getOrCreateTag().getBoolean("reloading") && usehand.getOrCreateTag().getDouble("prepare") == 0 && usehand.getOrCreateTag().getInt("ammo") > 0) {
            usehand.getOrCreateTag().putDouble("forcestop", 1);
        }
        if (usehand.getItem() == TargetModItems.M_870.get() && !usehand.getOrCreateTag().getBoolean("reloading") && !player.getCooldowns().isOnCooldown(usehand.getItem())
                && usehand.getOrCreateTag().getInt("ammo") > 0) {
            for (int index0 = 0; index0 < 12; index0++) {
                GunsTool.spawnBullet(player);
            }
            player.getCooldowns().addCooldown(usehand.getItem(), 13);
            usehand.getOrCreateTag().putDouble("recoil", 1);
            usehand.getOrCreateTag().putDouble("firing", 13);

            if (!player.level().isClientSide() && player.getServer() != null) {
                player.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, player.position(), player.getRotationVector(), (ServerLevel) player.level(), 4,
                        player.getName().getString(), player.getDisplayName(), player.level().getServer(), player), "playsound target:m_870_fire_1p player @s ~ ~ ~ 2 1");
                player.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, player.position(), player.getRotationVector(), (ServerLevel) player.level(), 4,
                        player.getName().getString(), player.getDisplayName(), player.level().getServer(), player), "playsound target:m_870_fire_3p player @a ~ ~ ~ 4 1");
                player.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, player.position(), player.getRotationVector(), (ServerLevel) player.level(), 4,
                        player.getName().getString(), player.getDisplayName(), player.level().getServer(), player), "playsound target:m_870_far player @s ~ ~ ~ 12 1");
                player.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, player.position(), player.getRotationVector(), (ServerLevel) player.level(), 4,
                        player.getName().getString(), player.getDisplayName(), player.level().getServer(), player), "playsound target:m_870_veryfar player @a ~ ~ ~ 24 1");
            }
            usehand.getOrCreateTag().putInt("ammo", (usehand.getOrCreateTag().getInt("ammo") - 1));
            usehand.getOrCreateTag().putInt("fire_animation", 2);
        }
    }
}
