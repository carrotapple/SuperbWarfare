package com.atsuishio.superbwarfare.client.model.entity;

import com.atsuishio.superbwarfare.ModUtils;
import com.atsuishio.superbwarfare.entity.vehicle.LaserTowerEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

import static com.atsuishio.superbwarfare.entity.vehicle.LaserTowerEntity.LASER_LENGTH;

public class LaserTowerModel extends GeoModel<LaserTowerEntity> {

    @Override
    public ResourceLocation getAnimationResource(LaserTowerEntity entity) {
        return ModUtils.loc("animations/laser_tower.animation.json");
    }

    @Override
    public ResourceLocation getModelResource(LaserTowerEntity entity) {
        return ModUtils.loc("geo/laser_tower.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(LaserTowerEntity entity) {
        return ModUtils.loc("textures/entity/laser_tower.png");
    }

    @Override
    public void setCustomAnimations(LaserTowerEntity animatable, long instanceId, AnimationState<LaserTowerEntity> animationState) {
        CoreGeoBone laser = getAnimationProcessor().getBone("laser");
        laser.setScaleZ(10 * animatable.getEntityData().get(LASER_LENGTH));
    }
}
