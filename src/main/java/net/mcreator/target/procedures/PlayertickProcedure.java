package net.mcreator.target.procedures;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.event.TickEvent;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.core.BlockPos;

import net.mcreator.target.network.TargetModVariables;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber
public class PlayertickProcedure {
	@SubscribeEvent
	public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
		if (event.phase == TickEvent.Phase.END) {
			execute(event, event.player.level(), event.player);
		}
	}

	public static void execute(LevelAccessor world, Entity entity) {
		execute(null, world, entity);
	}

	private static void execute(@Nullable Event event, LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		if (entity instanceof Player && entity.getBbHeight() <= 1) {
			entity.getPersistentData().putDouble("prone", 3);
		}
		if (entity.isShiftKeyDown() && world.getBlockState(BlockPos.containing(entity.getX() + 0.7 * entity.getLookAngle().x, entity.getY() + 0.5, entity.getZ() + 0.7 * entity.getLookAngle().z)).canOcclude()
				&& !world.getBlockState(BlockPos.containing(entity.getX() + 0.7 * entity.getLookAngle().x, entity.getY() + 1.5, entity.getZ() + 0.7 * entity.getLookAngle().z)).canOcclude()) {
			entity.getPersistentData().putDouble("prone", 3);
		}
		if (entity.getPersistentData().getDouble("prone") > 0) {
			entity.getPersistentData().putDouble("prone", (entity.getPersistentData().getDouble("prone") - 1));
		}
		if ((entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).refresh == false) {
			{
				boolean _setval = true;
				entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
					capability.refresh = _setval;
					capability.syncPlayerVariables(entity);
				});
			}
		} else {
			{
				boolean _setval = false;
				entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
					capability.refresh = _setval;
					capability.syncPlayerVariables(entity);
				});
			}
		}
	}
}
