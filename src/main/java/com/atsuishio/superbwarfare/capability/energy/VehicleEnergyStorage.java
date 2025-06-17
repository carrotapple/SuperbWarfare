package com.atsuishio.superbwarfare.capability.energy;

import com.atsuishio.superbwarfare.data.vehicle.VehicleData;
import com.atsuishio.superbwarfare.entity.vehicle.base.EnergyVehicleEntity;

public class VehicleEnergyStorage extends SyncedEntityEnergyStorage {

    protected EnergyVehicleEntity vehicle;

    public VehicleEnergyStorage(EnergyVehicleEntity vehicle) {
        super(Integer.MAX_VALUE, vehicle.getEntityData(), vehicle.getEnergyDataAccessor());

        this.vehicle = vehicle;
        this.maxExtract = Integer.MAX_VALUE;
        this.maxReceive = Integer.MAX_VALUE;
    }

    @Override
    public int getMaxEnergyStored() {
        return VehicleData.from(vehicle).maxEnergy();
    }
}
