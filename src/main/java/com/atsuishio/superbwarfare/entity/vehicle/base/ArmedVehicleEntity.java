package com.atsuishio.superbwarfare.entity.vehicle.base;

import net.minecraft.world.entity.player.Player;

public interface ArmedVehicleEntity {

    /**
     * 载具开火
     *
     * @param player 玩家
     */
    void vehicleShoot(Player player);

    /**
     * 判断指定玩家是否是载具驾驶员
     *
     * @param player 玩家
     * @return 是否是驾驶员
     */
    boolean isDriver(Player player);

    /**
     * 主武器射速
     *
     * @return 射速
     */
    int mainGunRpm();

    /**
     * 当前情况载具是否可以开火
     *
     * @param player 玩家
     * @return 是否可以开火
     */
    boolean canShoot(Player player);

    /**
     * 获取当前选择的主武器的备弹数量
     *
     * @param player 玩家
     * @return 备弹数量
     */
    int getAmmoCount(Player player);

    /**
     * 是否隐藏玩家手臂
     *
     * @param player 玩家
     * @return 是否隐藏
     */
    boolean banHand(Player player);

    /**
     * 是否隐藏载具上的玩家
     *
     * @return 是否隐藏
     */
    boolean hidePassenger();

    /**
     * 瞄准时的放大倍率
     *
     * @return 放大倍率
     */
    int zoomFov();
}
