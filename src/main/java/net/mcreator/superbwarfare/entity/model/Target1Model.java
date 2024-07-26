package net.mcreator.superbwarfare.entity.model;

import net.mcreator.superbwarfare.entity.Target1Entity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class Target1Model extends GeoModel<Target1Entity> {
    @Override
    public ResourceLocation getAnimationResource(Target1Entity entity) {
        return new ResourceLocation("target", "animations/superbwarfare.animation.json");
    }

    @Override
    public ResourceLocation getModelResource(Target1Entity entity) {
        return new ResourceLocation("target", "geo/superbwarfare.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Target1Entity entity) {
        return new ResourceLocation("target", "textures/entity/superbwarfare.png");
    }

    @Override
    public void setCustomAnimations(Target1Entity animatable, long instanceId, AnimationState animationState) {
        CoreGeoBone head = getAnimationProcessor().getBone("ba");
        EntityModelData entityData = (EntityModelData) animationState.getData(DataTickets.ENTITY_MODEL_DATA);
        head.setRotX(entityData.headPitch() * Mth.DEG_TO_RAD);
    }
}
