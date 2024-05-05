package net.mcreator.target.procedures;

import net.minecraft.world.entity.Entity;

import net.mcreator.target.network.TargetModVariables;

public class OutZoomProcedure {
    public static void execute(Entity entity) {
        if (entity == null)
            return;
        {
            boolean _setval = false;
            entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                capability.zoom = _setval;
                capability.syncPlayerVariables(entity);
            });
        }
        {
            boolean _setval = false;
            entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                capability.zooming = _setval;
                capability.syncPlayerVariables(entity);
            });
        }
        entity.getPersistentData().putDouble("miaozhunshijian", 0);
    }
}
