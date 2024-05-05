package net.mcreator.target.procedures;

import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;

public class GunGrenadeDanSheWuFeiXingShiMeiKeFaShengProcedure {
    public static void execute(Entity immediatesourceentity) {
        if (immediatesourceentity == null)
            return;
        immediatesourceentity.getPersistentData().putDouble("baoxian", (immediatesourceentity.getPersistentData().getDouble("baoxian") + 1));
        {
            if (!immediatesourceentity.level().isClientSide() && immediatesourceentity.getServer() != null) {
                immediatesourceentity.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, immediatesourceentity.position(), immediatesourceentity.getRotationVector(), immediatesourceentity.level() instanceof ServerLevel ? (ServerLevel) immediatesourceentity.level() : null, 4,
                        immediatesourceentity.getName().getString(), immediatesourceentity.getDisplayName(), immediatesourceentity.level().getServer(), immediatesourceentity), "particle minecraft:campfire_cosy_smoke ~ ~ ~ 0 0 0 0 1 force");
            }
        }
    }
}
