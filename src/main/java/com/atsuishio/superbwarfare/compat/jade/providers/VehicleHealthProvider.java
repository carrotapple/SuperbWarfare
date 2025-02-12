package com.atsuishio.superbwarfare.compat.jade.providers;

import com.atsuishio.superbwarfare.ModUtils;
import com.atsuishio.superbwarfare.entity.vehicle.VehicleEntity;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.EntityAccessor;
import snownee.jade.api.IEntityComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.impl.ui.HealthElement;

public enum VehicleHealthProvider implements IEntityComponentProvider {
    INSTANCE;

    private static final ResourceLocation ID = new ResourceLocation(ModUtils.MODID, "vehicle_health");

    public void appendTooltip(ITooltip tooltip, EntityAccessor accessor, IPluginConfig config) {
        // 对EntityHealthProvider的拙劣模仿

        var vehicle = (VehicleEntity) accessor.getEntity();
        float health = vehicle.getHealth();
        float maxHealth = vehicle.getMaxHealth();
        tooltip.add(new HealthElement(maxHealth, health));
    }

    public ResourceLocation getUid() {
        return ID;
    }

    public int getDefaultPriority() {
        return -4501;
    }
}

