package net.mcreator.target.procedures;

import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;

public class BullettestDanSheWuJiZhongFangKuaiShiProcedure {
    public static void execute(Entity immediatesourceentity) {
        if (immediatesourceentity == null)
            return;
        {
            if (!immediatesourceentity.level().isClientSide() && immediatesourceentity.getServer() != null) {
                immediatesourceentity.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, immediatesourceentity.position(), immediatesourceentity.getRotationVector(), immediatesourceentity.level() instanceof ServerLevel ? (ServerLevel) immediatesourceentity.level() : null, 4,
                        immediatesourceentity.getName().getString(), immediatesourceentity.getDisplayName(), immediatesourceentity.level().getServer(), immediatesourceentity), "particle target:bullthole ~ ~ ~ 0 0 0 0 1 force");
            }
        }
        {
            if (!immediatesourceentity.level().isClientSide() && immediatesourceentity.getServer() != null) {
                immediatesourceentity.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, immediatesourceentity.position(), immediatesourceentity.getRotationVector(), immediatesourceentity.level() instanceof ServerLevel ? (ServerLevel) immediatesourceentity.level() : null, 4,
                        immediatesourceentity.getName().getString(), immediatesourceentity.getDisplayName(), immediatesourceentity.level().getServer(), immediatesourceentity), "particle minecraft:smoke ~ ~ ~ 0 0.1 0 0.01 3 force");
            }
        }
        {
            if (!immediatesourceentity.level().isClientSide() && immediatesourceentity.getServer() != null) {
                immediatesourceentity.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, immediatesourceentity.position(), immediatesourceentity.getRotationVector(), immediatesourceentity.level() instanceof ServerLevel ? (ServerLevel) immediatesourceentity.level() : null, 4,
                        immediatesourceentity.getName().getString(), immediatesourceentity.getDisplayName(), immediatesourceentity.level().getServer(), immediatesourceentity), "playsound target:land block @a ~ ~ ~ 1 1");
            }
        }
        if (!immediatesourceentity.level().isClientSide())
            immediatesourceentity.discard();
    }
}
