package com.atsuishio.superbwarfare.entity.vehicle;

import net.minecraft.world.entity.Entity;

public interface MultiSeatVehicleEntity {
    public Entity getNthEntity(int index);

    public boolean changeSeat(Entity entity, int index);

    public int getSeatIndex(Entity entity);

    public int getSeatCount();
}
