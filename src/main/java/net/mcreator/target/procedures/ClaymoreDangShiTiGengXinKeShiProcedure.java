package net.mcreator.target.procedures;

import net.minecraftforge.registries.ForgeRegistries;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.GameType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.sounds.SoundSource;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;
import net.minecraft.client.Minecraft;

import net.mcreator.target.init.TargetModItems;
import net.mcreator.target.entity.Target1Entity;
import net.mcreator.target.entity.ClaymoreEntity;
import net.mcreator.target.TargetMod;

import java.util.List;
import java.util.Comparator;

public class ClaymoreDangShiTiGengXinKeShiProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		boolean target = false;
		entity.getPersistentData().putDouble("life", (entity.getPersistentData().getDouble("life") + 1));
		if (entity.getPersistentData().getDouble("life") >= 12000) {
			if (!entity.level().isClientSide())
				entity.discard();
		}
		if (entity.getPersistentData().getDouble("def") >= 100) {
			{
				Entity _ent = entity;
				if (!_ent.level().isClientSide() && _ent.getServer() != null) {
					_ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4,
							_ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), "playsound minecraft:item.shield.break player @p ~ ~ ~ 1 1");
				}
			}
			if (!entity.level().isClientSide())
				entity.discard();
			if (world instanceof Level _level) {
				if (!_level.isClientSide()) {
					_level.playSound(null, BlockPos.containing(x, y, z), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("item.shield.break")), SoundSource.PLAYERS, 1, 1);
				} else {
					_level.playLocalSound(x, y, z, ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("item.shield.break")), SoundSource.PLAYERS, 1, 1, false);
				}
			}
			if (world instanceof ServerLevel _level) {
				ItemEntity entityToSpawn = new ItemEntity(_level, x, y, z, new ItemStack(TargetModItems.CLAYMORE_MINE.get()));
				entityToSpawn.setPickUpDelay(10);
				_level.addFreshEntity(entityToSpawn);
			}
		}
		if (entity instanceof LivingEntity _entity)
			_entity.removeAllEffects();
		entity.clearFire();
		if (entity.getPersistentData().getDouble("trigger") <= 60) {
			entity.getPersistentData().putDouble("trigger", (entity.getPersistentData().getDouble("trigger") + 1));
		}
		if (entity.getPersistentData().getDouble("trigger") >= 40) {
			{
				final Vec3 _center = new Vec3((x + 1.5 * entity.getLookAngle().x), (y + 1.5 * entity.getLookAngle().y), (z + 1.5 * entity.getLookAngle().z));
				List<Entity> _entfound = world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(2.5 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList();
				for (Entity entityiterator : _entfound) {
					target = !((entity instanceof TamableAnimal _tamEnt ? (Entity) _tamEnt.getOwner() : null) == entityiterator) && entityiterator instanceof LivingEntity && !(entityiterator instanceof ClaymoreEntity)
							&& !(entityiterator instanceof Target1Entity)
							&& !(entityiterator instanceof Player player && (player.isCreative() || player.isSpectator()))
							&& (!entity.isAlliedTo(entityiterator) || entityiterator.getTeam() == null  || entityiterator.getTeam().getName().equals("TDM"))
							&& !entityiterator.isShiftKeyDown();
					if (target) {
						{
							Entity _ent = entity;
							if (!_ent.level().isClientSide() && _ent.getServer() != null) {
								_ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4,
										_ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), "target:mediumexp");
							}
						}
						if (!entity.level().isClientSide())
							entity.discard();
							entityiterator.getPersistentData().putDouble("claymore", 5);
						TargetMod.queueServerWork(1, () -> {
							if (world instanceof Level _level && !_level.isClientSide())
								_level.explode((entity instanceof TamableAnimal _tamEnt ? (Entity) _tamEnt.getOwner() : null), (entityiterator.getX()), (entityiterator.getY()), (entityiterator.getZ()), 6.5f, Level.ExplosionInteraction.NONE);
						});
					}
				}
			}
		}
	}
}
