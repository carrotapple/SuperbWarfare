package com.atsuishio.superbwarfare.client.model.entity;

import com.atsuishio.superbwarfare.ModUtils;
import com.atsuishio.superbwarfare.entity.projectile.GunGrenadeEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

public class GunGrenadeModel extends GeoModel<GunGrenadeEntity> {
	@Override
	public ResourceLocation getAnimationResource(GunGrenadeEntity entity) {
		return new ResourceLocation(ModUtils.MODID, "animations/cannon_shell.animation.json");
	}

	@Override
	public ResourceLocation getModelResource(GunGrenadeEntity entity) {
		return new ResourceLocation(ModUtils.MODID, "geo/cannon_shell.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(GunGrenadeEntity entity) {
		return new ResourceLocation(ModUtils.MODID, "textures/entity/cannon_shell.png");
	}

	@Override
	public void setCustomAnimations(GunGrenadeEntity animatable, long instanceId, AnimationState animationState) {
		CoreGeoBone bone = getAnimationProcessor().getBone("bone");
		bone.setScaleX(0.15f);
		bone.setScaleY(0.15f);
		bone.setScaleZ(0.15f);
	}
}
