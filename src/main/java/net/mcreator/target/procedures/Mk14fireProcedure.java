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

import net.mcreator.target.init.TargetModItems;

public class Mk14fireProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		ItemStack usehand = ItemStack.EMPTY;
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
			if (usehand.getItem() == TargetModItems.MK_14.get()) {
				if (usehand.getOrCreateTag().getDouble("firemode") == 0) {
					if (usehand.getOrCreateTag().getDouble("reloading") == 0 && usehand.getOrCreateTag().getDouble("ammo") > 0 && !(entity instanceof Player _plrCldCheck6 && _plrCldCheck6.getCooldowns().isOnCooldown(usehand.getItem()))) {
						BulletfireNormalProcedure.execute(entity);
						if (entity instanceof Player _player)
							_player.getCooldowns().addCooldown(usehand.getItem(), 2);
						{
							Entity _ent = entity;
							if (!_ent.level().isClientSide() && _ent.getServer() != null) {
								_ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4,
										_ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), "playsound target:mk14fire player @a ~ ~ ~ 4 1");
							}
						}
						usehand.getOrCreateTag().putDouble("fireanim", 2);
						usehand.getOrCreateTag().putDouble("ammo", (usehand.getOrCreateTag().getDouble("ammo") - 1));
					}
				} else if (usehand.getOrCreateTag().getDouble("firemode") == 2) {
					entity.getPersistentData().putDouble("firing", 1);
				}
			}
		}
	}
}
