package net.mcreator.target.procedures;

import net.mcreator.target.init.TargetModItems;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class M98bfireProcedure {
    public static void execute(Player player) {
        if (player.isSpectator()) return;

        ItemStack usehand = player.getMainHandItem();
        if (usehand.getItem() == TargetModItems.M_98B.get() && usehand.getOrCreateTag().getDouble("reloading") == 0 && !(player.getCooldowns().isOnCooldown(usehand.getItem()))
                && usehand.getOrCreateTag().getDouble("ammo") > 0) {
            usehand.getOrCreateTag().putDouble("fireanim", 17);
            BulletFireNormalProcedure.execute(player);
            player.getCooldowns().addCooldown(usehand.getItem(), 17);

            if (!player.level().isClientSide() && player.getServer() != null) {
                player.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, player.position(), player.getRotationVector(), (ServerLevel) player.level(), 4,
                        player.getName().getString(), player.getDisplayName(), player.level().getServer(), player), "playsound target:m_98b_fire_1p player @s ~ ~ ~ 2 1");
                player.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, player.position(), player.getRotationVector(), (ServerLevel) player.level(), 4,
                        player.getName().getString(), player.getDisplayName(), player.level().getServer(), player), "playsound target:m_98b_fire_3p player @a ~ ~ ~ 4 1");
                player.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, player.position(), player.getRotationVector(), (ServerLevel) player.level(), 4,
                        player.getName().getString(), player.getDisplayName(), player.level().getServer(), player), "playsound target:m_98b_far player @a ~ ~ ~ 12 1");
                player.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, player.position(), player.getRotationVector(), (ServerLevel) player.level(), 4,
                        player.getName().getString(), player.getDisplayName(), player.level().getServer(), player), "playsound target:m_98b_veryfar player @a ~ ~ ~ 24 1");
            }
            usehand.getOrCreateTag().putDouble("ammo", (usehand.getOrCreateTag().getDouble("ammo") - 1));
        }
    }
}
