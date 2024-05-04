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

import net.mcreator.target.network.TargetModVariables;
import net.mcreator.target.init.TargetModItems;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber
public class M60autofireProcedure {
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
		if (entity.getPersistentData().getDouble("firing") == 1) {
			if (usehand.getItem() == TargetModItems.M_60.get() && usehand.getOrCreateTag().getDouble("reloading") == 0 && usehand.getOrCreateTag().getDouble("ammo") > 0
					&& !(entity instanceof Player _plrCldCheck5 && _plrCldCheck5.getCooldowns().isOnCooldown(usehand.getItem()))) {
				if (usehand.getOrCreateTag().getDouble("animindex") == 1) {
					usehand.getOrCreateTag().putDouble("animindex", 0);
				} else {
					usehand.getOrCreateTag().putDouble("animindex", 1);
				}
				if (entity instanceof Player _player)
					_player.getCooldowns().addCooldown(usehand.getItem(), 2);
				BulletfireNormalProcedure.execute(entity);
				{
					Entity _ent = entity;
					if (!_ent.level().isClientSide() && _ent.getServer() != null) {
						_ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4,
								_ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), "playsound target:m60fire player @a ~ ~ ~ 4 1");
					}
				}
				usehand.getOrCreateTag().putDouble("ammo", (usehand.getOrCreateTag().getDouble("ammo") - 1));
				usehand.getOrCreateTag().putDouble("empty", 1);
				usehand.getOrCreateTag().putDouble("fireanim", 2);
				{
					double _setval = 1;
					entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
						capability.firing = _setval;
						capability.syncPlayerVariables(entity);
					});
				}
			}
		}
	}
}
