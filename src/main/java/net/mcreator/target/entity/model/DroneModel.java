package net.mcreator.target.entity.model;

import software.bernie.geckolib.model.GeoModel;

import net.minecraft.resources.ResourceLocation;

import net.mcreator.target.entity.DroneEntity;

public class DroneModel extends GeoModel<DroneEntity> {
	@Override
	public ResourceLocation getAnimationResource(DroneEntity entity) {
		return new ResourceLocation("target", "animations/drone.animation.json");
	}

	@Override
	public ResourceLocation getModelResource(DroneEntity entity) {
		return new ResourceLocation("target", "geo/drone.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(DroneEntity entity) {
		return new ResourceLocation("target", "textures/entity/drone.png");
	}

}
