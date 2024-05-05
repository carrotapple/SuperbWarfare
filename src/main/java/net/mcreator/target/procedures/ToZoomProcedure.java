package net.mcreator.target.procedures;

import net.mcreator.target.network.TargetModVariables;
import net.minecraft.world.entity.Entity;

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
