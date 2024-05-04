package net.mcreator.target.procedures;

import net.minecraftforge.registries.ForgeRegistries;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.sounds.SoundSource;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.core.BlockPos;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

public class HugeexpProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z) {
		if (world instanceof Level _level) {
			if (!_level.isClientSide()) {
				_level.playSound(null, BlockPos.containing(x, y + 1, z), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("target:c4exp")), SoundSource.BLOCKS, 12, 1);
			} else {
				_level.playLocalSound(x, (y + 1), z, ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("target:c4exp")), SoundSource.BLOCKS, 12, 1, false);
			}
		}
		if (world instanceof Level _level) {
			if (!_level.isClientSide()) {
				_level.playSound(null, BlockPos.containing(x, y + 1, z), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("target:expfar")), SoundSource.BLOCKS, 24, 1);
			} else {
				_level.playLocalSound(x, (y + 1), z, ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("target:expfar")), SoundSource.BLOCKS, 24, 1, false);
			}
		}
		if (world instanceof Level _level) {
			if (!_level.isClientSide()) {
				_level.playSound(null, BlockPos.containing(x, y + 1, z), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("target:expveryfar")), SoundSource.BLOCKS, 64, 1);
			} else {
				_level.playLocalSound(x, (y + 1), z, ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("target:expveryfar")), SoundSource.BLOCKS, 64, 1, false);
			}
		}
		if (world instanceof ServerLevel _level)
			_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, (y + 1), z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
					"particle minecraft:campfire_cosy_smoke ~ ~ ~ 1 3 1 0.04 200 force");
		if (world instanceof ServerLevel _level)
			_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, (y + 1), z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
					"particle minecraft:large_smoke ~ ~1 ~ 1 3 1 0.02 1000 force");
		if (world instanceof ServerLevel _level)
			_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, (y + 1), z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
					"particle minecraft:campfire_cosy_smoke ~ ~ ~ 6 0.01 6 0.02 500 force");
		if (world instanceof ServerLevel _level)
			_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, (y + 1), z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
					"particle target:firestar ~ ~ ~ 0 0 0 0.5 600 force");
	}
}
