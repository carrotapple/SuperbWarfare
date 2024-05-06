package net.mcreator.target.client.model.item;

import net.mcreator.target.item.LightSaberItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class LightSaberItemModel extends GeoModel<LightSaberItem> {
    @Override
    public ResourceLocation getAnimationResource(LightSaberItem animatable) {
        return new ResourceLocation("target", "animations/lightsaber.animation.json");
    }

    @Override
    public ResourceLocation getModelResource(LightSaberItem animatable) {
        return new ResourceLocation("target", "geo/lightsaber.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(LightSaberItem animatable) {
        return new ResourceLocation("target", "textures/item/lightsaber.png");
    }
}
