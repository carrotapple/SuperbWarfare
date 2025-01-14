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

    public void extraEnergy(int extraAmount) {
        this.setEnergy(this.getEnergy() - extraAmount);
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
        this.setEnergy(Math.min(this.getEnergy() + amount, this.getMaxEnergy()));
    }

    @Override
    public boolean canCharge() {
        return this.getEnergy() < this.getMaxEnergy();
    }
}
