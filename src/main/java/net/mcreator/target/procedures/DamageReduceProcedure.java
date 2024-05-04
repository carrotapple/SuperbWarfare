package net.mcreator.target.procedures;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.Registries;

import net.mcreator.target.init.TargetModItems;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber
public class DamageReduceProcedure {
	@SubscribeEvent
	public static void onEntityAttacked(LivingHurtEvent event) {
		if (event != null && event.getEntity() != null) {
			execute(event, event.getSource(), event.getEntity(), event.getSource().getEntity(), event.getAmount());
		}
	}

	public static void execute(DamageSource damagesource, Entity entity, Entity sourceentity, double amount) {
		execute(null, damagesource, entity, sourceentity, amount);
	}

	private static void execute(@Nullable Event event, DamageSource damagesource, Entity entity, Entity sourceentity, double amount) {
		if (damagesource == null || entity == null || sourceentity == null)
			return;
		double distanse = 0;
		double damage = 0;
		ItemStack stack = ItemStack.EMPTY;
		if (!(sourceentity == null) && damagesource.is(ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation("target:arrow_in_brain")))) {
			stack = (sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY);
			damage = amount;
			stack.getOrCreateTag().putDouble("damagetotal", (stack.getOrCreateTag().getDouble("damagetotal") + damage));
		}
		if (!(sourceentity == null) && (damagesource.is(DamageTypes.EXPLOSION) || damagesource.is(DamageTypes.PLAYER_EXPLOSION) || damagesource.is(DamageTypes.ARROW))) {
			stack = (sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY);
			if ((sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() == TargetModItems.M_79.get()
					|| (sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() == TargetModItems.RPG.get()) {
				damage = amount;
				stack.getOrCreateTag().putDouble("damagetotal", (stack.getOrCreateTag().getDouble("damagetotal") + damage));
			}
		}
		if (!(sourceentity == null) && damagesource.is(ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation("target:gunfire")))) {
			stack = (sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY);
			distanse = (entity.position()).distanceTo((sourceentity.position()));
			if (stack.getOrCreateTag().getDouble("shotgun") == 1 || stack.getItem() == TargetModItems.BOCEK.get()) {
				if (distanse > 20) {
					((LivingHurtEvent) event).setAmount(((float) (amount / (1 + 0.05 * (distanse - 20)))));
					damage = amount / (1 + 0.05 * (distanse - 20));
				} else {
					damage = amount;
				}
			} else if (stack.getOrCreateTag().getDouble("sniperguns") == 1) {
				if (distanse > 200) {
					((LivingHurtEvent) event).setAmount(((float) (amount / (1 + 0.001 * (distanse - 200)))));
					damage = amount / (1 + 0.001 * (distanse - 200));
				} else {
					damage = amount;
				}
			} else if (stack.getOrCreateTag().getDouble("handgun") == 1) {
				if (distanse > 40) {
					((LivingHurtEvent) event).setAmount(((float) (amount / (1 + 0.04 * (distanse - 40)))));
					damage = amount / (1 + 0.04 * (distanse - 40));
				} else {
					damage = amount;
				}
			} else if (stack.getOrCreateTag().getDouble("smg") == 1) {
				if (distanse > 50) {
					((LivingHurtEvent) event).setAmount(((float) (amount / (1 + 0.03 * (distanse - 50)))));
					damage = amount / (1 + 0.03 * (distanse - 50));
				} else {
					damage = amount;
				}
			} else if (stack.getOrCreateTag().getDouble("rifle") == 1) {
				if (distanse > 100) {
					((LivingHurtEvent) event).setAmount(((float) (amount / (1 + 0.005 * (distanse - 100)))));
				} else {
					damage = amount;
				}
				damage = amount / (1 + 0.005 * (distanse - 100));
			}
			stack.getOrCreateTag().putDouble("damagetotal", (stack.getOrCreateTag().getDouble("damagetotal") + damage));
		}
	}
}
