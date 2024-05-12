package net.mcreator.target.procedures;

import net.mcreator.target.init.TargetModItems;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class ReloadsoundstopProcedure {
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            execute(event.player);
        }
    }

    private static void execute(Player entity) {
        if (entity.level().isClientSide() || entity.getServer() == null) return;
        if (entity.getMainHandItem().getOrCreateTag().getDouble("id") != entity.getPersistentData().getDouble("id")) {


            entity.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, entity.position(), entity.getRotationVector(), entity.level() instanceof ServerLevel ? (ServerLevel) entity.level() : null, 4,
                    entity.getName().getString(), entity.getDisplayName(), entity.level().getServer(), entity), "stopsound @s player target:taser_reload");


            entity.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, entity.position(), entity.getRotationVector(), entity.level() instanceof ServerLevel ? (ServerLevel) entity.level() : null, 4,
                    entity.getName().getString(), entity.getDisplayName(), entity.level().getServer(), entity), "stopsound @s player target:ak47_reload_empty");
            entity.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, entity.position(), entity.getRotationVector(), entity.level() instanceof ServerLevel ? (ServerLevel) entity.level() : null, 4,
                    entity.getName().getString(), entity.getDisplayName(), entity.level().getServer(), entity), "stopsound @s player target:ak47_reload_normal");


            entity.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, entity.position(), entity.getRotationVector(), entity.level() instanceof ServerLevel ? (ServerLevel) entity.level() : null, 4,
                    entity.getName().getString(), entity.getDisplayName(), entity.level().getServer(), entity), "stopsound @s player target:trachelium_reload");


            entity.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, entity.position(), entity.getRotationVector(), entity.level() instanceof ServerLevel ? (ServerLevel) entity.level() : null, 4,
                    entity.getName().getString(), entity.getDisplayName(), entity.level().getServer(), entity), "stopsound @s player target:hunting_rifle_reload");


            entity.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, entity.position(), entity.getRotationVector(), entity.level() instanceof ServerLevel ? (ServerLevel) entity.level() : null, 4,
                    entity.getName().getString(), entity.getDisplayName(), entity.level().getServer(), entity), "stopsound @s player target:m79_reload");


            entity.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, entity.position(), entity.getRotationVector(), entity.level() instanceof ServerLevel ? (ServerLevel) entity.level() : null, 4,
                    entity.getName().getString(), entity.getDisplayName(), entity.level().getServer(), entity), "stopsound @s player target:sks_reload_normal");
            entity.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, entity.position(), entity.getRotationVector(), entity.level() instanceof ServerLevel ? (ServerLevel) entity.level() : null, 4,
                    entity.getName().getString(), entity.getDisplayName(), entity.level().getServer(), entity), "stopsound @s player target:sks_reload_empty");


            entity.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, entity.position(), entity.getRotationVector(), entity.level() instanceof ServerLevel ? (ServerLevel) entity.level() : null, 4,
                    entity.getName().getString(), entity.getDisplayName(), entity.level().getServer(), entity), "stopsound @s player target:abekiri_reload_normal");
            entity.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, entity.position(), entity.getRotationVector(), entity.level() instanceof ServerLevel ? (ServerLevel) entity.level() : null, 4,
                    entity.getName().getString(), entity.getDisplayName(), entity.level().getServer(), entity), "stopsound @s player target:abekiri_reload_empty");


            entity.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, entity.position(), entity.getRotationVector(), entity.level() instanceof ServerLevel ? (ServerLevel) entity.level() : null, 4,
                    entity.getName().getString(), entity.getDisplayName(), entity.level().getServer(), entity), "stopsound @s player target:m98b_reload_normal");
            entity.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, entity.position(), entity.getRotationVector(), entity.level() instanceof ServerLevel ? (ServerLevel) entity.level() : null, 4,
                    entity.getName().getString(), entity.getDisplayName(), entity.level().getServer(), entity), "stopsound @s player target:m98b_reload_empty");


            entity.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, entity.position(), entity.getRotationVector(), entity.level() instanceof ServerLevel ? (ServerLevel) entity.level() : null, 4,
                    entity.getName().getString(), entity.getDisplayName(), entity.level().getServer(), entity), "stopsound @s player target:m4_reload_normal");
            entity.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, entity.position(), entity.getRotationVector(), entity.level() instanceof ServerLevel ? (ServerLevel) entity.level() : null, 4,
                    entity.getName().getString(), entity.getDisplayName(), entity.level().getServer(), entity), "stopsound @s player target:m4_reload_empty");


            entity.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, entity.position(), entity.getRotationVector(), entity.level() instanceof ServerLevel ? (ServerLevel) entity.level() : null, 4,
                    entity.getName().getString(), entity.getDisplayName(), entity.level().getServer(), entity), "stopsound @s player target:devotion_reload_normal");
            entity.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, entity.position(), entity.getRotationVector(), entity.level() instanceof ServerLevel ? (ServerLevel) entity.level() : null, 4,
                    entity.getName().getString(), entity.getDisplayName(), entity.level().getServer(), entity), "stopsound @s player target:devotion_reload_empty");


            entity.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, entity.position(), entity.getRotationVector(), entity.level() instanceof ServerLevel ? (ServerLevel) entity.level() : null, 4,
                    entity.getName().getString(), entity.getDisplayName(), entity.level().getServer(), entity), "stopsound @s player target:rpg7_reload");


            entity.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, entity.position(), entity.getRotationVector(), entity.level() instanceof ServerLevel ? (ServerLevel) entity.level() : null, 4,
                    entity.getName().getString(), entity.getDisplayName(), entity.level().getServer(), entity), "stopsound @s player target:aa12_reload_normal");
            entity.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, entity.position(), entity.getRotationVector(), entity.level() instanceof ServerLevel ? (ServerLevel) entity.level() : null, 4,
                    entity.getName().getString(), entity.getDisplayName(), entity.level().getServer(), entity), "stopsound @s player target:aa12_reload_empty");


            entity.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, entity.position(), entity.getRotationVector(), entity.level() instanceof ServerLevel ? (ServerLevel) entity.level() : null, 4,
                    entity.getName().getString(), entity.getDisplayName(), entity.level().getServer(), entity), "stopsound @s player target:hk416_reload_normal");
            entity.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, entity.position(), entity.getRotationVector(), entity.level() instanceof ServerLevel ? (ServerLevel) entity.level() : null, 4,
                    entity.getName().getString(), entity.getDisplayName(), entity.level().getServer(), entity), "stopsound @s player target:hk416_reload_empty");


            entity.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, entity.position(), entity.getRotationVector(), entity.level() instanceof ServerLevel ? (ServerLevel) entity.level() : null, 4,
                    entity.getName().getString(), entity.getDisplayName(), entity.level().getServer(), entity), "stopsound @s player target:rpk_reload_normal");
            entity.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, entity.position(), entity.getRotationVector(), entity.level() instanceof ServerLevel ? (ServerLevel) entity.level() : null, 4,
                    entity.getName().getString(), entity.getDisplayName(), entity.level().getServer(), entity), "stopsound @s player target:rpk_reload_empty");


            entity.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, entity.position(), entity.getRotationVector(), entity.level() instanceof ServerLevel ? (ServerLevel) entity.level() : null, 4,
                    entity.getName().getString(), entity.getDisplayName(), entity.level().getServer(), entity), "stopsound @s player target:kraber_reload_normal");
            entity.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, entity.position(), entity.getRotationVector(), entity.level() instanceof ServerLevel ? (ServerLevel) entity.level() : null, 4,
                    entity.getName().getString(), entity.getDisplayName(), entity.level().getServer(), entity), "stopsound @s player target:kraber_reload_empty");


            entity.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, entity.position(), entity.getRotationVector(), entity.level() instanceof ServerLevel ? (ServerLevel) entity.level() : null, 4,
                    entity.getName().getString(), entity.getDisplayName(), entity.level().getServer(), entity), "stopsound @s player target:vector_reload_normal");
            entity.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, entity.position(), entity.getRotationVector(), entity.level() instanceof ServerLevel ? (ServerLevel) entity.level() : null, 4,
                    entity.getName().getString(), entity.getDisplayName(), entity.level().getServer(), entity), "stopsound @s player target:vector_reload_empty");


            entity.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, entity.position(), entity.getRotationVector(), entity.level() instanceof ServerLevel ? (ServerLevel) entity.level() : null, 4,
                    entity.getName().getString(), entity.getDisplayName(), entity.level().getServer(), entity), "stopsound @s player target:mk14_reload_normal");
            entity.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, entity.position(), entity.getRotationVector(), entity.level() instanceof ServerLevel ? (ServerLevel) entity.level() : null, 4,
                    entity.getName().getString(), entity.getDisplayName(), entity.level().getServer(), entity), "stopsound @s player target:mk14_reload_empty");


            entity.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, entity.position(), entity.getRotationVector(), entity.level() instanceof ServerLevel ? (ServerLevel) entity.level() : null, 4,
                    entity.getName().getString(), entity.getDisplayName(), entity.level().getServer(), entity), "stopsound @s player target:sentinel_reload_normal");
            entity.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, entity.position(), entity.getRotationVector(), entity.level() instanceof ServerLevel ? (ServerLevel) entity.level() : null, 4,
                    entity.getName().getString(), entity.getDisplayName(), entity.level().getServer(), entity), "stopsound @s player target:sentinel_reload_empty");


            entity.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, entity.position(), entity.getRotationVector(), entity.level() instanceof ServerLevel ? (ServerLevel) entity.level() : null, 4,
                    entity.getName().getString(), entity.getDisplayName(), entity.level().getServer(), entity), "stopsound @s player target:sentinel_charge");


            entity.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, entity.position(), entity.getRotationVector(), entity.level() instanceof ServerLevel ? (ServerLevel) entity.level() : null, 4,
                    entity.getName().getString(), entity.getDisplayName(), entity.level().getServer(), entity), "stopsound @s player target:m60_reload_normal");
            entity.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, entity.position(), entity.getRotationVector(), entity.level() instanceof ServerLevel ? (ServerLevel) entity.level() : null, 4,
                    entity.getName().getString(), entity.getDisplayName(), entity.level().getServer(), entity), "stopsound @s player target:m60_reload_empty");


            entity.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, entity.position(), entity.getRotationVector(), entity.level() instanceof ServerLevel ? (ServerLevel) entity.level() : null, 4,
                    entity.getName().getString(), entity.getDisplayName(), entity.level().getServer(), entity), "stopsound @s player target:svd_reload_normal");
            entity.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, entity.position(), entity.getRotationVector(), entity.level() instanceof ServerLevel ? (ServerLevel) entity.level() : null, 4,
                    entity.getName().getString(), entity.getDisplayName(), entity.level().getServer(), entity), "stopsound @s player target:svd_reload_empty");

        }
        if (!(entity.getMainHandItem().getItem() == TargetModItems.MARLIN.get())) {
            entity.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, entity.position(), entity.getRotationVector(), entity.level() instanceof ServerLevel ? (ServerLevel) entity.level() : null, 4,
                    entity.getName().getString(), entity.getDisplayName(), entity.level().getServer(), entity), "stopsound @s player target:marlin_loop");
            entity.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, entity.position(), entity.getRotationVector(), entity.level() instanceof ServerLevel ? (ServerLevel) entity.level() : null, 4,
                    entity.getName().getString(), entity.getDisplayName(), entity.level().getServer(), entity), "stopsound @s player target:marlin_start");
            entity.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, entity.position(), entity.getRotationVector(), entity.level() instanceof ServerLevel ? (ServerLevel) entity.level() : null, 4,
                    entity.getName().getString(), entity.getDisplayName(), entity.level().getServer(), entity), "stopsound @s player target:marlin_end");

        }
        if (!(entity.getMainHandItem().getItem() == TargetModItems.M_870.get())) {
            entity.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, entity.position(), entity.getRotationVector(), entity.level() instanceof ServerLevel ? (ServerLevel) entity.level() : null, 4,
                    entity.getName().getString(), entity.getDisplayName(), entity.level().getServer(), entity), "stopsound @s player target:m870_reloadloop");
            entity.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, entity.position(), entity.getRotationVector(), entity.level() instanceof ServerLevel ? (ServerLevel) entity.level() : null, 4,
                    entity.getName().getString(), entity.getDisplayName(), entity.level().getServer(), entity), "stopsound @s player target:m870_preparealt");

        }
        if (entity.getMainHandItem().getOrCreateTag().getDouble("cid") != entity.getPersistentData().getDouble("cid")) {
            entity.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, entity.position(), entity.getRotationVector(), entity.level() instanceof ServerLevel ? (ServerLevel) entity.level() : null, 4,
                    entity.getName().getString(), entity.getDisplayName(), entity.level().getServer(), entity), "stopsound @s player target:sentinel_charge");

        }
    }
}
