package com.atsuishio.superbwarfare.entity.vehicle;

public interface MultiWeaponVehicleEntity extends IArmedVehicleEntity {
    void changeWeapon(int scroll);
    int getWeaponType();
}
