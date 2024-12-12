package com.atsuishio.superbwarfare.client.model.entity;

import com.atsuishio.superbwarfare.ModUtils;
import com.atsuishio.superbwarfare.entity.SpeedboatEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

import static com.atsuishio.superbwarfare.entity.SpeedboatEntity.DELTA_ROT;
import static com.atsuishio.superbwarfare.entity.SpeedboatEntity.POWER;

public class SpeedboatModel extends GeoModel<SpeedboatEntity> {
    public static float lerpRotY = 0f;
    public static float rotorSpeed = 0f;

    @Override
    public ResourceLocation getAnimationResource(SpeedboatEntity entity) {
        return null;
//        return ModUtils.loc("animations/mk_42.animation.json");
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
        float times = Minecraft.getInstance().getDeltaFrameTime();

        CoreGeoBone rotor = getAnimationProcessor().getBone("Rotor");
        CoreGeoBone duo = getAnimationProcessor().getBone("duo");

        rotorSpeed = Mth.lerp(0.1f * times, rotorSpeed, 10 * animatable.getEntityData().get(POWER));
        rotor.setRotZ(rotor.getRotZ() + rotorSpeed);

        lerpRotY = Mth.lerp(0.5f * times, lerpRotY, animatable.getEntityData().get(POWER) > 0 ? animatable.getEntityData().get(DELTA_ROT) : -animatable.getEntityData().get(DELTA_ROT));

        duo.setRotY(0.5f * lerpRotY);
    }
}
