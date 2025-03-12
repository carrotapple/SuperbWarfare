package com.atsuishio.superbwarfare.client.model.entity;

import com.atsuishio.superbwarfare.ModUtils;
import com.atsuishio.superbwarfare.entity.vehicle.Yx100Entity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

public class Yx100Model extends GeoModel<Yx100Entity> {

    @Override
    public ResourceLocation getAnimationResource(Yx100Entity entity) {
        return ModUtils.loc("animations/yx_100.animation.json");
    }

    @Override
    public ResourceLocation getModelResource(Yx100Entity entity) {
        return ModUtils.loc("geo/yx_100.geo.json");
//        Player player = Minecraft.getInstance().player;
//
//        int distance = 0;
//
//        if (player != null) {
//            distance = (int) player.position().distanceTo(entity.position());
//        }
//
//        if (distance < 32) {
//            return ModUtils.loc("geo/Yx100.geo.json");
//        } else {
//            return ModUtils.loc("geo/speedboat.lod1.geo.json");
//        }
    }

    @Override
    public ResourceLocation getTextureResource(Yx100Entity entity) {
        return ModUtils.loc("textures/entity/yx_100.png");
    }

    @Override
    public void setCustomAnimations(Yx100Entity animatable, long instanceId, AnimationState<Yx100Entity> animationState) {
        for (int i = 0; i < 40; i++) {
            CoreGeoBone trackL = getAnimationProcessor().getBone("trackL" + i);
            CoreGeoBone trackLRot = getAnimationProcessor().getBone("trackLRot" + i);
            CoreGeoBone trackR = getAnimationProcessor().getBone("trackR" + i);
            CoreGeoBone trackRRot = getAnimationProcessor().getBone("trackRRot" + i);

            float t = animatable.getLeftTrack() + 2 * i;

            if (t >= 80) {
                t -= 80;
            }

            trackAnimation(trackL, trackLRot, t);

            float t2 = animatable.getRightTrack() + 2 * i;

            if (t2 >= 80) {
                t2 -= 80;
            }

            trackAnimation(trackR, trackRRot, t2);
        }
    }

    public void trackAnimation(CoreGeoBone track, CoreGeoBone trackRot, float t) {
        if (t < 34.5) {
            track.setPosY(0);
            track.setPosZ(4f * t);
            trackRot.setRotX(0);
        }

        if (t >= 34.5 && t < 35.75) {
            track.setPosY(-(t - 34.5f) * 4f * 0.7071f);
            track.setPosZ(4f * 34.5f + (t - 34.5f) * 4f * 0.7071f);
            trackRot.setRotX(45 * Mth.DEG_TO_RAD);
        }

        if (t >= 35.75 && t < 37.75) {
            track.setPosY(-4f - (t - 35.75f) * 4f);
            track.setPosZ(142);
            trackRot.setRotX(90 * Mth.DEG_TO_RAD);
        }

        if (t >= 37.75 && t < 44) {
            track.setPosY(-11.4f - (t - 37.75f) * 4.5f * 0.42f);
            track.setPosZ(141f - (t - 37.75f) * 4.5f * 0.75f);
            trackRot.setRotX(150 * Mth.DEG_TO_RAD);
        }

        if (t >= 44 && t < 70.75) {
            track.setPosY(-21.5f);
            track.setPosZ(120f - (t - 44f) * 4);
            trackRot.setRotX(180 * Mth.DEG_TO_RAD);
        }

        if (t >= 70.75 && t < 77) {
            track.setPosY(-21f + (t - 70.75f) * 4.5F * 0.42f);
            track.setPosZ(11.4f - (t - 70.75f) * 4.5f * 0.75f);
            trackRot.setRotX(210 * Mth.DEG_TO_RAD);
        }

        if (t >= 77 && t < 78.25) {
            track.setPosY(-7.8f + (t - 77f) * 4f);
            track.setPosZ(-10.6f);
            trackRot.setRotX(270 * Mth.DEG_TO_RAD);
        }

        if (t >= 78.25 && t < 80) {
            track.setPosY(-2.3f + (t - 78.25f) * 4 * 0.7071f);
            track.setPosZ(-9.6f + (t - 78.25f) * 4 * 0.7071f);
            trackRot.setRotX(315 * Mth.DEG_TO_RAD);
        }
    }
}
