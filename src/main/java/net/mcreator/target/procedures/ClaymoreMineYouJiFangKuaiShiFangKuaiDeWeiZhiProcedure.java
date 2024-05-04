package net.mcreator.target.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.GameType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.InteractionHand;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.client.Minecraft;

import net.mcreator.target.init.TargetModEntities;
import net.mcreator.target.entity.ClaymoreEntity;

public class ClaymoreMineYouJiFangKuaiShiFangKuaiDeWeiZhiProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity, ItemStack itemstack) {
		if (entity == null)
			return;
		if (entity instanceof LivingEntity _entity)
			_entity.swing(InteractionHand.MAIN_HAND, true);
		if (world instanceof ServerLevel _level) {
			Entity entityToSpawn = new ClaymoreEntity(TargetModEntities.CLAYMORE.get(), _level);
			entityToSpawn.moveTo(x, (y + 1.1), z, entity.getYRot(), entity.getXRot());
			entityToSpawn.setYBodyRot(entity.getYRot());
			entityToSpawn.setYHeadRot(entity.getYRot());
			entityToSpawn.setDeltaMovement((0.5 * entity.getLookAngle().x), (0.5 * entity.getLookAngle().y), (0.5 * entity.getLookAngle().z));
			if (entityToSpawn instanceof Mob _mobToSpawn) {
				_mobToSpawn.finalizeSpawn(_level, _level.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
				if (entityToSpawn instanceof TamableAnimal _toTame && entity instanceof Player _owner) {
					_toTame.tame(_owner);
				}
			}
			_level.addFreshEntity(entityToSpawn);
		}
		if (entity instanceof Player _player)
			_player.getCooldowns().addCooldown(itemstack.getItem(), 20);
		if (!(new Object() {
			public boolean checkGamemode(Entity _ent) {
				if (_ent instanceof ServerPlayer _serverPlayer) {
					return _serverPlayer.gameMode.getGameModeForPlayer() == GameType.CREATIVE;
				} else if (_ent.level().isClientSide() && _ent instanceof Player _player) {
					return Minecraft.getInstance().getConnection().getPlayerInfo(_player.getGameProfile().getId()) != null && Minecraft.getInstance().getConnection().getPlayerInfo(_player.getGameProfile().getId()).getGameMode() == GameType.CREATIVE;
				}
				return false;
			}
		}.checkGamemode(entity))) {
			if (entity instanceof Player _player) {
				ItemStack _stktoremove = itemstack;
				_player.getInventory().clearOrCountMatchingItems(p -> _stktoremove.getItem() == p.getItem(), 1, _player.inventoryMenu.getCraftSlots());
			}
		}
	}
}
