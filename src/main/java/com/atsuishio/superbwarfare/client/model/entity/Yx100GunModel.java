package com.atsuishio.superbwarfare.client.model.entity;

import com.atsuishio.superbwarfare.ModUtils;
import com.atsuishio.superbwarfare.entity.vehicle.Yx100GunEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class Yx100GunModel extends GeoModel<Yx100GunEntity> {

    @Override
    public ResourceLocation getAnimationResource(Yx100GunEntity entity) {
        return null;
    }

    @Override
    public ResourceLocation getModelResource(Yx100GunEntity entity) {
        return ModUtils.loc("geo/yx_100_gun.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Yx100GunEntity entity) {
        return ModUtils.loc("textures/entity/yx_100.png");
    }

    @Override
    public void setCustomAnimations(Yx100GunEntity animatable, long instanceId, AnimationState<Yx100GunEntity> animationState) {
        CoreGeoBone bone = getAnimationProcessor().getBone("barrel");
        EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
        bone.setRotX((entityData.headPitch()) * Mth.DEG_TO_RAD);
    }
}
