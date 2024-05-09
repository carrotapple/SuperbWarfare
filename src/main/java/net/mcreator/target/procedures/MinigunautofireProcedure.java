package net.mcreator.target.procedures;

import net.mcreator.target.init.TargetModItems;
import net.mcreator.target.network.TargetModVariables;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber
public class MinigunautofireProcedure {
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
        ItemStack usehand;
        usehand = (entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY);
        if (usehand.getItem() == TargetModItems.MINIGUN.get()) {
            if (entity.getPersistentData().getDouble("minifiring") == 1 && !entity.isSprinting()) {
                if (usehand.getOrCreateTag().getDouble("rot") < 10) {
                    usehand.getOrCreateTag().putDouble("rot", (usehand.getOrCreateTag().getDouble("rot") + 1));
                }
                {
                    if (!entity.level().isClientSide() && entity.getServer() != null) {
                        entity.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, entity.position(), entity.getRotationVector(), entity.level() instanceof ServerLevel ? (ServerLevel) entity.level() : null, 4,
                                entity.getName().getString(), entity.getDisplayName(), entity.level().getServer(), entity), "playsound target:minigun_rot player @s ~ ~ ~ 2 1");
                    }
                }
            } else {
                if (usehand.getOrCreateTag().getDouble("rot") > 0) {
                    usehand.getOrCreateTag().putDouble("rot", (usehand.getOrCreateTag().getDouble("rot") - 0.5));
                }
            }
        }
        if (usehand.getItem() == TargetModItems.MINIGUN.get() && usehand.getOrCreateTag().getDouble("overheat") == 0
                && (entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).rifleammo > 0
                && !(entity instanceof Player _plrCldCheck13 && _plrCldCheck13.getCooldowns().isOnCooldown(usehand.getItem())) && usehand.getOrCreateTag().getDouble("rot") >= 10) {
            usehand.getOrCreateTag().putDouble("heat", (usehand.getOrCreateTag().getDouble("heat") + 1));
            if (usehand.getOrCreateTag().getDouble("heat") >= 50.5) {
                usehand.getOrCreateTag().putDouble("overheat", 40);
                if (entity instanceof Player _player)
                    _player.getCooldowns().addCooldown(usehand.getItem(), 40);
                {
                    if (!entity.level().isClientSide() && entity.getServer() != null) {
                        entity.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, entity.position(), entity.getRotationVector(), entity.level() instanceof ServerLevel ? (ServerLevel) entity.level() : null, 4,
                                entity.getName().getString(), entity.getDisplayName(), entity.level().getServer(), entity), "playsound target:minigun_overheat player @s ~ ~ ~ 2 1");
                    }
                }
            }
            if (usehand.getOrCreateTag().getDouble("heat") <= 40) {
                {
                    if (!entity.level().isClientSide() && entity.getServer() != null) {
                        entity.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, entity.position(), entity.getRotationVector(), entity.level() instanceof ServerLevel ? (ServerLevel) entity.level() : null, 4,
                                entity.getName().getString(), entity.getDisplayName(), entity.level().getServer(), entity), "playsound target:minigun_fire_1p player @s ~ ~ ~ 2 1");
                        entity.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, entity.position(), entity.getRotationVector(), entity.level() instanceof ServerLevel ? (ServerLevel) entity.level() : null, 4,
                                entity.getName().getString(), entity.getDisplayName(), entity.level().getServer(), entity), "playsound target:minigun_fire_3p player @a ~ ~ ~ 4 1");
                        entity.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, entity.position(), entity.getRotationVector(), entity.level() instanceof ServerLevel ? (ServerLevel) entity.level() : null, 4,
                                entity.getName().getString(), entity.getDisplayName(), entity.level().getServer(), entity), "playsound target:minigun_far player @a ~ ~ ~ 12 1");
                        entity.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, entity.position(), entity.getRotationVector(), entity.level() instanceof ServerLevel ? (ServerLevel) entity.level() : null, 4,
                                entity.getName().getString(), entity.getDisplayName(), entity.level().getServer(), entity), "playsound target:minigun_veryfar player @a ~ ~ ~ 24 1");
                    }
                }
            } else {
                {
                    if (!entity.level().isClientSide() && entity.getServer() != null) {
                        entity.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, entity.position(), entity.getRotationVector(), entity.level() instanceof ServerLevel ? (ServerLevel) entity.level() : null, 4,
                                entity.getName().getString(), entity.getDisplayName(), entity.level().getServer(), entity), ("playsound target:minigun_fire_1p player @s ~ ~ ~ 2 " + (1 - 0.025 * Math.abs(40 - usehand.getOrCreateTag().getDouble("heat")))));
                        entity.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, entity.position(), entity.getRotationVector(), entity.level() instanceof ServerLevel ? (ServerLevel) entity.level() : null, 4,
                                entity.getName().getString(), entity.getDisplayName(), entity.level().getServer(), entity), ("playsound target:minigun_fire_3p player @a ~ ~ ~ 4 " + (1 - 0.025 * Math.abs(40 - usehand.getOrCreateTag().getDouble("heat")))));
                        entity.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, entity.position(), entity.getRotationVector(), entity.level() instanceof ServerLevel ? (ServerLevel) entity.level() : null, 4,
                                entity.getName().getString(), entity.getDisplayName(), entity.level().getServer(), entity), ("playsound target:minigun_far player @a ~ ~ ~ 12 " + (1 - 0.025 * Math.abs(40 - usehand.getOrCreateTag().getDouble("heat")))));
                        entity.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, entity.position(), entity.getRotationVector(), entity.level() instanceof ServerLevel ? (ServerLevel) entity.level() : null, 4,
                                entity.getName().getString(), entity.getDisplayName(), entity.level().getServer(), entity), ("playsound target:minigun_veryfar player @a ~ ~ ~ 24 " + (1 - 0.025 * Math.abs(40 - usehand.getOrCreateTag().getDouble("heat")))));
                    }
                }
            }
            BulletFireNormalProcedure.execute(entity);
            {
                double _setval = (entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).rifleammo - 1;
                entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                    capability.rifleammo = _setval;
                    capability.syncPlayerVariables(entity);
                });
            }
            usehand.getOrCreateTag().putDouble("fireanim", 2);
        }
    }
}
