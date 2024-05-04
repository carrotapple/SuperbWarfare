package net.mcreator.target.procedures;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;

import net.mcreator.target.network.TargetModVariables;

public class ShotgunReload2Procedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		ItemStack stack = ItemStack.EMPTY;
		double ammo2 = 0;
		double ammo1 = 0;
		stack = (entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY);
		ammo2 = (stack.getOrCreateTag().getDouble("mag") + 1) - stack.getOrCreateTag().getDouble("ammo");
		if ((entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).shotgunammo >= ammo2) {
			{
				double _setval = (entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).shotgunammo - ammo2;
				entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
					capability.shotgunammo = _setval;
					capability.syncPlayerVariables(entity);
				});
			}
			stack.getOrCreateTag().putDouble("ammo", (stack.getOrCreateTag().getDouble("ammo") + ammo2));
			stack.getOrCreateTag().putDouble("reloading", 0);
			stack.getOrCreateTag().putDouble("emptyreload", 0);
		} else {
			stack.getOrCreateTag().putDouble("ammo", (stack.getOrCreateTag().getDouble("ammo") + (entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).shotgunammo));
			{
				double _setval = 0;
				entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
					capability.shotgunammo = _setval;
					capability.syncPlayerVariables(entity);
				});
			}
			stack.getOrCreateTag().putDouble("reloading", 0);
			stack.getOrCreateTag().putDouble("emptyreload", 0);
		}
	}
}
