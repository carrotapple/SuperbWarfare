package com.atsuishio.superbwarfare.entity.vehicle;

import net.minecraft.world.entity.player.Player;

public interface IArmedVehicleEntity {

    void vehicleShoot(Player player);

    boolean isDriver(Player player);

    int mainGunRpm();

    boolean canShoot(Player player);

    int getAmmoCount(Player player);

    boolean banHand(Player player);

    boolean hidePassenger();

    int zoomFov();
}
