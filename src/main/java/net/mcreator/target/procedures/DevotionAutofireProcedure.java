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
public class DevotionAutofireProcedure {
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
		double dev = 0;
		usehand = (entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY);
		if (entity.getPersistentData().getDouble("firing") == 1) {
			entity.getPersistentData().putDouble("dvfire", (entity.getPersistentData().getDouble("dvfire") + 1));
		} else {
			if (entity.getPersistentData().getDouble("dvfire") > 0) {
				entity.getPersistentData().putDouble("dvfire", (entity.getPersistentData().getDouble("dvfire") - 1));
			}
		}
		if (entity.getPersistentData().getDouble("firing") == 1) {
			if (usehand.getItem() == TargetModItems.DEVOTION.get() && usehand.getOrCreateTag().getDouble("reloading") == 0 && usehand.getOrCreateTag().getDouble("ammo") > 0 && entity.getPersistentData().getDouble("dvfire") == 1) {
				DevofireProcedure.execute(entity);
			}
			if (usehand.getItem() == TargetModItems.DEVOTION.get() && usehand.getOrCreateTag().getDouble("reloading") == 0 && usehand.getOrCreateTag().getDouble("ammo") > 0 && entity.getPersistentData().getDouble("dvfire") == 5) {
				DevofireProcedure.execute(entity);
			}
			if (usehand.getItem() == TargetModItems.DEVOTION.get() && usehand.getOrCreateTag().getDouble("reloading") == 0 && usehand.getOrCreateTag().getDouble("ammo") > 0 && entity.getPersistentData().getDouble("dvfire") == 9) {
				DevofireProcedure.execute(entity);
			}
			if (usehand.getItem() == TargetModItems.DEVOTION.get() && usehand.getOrCreateTag().getDouble("reloading") == 0 && usehand.getOrCreateTag().getDouble("ammo") > 0 && entity.getPersistentData().getDouble("dvfire") == 12) {
				DevofireProcedure.execute(entity);
			}
			if (usehand.getItem() == TargetModItems.DEVOTION.get() && usehand.getOrCreateTag().getDouble("reloading") == 0 && usehand.getOrCreateTag().getDouble("ammo") > 0 && entity.getPersistentData().getDouble("dvfire") == 15) {
				DevofireProcedure.execute(entity);
			}
			if (usehand.getItem() == TargetModItems.DEVOTION.get() && usehand.getOrCreateTag().getDouble("reloading") == 0 && usehand.getOrCreateTag().getDouble("ammo") > 0
					&& (entity.getPersistentData().getDouble("dvfire") == 17 || entity.getPersistentData().getDouble("dvfire") == 18 || entity.getPersistentData().getDouble("dvfire") == 19)) {
				DevofireProcedure.execute(entity);
			}
			if (entity.getPersistentData().getDouble("dvfire") >= 19) {
				entity.getPersistentData().putDouble("dvfire", 15);
			}
		}
	}
}
