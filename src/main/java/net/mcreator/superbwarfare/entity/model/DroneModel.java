package net.mcreator.superbwarfare.entity.model;

import net.mcreator.superbwarfare.ModUtils;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import net.minecraft.resources.ResourceLocation;
import net.mcreator.superbwarfare.entity.DroneEntity;
import static net.mcreator.superbwarfare.entity.DroneEntity.*;

public class DroneModel extends GeoModel<DroneEntity> {

	@Override
	public ResourceLocation getAnimationResource(DroneEntity entity) {
		return new ResourceLocation(ModUtils.MODID, "animations/drone.animation.json");
	}

	@Override
	public ResourceLocation getModelResource(DroneEntity entity) {
		return new ResourceLocation(ModUtils.MODID, "geo/drone.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(DroneEntity entity) {
		return new ResourceLocation(ModUtils.MODID, "textures/entity/drone.png");
	}

	@Override
	public void setCustomAnimations(DroneEntity animatable, long instanceId, AnimationState animationState) {
		CoreGeoBone ammo1 = getAnimationProcessor().getBone("ammo1");
		CoreGeoBone ammo2 = getAnimationProcessor().getBone("ammo2");
		CoreGeoBone ammo3 = getAnimationProcessor().getBone("ammo3");
		CoreGeoBone ammo4 = getAnimationProcessor().getBone("ammo4");
		CoreGeoBone ammo5 = getAnimationProcessor().getBone("ammo5");
		CoreGeoBone ammo6 = getAnimationProcessor().getBone("ammo6");
		CoreGeoBone shell = getAnimationProcessor().getBone("shell");
		CoreGeoBone body = getAnimationProcessor().getBone("0");

        ammo6.setHidden(animatable.getEntityData().get(AMMO) <= 5);
		ammo5.setHidden(animatable.getEntityData().get(AMMO) <= 4);
		ammo4.setHidden(animatable.getEntityData().get(AMMO) <= 3);
		ammo3.setHidden(animatable.getEntityData().get(AMMO) <= 2);
		ammo2.setHidden(animatable.getEntityData().get(AMMO) <= 1);
		ammo1.setHidden(animatable.getEntityData().get(AMMO) <= 0);
        shell.setHidden(!animatable.getEntityData().get(KAMIKAZE));


		body.setRotZ(animatable.getEntityData().get(ROT_X));
		body.setRotX(animatable.getEntityData().get(ROT_Z));
	}
}
