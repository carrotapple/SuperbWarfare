package net.mcreator.target.procedures;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.event.TickEvent;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

import net.mcreator.target.init.TargetModItems;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber
public class Aa12autofireProcedure {
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
		if (entity.getPersistentData().getDouble("firing") == 1 && usehand.getOrCreateTag().getDouble("firemode") == 2) {
			if (usehand.getItem() == TargetModItems.AA_12.get() && usehand.getOrCreateTag().getDouble("reloading") == 0 && usehand.getOrCreateTag().getDouble("ammo") > 0
					&& !(entity instanceof Player _plrCldCheck6 && _plrCldCheck6.getCooldowns().isOnCooldown(usehand.getItem()))) {
				for (int index0 = 0; index0 < 8; index0++) {
					BulletfireNormalProcedure.execute(entity);
				}
				As12DsProcedure.execute(entity);
				if (entity instanceof Player _player)
					_player.getCooldowns().addCooldown(usehand.getItem(), 4);
				{
					Entity _ent = entity;
					if (!_ent.level().isClientSide() && _ent.getServer() != null) {
						_ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4,
								_ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), "playsound target:aa12_fire_1p player @s ~ ~ ~ 2 1");
					}
				}
				{
					Entity _ent = entity;
					if (!_ent.level().isClientSide() && _ent.getServer() != null) {
						_ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4,
								_ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), "playsound target:aa12fire player @a ~ ~ ~ 6 1");
					}
				}
				{
					Entity _ent = entity;
					if (!_ent.level().isClientSide() && _ent.getServer() != null) {
						_ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4,
								_ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), "stopsound @s player target:aa12fire");
					}
				}
				usehand.getOrCreateTag().putDouble("fireanim", 2);
				usehand.getOrCreateTag().putDouble("ammo", (usehand.getOrCreateTag().getDouble("ammo") - 1));
			}
		} else if (entity.getPersistentData().getDouble("firing") == 1 && usehand.getOrCreateTag().getDouble("firemode") == 0) {
			entity.getPersistentData().putDouble("firing", 0);
			if (usehand.getItem() == TargetModItems.AA_12.get() && usehand.getOrCreateTag().getDouble("reloading") == 0 && usehand.getOrCreateTag().getDouble("ammo") > 0
					&& !(entity instanceof Player _plrCldCheck20 && _plrCldCheck20.getCooldowns().isOnCooldown(usehand.getItem()))) {
				for (int index1 = 0; index1 < 8; index1++) {
					BulletfireNormalProcedure.execute(entity);
				}
				As12DsProcedure.execute(entity);
				if (entity instanceof Player _player)
					_player.getCooldowns().addCooldown(usehand.getItem(), 4);
				{
					Entity _ent = entity;
					if (!_ent.level().isClientSide() && _ent.getServer() != null) {
						_ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4,
								_ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), "playsound target:aa12_fire_1p player @s ~ ~ ~ 2 1");
					}
				}
				{
					Entity _ent = entity;
					if (!_ent.level().isClientSide() && _ent.getServer() != null) {
						_ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4,
								_ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), "playsound target:aa12fire player @a ~ ~ ~ 6 1");
					}
				}
				{
					Entity _ent = entity;
					if (!_ent.level().isClientSide() && _ent.getServer() != null) {
						_ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4,
								_ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), "stopsound @s player target:aa12fire");
					}
				}
				usehand.getOrCreateTag().putDouble("fireanim", 2);
				usehand.getOrCreateTag().putDouble("ammo", (usehand.getOrCreateTag().getDouble("ammo") - 1));
			}
		}
	}
}
