package net.mcreator.target.entity.model;

import net.mcreator.target.entity.ClaymoreEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class ClaymoreModel extends GeoModel<ClaymoreEntity> {
    @Override
    public ResourceLocation getAnimationResource(ClaymoreEntity entity) {
        return new ResourceLocation("target", "animations/claymore.animation.json");
    }

    @Override
    public ResourceLocation getModelResource(ClaymoreEntity entity) {
        return new ResourceLocation("target", "geo/claymore.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(ClaymoreEntity entity) {
        return new ResourceLocation("target", "textures/entity/claymore.png");
    }

}
