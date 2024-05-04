package net.mcreator.target.procedures;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;

import net.mcreator.target.network.TargetModVariables;

public class WeaponDrawLightProcedure {
	public static void execute(Entity entity, ItemStack itemstack) {
		if (entity == null)
			return;
		if ((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() == itemstack.getItem()) {
			if (itemstack.getOrCreateTag().getDouble("draw") < 2) {
				itemstack.getOrCreateTag().putDouble("draw", (itemstack.getOrCreateTag().getDouble("draw") + 1));
			}
		} else {
			itemstack.getOrCreateTag().putDouble("draw", 0);
		}
		if (itemstack.getOrCreateTag().getDouble("draw") == 1) {
			{
				boolean _setval = false;
				entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
					capability.zooming = _setval;
					capability.syncPlayerVariables(entity);
				});
			}
			if (entity instanceof Player _player)
				_player.getCooldowns().addCooldown(itemstack.getItem(), 13);
		}
		if (itemstack.getOrCreateTag().getDouble("fireanim") > 0) {
			itemstack.getOrCreateTag().putDouble("fireanim", (itemstack.getOrCreateTag().getDouble("fireanim") - 1));
		}
		if ((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() == itemstack.getItem()) {
			if (itemstack.getOrCreateTag().getDouble("drawtime") < 11) {
				itemstack.getOrCreateTag().putDouble("drawtime", (itemstack.getOrCreateTag().getDouble("drawtime") + 1));
			}
		} else {
			itemstack.getOrCreateTag().putDouble("drawtime", 0);
		}
	}
}
