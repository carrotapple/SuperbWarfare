package net.mcreator.superbwarfare.entity.model;

import net.mcreator.superbwarfare.ModUtils;
import net.mcreator.superbwarfare.entity.ProjectileEntity;
import net.mcreator.superbwarfare.init.ModItems;
import net.mcreator.superbwarfare.network.ModVariables;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
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
		Player player = Minecraft.getInstance().player;
		if ((player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables())).zooming || player.getMainHandItem().is(ModItems.GLOCK_17.get())) {
			return new ResourceLocation(ModUtils.MODID, "geo/projectile_entity.geo.json");
		} else {
			return new ResourceLocation(ModUtils.MODID, "geo/projectile_entity2.geo.json");
		}
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
