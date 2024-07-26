package net.mcreator.superbwarfare.entity.model;

import net.mcreator.superbwarfare.entity.SenpaiEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

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


}
