package net.mcreator.target.procedures;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.event.TickEvent;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;

import net.mcreator.target.network.TargetModVariables;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber
public class UnsprintableProcedure {
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
		if (entity == null)
			return;
		if ((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getOrCreateTag().getDouble("fireanim") > 0) {
			entity.getPersistentData().putDouble("unspringtable", 20);
		}
		if (entity.isShiftKeyDown() || entity.isPassenger() || entity.isInWater() || (entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).zooming == true) {
			entity.getPersistentData().putDouble("unspringtable", 1);
		}
		if (entity.getPersistentData().getDouble("unspringtable") > 0) {
			entity.getPersistentData().putDouble("unspringtable", (entity.getPersistentData().getDouble("unspringtable") - 1));
		}
		if ((entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).zooming == true) {
			entity.setSprinting(false);
		}
	}
}
