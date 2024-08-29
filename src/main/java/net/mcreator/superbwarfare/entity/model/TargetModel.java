package net.mcreator.superbwarfare.entity.model;

import net.mcreator.superbwarfare.ModUtils;
import net.mcreator.superbwarfare.entity.TargetEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class TargetModel extends GeoModel<TargetEntity> {
    @Override
    public ResourceLocation getAnimationResource(TargetEntity entity) {
        return new ResourceLocation(ModUtils.MODID, "animations/target.animation.json");
    }

    @Override
    public ResourceLocation getModelResource(TargetEntity entity) {
        return new ResourceLocation(ModUtils.MODID, "geo/target.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(TargetEntity entity) {
        return new ResourceLocation(ModUtils.MODID, "textures/entity/target.png");
    }

    @Override
    public void setCustomAnimations(TargetEntity animatable, long instanceId, AnimationState animationState) {
        CoreGeoBone head = getAnimationProcessor().getBone("ba");
        EntityModelData entityData = (EntityModelData) animationState.getData(DataTickets.ENTITY_MODEL_DATA);
        if (entityData.headPitch() > 10) {
            head.setRotX(Mth.clamp(Mth.clamp(90 - entityData.headPitch(),0,4) * 22.5f * Mth.DEG_TO_RAD,0,90));
        } else {
            head.setRotX(Mth.clamp(9f * entityData.headPitch() * Mth.DEG_TO_RAD,0,90));
        }
    }
}
