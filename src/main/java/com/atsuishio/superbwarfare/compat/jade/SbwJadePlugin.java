package com.atsuishio.superbwarfare.compat.jade;


import com.atsuishio.superbwarfare.compat.jade.providers.EnergyVehicleProvider;
import com.atsuishio.superbwarfare.compat.jade.providers.VehicleHealthProvider;
import com.atsuishio.superbwarfare.entity.vehicle.EnergyVehicleEntity;
import com.atsuishio.superbwarfare.entity.vehicle.VehicleEntity;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;


@WailaPlugin
public class SbwJadePlugin implements IWailaPlugin {
    @Override
    public void register(IWailaCommonRegistration registration) {
        registration.registerEntityDataProvider(EnergyVehicleProvider.INSTANCE, EnergyVehicleEntity.class);
    }

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerEntityComponent(VehicleHealthProvider.INSTANCE, VehicleEntity.class);
        registration.registerEntityComponent(EnergyVehicleProvider.INSTANCE, EnergyVehicleEntity.class);
    }
}
