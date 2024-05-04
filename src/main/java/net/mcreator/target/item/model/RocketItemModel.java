package net.mcreator.target.item.model;

import software.bernie.geckolib.model.GeoModel;

import net.minecraft.resources.ResourceLocation;

import net.mcreator.target.item.RocketItem;

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
