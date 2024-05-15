package net.mcreator.target.procedures;

import net.mcreator.target.init.TargetModSounds;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;

public class HugeexpProcedure {
    public static void execute(Level world, double x, double y, double z) {
        if (!world.isClientSide()) {
            world.playSound(null, BlockPos.containing(x, y + 1, z), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("target:c4exp")), SoundSource.BLOCKS, 12, 1);
            world.playSound(null, BlockPos.containing(x, y + 1, z), TargetModSounds.EXPFAR.get(), SoundSource.BLOCKS, 24, 1);
            world.playSound(null, BlockPos.containing(x, y + 1, z), TargetModSounds.EXPVERYFAR.get(), SoundSource.BLOCKS, 64, 1);
        } else {
            world.playLocalSound(x, (y + 1), z, ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("target:c4exp")), SoundSource.BLOCKS, 12, 1, false);
            world.playLocalSound(x, (y + 1), z, TargetModSounds.EXPFAR.get(), SoundSource.BLOCKS, 24, 1, false);
            world.playLocalSound(x, (y + 1), z, TargetModSounds.EXPVERYFAR.get(), SoundSource.BLOCKS, 64, 1, false);
        }
        if (world instanceof ServerLevel server) {
            server.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, (y + 1), z), Vec2.ZERO, server, 4, "", Component.literal(""), server.getServer(), null).withSuppressedOutput(),
                    "particle minecraft:campfire_cosy_smoke ~ ~ ~ 1 3 1 0.04 200 force");
            server.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, (y + 1), z), Vec2.ZERO, server, 4, "", Component.literal(""), server.getServer(), null).withSuppressedOutput(),
                    "particle minecraft:large_smoke ~ ~1 ~ 1 3 1 0.02 1000 force");
            server.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, (y + 1), z), Vec2.ZERO, server, 4, "", Component.literal(""), server.getServer(), null).withSuppressedOutput(),
                    "particle minecraft:campfire_cosy_smoke ~ ~ ~ 6 0.01 6 0.02 500 force");
            server.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, (y + 1), z), Vec2.ZERO, server, 4, "", Component.literal(""), server.getServer(), null).withSuppressedOutput(),
                    "particle target:firestar ~ ~ ~ 0 0 0 0.5 600 force");
        }
    }
}
