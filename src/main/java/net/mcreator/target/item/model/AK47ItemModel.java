package net.mcreator.target.item.model;

import software.bernie.geckolib.model.GeoModel;

import net.minecraft.resources.ResourceLocation;

import net.mcreator.target.item.AK47Item;

import net.minecraft.client.Minecraft;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import net.minecraft.world.entity.player.Player;
import net.mcreator.target.init.TargetModMobEffects;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.item.ItemStack;

import net.mcreator.target.network.TargetModVariables;

public class AK47ItemModel extends GeoModel<AK47Item> {
	@Override
	public ResourceLocation getAnimationResource(AK47Item animatable) {
		return new ResourceLocation("target", "animations/ak.animation.json");
	}

	@Override
	public ResourceLocation getModelResource(AK47Item animatable) {
		return new ResourceLocation("target", "geo/ak.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(AK47Item animatable) {
		return new ResourceLocation("target", "textures/item/ak47.png");
	}

	@Override
	public void setCustomAnimations(AK47Item animatable, long instanceId, AnimationState animationState) {
		CoreGeoBone gun = getAnimationProcessor().getBone("bone");
		CoreGeoBone shen = getAnimationProcessor().getBone("shen");
		CoreGeoBone scope = getAnimationProcessor().getBone("kobra");
		CoreGeoBone shuan = getAnimationProcessor().getBone("shuan");

		Player player = Minecraft.getInstance().player;
		ItemStack stack = player.getMainHandItem();

		double p = 0;
		p = player.getPersistentData().getDouble("zoompos");

		double zp = 0;
		zp = player.getPersistentData().getDouble("zoomposz");

		gun.setPosX(2.11f * (float)p);

		gun.setPosY(0.89f * (float)p - (float)(0.6f * zp));

		gun.setPosZ(4.4f * (float)p + (float)(0.5f * zp));

		gun.setScaleZ(1f - (0.2f * (float)p));

		scope.setScaleZ(1f - (0.4f * (float)p));

		gun.setRotZ(-0.087f * (float)p + (float)(0.05f * zp));

		CoreGeoBone holo = getAnimationProcessor().getBone("holo");
		CoreGeoBone flare = getAnimationProcessor().getBone("flare");
		if (gun.getPosY() > 0.5) {
			holo.setScaleX(1);
			holo.setScaleY(1);
		} else {
			holo.setScaleX(0);
			holo.setScaleY(0);
		}
		
		double fp = 0;
		fp = player.getPersistentData().getDouble("firepos");


		if ((player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).zooming == true) {
			shen.setPosZ(0.5f * (float)fp);
		} else {
			shen.setPosZ(1.0f * (float)fp);
		}

		if ((player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).zooming == true) {
			shen.setRotX(0.003f * (float)fp);
		} else {
			shen.setRotX(0.02f * (float)fp);
		}

		if ((player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).recoilhorizon == 1) {
			shen.setRotZ(0.003f * (float)fp);
		} else {
			shen.setRotZ(-0.003f * (float)fp);
		}

		shuan.setPosZ(2.4f * (float)fp);

		if (stack.getOrCreateTag().getDouble("fireanim") > 0) {
			flare.setScaleX((float) (1.0 + 0.5 * (Math.random() - 0.5)));
			flare.setScaleY((float) (1.0 + 0.5 * (Math.random() - 0.5)));
			flare.setRotZ((float) (0.5 * (Math.random() - 0.5)));
		} else {
			flare.setScaleX(0);
			flare.setScaleY(0);
			flare.setRotZ(0);
		}

		CoreGeoBone root = getAnimationProcessor().getBone("root");
        
		double y = 0;
		double x = 0;
		y = player.getPersistentData().getDouble("y");
		x = player.getPersistentData().getDouble("x");

		root.setPosY((float)y);
		root.setRotX((float)x);

		CoreGeoBone move = getAnimationProcessor().getBone("move");
        
		double m = 0;
		m = player.getPersistentData().getDouble("move");

		double yaw = 0;
		yaw = player.getPersistentData().getDouble("yaw");

		double pit = 0;
		pit = player.getPersistentData().getDouble("gunpitch");

		double vy = 0;
		vy = player.getPersistentData().getDouble("vy");

		move.setPosY(-0.95f * (float)vy);

		move.setPosX(9.3f * (float)m);

		move.setRotX(2.0f * (float)pit);

		move.setRotZ(3.7f * (float)yaw + 2.7f * (float)m);

		move.setRotY(1.9f * (float)yaw - (float)m);
	
	}
}
