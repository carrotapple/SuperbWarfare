package net.mcreator.target.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.core.BlockPos;

import net.mcreator.target.TargetMod;

public class AmmoboxFangZhiFangKuaiShiProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z) {
		TargetMod.queueServerWork(1200, () -> {
			world.destroyBlock(BlockPos.containing(x, y, z), false);
		});
	}
}
