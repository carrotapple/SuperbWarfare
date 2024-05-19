package net.mcreator.target.entity.model;

import net.mcreator.target.entity.SenpaiEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class SenpaiModel extends GeoModel<SenpaiEntity> {
    @Override
    public ResourceLocation getAnimationResource(SenpaiEntity entity) {
        return new ResourceLocation("target", "animations/senpai.animation.json");
    }

    @Override
    public ResourceLocation getModelResource(SenpaiEntity entity) {
        return new ResourceLocation("target", "geo/senpai.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(SenpaiEntity entity) {
        return new ResourceLocation("target", "textures/entity/senpai.png");
    }

    @Override
    public void setCustomAnimations(SenpaiEntity animatable, long instanceId, AnimationState animationState) {
        CoreGeoBone head = getAnimationProcessor().getBone("head");
        if (head != null) {
            EntityModelData entityData = (EntityModelData) animationState.getData(DataTickets.ENTITY_MODEL_DATA);
            head.setRotX(entityData.headPitch() * Mth.DEG_TO_RAD);
            head.setRotY(entityData.netHeadYaw() * Mth.DEG_TO_RAD);
        }

    }
}
