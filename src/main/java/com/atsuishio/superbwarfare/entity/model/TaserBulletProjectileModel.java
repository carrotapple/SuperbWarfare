package com.atsuishio.superbwarfare.entity.model;

import com.atsuishio.superbwarfare.ModUtils;
import com.atsuishio.superbwarfare.entity.projectile.TaserBulletProjectileEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class TaserBulletProjectileModel extends GeoModel<TaserBulletProjectileEntity> {
	@Override
	public ResourceLocation getAnimationResource(TaserBulletProjectileEntity entity) {
		return null;
	}

	@Override
	public ResourceLocation getModelResource(TaserBulletProjectileEntity entity) {
		return new ResourceLocation(ModUtils.MODID, "geo/taser_rod.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(TaserBulletProjectileEntity entity) {
		return new ResourceLocation(ModUtils.MODID, "textures/entity/taser_rod.png");
	}
}
