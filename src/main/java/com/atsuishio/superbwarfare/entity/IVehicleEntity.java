package com.atsuishio.superbwarfare.entity;

import net.minecraft.world.entity.player.Player;

public interface IVehicleEntity {

    void vehicleShoot(Player player);

    float getHealth();

    float getMaxHealth();

    boolean isDriver(Player player);

    int mainGunRpm();

    boolean canShoot(Player player);

    int getAmmoCount(Player player);
}
