package com.atsuishio.superbwarfare.compat.jade.providers;

import com.atsuishio.superbwarfare.ModUtils;
import com.atsuishio.superbwarfare.entity.vehicle.EnergyVehicleEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.EntityAccessor;
import snownee.jade.api.IEntityComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public enum EnergyVehicleProvider implements IEntityComponentProvider,
        IServerDataProvider<EntityAccessor> {
    INSTANCE;

    private static final ResourceLocation ID = new ResourceLocation(ModUtils.MODID, "energy_vehicle");


    @Override
    public void appendTooltip(
            ITooltip tooltip,
            EntityAccessor accessor,
            IPluginConfig config
    ) {
        // TODO 正确实现能量显示
        if (!accessor.getServerData().contains("Energy")) return;

        tooltip.add(
                Component.translatable(
                        accessor.getServerData().getInt("Energy")
                                + " / "
                                + accessor.getServerData().getInt("MaxEnergy")
                )
        );
    }

    @Override
    public void appendServerData(CompoundTag data, EntityAccessor accessor) {
        var vehicle = (EnergyVehicleEntity) accessor.getEntity();
        data.putInt("Energy", vehicle.getEnergy());
        data.putInt("MaxEnergy", vehicle.getMaxEnergy());
    }

    @Override
    public ResourceLocation getUid() {
        return ID;
    }
}