package net.mcreator.target.procedures;

import net.minecraft.world.entity.Entity;

public class BazipengzhuangProcedure {
    public static double execute(Entity entity) {
        if (entity == null)
            return 0;
        if (entity.getPersistentData().getDouble("targetdown") > 0) {
            return 0.1;
        }
        return 1;
    }
}
