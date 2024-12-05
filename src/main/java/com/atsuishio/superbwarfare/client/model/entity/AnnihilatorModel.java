package com.atsuishio.superbwarfare.client.model.entity;

import com.atsuishio.superbwarfare.ModUtils;
import com.atsuishio.superbwarfare.entity.AnnihilatorEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

import static com.atsuishio.superbwarfare.entity.AnnihilatorEntity.*;

public class AnnihilatorModel extends GeoModel<AnnihilatorEntity> {

    @Override
    public ResourceLocation getAnimationResource(AnnihilatorEntity entity) {
        return ModUtils.loc("animations/annihilator.animation.json");
    }

    @Override
    public ResourceLocation getModelResource(AnnihilatorEntity entity) {

        return ModUtils.loc("geo/annihilator.geo.json");

//        Player player = Minecraft.getInstance().player;
//
//        int distance = 0;
//
//        if (player != null) {
//            distance = (int) player.position().distanceTo(entity.position());
//        }
//
//        if (distance < 32) {
//            return ModUtils.loc("geo/sherman.geo.json");
//        } else if (distance < 64) {
//            return ModUtils.loc("geo/sherman_lod1.geo.json");
//        } else {
//            return ModUtils.loc("geo/sherman_lod2.geo.json");
//        }
    }

    @Override
    public ResourceLocation getTextureResource(AnnihilatorEntity entity) {
        return ModUtils.loc("textures/entity/annihilator.png");
    }

    @Override
    public void setCustomAnimations(AnnihilatorEntity animatable, long instanceId, AnimationState<AnnihilatorEntity> animationState) {
        CoreGeoBone bone = getAnimationProcessor().getBone("PaoGuan");
        EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
        bone.setRotX((entityData.headPitch()) * Mth.DEG_TO_RAD);

        CoreGeoBone laserLeft = getAnimationProcessor().getBone("laser1");
        CoreGeoBone laserMiddle = getAnimationProcessor().getBone("laser2");
        CoreGeoBone laserRight = getAnimationProcessor().getBone("laser3");

        laserLeft.setScaleZ(animatable.getEntityData().get(LASER_LEFT_LENGTH) + 0.5f);
        laserMiddle.setScaleZ(animatable.getEntityData().get(LASER_MIDDLE_LENGTH) + 0.5f);
        laserRight.setScaleZ(animatable.getEntityData().get(LASER_RIGHT_LENGTH) + 0.5f);
    }
}
