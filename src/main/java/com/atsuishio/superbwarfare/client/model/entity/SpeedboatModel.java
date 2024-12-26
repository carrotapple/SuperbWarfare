package com.atsuishio.superbwarfare.client.model.entity;

import com.atsuishio.superbwarfare.ModUtils;
import com.atsuishio.superbwarfare.entity.SpeedboatEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

import static com.atsuishio.superbwarfare.entity.SpeedboatEntity.*;

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

    @Override
    public void setCustomAnimations(SpeedboatEntity animatable, long instanceId, AnimationState<SpeedboatEntity> animationState) {
        CoreGeoBone rotor = getAnimationProcessor().getBone("Rotor");
        CoreGeoBone duo = getAnimationProcessor().getBone("duo");

        rotor.setRotZ(5 * animatable.getEntityData().get(ROTOR));
        duo.setRotY((animatable.getEntityData().get(POWER) > 0 ? -0.5f : 0.5f) * animatable.getEntityData().get(DELTA_ROT));
    }
}
