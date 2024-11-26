package com.atsuishio.superbwarfare.entity.model;

import com.atsuishio.superbwarfare.ModUtils;
import com.atsuishio.superbwarfare.entity.projectile.RpgRocketEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class RpgRocketModel extends GeoModel<RpgRocketEntity> {
	@Override
	public ResourceLocation getAnimationResource(RpgRocketEntity entity) {
		return new ResourceLocation(ModUtils.MODID, "animations/rpg_rocket.animation.json");
	}

	@Override
	public ResourceLocation getModelResource(RpgRocketEntity entity) {
		return new ResourceLocation(ModUtils.MODID, "geo/rpg_rocket.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(RpgRocketEntity entity) {
		return new ResourceLocation(ModUtils.MODID, "textures/entity/rpg_rocket.png");
	}

}
