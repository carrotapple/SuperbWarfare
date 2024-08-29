package net.mcreator.superbwarfare.tools;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.Comparator;
import java.util.List;

public class SeekTool {

    // TODO 固体方块阻挡搜寻，以及目标锁定为夹角最小的实体
    public static Entity seekEntity(Entity entity, LevelAccessor world, double seekRange, double seekAngle) {
        final Vec3 _center = new Vec3(entity.getX(), entity.getY(), entity.getZ());
        List<Entity> _entfound = world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(seekRange), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList();
        for (Entity entityiterator : _entfound) {
            if ((new Object() {
                public double angle(Vec3 _start, Vec3 _end) {
                    double _d0 = _start.length();
                    double _d1 = _end.length();
                    if (_d0 > 0.0D && _d1 > 0.0D) {
                        return Math.toDegrees(Math.acos(_start.dot(_end) / (_d0 * _d1)));
                    } else {
                        return 0.0D;
                    }
                }
            }).angle((new Vec3((entityiterator.getX() - entity.getX()), (entityiterator.getY() - entity.getY()), (entityiterator.getZ() - entity.getZ()))), (entity.getLookAngle())) < seekAngle && entityiterator instanceof LivingEntity) {
                if (entityiterator != entity) {
                    return entityiterator;
                }
            }
        }
        return null;
    }
}
