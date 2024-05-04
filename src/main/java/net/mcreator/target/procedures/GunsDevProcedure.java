package net.mcreator.target.procedures;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.event.TickEvent;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;

import net.mcreator.target.init.TargetModMobEffects;
import net.mcreator.target.init.TargetModAttributes;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber
public class GunsDevProcedure {
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
		double totalTime = 20;
		int sleepTime = 2;
		double recoilDuration = totalTime / sleepTime;
		Runnable recoilRunnable = () -> {
		while (recoilTimer[0] < recoilDuration) {
		if (entity == null)
			return;
		double walk = 0;
		double sprint = 0;
		double qianxing = 0;
		double basicdev = 0;
		double prone = 0;
		double jump = 0;
		double xishu = 0;
		double zoom = 0;
		double fire = 0;
		double ride = 0;
		basicdev = (entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getOrCreateTag().getDouble("dev");
		if (entity.getPersistentData().getDouble("qian") == 1 || entity.getPersistentData().getDouble("tui") == 1 || entity.getPersistentData().getDouble("mover") == 1 || entity.getPersistentData().getDouble("movel") == 1) {
			walk = 0.2 * basicdev;
		} else {
			walk = 0;
		}
		if (entity.isSprinting()) {
			sprint = 0.5 * basicdev;
		} else {
			sprint = 0;
		}
		if (entity.isShiftKeyDown()) {
			qianxing = (-0.25) * basicdev;
		} else {
			qianxing = 0;
		}
		if (entity.getPersistentData().getDouble("prone") > 0) {
			prone = (-0.5) * basicdev;
		} else {
			prone = 0;
		}
		if (entity.onGround()) {
			jump = 0;
		} else {
			jump = 1.5 * basicdev;
		}
		if (entity.getPersistentData().getDouble("miaozhunshijian") > 4) {
			if ((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getOrCreateTag().getDouble("sniperguns") == 1) {
				zoom = 0.001;
			} else if ((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getOrCreateTag().getDouble("shotgun") == 1) {
				zoom = 0.9;
			} else {
				zoom = 0.1;
			}
		} else {
			zoom = 1;
		}
		if ((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getOrCreateTag().getDouble("fireanim") > 0) {
			fire = 0.5 * basicdev;
		} else {
			fire = 0;
		}
		if (entity.isPassenger()) {
			ride = (-0.5) * basicdev;
		} else {
			ride = 0;
		}
		xishu = zoom * (basicdev + walk + sprint + qianxing + prone + jump + fire + ride);
		if (((LivingEntity) entity).getAttribute(TargetModAttributes.SPREAD.get()).getBaseValue() < xishu) {
			((LivingEntity) entity).getAttribute(TargetModAttributes.SPREAD.get())
					.setBaseValue((((LivingEntity) entity).getAttribute(TargetModAttributes.SPREAD.get()).getBaseValue() + 0.0125 * Math.pow(xishu - ((LivingEntity) entity).getAttribute(TargetModAttributes.SPREAD.get()).getBaseValue(), 2)));
		} else {
			((LivingEntity) entity).getAttribute(TargetModAttributes.SPREAD.get())
					.setBaseValue((((LivingEntity) entity).getAttribute(TargetModAttributes.SPREAD.get()).getBaseValue() - 0.0125 * Math.pow(xishu - ((LivingEntity) entity).getAttribute(TargetModAttributes.SPREAD.get()).getBaseValue(), 2)));
		}

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
