package com.atsuishio.superbwarfare.client.model.entity;

import com.atsuishio.superbwarfare.ModUtils;
import com.atsuishio.superbwarfare.entity.TargetEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class TargetModel extends GeoModel<TargetEntity> {
    @Override
    public ResourceLocation getAnimationResource(TargetEntity entity) {
        return new ResourceLocation(ModUtils.MODID, "animations/target.animation.json");
    }

    @Override
    public ResourceLocation getModelResource(TargetEntity entity) {
        return new ResourceLocation(ModUtils.MODID, "geo/target.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(TargetEntity entity) {
        return new ResourceLocation(ModUtils.MODID, "textures/entity/target.png");
    }
}
