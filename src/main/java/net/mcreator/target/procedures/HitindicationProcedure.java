package net.mcreator.target.procedures;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.event.TickEvent;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.network.chat.Component;

import net.mcreator.target.network.TargetModVariables;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber
public class HitindicationProcedure {
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
		if ((entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).hitind > 0) {
			{
				double _setval = (entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).hitind - 1;
				entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
					capability.hitind = _setval;
					capability.syncPlayerVariables(entity);
				});
			}
		}
		if ((entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).headind > 0) {
			{
				double _setval = (entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).headind - 1;
				entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
					capability.headind = _setval;
					capability.syncPlayerVariables(entity);
				});
			}
		}
		if ((entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).killind > 0) {
			{
				double _setval = (entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).killind - 1;
				entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
					capability.killind = _setval;
					capability.syncPlayerVariables(entity);
				});
			}
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
