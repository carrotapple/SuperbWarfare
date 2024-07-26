package net.mcreator.superbwarfare.entity.model;

import net.mcreator.superbwarfare.ModUtils;
import net.mcreator.superbwarfare.entity.ClaymoreEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class ClaymoreModel extends GeoModel<ClaymoreEntity> {
    @Override
    public ResourceLocation getAnimationResource(ClaymoreEntity entity) {
        return new ResourceLocation(ModUtils.MODID, "animations/claymore.animation.json");
    }

    @Override
    public ResourceLocation getModelResource(ClaymoreEntity entity) {
        return new ResourceLocation(ModUtils.MODID, "geo/claymore.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(ClaymoreEntity entity) {
        return new ResourceLocation(ModUtils.MODID, "textures/entity/claymore.png");
    }

}
