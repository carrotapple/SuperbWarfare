package net.mcreator.target.entity.model;

import net.mcreator.target.entity.Mk42Entity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class Mk42Model extends GeoModel<Mk42Entity> {
    @Override
    public ResourceLocation getAnimationResource(Mk42Entity entity) {
        return new ResourceLocation("target", "animations/mk_42.animation.json");
    }

    @Override
    public ResourceLocation getModelResource(Mk42Entity entity) {
        return new ResourceLocation("target", "geo/sherman.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Mk42Entity entity) {
        return new ResourceLocation("target", "textures/entity/sherman.png");
    }

    @Override
    public void setCustomAnimations(Mk42Entity animatable, long instanceId, AnimationState animationState) {
        CoreGeoBone barrle = getAnimationProcessor().getBone("maingun");
        EntityModelData entityData = (EntityModelData) animationState.getData(DataTickets.ENTITY_MODEL_DATA);
        barrle.setRotX((entityData.headPitch()) * Mth.DEG_TO_RAD);
    }
}
