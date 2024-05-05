package net.mcreator.target.procedures;

import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

public class GunGrenadeDanSheWuJiZhongShiTiShiProcedure {
    public static void execute(LevelAccessor world, Entity immediatesourceentity) {
        if (immediatesourceentity == null)
            return;
        if (immediatesourceentity.getPersistentData().getDouble("baoxian") > 0) {
            if (world instanceof Level _level && !_level.isClientSide())
                _level.explode(immediatesourceentity, (immediatesourceentity.getX()), (immediatesourceentity.getY()), (immediatesourceentity.getZ()), 5.5f, Level.ExplosionInteraction.NONE);
            if (world instanceof ServerLevel _level)
                _level.getServer().getCommands().performPrefixedCommand(
                        new CommandSourceStack(CommandSource.NULL, new Vec3((immediatesourceentity.getX()), (immediatesourceentity.getY()), (immediatesourceentity.getZ())), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null)
                                .withSuppressedOutput(),
                        "target:mediumexp");
            if (!immediatesourceentity.level().isClientSide())
                immediatesourceentity.discard();
        }
    }
}
