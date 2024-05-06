package net.mcreator.target.client.model.item;

import net.mcreator.target.item.common.ammo.RocketItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class RocketItemModel extends GeoModel<RocketItem> {
    @Override
    public ResourceLocation getAnimationResource(RocketItem animatable) {
        return new ResourceLocation("target", "animations/rpg.head.animation.json");
    }

    @Override
    public ResourceLocation getModelResource(RocketItem animatable) {
        return new ResourceLocation("target", "geo/rpg.head.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(RocketItem animatable) {
        return new ResourceLocation("target", "textures/item/rpg7.png");
    }
}
