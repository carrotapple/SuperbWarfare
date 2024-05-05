package net.mcreator.target.procedures;

import net.minecraft.client.gui.components.EditBox;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.phys.*;

import java.util.HashMap;

public class MortarGUIGaiGUIDaKaiShiProcedure {
    public static void execute(Entity entity, HashMap guistate) {
        if (entity == null || guistate == null)
            return;
        if (!((new Object() {
            public Entity func(Entity player, double entityReach) {
                double distance = entityReach * entityReach;
                Vec3 eyePos = player.getEyePosition(1.0f);
                HitResult hitResult = entity.pick(entityReach, 1.0f, false);
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
                AABB aabb = entity.getBoundingBox().expandTowards(viewVec.scale(entityReach)).inflate(1.0D, 1.0D, 1.0D);
                EntityHitResult entityhitresult = ProjectileUtil.getEntityHitResult(player, eyePos, toVec, aabb, (p_234237_) -> !p_234237_.isSpectator(), distance);
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
        }.func(entity, 6)) == null)) {
            if (guistate.get("text:pitch") instanceof EditBox _tf)
                _tf.setValue((new java.text.DecimalFormat("##").format(-((new Object() {
                    public Entity func(Entity player, double entityReach) {
                        double distance = entityReach * entityReach;
                        Vec3 eyePos = player.getEyePosition(1.0f);
                        HitResult hitResult = entity.pick(entityReach, 1.0f, false);
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
                        AABB aabb = entity.getBoundingBox().expandTowards(viewVec.scale(entityReach)).inflate(1.0D, 1.0D, 1.0D);
                        EntityHitResult entityhitresult = ProjectileUtil.getEntityHitResult(player, eyePos, toVec, aabb, (p_234237_) -> !p_234237_.isSpectator(), distance);
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
                }.func(entity, 6)).getXRot()))));
        }
    }
}
