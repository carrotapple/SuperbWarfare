package net.mcreator.target.entity.model;

import software.bernie.geckolib.model.GeoModel;

import net.minecraft.resources.ResourceLocation;

import net.mcreator.target.entity.TargetEntity;

public class TargetModel extends GeoModel<TargetEntity> {
	@Override
	public ResourceLocation getAnimationResource(TargetEntity entity) {
		return new ResourceLocation("target", "animations/target2.animation.json");
	}

	@Override
	public ResourceLocation getModelResource(TargetEntity entity) {
		return new ResourceLocation("target", "geo/target2.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(TargetEntity entity) {
		return new ResourceLocation("target", "textures/entities/" + entity.getTexture() + ".png");
	}

}
