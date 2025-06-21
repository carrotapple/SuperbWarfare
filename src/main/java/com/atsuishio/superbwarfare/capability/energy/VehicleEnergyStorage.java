package com.atsuishio.superbwarfare.capability.energy;

import com.atsuishio.superbwarfare.data.vehicle.VehicleData;
import com.atsuishio.superbwarfare.entity.vehicle.base.EnergyVehicleEntity;

public class VehicleEnergyStorage extends SyncedEntityEnergyStorage {

    protected EnergyVehicleEntity vehicle;

    public VehicleEnergyStorage(EnergyVehicleEntity vehicle) {
        super(Integer.MAX_VALUE, vehicle.getEntityData(), vehicle.getEnergyDataAccessor());

        this.vehicle = vehicle;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        if (VehicleData.from(vehicle).isDefaultData) return 0;

        this.maxExtract = getMaxEnergyStored();
        return super.extractEnergy(maxExtract, simulate);
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        if (VehicleData.from(vehicle).isDefaultData) return 0;

        this.maxReceive = getMaxEnergyStored();
        return super.receiveEnergy(maxReceive, simulate);
    }

    @Override
    public boolean canReceive() {
        return !VehicleData.from(vehicle).isDefaultData && super.canReceive();
    }

    @Override
    public boolean canExtract() {
        return !VehicleData.from(vehicle).isDefaultData && super.canExtract();
    }

    @Override
    public int getMaxEnergyStored() {
        return VehicleData.from(vehicle).maxEnergy();
    }
}
