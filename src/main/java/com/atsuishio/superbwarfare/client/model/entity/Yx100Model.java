package com.atsuishio.superbwarfare.client.model.entity;

import com.atsuishio.superbwarfare.ModUtils;
import com.atsuishio.superbwarfare.entity.vehicle.Yx100Entity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class Yx100Model extends GeoModel<Yx100Entity> {

    @Override
    public ResourceLocation getAnimationResource(Yx100Entity entity) {
        return null;
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

//    @Override
//    public void setCustomAnimations(Yx100Entity animatable, long instanceId, AnimationState<Yx100Entity> animationState) {
//        for (int i = 0; i < 50; i++) {
//            CoreGeoBone trackL = getAnimationProcessor().getBone("trackL" + i);
//            CoreGeoBone trackLRot = getAnimationProcessor().getBone("trackLRot" + i);
//            CoreGeoBone trackR = getAnimationProcessor().getBone("trackR" + i);
//            CoreGeoBone trackRRot = getAnimationProcessor().getBone("trackRRot" + i);
//
//            float t = animatable.getEntityData().get(TRACK_L) + 2 * i;
//
//            if (t >= 100) {
//                t -= 100;
//            }
//
//            trackAnimation(trackL, trackLRot, t);
//
//            float t2 = animatable.getEntityData().get(TRACK_R) + 2 * i;
//
//            if (t2 >= 100) {
//                t2 -= 100;
//            }
//
//            trackAnimation(trackR, trackRRot, t2);
//        }
//    }
//
//    public void trackAnimation(CoreGeoBone track, CoreGeoBone trackRot, float t) {
//        if (t < 37.5) {
//            track.setPosY(0);
//            track.setPosZ(3 * t);
//            trackRot.setRotX(0);
//        }
//
//        if (t >= 37.5 && t < 39.5) {
//            track.setPosY(-(t - 37.5f) * 3 * 0.7071f);
//            track.setPosZ(3 * 37.5f + (t - 37.5f) * 3 * 0.7071f);
//            trackRot.setRotX(45 * Mth.DEG_TO_RAD);
//        }
//
//        if (t >= 39.5 && t < 41.5) {
//            track.setPosY(-5.3f - (t - 39.5f) * 3);
//            track.setPosZ(116);
//            trackRot.setRotX(90 * Mth.DEG_TO_RAD);
//        }
//
//        if (t >= 41.5 && t < 43) {
//            track.setPosY(-11.6f - (t - 41.5f) * 3 * 0.7071f);
//            track.setPosZ(115 - (t - 41.5f) * 3 * 0.7071f);
//            trackRot.setRotX(135 * Mth.DEG_TO_RAD);
//        }
//
//        if (t >= 43 && t < 49.5) {
//            track.setPosY(-15.6f - (t - 43f) * 3 * 0.45f);
//            track.setPosZ(109.5f - (t - 43f) * 3 * 0.75f);
//            trackRot.setRotX(150 * Mth.DEG_TO_RAD);
//        }
//
//        if (t >= 49.5 && t < 76.5) {
//            track.setPosY(-23.5f);
//            track.setPosZ(95f - (t - 49.5f) * 3);
//            trackRot.setRotX(180 * Mth.DEG_TO_RAD);
//        }
//
//        if (t >= 76.5 && t < 83.5) {
//            track.setPosY(-23.5f + (t - 76.5f) * 3 * 0.45f);
//            track.setPosZ(13.5f - (t - 76.5f) * 3 * 0.75f);
//            trackRot.setRotX(210 * Mth.DEG_TO_RAD);
//        }
//
//        if (t >= 83.5 && t < 85.5) {
//            track.setPosY(-12.7f + (t - 83.5f) * 3 * 0.7071f);
//            track.setPosZ(-3.5f - (t - 83.5f) * 3 * 0.7071f);
//            trackRot.setRotX(225 * Mth.DEG_TO_RAD);
//        }
//
//        if (t >= 85.5 && t < 87) {
//            track.setPosY(-9.2f + (t - 85.5f) * 3);
//            track.setPosZ(-6.9f);
//            trackRot.setRotX(270 * Mth.DEG_TO_RAD);
//        }
//
//        if (t >= 87 && t < 89) {
//            track.setPosY(-4.3f + (t - 87f) * 3 * 0.7071f);
//            track.setPosZ(-6.9f + (t - 87f) * 3 * 0.7071f);
//            trackRot.setRotX(315 * Mth.DEG_TO_RAD);
//        }
//    }
}
