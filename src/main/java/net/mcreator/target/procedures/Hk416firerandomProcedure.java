package net.mcreator.target.procedures;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.event.TickEvent;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;

import net.mcreator.target.init.TargetModItems;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber
public class Hk416firerandomProcedure {
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
		ItemStack usehand = ItemStack.EMPTY;
		usehand = (entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY);
		if (entity.getPersistentData().getDouble("firing") == 1) {
			entity.getPersistentData().putDouble("hkfire", (entity.getPersistentData().getDouble("hkfire") + 1));
		} else {
			entity.getPersistentData().putDouble("hkfire", 0);
		}
		if (entity.getPersistentData().getDouble("firing") == 1) {
			if (usehand.getItem() == TargetModItems.HK_416.get() && usehand.getOrCreateTag().getDouble("reloading") == 0 && usehand.getOrCreateTag().getDouble("ammo") > 0 && entity.getPersistentData().getDouble("hkfire") == 1) {
				Hk416autofireProcedure.execute(entity);
			}
			if (usehand.getItem() == TargetModItems.HK_416.get() && usehand.getOrCreateTag().getDouble("reloading") == 0 && usehand.getOrCreateTag().getDouble("ammo") > 0 && entity.getPersistentData().getDouble("hkfire") == 3) {
				Hk416autofireProcedure.execute(entity);
			}
			if (usehand.getItem() == TargetModItems.HK_416.get() && usehand.getOrCreateTag().getDouble("reloading") == 0 && usehand.getOrCreateTag().getDouble("ammo") > 0 && entity.getPersistentData().getDouble("hkfire") == 5) {
				Hk416autofireProcedure.execute(entity);
			}
			if (entity.getPersistentData().getDouble("hkfire") >= 5) {
				entity.getPersistentData().putDouble("hkfire", 0);
			}
		}
	}
}
