package net.mcreator.superbwarfare.entity.model;

import net.mcreator.superbwarfare.ModUtils;
import net.mcreator.superbwarfare.entity.DroneEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

import static net.mcreator.superbwarfare.entity.DroneEntity.*;
import static net.mcreator.superbwarfare.event.ClientEventHandler.droneBodyAngle;

public class DroneModel extends GeoModel<DroneEntity> {
	public static float rotX = 0;
	public static float rotZ = 0;

	public static float rotation = 0;


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

		float times = (float) (0.5f * Math.min(Minecraft.getInstance().getDeltaFrameTime(), 0.8));

		rotX = Mth.lerp(0.5f * times, rotX, animatable.getEntityData().get(ROT_X));
		rotZ = Mth.lerp(0.5f * times, rotZ, animatable.getEntityData().get(ROT_Z));

		body.setRotZ(rotX);
		body.setRotX(rotZ);

		droneBodyAngle(rotX, rotZ);

		//螺旋桨控制

		CoreGeoBone wingFL = getAnimationProcessor().getBone("wingFL");
		CoreGeoBone wingFR = getAnimationProcessor().getBone("wingFR");
		CoreGeoBone wingBL = getAnimationProcessor().getBone("wingBL");
		CoreGeoBone wingBR = getAnimationProcessor().getBone("wingBR");

		rotation = (float) Mth.lerp(times, rotation, animatable.onGround() ? 0 : 0.08 - 0.1 * animatable.getEntityData().get(MOVE_Y));

		wingFL.setRotY(wingFL.getRotY() - rotation);
		wingFR.setRotY(wingFL.getRotY() - rotation);
		wingBL.setRotY(wingFL.getRotY() - rotation);
		wingBR.setRotY(wingFL.getRotY() - rotation);

	}
}
