package net.mcreator.target.procedures;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class TaserBulletTouZhiWuFeiXingKeProcedure {
    public static void execute(Entity immediatesourceentity) {
        if (immediatesourceentity == null)
            return;
        immediatesourceentity.getPersistentData().putDouble("live", (immediatesourceentity.getPersistentData().getDouble("live") + 1));
        if (immediatesourceentity.getPersistentData().getDouble("live") == 5) {
            immediatesourceentity.setDeltaMovement(new Vec3(0, 0, 0));
        }
    }
}
