package com.atsuishio.superbwarfare.entity;

public interface IChargeEntity {

    void charge(int amount);

    boolean canCharge();
    int getEnergy();
    int getMaxEnergy();
}
