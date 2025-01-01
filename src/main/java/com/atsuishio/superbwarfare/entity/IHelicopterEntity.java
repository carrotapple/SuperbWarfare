package com.atsuishio.superbwarfare.entity;

public interface IHelicopterEntity extends IArmedVehicleEntity {
    float getRotX(float tickDelta);
    float getRotY(float tickDelta);
    float getRotZ(float tickDelta);
    float getPower();
}
