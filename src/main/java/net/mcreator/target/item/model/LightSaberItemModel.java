package net.mcreator.target.item.model;

import software.bernie.geckolib.model.GeoModel;

import net.minecraft.resources.ResourceLocation;

import net.mcreator.target.item.LightSaberItem;

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
