package com.atsuishio.superbwarfare.tools;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.*;

public class TraceTool {

    public static boolean laserHeadshot = false;

    public static Entity findLookingEntity(Entity entity, double entityReach) {
        double distance = entityReach * entityReach;
        Vec3 eyePos = entity.getEyePosition(1.0f);
        HitResult hitResult = entity.pick(entityReach, 1.0f, false);
        if (hitResult.getType() != HitResult.Type.MISS) {
            distance = hitResult.getLocation().distanceToSqr(eyePos);
            double blockReach = 5;
            if (distance > blockReach * blockReach) {
                Vec3 pos = hitResult.getLocation();
                hitResult = BlockHitResult.miss(pos, Direction.getNearest(eyePos.x, eyePos.y, eyePos.z), BlockPos.containing(pos));
            }
        }
        Vec3 viewVec = entity.getViewVector(1.0F);
        Vec3 toVec = eyePos.add(viewVec.x * entityReach, viewVec.y * entityReach, viewVec.z * entityReach);
        AABB aabb = entity.getBoundingBox().expandTowards(viewVec.scale(entityReach)).inflate(1.0D, 1.0D, 1.0D);
        EntityHitResult entityhitresult = ProjectileUtil.getEntityHitResult(entity, eyePos, toVec, aabb, p -> !p.isSpectator(), distance);
        if (entityhitresult != null) {
            Vec3 targetPos = entityhitresult.getLocation();
            double distanceToTarget = eyePos.distanceToSqr(targetPos);
            if (distanceToTarget > distance || distanceToTarget > entityReach * entityReach) {
                hitResult = BlockHitResult.miss(targetPos, Direction.getNearest(viewVec.x, viewVec.y, viewVec.z), BlockPos.containing(targetPos));
            } else if (distanceToTarget < distance) {
                hitResult = entityhitresult;
            }
        }
        if (hitResult.getType() == HitResult.Type.ENTITY) {
            return ((EntityHitResult) hitResult).getEntity();
        }
        return null;
    }

    public static Entity laserfindLookingEntity(Entity player, double entityReach) {

        BlockHitResult blockResult = player.level().clip(
                new ClipContext(player.getEyePosition(), player.getEyePosition().add(player.getViewVector(1f).scale(512)),
                        ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, player));

        BlockPos resultPos = blockResult.getBlockPos();
        BlockState state = player.level().getBlockState(resultPos);

        double distance = entityReach * entityReach;
        Vec3 eyePos = player.getEyePosition(1.0f);
        HitResult hitResult = player.pick(entityReach, 1.0f, false);
        if (hitResult.getType() != HitResult.Type.MISS) {
            distance = hitResult.getLocation().distanceToSqr(eyePos);
            double blockReach = 5;
            if (distance > blockReach * blockReach) {
                Vec3 pos = hitResult.getLocation();
                hitResult = BlockHitResult.miss(pos, Direction.getNearest(eyePos.x, eyePos.y, eyePos.z), BlockPos.containing(pos));
            }
        }

        Vec3 viewVec = player.getViewVector(1.0F);
        Vec3 toVec = eyePos.add(viewVec.x * entityReach, viewVec.y * entityReach, viewVec.z * entityReach);
        AABB aabb = player.getBoundingBox().expandTowards(viewVec.scale(entityReach)).inflate(1.0D, 1.0D, 1.0D);
        EntityHitResult entityhitresult = ProjectileUtil.getEntityHitResult(player, eyePos, toVec, aabb, p -> !p.isSpectator() && p.isAlive(), distance);
        if (entityhitresult != null) {
            Vec3 targetPos = entityhitresult.getLocation();
            double distanceToTarget = eyePos.distanceToSqr(targetPos);
            if (distanceToTarget > distance || distanceToTarget > entityReach * entityReach) {
                hitResult = BlockHitResult.miss(targetPos, Direction.getNearest(viewVec.x, viewVec.y, viewVec.z), BlockPos.containing(targetPos));
            } else if (distanceToTarget < distance) {
                hitResult = entityhitresult;
            }
            Vec3 hitVec = entityhitresult.getLocation();
            if (hitResult.getType() == HitResult.Type.ENTITY && ((EntityHitResult) hitResult).getEntity().isAlive()) {
                if (((EntityHitResult) hitResult).getEntity() instanceof LivingEntity living) {
                    laserHeadshot = living.getEyeY() - 0.4 < hitVec.y && hitVec.y < living.getEyeY() + 0.5;
                } else {
                    laserHeadshot = false;
                }
                return ((EntityHitResult) hitResult).getEntity();
            }
        }

        return null;
    }
}
