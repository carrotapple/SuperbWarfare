package net.mcreator.target.procedures;

import net.mcreator.target.network.TargetModVariables;
import net.minecraft.world.entity.Entity;

public class RleaseFireProcedure {
    public static void execute(Entity entity) {
        if (entity == null)
            return;
        entity.getPersistentData().putDouble("firing", 0);
        entity.getPersistentData().putDouble("minifiring", 0);
        entity.getPersistentData().putDouble("minigunfiring", 0);
        {
            boolean _setval = false;
            entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                capability.bowpullhold = _setval;
                capability.syncPlayerVariables(entity);
            });
        }
        BowlooseProcedure.execute(entity);
    }
}
