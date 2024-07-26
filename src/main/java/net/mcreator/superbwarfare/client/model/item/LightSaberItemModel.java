package net.mcreator.superbwarfare.client.model.item;

import net.mcreator.superbwarfare.item.LightSaber;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class LightSaberItemModel extends GeoModel<LightSaber> {
    @Override
    public ResourceLocation getAnimationResource(LightSaber animatable) {
        return new ResourceLocation("target", "animations/lightsaber.animation.json");
    }

    @Override
    public ResourceLocation getModelResource(LightSaber animatable) {
        return new ResourceLocation("target", "geo/lightsaber.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(LightSaber animatable) {
        return new ResourceLocation("target", "textures/item/lightsaber.png");
    }
}
