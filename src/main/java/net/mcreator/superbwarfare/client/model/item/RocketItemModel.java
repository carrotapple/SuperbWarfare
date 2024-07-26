package net.mcreator.superbwarfare.client.model.item;

import net.mcreator.superbwarfare.ModUtils;
import net.mcreator.superbwarfare.item.common.ammo.Rocket;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class RocketItemModel extends GeoModel<Rocket> {
    @Override
    public ResourceLocation getAnimationResource(Rocket animatable) {
        return new ResourceLocation(ModUtils.MODID, "animations/rpg.head.animation.json");
    }

    @Override
    public ResourceLocation getModelResource(Rocket animatable) {
        return new ResourceLocation(ModUtils.MODID, "geo/rpg.head.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Rocket animatable) {
        return new ResourceLocation(ModUtils.MODID, "textures/item/rpg7.png");
    }
}
