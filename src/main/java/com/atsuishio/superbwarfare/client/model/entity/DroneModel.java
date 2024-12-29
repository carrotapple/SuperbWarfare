package com.atsuishio.superbwarfare.client.model.entity;

import com.atsuishio.superbwarfare.ModUtils;
import com.atsuishio.superbwarfare.entity.DroneEntity;
import com.atsuishio.superbwarfare.init.ModItems;
import com.atsuishio.superbwarfare.tools.EntityFindUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

import java.util.UUID;

import static com.atsuishio.superbwarfare.entity.DroneEntity.*;
import static com.atsuishio.superbwarfare.event.ClientEventHandler.droneBodyAngle;

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
		CoreGeoBone main = getAnimationProcessor().getBone("0");

        ammo6.setHidden(animatable.getEntityData().get(AMMO) <= 5);
		ammo5.setHidden(animatable.getEntityData().get(AMMO) <= 4);
		ammo4.setHidden(animatable.getEntityData().get(AMMO) <= 3);
		ammo3.setHidden(animatable.getEntityData().get(AMMO) <= 2);
		ammo2.setHidden(animatable.getEntityData().get(AMMO) <= 1);
		ammo1.setHidden(animatable.getEntityData().get(AMMO) <= 0);
        shell.setHidden(!animatable.getEntityData().get(KAMIKAZE));

		float times = Math.min(Minecraft.getInstance().getDeltaFrameTime(), 1);

		if (Minecraft.getInstance().options.keyRight.isDown()) {
			rotZ = Mth.lerp(0.025f * times, rotZ, -0.25f);
		} else if (Minecraft.getInstance().options.keyLeft.isDown()) {
			rotZ = Mth.lerp(0.025f * times, rotZ, 0.25f);
		} else {
			rotZ = Mth.lerp(0.04f * times, rotZ, 0);
		}

		main.setRotZ(rotZ);

		droneBodyAngle(rotZ);

		//螺旋桨控制

		CoreGeoBone wingFL = getAnimationProcessor().getBone("wingFL");
		CoreGeoBone wingFR = getAnimationProcessor().getBone("wingFR");
		CoreGeoBone wingBL = getAnimationProcessor().getBone("wingBL");
		CoreGeoBone wingBR = getAnimationProcessor().getBone("wingBR");

		rotation = (float) Mth.lerp(times, rotation, animatable.onGround() ? 0 : 0.2);

		wingFL.setRotY(wingFL.getRotY() - rotation);
		wingFR.setRotY(wingFL.getRotY() - rotation);
		wingBL.setRotY(wingFL.getRotY() - rotation);
		wingBR.setRotY(wingFL.getRotY() - rotation);

		CoreGeoBone weapon = getAnimationProcessor().getBone("Weapon");

		String id = animatable.getEntityData().get(CONTROLLER);

		UUID uuid;
		try {
			uuid = UUID.fromString(id);
		} catch (IllegalArgumentException e) {
			return;
		}

		Player player = animatable.level().getPlayerByUUID(uuid);

		if (player != null) {
			ItemStack stack = player.getMainHandItem();
			DroneEntity drone = EntityFindUtil.findDrone(player.level(), stack.getOrCreateTag().getString("LinkedDrone"));

			if (stack.is(ModItems.MONITOR.get()) && stack.getOrCreateTag().getBoolean("Using") && stack.getOrCreateTag().getBoolean("Linked")) {
				if (drone != null && drone.getUUID() == animatable.getUUID()) {
					weapon.setHidden(true);
				}
            } else {
				weapon.setHidden(false);
			}
		}
	}
}
