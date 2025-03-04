package com.atsuishio.superbwarfare.entity.vehicle.base;

/**
 * 拥有任意武器的载具
 */
public interface WeaponVehicleEntity extends ArmedVehicleEntity {
    /**
     * 检测该槽位是否有可用武器
     *
     * @param index 武器槽位
     * @return 武器是否可用
     */
    default boolean hasWeapon(int index) {
        return getWeaponType(index) != -1;
    }

    /**
     * 切换武器事件
     *
     * @param index  武器槽位
     * @param scroll 滚动值，-1~1之间的整数
     */
    default void changeWeapon(int index, int scroll) {
    }

    /**
     * 获取该槽位当前的武器类型，返回-1则表示该位置没有可用武器
     *
     * @param index 武器槽位
     * @return 武器类型
     */
    default int getWeaponType(int index) {
        // 默认认为只有第一个位置拥有一个武器
        return index == 0 ? 0 : -1;
    }

    /**
     * 设置该槽位当前的武器类型
     *
     * @param index 武器槽位
     * @param type  武器类型
     */

    default void setWeaponType(int index, int type) {
    }
}
