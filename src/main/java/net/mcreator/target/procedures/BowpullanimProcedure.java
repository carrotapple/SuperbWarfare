package net.mcreator.target.procedures;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.Entity;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.Minecraft;

import net.mcreator.target.network.TargetModVariables;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class BowpullanimProcedure {
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
		float fps = Minecraft.getInstance().getFps();
		if (fps <= 0) {
			fps = 1f;
		}
		float times = 90f / fps;
		if ((entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).bowpull == true) {
			if (entity.getPersistentData().getDouble("pulltime") < 1) {
				entity.getPersistentData().putDouble("pulltime", (entity.getPersistentData().getDouble("pulltime") + 0.014 * times));
			} else {
				entity.getPersistentData().putDouble("pulltime", 1);
			}
		} else {
			if (entity.getPersistentData().getDouble("pulltime") > 0) {
				entity.getPersistentData().putDouble("pulltime", (entity.getPersistentData().getDouble("pulltime") - 0.009 * times));
			} else {
				entity.getPersistentData().putDouble("pulltime", 0);
			}
		}
		entity.getPersistentData().putDouble("pullpos", (0.5 * Math.cos(Math.PI * Math.pow(Math.pow(entity.getPersistentData().getDouble("pulltime"), 2) - 1, 2)) + 0.5));
		if ((entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).bowpull == true) {
			if (entity.getPersistentData().getDouble("bowtime") < 1) {
				entity.getPersistentData().putDouble("bowtime", (entity.getPersistentData().getDouble("bowtime") + 0.014 * times));
			} else {
				entity.getPersistentData().putDouble("bowtime", 1);
			}
		} else {
			if (entity.getPersistentData().getDouble("bowtime") > 0) {
				entity.getPersistentData().putDouble("bowtime", (entity.getPersistentData().getDouble("bowtime") - 1 * times));
			} else {
				entity.getPersistentData().putDouble("bowtime", 0);
			}
		}
		entity.getPersistentData().putDouble("bowpos", (0.5 * Math.cos(Math.PI * Math.pow(Math.pow(entity.getPersistentData().getDouble("bowtime"), 2) - 1, 2)) + 0.5));
		if ((entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).bowpull == true) {
			if (entity.getPersistentData().getDouble("handtime") < 1) {
				entity.getPersistentData().putDouble("handtime", (entity.getPersistentData().getDouble("handtime") + 0.014 * times));
			} else {
				entity.getPersistentData().putDouble("handtime", 1);
			}
			entity.getPersistentData().putDouble("handpos", (0.5 * Math.cos(Math.PI * Math.pow(Math.pow(entity.getPersistentData().getDouble("handtime"), 2) - 1, 2)) + 0.5));
		} else {
			if (entity.getPersistentData().getDouble("handtime") > 0) {
				entity.getPersistentData().putDouble("handtime", (entity.getPersistentData().getDouble("handtime") - 0.04 * times));
			} else {
				entity.getPersistentData().putDouble("handtime", 0);
			}
			if (entity.getPersistentData().getDouble("handtime") > 0 && entity.getPersistentData().getDouble("handtime") < 0.5) {
				entity.getPersistentData().putDouble("handpos", (0.5 * Math.cos(Math.PI * Math.pow(Math.pow(entity.getPersistentData().getDouble("handtime"), 2) - 1, 2)) + 0.5));
			}
		}
	}
}
