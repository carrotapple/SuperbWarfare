package com.atsuishio.superbwarfare.entity.vehicle;

import net.minecraft.world.phys.Vec3;

public interface ILandArmorEntity extends IArmedVehicleEntity {
    float turretYRotO();
    float turretYRot();
    Vec3 getBarrelVec(float ticks);
}
