package net.mcreator.target.procedures;

import net.minecraft.world.entity.Entity;

import net.mcreator.target.network.TargetModVariables;

public class ToZoomProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		{
			boolean _setval = true;
			entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
				capability.zoom = _setval;
				capability.syncPlayerVariables(entity);
			});
		}
	}
}
