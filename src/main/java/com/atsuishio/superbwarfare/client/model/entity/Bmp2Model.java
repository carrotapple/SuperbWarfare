package com.atsuishio.superbwarfare.client.model.entity;

import com.atsuishio.superbwarfare.ModUtils;
import com.atsuishio.superbwarfare.entity.vehicle.Bmp2Entity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

import static com.atsuishio.superbwarfare.entity.vehicle.Bmp2Entity.TRACK_L;
import static com.atsuishio.superbwarfare.entity.vehicle.Bmp2Entity.TRACK_R;

public class Bmp2Model extends GeoModel<Bmp2Entity> {

    @Override
    public ResourceLocation getAnimationResource(Bmp2Entity entity) {
        return ModUtils.loc("animations/lav.animation.json");
    }

    @Override
    public ResourceLocation getModelResource(Bmp2Entity entity) {
        return ModUtils.loc("geo/bmp2.geo.json");
//        Player player = Minecraft.getInstance().player;
//
//        int distance = 0;
//
//        if (player != null) {
//            distance = (int) player.position().distanceTo(entity.position());
//        }
//
//        if (distance < 32) {
//            return ModUtils.loc("geo/Bmp2.geo.json");
//        } else {
//            return ModUtils.loc("geo/speedboat.lod1.geo.json");
//        }
    }

    @Override
    public ResourceLocation getTextureResource(Bmp2Entity entity) {
        return ModUtils.loc("textures/entity/bmp2.png");
    }

    @Override
    public void setCustomAnimations(Bmp2Entity animatable, long instanceId, AnimationState<Bmp2Entity> animationState) {

        for (int i = 0; i < 50; i++) {
            CoreGeoBone trackL = getAnimationProcessor().getBone("trackL" + i);
            CoreGeoBone trackLRot = getAnimationProcessor().getBone("trackLRot" + i);
            CoreGeoBone trackR = getAnimationProcessor().getBone("trackR" + i);
            CoreGeoBone trackRRot = getAnimationProcessor().getBone("trackRRot" + i);

            float t = animatable.getEntityData().get(TRACK_L) + 2 * i;

            if (t >= 100) {
                t -= 100;
            }

            if (t < 37.5) {
                trackL.setPosY(0);
                trackL.setPosZ(3 * t);
                trackLRot.setRotX(0);
            }

            if (t >= 37.5 && t < 39.5) {
                trackL.setPosY(-(t - 37.5f) * 3 * 0.7071f);
                trackL.setPosZ(3 * 37.5f + (t - 37.5f) * 3 * 0.7071f);
                trackLRot.setRotX(45 * Mth.DEG_TO_RAD);
            }

            if (t >= 39.5 && t < 41.5) {
                trackL.setPosY(-5.3f - (t - 39.5f) * 3);
                trackL.setPosZ(116);
                trackLRot.setRotX(90 * Mth.DEG_TO_RAD);
            }

            if (t >= 41.5 && t < 43) {
                trackL.setPosY(-11.6f - (t - 41.5f) * 3 * 0.7071f);
                trackL.setPosZ(115 - (t - 41.5f) * 3 * 0.7071f);
                trackLRot.setRotX(135 * Mth.DEG_TO_RAD);
            }

            if (t >= 43 && t < 49.5) {
                trackL.setPosY(-15.6f - (t - 43f) * 3 * 0.45f);
                trackL.setPosZ(109.5f - (t - 43f) * 3 * 0.75f);
                trackLRot.setRotX(150 * Mth.DEG_TO_RAD);
            }

            if (t >= 49.5 && t < 76.5) {
                trackL.setPosY(-23.5f);
                trackL.setPosZ(95f - (t - 49.5f) * 3);
                trackLRot.setRotX(180 * Mth.DEG_TO_RAD);
            }

            if (t >= 76.5 && t < 83.5) {
                trackL.setPosY(-23.5f + (t - 76.5f) * 3 * 0.45f);
                trackL.setPosZ(13.5f - (t - 76.5f) * 3 * 0.75f);
                trackLRot.setRotX(210 * Mth.DEG_TO_RAD);
            }

            if (t >= 83.5 && t < 85.5) {
                trackL.setPosY(-12.7f + (t - 83.5f) * 3 * 0.7071f);
                trackL.setPosZ(-3.5f - (t - 83.5f) * 3 * 0.7071f);
                trackLRot.setRotX(225 * Mth.DEG_TO_RAD);
            }

            if (t >= 85.5 && t < 87) {
                trackL.setPosY(-9.2f + (t - 85.5f) * 3);
                trackL.setPosZ(-6.9f);
                trackLRot.setRotX(270 * Mth.DEG_TO_RAD);
            }

            if (t >= 87 && t < 89) {
                trackL.setPosY(-4.3f + (t - 87f) * 3 * 0.7071f);
                trackL.setPosZ(-6.9f + (t - 87f) * 3 * 0.7071f);
                trackLRot.setRotX(315 * Mth.DEG_TO_RAD);
            }

            float t2 = animatable.getEntityData().get(TRACK_R) + 2 * i;

            if (t2 >= 100) {
                t2 -= 100;
            }

            if (t2 < 37.5) {
                trackR.setPosY(0);
                trackR.setPosZ(3 * t2);
                trackRRot.setRotX(0);
            }

            if (t2>= 37.5 && t2< 39.5) {
                trackR.setPosY(-(t2- 37.5f) * 3 * 0.7071f);
                trackR.setPosZ(3 * 37.5f + (t2- 37.5f) * 3 * 0.7071f);
                trackRRot.setRotX(45 * Mth.DEG_TO_RAD);
            }

            if (t2>= 39.5 && t2< 41.5) {
                trackR.setPosY(-5.3f - (t2- 39.5f) * 3);
                trackR.setPosZ(116);
                trackRRot.setRotX(90 * Mth.DEG_TO_RAD);
            }

            if (t2>= 41.5 && t2< 43) {
                trackR.setPosY(-11.6f - (t2- 41.5f) * 3 * 0.7071f);
                trackR.setPosZ(115 - (t2- 41.5f) * 3 * 0.7071f);
                trackRRot.setRotX(135 * Mth.DEG_TO_RAD);
            }

            if (t2>= 43 && t2< 49.5) {
                trackR.setPosY(-15.6f - (t2- 43f) * 3 * 0.45f);
                trackR.setPosZ(109.5f - (t2- 43f) * 3 * 0.75f);
                trackRRot.setRotX(150 * Mth.DEG_TO_RAD);
            }

            if (t2>= 49.5 && t2< 76.5) {
                trackR.setPosY(-23.5f);
                trackR.setPosZ(95f - (t2- 49.5f) * 3);
                trackRRot.setRotX(180 * Mth.DEG_TO_RAD);
            }

            if (t2>= 76.5 && t2< 83.5) {
                trackR.setPosY(-23.5f + (t2- 76.5f) * 3 * 0.45f);
                trackR.setPosZ(13.5f - (t2- 76.5f) * 3 * 0.75f);
                trackRRot.setRotX(210 * Mth.DEG_TO_RAD);
            }

            if (t2>= 83.5 && t2< 85.5) {
                trackR.setPosY(-12.7f + (t2- 83.5f) * 3 * 0.7071f);
                trackR.setPosZ(-3.5f - (t2- 83.5f) * 3 * 0.7071f);
                trackRRot.setRotX(225 * Mth.DEG_TO_RAD);
            }

            if (t2>= 85.5 && t2< 87) {
                trackR.setPosY(-9.2f + (t2- 85.5f) * 3);
                trackR.setPosZ(-6.9f);
                trackRRot.setRotX(270 * Mth.DEG_TO_RAD);
            }

            if (t2>= 87 && t2< 89) {
                trackR.setPosY(-4.3f + (t2- 87f) * 3 * 0.7071f);
                trackR.setPosZ(-6.9f + (t2- 87f) * 3 * 0.7071f);
                trackRRot.setRotX(315 * Mth.DEG_TO_RAD);
            }
        }
    }
}
