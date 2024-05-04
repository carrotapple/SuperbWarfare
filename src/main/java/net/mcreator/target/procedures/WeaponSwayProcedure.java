package net.mcreator.target.procedures;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.event.TickEvent;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;

import net.mcreator.target.init.TargetModMobEffects;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber
public class WeaponSwayProcedure {
	@SubscribeEvent
	public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
		if (event.phase == TickEvent.Phase.END) {
			execute(event, event.player);
		}
	}

	public static void execute(Entity entity) {
		execute(null, entity);
	}

	private static void execute(@Nullable Event event, Entity entity) {
	
		double[] recoilTimer = {0};
		double totalTime = 10;
		int sleepTime = 2;
		double recoilDuration = totalTime / sleepTime;
		Runnable recoilRunnable = () -> {
			while (recoilTimer[0] < recoilDuration) {

		if (entity == null)
			return;
		double pose = 0;
		if (entity.isShiftKeyDown() && entity.getBbHeight() >= 1 && entity.getPersistentData().getDouble("prone") == 0) {
			pose = 0.85;
		} else if (entity.getPersistentData().getDouble("prone") > 0) {
			if ((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getOrCreateTag().getDouble("bipod") == 1) {
				pose = 0;
			} else {
				pose = 0.25;
			}
		} else {
			pose = 1;
		}
		entity.getPersistentData().putDouble("time", (entity.getPersistentData().getDouble("time") + 0.015));
		entity.getPersistentData().putDouble("x", (pose * (-0.008) * Math.sin(1 * entity.getPersistentData().getDouble("time")) * (1 - 0.9 * entity.getPersistentData().getDouble("zoomtime"))));
		entity.getPersistentData().putDouble("y", (pose * 0.125 * Math.sin(entity.getPersistentData().getDouble("time") - 1.585) * (1 - 0.9 * entity.getPersistentData().getDouble("zoomtime"))));

		recoilTimer[0]++;
				try {
					Thread.sleep(sleepTime);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		};
		Thread recoilThread = new Thread(recoilRunnable);
		recoilThread.start();
	}
}
