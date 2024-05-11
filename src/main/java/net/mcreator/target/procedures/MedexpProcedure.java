package net.mcreator.target.procedures;

import net.mcreator.target.init.TargetModSounds;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

public class MedexpProcedure {
    public static void execute(LevelAccessor world, double x, double y, double z) {
        if (world instanceof Level level) {
            if (!level.isClientSide()) {
                level.playSound(null, BlockPos.containing(x, y + 1, z), TargetModSounds.EXP.get(), SoundSource.BLOCKS, 12, 1);
                level.playSound(null, BlockPos.containing(x, y + 1, z), TargetModSounds.EXPFAR.get(), SoundSource.BLOCKS, 28, 1);
                level.playSound(null, BlockPos.containing(x, y + 1, z), TargetModSounds.EXPVERYFAR.get(), SoundSource.BLOCKS, 48, 1);
            } else {
                level.playLocalSound(x, (y + 1), z, TargetModSounds.EXP.get(), SoundSource.BLOCKS, 12, 1, false);
                level.playLocalSound(x, (y + 1), z, TargetModSounds.EXPFAR.get(), SoundSource.BLOCKS, 28, 1, false);
                level.playLocalSound(x, (y + 1), z, TargetModSounds.EXPVERYFAR.get(), SoundSource.BLOCKS, 48, 1, false);
            }
        }
        if (world instanceof ServerLevel level) {
            level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, (y + 1), z), Vec2.ZERO, level, 4, "", Component.literal(""), level.getServer(), null).withSuppressedOutput(),
                    "particle minecraft:campfire_cosy_smoke ~ ~ ~ 0.4 1 0.4 0.02 80 force");
            level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, (y + 1), z), Vec2.ZERO, level, 4, "", Component.literal(""), level.getServer(), null).withSuppressedOutput(),
                    "particle minecraft:large_smoke ~ ~1 ~ 0.4 1 0.4 0.02 100 force");
            level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, (y + 1), z), Vec2.ZERO, level, 4, "", Component.literal(""), level.getServer(), null).withSuppressedOutput(),
                    "particle minecraft:campfire_cosy_smoke ~ ~ ~ 2 0.001 2 0.01 100 force");
            level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, (y + 1), z), Vec2.ZERO, level, 4, "", Component.literal(""), level.getServer(), null).withSuppressedOutput(),
                    "particle target:firestar ~ ~ ~ 0 0 0 0.2 100 force");
        }
    }
}
