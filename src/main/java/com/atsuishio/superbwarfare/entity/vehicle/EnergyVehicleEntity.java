package com.atsuishio.superbwarfare.entity.vehicle;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import org.joml.Math;

public class EnergyVehicleEntity extends VehicleEntity implements IChargeEntity {

    public static final EntityDataAccessor<Integer> ENERGY = SynchedEntityData.defineId(EnergyVehicleEntity.class, EntityDataSerializers.INT);

    public EnergyVehicleEntity(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.setEnergy(0);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ENERGY, 0);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.entityData.set(ENERGY, compound.getInt("Energy"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("Energy", this.entityData.get(ENERGY));
    }

    /**
     * 消耗指定电量
     *
     * @param amount 要消耗的电量
     */
    public void consumeEnergy(int amount) {
        this.setEnergy(Math.max(0, this.getEnergy() - amount));
    }

    public boolean canConsume(int amount) {
        return this.getEnergy() >= amount;
    }

    public int getEnergy() {
        return this.entityData.get(ENERGY);
    }

    public void setEnergy(int pEnergy) {
        this.entityData.set(ENERGY, Mth.clamp(pEnergy, 0, this.getMaxEnergy()));
    }

    public int getMaxEnergy() {
        return 100000;
    }

    @Override
    public void charge(int amount) {
        long energy = (long) this.getEnergy() + (long) amount;
        int e = energy > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) energy;
        this.setEnergy(Math.min(e, this.getMaxEnergy()));
    }

    @Override
    public boolean canCharge() {
        return this.getEnergy() < this.getMaxEnergy();
    }
}
