package com.atsuishio.superbwarfare.tools;

import com.atsuishio.superbwarfare.entity.BeamEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.Comparator;
import java.util.stream.StreamSupport;

public class SeekTool {

    public static Entity seekEntity(Entity entity, Level level, double seekRange, double seekAngle) {
        return StreamSupport.stream(EntityFindUtil.getEntities(level).getAll().spliterator(), false)
                .filter(e -> {
                    if (e.distanceTo(entity) <= seekRange && calculateAngle(e, entity) < seekAngle && e != entity && !(e instanceof BeamEntity) && e.isAlive()) {
                        return level.clip(new ClipContext(entity.getEyePosition(), e.getEyePosition(),
                                ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, entity)).getType() != HitResult.Type.BLOCK;
                    }
                    return false;
                }).min(Comparator.comparingDouble(e -> calculateAngle(e, entity))).orElse(null);
    }

    private static double calculateAngle(Entity entityA, Entity entityB) {
        Vec3 start = new Vec3(entityA.getX() - entityB.getX(), entityA.getY() - entityB.getY(), entityA.getZ() - entityB.getZ());
        Vec3 end = entityB.getLookAngle();
        return calculateAngle(start, end);
    }

    private static double calculateAngle(Vec3 start, Vec3 end) {
        double startLength = start.length();
        double endLength = end.length();
        if (startLength > 0.0D && endLength > 0.0D) {
            return Math.toDegrees(Math.acos(start.dot(end) / (startLength * endLength)));
        } else {
            return 0.0D;
        }
    }
}
