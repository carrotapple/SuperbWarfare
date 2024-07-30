package net.mcreator.superbwarfare.entity.model;

import net.mcreator.superbwarfare.ModUtils;
import net.mcreator.superbwarfare.entity.ProjectileEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

public class ProjectileEntityModel extends GeoModel<ProjectileEntity> {
	@Override
	public ResourceLocation getAnimationResource(ProjectileEntity entity) {
		return null;
	}

	@Override
	public ResourceLocation getModelResource(ProjectileEntity entity) {
		return new ResourceLocation(ModUtils.MODID, "geo/projectile_entity.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(ProjectileEntity entity) {
		return new ResourceLocation(ModUtils.MODID, "textures/entity/empty.png");
	}

	@Override
	public void setCustomAnimations(ProjectileEntity animatable, long instanceId, AnimationState animationState) {
		CoreGeoBone bone = getAnimationProcessor().getBone("bone");

        bone.setHidden(animatable.tickCount <= 1);

	}
}
