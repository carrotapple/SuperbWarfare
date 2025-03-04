package com.atsuishio.superbwarfare.entity.vehicle;

import net.minecraft.world.entity.Entity;

import java.util.List;

public interface MultiSeatVehicleEntity {
    List<Entity> getOrderedPassengers();

    Entity getNthEntity(int index);

    boolean changeSeat(Entity entity, int index);

    int getSeatIndex(Entity entity);

    int getSeatCount();
}
