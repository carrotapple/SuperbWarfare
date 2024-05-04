package net.mcreator.target.entity.model;

import software.bernie.geckolib.model.GeoModel;

import net.minecraft.resources.ResourceLocation;

import net.mcreator.target.entity.ClaymoreEntity;

public class ClaymoreModel extends GeoModel<ClaymoreEntity> {
	@Override
	public ResourceLocation getAnimationResource(ClaymoreEntity entity) {
		return new ResourceLocation("target", "animations/claymore.animation.json");
	}

	@Override
	public ResourceLocation getModelResource(ClaymoreEntity entity) {
		return new ResourceLocation("target", "geo/claymore.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(ClaymoreEntity entity) {
		return new ResourceLocation("target", "textures/entities/" + entity.getTexture() + ".png");
	}

}
