package com.atsuishio.superbwarfare.tools;

import com.atsuishio.superbwarfare.entity.IVehicleEntity;
import com.atsuishio.superbwarfare.entity.projectile.ProjectileEntity;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.Comparator;
import java.util.List;
import java.util.stream.StreamSupport;

public class SeekTool {

    public static Entity seekEntity(Entity entity, Level level, double seekRange, double seekAngle) {
        return StreamSupport.stream(EntityFindUtil.getEntities(level).getAll().spliterator(), false)
                .filter(e -> {
                    if (e.distanceTo(entity) <= seekRange && calculateAngle(e, entity) < seekAngle
                            && e != entity
                            && e.isAlive()
                            && e.getVehicle() == null
                            && !(e instanceof ItemEntity || e instanceof ExperienceOrb || e instanceof HangingEntity || e instanceof ProjectileEntity || e instanceof Projectile || e instanceof ArmorStand)
                    ) {
                        return level.clip(new ClipContext(entity.getEyePosition(), e.getEyePosition(),
                                ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, entity)).getType() != HitResult.Type.BLOCK;
                    }
                    return false;
                }).min(Comparator.comparingDouble(e -> calculateAngle(e, entity))).orElse(null);
    }

    public static Entity seekLivingEntity(Entity entity, Level level, double seekRange, double seekAngle) {
        return StreamSupport.stream(EntityFindUtil.getEntities(level).getAll().spliterator(), false)
                .filter(e -> {
                    if (e.distanceTo(entity) <= seekRange && calculateAngle(e, entity) < seekAngle
                            && e != entity
                            && e.isAlive()
                            && !(e instanceof ItemEntity || e instanceof ExperienceOrb || e instanceof HangingEntity || e instanceof ProjectileEntity || e instanceof Projectile || e instanceof ArmorStand)
                            && (e instanceof LivingEntity || e instanceof IVehicleEntity)
                            && !(e instanceof Player player && (player.isSpectator()))
                            && (!e.isAlliedTo(entity) || e.getTeam() == null || e.getTeam().getName().equals("TDM"))) {
                        return level.clip(new ClipContext(entity.getEyePosition(), e.getEyePosition(),
                                ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, entity)).getType() != HitResult.Type.BLOCK;
                    }
                    return false;
                }).min(Comparator.comparingDouble(e -> calculateAngle(e, entity))).orElse(null);
    }

    public static List<Entity> seekLivingEntities(Entity entity, Level level, double seekRange, double seekAngle) {
        return StreamSupport.stream(EntityFindUtil.getEntities(level).getAll().spliterator(), false)
                .filter(e -> {
                    if (e.distanceTo(entity) <= seekRange && calculateAngle(e, entity) < seekAngle
                            && e != entity
                            && e.isAlive()
                            && !(e instanceof ItemEntity || e instanceof ExperienceOrb || e instanceof HangingEntity || e instanceof ProjectileEntity || e instanceof Projectile || e instanceof ArmorStand)
                            && (e instanceof LivingEntity || e instanceof IVehicleEntity)
                            && !(e instanceof Player player && (player.isCreative() || player.isSpectator()))
                            && (!e.isAlliedTo(entity) || e.getTeam() == null || e.getTeam().getName().equals("TDM"))) {
                        return level.clip(new ClipContext(entity.getEyePosition(), e.getEyePosition(),
                                ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, entity)).getType() != HitResult.Type.BLOCK;
                    }
                    return false;
                }).toList();
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
            return Math.toDegrees(Math.acos(Mth.clamp(start.dot(end) / (startLength * endLength), -1, 1)));
        } else {
            return 0.0D;
        }
    }
}
