package net.mcreator.target.procedures;

import net.minecraft.world.level.GameType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;
import net.minecraft.client.Minecraft;

import net.mcreator.target.network.TargetModVariables;
import net.mcreator.target.init.TargetModItems;

public class MarlinfireProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		ItemStack usehand = ItemStack.EMPTY;
		double ammo1 = 0;
		double id = 0;
		if (!(new Object() {
			public boolean checkGamemode(Entity _ent) {
				if (_ent instanceof ServerPlayer _serverPlayer) {
					return _serverPlayer.gameMode.getGameModeForPlayer() == GameType.SPECTATOR;
				} else if (_ent.level().isClientSide() && _ent instanceof Player _player) {
					return Minecraft.getInstance().getConnection().getPlayerInfo(_player.getGameProfile().getId()) != null && Minecraft.getInstance().getConnection().getPlayerInfo(_player.getGameProfile().getId()).getGameMode() == GameType.SPECTATOR;
				}
				return false;
			}
		}.checkGamemode(entity))) {
			usehand = (entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY);
			if (usehand.getOrCreateTag().getDouble("reloading") == 1 && usehand.getOrCreateTag().getDouble("prepare") == 0 && usehand.getOrCreateTag().getDouble("ammo") > 0) {
				usehand.getOrCreateTag().putDouble("forcestop", 1);
			}
			if (usehand.getItem() == TargetModItems.MARLIN.get() && usehand.getOrCreateTag().getDouble("reloading") == 0 && !(entity instanceof Player _plrCldCheck8 && _plrCldCheck8.getCooldowns().isOnCooldown(usehand.getItem()))
					&& usehand.getOrCreateTag().getDouble("ammo") > 0) {
				if ((entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).zooming == true) {
					if (entity instanceof Player _player)
						_player.getCooldowns().addCooldown(usehand.getItem(), 15);
					usehand.getOrCreateTag().putDouble("firing", 15);
					usehand.getOrCreateTag().putDouble("fastfiring", 0);
				} else {
					if (entity instanceof Player _player)
						_player.getCooldowns().addCooldown(usehand.getItem(), 10);
					usehand.getOrCreateTag().putDouble("fastfiring", 1);
					usehand.getOrCreateTag().putDouble("firing", 10);
				}
				BulletfireNormalProcedure.execute(entity);
				{
					Entity _ent = entity;
					if (!_ent.level().isClientSide() && _ent.getServer() != null) {
						_ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4,
								_ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), "playsound target:marlin_fire1p player @s ~ ~ ~ 100 1");
					}
				}
				{
					Entity _ent = entity;
					if (!_ent.level().isClientSide() && _ent.getServer() != null) {
						_ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4,
								_ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), "playsound target:marlin_fire3p player @a ~ ~ ~ 4 1");
					}
				}
				{
					Entity _ent = entity;
					if (!_ent.level().isClientSide() && _ent.getServer() != null) {
						_ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4,
								_ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), "stopsound @s player target:marlin_fire3p");
					}
				}
				usehand.getOrCreateTag().putDouble("ammo", (usehand.getOrCreateTag().getDouble("ammo") - 1));
				if (usehand.getOrCreateTag().getDouble("animindex") == 1) {
					usehand.getOrCreateTag().putDouble("animindex", 0);
				} else {
					usehand.getOrCreateTag().putDouble("animindex", 1);
				}
				usehand.getOrCreateTag().putDouble("fireanim", 2);
			}
		}
	}
}
