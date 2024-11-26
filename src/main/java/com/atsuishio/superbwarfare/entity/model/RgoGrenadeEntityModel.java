package com.atsuishio.superbwarfare.entity.model;

import com.atsuishio.superbwarfare.ModUtils;
import com.atsuishio.superbwarfare.entity.projectile.RgoGrenadeEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class RgoGrenadeEntityModel extends GeoModel<RgoGrenadeEntity> {
	@Override
	public ResourceLocation getAnimationResource(RgoGrenadeEntity entity) {
		return new ResourceLocation(ModUtils.MODID, "animations/rgo_grenade.animation.json");
	}

	@Override
	public ResourceLocation getModelResource(RgoGrenadeEntity entity) {
		return new ResourceLocation(ModUtils.MODID, "geo/rgo_grenade.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(RgoGrenadeEntity entity) {
		return new ResourceLocation(ModUtils.MODID, "textures/item/rgo_grenade.png");
	}
}
