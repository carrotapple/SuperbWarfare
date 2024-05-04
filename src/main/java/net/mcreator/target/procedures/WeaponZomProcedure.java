package net.mcreator.target.procedures;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.Minecraft;

import net.mcreator.target.network.TargetModVariables;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class WeaponZomProcedure {
	private static ViewportEvent.ComputeCameraAngles _provider = null;

	private static void setAngles(float yaw, float pitch, float roll) {
		_provider.setYaw(yaw);
		_provider.setPitch(pitch);
		_provider.setRoll(roll);
	}

	@SubscribeEvent
	public static void computeCameraangles(ViewportEvent.ComputeCameraAngles event) {
		_provider = event;
		ClientLevel level = Minecraft.getInstance().level;
		Entity entity = _provider.getCamera().getEntity();
		if (level != null && entity != null) {
			Vec3 entPos = entity.getPosition((float) _provider.getPartialTick());
			execute(_provider, entity);
		}
	}

	public static void execute(Entity entity) {
		execute(null, entity);
	}

	private static void execute(@Nullable Event event, Entity entity) {
		if (entity == null)
			return;
		double time = 0;
		float fps = Minecraft.getInstance().getFps();
		if (fps <= 0) {
			fps = 1f;
		}
		float times = 110f / fps;
		if ((entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).zooming == true) {
			if (entity.getPersistentData().getDouble("zoomtime") < 1) {
				entity.getPersistentData().putDouble("zoomtime",
						(entity.getPersistentData().getDouble("zoomtime") + (entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getOrCreateTag().getDouble("zoomspeed") * 0.02 * times));
			} else {
				entity.getPersistentData().putDouble("zoomtime", 1);
			}
		} else {
			if (entity.getPersistentData().getDouble("zoomtime") > 0) {
				entity.getPersistentData().putDouble("zoomtime", (entity.getPersistentData().getDouble("zoomtime") - 0.02 * times));
			} else {
				entity.getPersistentData().putDouble("zoomtime", 0);
			}
		}
		entity.getPersistentData().putDouble("zoompos", (0.5 * Math.cos(Math.PI * Math.pow(Math.pow(entity.getPersistentData().getDouble("zoomtime"), 2) - 1, 2)) + 0.5));
		entity.getPersistentData().putDouble("zoomposz", (-Math.pow(2 * entity.getPersistentData().getDouble("zoomtime") - 1, 2) + 1));
	}
}
