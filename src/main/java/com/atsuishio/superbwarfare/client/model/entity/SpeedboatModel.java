package com.atsuishio.superbwarfare.client.model.entity;

import com.atsuishio.superbwarfare.ModUtils;
import com.atsuishio.superbwarfare.entity.vehicle.SpeedboatEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class SpeedboatModel extends GeoModel<SpeedboatEntity> {

    @Override
    public ResourceLocation getAnimationResource(SpeedboatEntity entity) {
        return ModUtils.loc("animations/speedboat.animation.json");
    }

    @Override
    public ResourceLocation getModelResource(SpeedboatEntity entity) {
        return new ResourceLocation(ModUtils.MODID, "geo/speedboat.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(SpeedboatEntity entity) {
        return ModUtils.loc("textures/entity/speedboat.png");
    }
}
