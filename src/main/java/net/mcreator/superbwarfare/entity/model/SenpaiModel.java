package net.mcreator.superbwarfare.entity.model;

import net.mcreator.superbwarfare.ModUtils;
import net.mcreator.superbwarfare.entity.SenpaiEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class SenpaiModel extends GeoModel<SenpaiEntity> {
    @Override
    public ResourceLocation getAnimationResource(SenpaiEntity entity) {
        return ModUtils.loc("animations/senpai.animation.json");
    }

    @Override
    public ResourceLocation getModelResource(SenpaiEntity entity) {
        return ModUtils.loc("geo/senpai.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(SenpaiEntity entity) {
        return ModUtils.loc("textures/entity/senpai.png");
    }

}
