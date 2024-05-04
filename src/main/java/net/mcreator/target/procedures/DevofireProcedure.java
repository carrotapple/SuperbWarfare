package net.mcreator.target.procedures;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

import net.mcreator.target.network.TargetModVariables;

public class DevofireProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		ItemStack usehand = ItemStack.EMPTY;
		double dev = 0;
		usehand = (entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY);
		if (Math.random() < 0.5) {
			{
				double _setval = -1;
				entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
					capability.recoilhorizon = _setval;
					capability.syncPlayerVariables(entity);
				});
			}
		} else {
			{
				double _setval = 1;
				entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
					capability.recoilhorizon = _setval;
					capability.syncPlayerVariables(entity);
				});
			}
		}
		{
			double _setval = 0.1;
			entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
				capability.recoil = _setval;
				capability.syncPlayerVariables(entity);
			});
		}
		{
			double _setval = 1;
			entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
				capability.firing = _setval;
				capability.syncPlayerVariables(entity);
			});
		}
		BulletfireNormalProcedure.execute(entity);
		ArDsProcedure.execute(entity);
		{
			Entity _ent = entity;
			if (!_ent.level().isClientSide() && _ent.getServer() != null) {
				_ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4,
						_ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), "playsound target:devotion_fire_1p player @s ~ ~ ~ 2 1");
			}
		}
		{
			Entity _ent = entity;
			if (!_ent.level().isClientSide() && _ent.getServer() != null) {
				_ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4,
						_ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), "playsound target:devotion_fire_3p player @a ~ ~ ~ 4 1");
			}
		}
		{
			Entity _ent = entity;
			if (!_ent.level().isClientSide() && _ent.getServer() != null) {
				_ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4,
						_ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), "stopsound @s player target:devotion_fire_3p");
			}
		}
		usehand.getOrCreateTag().putDouble("fireanim", 2);
		usehand.getOrCreateTag().putDouble("ammo", (usehand.getOrCreateTag().getDouble("ammo") - 1));
	}
}
