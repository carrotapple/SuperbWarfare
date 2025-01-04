package com.atsuishio.superbwarfare.entity.vehicle;

public interface IHelicopterEntity extends IArmedVehicleEntity {

    float getRotX(float tickDelta);

    float getRotY(float tickDelta);

    float getRotZ(float tickDelta);

    float getPower();
}
