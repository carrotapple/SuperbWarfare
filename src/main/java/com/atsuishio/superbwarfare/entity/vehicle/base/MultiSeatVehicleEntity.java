package com.atsuishio.superbwarfare.entity.vehicle.base;

import net.minecraft.world.entity.Entity;

import java.util.List;

/**
 * 拥有多个按顺序排列的座位的载具，每个座位可以为空
 * <p>
 * 需要为实体自己重写addPassenger、getControllingPassenger、getFirstPassenger、removePassenger等方法
 */

public interface MultiSeatVehicleEntity {

    /**
     * 获取按顺序排列的成员列表
     *
     * @return 按顺序排列的成员列表
     */
    List<Entity> getOrderedPassengers();

    /**
     * 获取第index个乘客
     *
     * @param index 目标座位
     * @return 目标座位的乘客
     */

    Entity getNthEntity(int index);

    /**
     * 尝试切换座位
     *
     * @param entity 乘客
     * @param index  目标座位
     * @return 是否切换成功
     */

    boolean changeSeat(Entity entity, int index);

    /**
     * 获取乘客所在座位索引
     *
     * @param entity 乘客
     * @return 座位索引
     */

    int getSeatIndex(Entity entity);

    /**
     * 获取座位数量
     *
     * @return 座位数量
     */
    int getSeatCount();
}
