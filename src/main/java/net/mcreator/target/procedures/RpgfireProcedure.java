package net.mcreator.target.procedures;

import net.minecraft.world.level.Level;
import net.minecraft.world.level.GameType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.AbstractArrow;
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
import net.mcreator.target.init.TargetModEntities;
import net.mcreator.target.init.TargetModAttributes;
import net.mcreator.target.entity.RpgRocketEntity;

public class RpgfireProcedure {
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
			if (usehand.getItem() == TargetModItems.RPG.get() && usehand.getOrCreateTag().getDouble("reloading") == 0 && !(entity instanceof Player _plrCldCheck4 && _plrCldCheck4.getCooldowns().isOnCooldown(usehand.getItem()))
					&& usehand.getOrCreateTag().getDouble("ammo") > 0) {
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
				{
					Entity _shootFrom = entity;
					Level projectileLevel = _shootFrom.level();
					if (!projectileLevel.isClientSide()) {
						Projectile _entityToSpawn = new Object() {
							public Projectile getArrow(Level level, Entity shooter, float damage, int knockback) {
								AbstractArrow entityToSpawn = new RpgRocketEntity(TargetModEntities.RPG_ROCKET.get(), level);
								entityToSpawn.setOwner(shooter);
								entityToSpawn.setBaseDamage(damage);
								entityToSpawn.setKnockback(knockback);
								entityToSpawn.setSilent(true);
								return entityToSpawn;
							}
						}.getArrow(projectileLevel, entity, (float) ((usehand.getOrCreateTag().getDouble("damage") / usehand.getOrCreateTag().getDouble("velocity")) * (1 + 0.05 * usehand.getOrCreateTag().getDouble("level"))), 0);
						_entityToSpawn.setPos(_shootFrom.getX(), _shootFrom.getEyeY() - 0.1, _shootFrom.getZ());
						_entityToSpawn.shoot(_shootFrom.getLookAngle().x, _shootFrom.getLookAngle().y, _shootFrom.getLookAngle().z, (float) usehand.getOrCreateTag().getDouble("velocity"),
								(float) ((LivingEntity) entity).getAttribute(TargetModAttributes.SPREAD.get()).getBaseValue());
						projectileLevel.addFreshEntity(_entityToSpawn);
					}
				}
				{
					Entity _ent = entity;
					if (!_ent.level().isClientSide() && _ent.getServer() != null) {
						_ent.getServer().getCommands().performPrefixedCommand(
								new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4, _ent.getName().getString(), _ent.getDisplayName(),
										_ent.level().getServer(), _ent),
								("particle minecraft:cloud" + (" " + (entity.getX() + 1.8 * entity.getLookAngle().x)) + (" " + (entity.getY() + entity.getBbHeight() - 0.1 + 1.8 * entity.getLookAngle().y))
										+ (" " + (entity.getZ() + 1.8 * entity.getLookAngle().z)) + " 0.4 0.4 0.4 0.005 30 force @s"));
					}
				}
				if (usehand.getOrCreateTag().getDouble("ammo") == 1) {
					usehand.getOrCreateTag().putDouble("empty", 1);
				}
				if (entity instanceof Player _player)
					_player.getCooldowns().addCooldown(usehand.getItem(), 10);
				{
					Entity _ent = entity;
					if (!_ent.level().isClientSide() && _ent.getServer() != null) {
						_ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4,
								_ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), "playsound target:rpg7_fire player @a ~ ~ ~ 3 1");
					}
				}
				usehand.getOrCreateTag().putDouble("fireanim", 2);
				usehand.getOrCreateTag().putDouble("ammo", (usehand.getOrCreateTag().getDouble("ammo") - 1));
			}
		}
	}
}
