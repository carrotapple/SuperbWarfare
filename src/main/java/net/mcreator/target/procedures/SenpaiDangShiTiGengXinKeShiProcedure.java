package net.mcreator.target.procedures;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.Comparator;

public class SenpaiDangShiTiGengXinKeShiProcedure {
    public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
        if (entity == null) return;
        double target;

        entity.getPersistentData().putDouble("findtarget", (entity.getPersistentData().getDouble("findtarget") + 1));
        target = entity.getPersistentData().getDouble("findtarget");
        if (target == 1 && entity instanceof Mob mob) {
            final Vec3 _center = new Vec3(x, y, z);
            world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(1024 / 2d), e -> true)
                    .stream()
                    .sorted(Comparator.comparingDouble(e -> e.distanceToSqr(_center)))
                    .filter(e -> e instanceof Player player && !player.isCreative())
                    .forEach(e -> mob.setTarget((LivingEntity) e));
        }
        if (target >= 100) {
            entity.getPersistentData().putDouble("findtarget", 0);
        }
    }
}
