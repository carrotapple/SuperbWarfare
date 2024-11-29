package com.atsuishio.superbwarfare.entity.model;

import com.atsuishio.superbwarfare.ModUtils;
import com.atsuishio.superbwarfare.entity.BeamEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

import static com.atsuishio.superbwarfare.entity.BeamEntity.LENGTH;

public class BeamModel extends GeoModel<BeamEntity> {

    @Override
    public ResourceLocation getAnimationResource(BeamEntity entity) {
        return null;
    }

    @Override
    public ResourceLocation getModelResource(BeamEntity entity) {
        return ModUtils.loc("geo/laser_beam.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(BeamEntity entity) {
        return ModUtils.loc("textures/entity/laser_beam.png");
    }

    @Override
    public void setCustomAnimations(BeamEntity animatable, long instanceId, AnimationState animationState) {
        CoreGeoBone bone = getAnimationProcessor().getBone("bone");
        bone.setScaleX(3);
        bone.setScaleY(3);
        bone.setScaleZ(2 * (float)(Mth.clamp(animatable.getEntityData().get(LENGTH), 0.1, 512)));
//        bone.setHidden(Minecraft.getInstance().options.getCameraType() == CameraType.FIRST_PERSON && animatable.getOwner() == Minecraft.getInstance().player);
    }
}
