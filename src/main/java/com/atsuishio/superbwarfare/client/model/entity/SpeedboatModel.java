package com.atsuishio.superbwarfare.client.model.entity;

import com.atsuishio.superbwarfare.ModUtils;
import com.atsuishio.superbwarfare.entity.SpeedboatEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

public class SpeedboatModel extends GeoModel<SpeedboatEntity> {

    @Override
    public ResourceLocation getAnimationResource(SpeedboatEntity entity) {
        return null;
//        return ModUtils.loc("animations/mk_42.animation.json");
    }

    @Override
    public ResourceLocation getModelResource(SpeedboatEntity entity) {
        return ModUtils.loc("geo/speedboat.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(SpeedboatEntity entity) {
        return ModUtils.loc("textures/entity/speedboat.png");
    }

    @Override
    public void setCustomAnimations(SpeedboatEntity animatable, long instanceId, AnimationState<SpeedboatEntity> animationState) {
    }
}
