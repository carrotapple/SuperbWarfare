package com.atsuishio.superbwarfare.client.model.entity;

import com.atsuishio.superbwarfare.ModUtils;
import com.atsuishio.superbwarfare.entity.projectile.SwarmDroneEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class SwarmDroneModel extends GeoModel<SwarmDroneEntity> {

    @Override
    public ResourceLocation getAnimationResource(SwarmDroneEntity entity) {
        return ModUtils.loc("animations/swarm_drone.animation.json");
    }

    @Override
    public ResourceLocation getModelResource(SwarmDroneEntity entity) {
        return ModUtils.loc("geo/swarm_drone.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(SwarmDroneEntity entity) {
        return ModUtils.loc("textures/entity/swamDrone.png");
    }
}
