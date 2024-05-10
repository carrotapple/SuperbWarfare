package net.mcreator.target.procedures;

import net.mcreator.target.init.TargetModItems;
import net.mcreator.target.network.TargetModVariables;
import net.mcreator.target.tools.GunsTool;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class MinigunautofireProcedure {
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            execute(event.player);
        }
    }

    private static void execute(Player player) {
        ItemStack usehand;
        usehand = player.getMainHandItem();
        if (usehand.getItem() == TargetModItems.MINIGUN.get()) {
            if (player.getPersistentData().getDouble("minifiring") == 1 && !player.isSprinting()) {
                if (usehand.getOrCreateTag().getDouble("rot") < 10) {
                    usehand.getOrCreateTag().putDouble("rot", (usehand.getOrCreateTag().getDouble("rot") + 1));
                }
                if (!player.level().isClientSide() && player.getServer() != null) {
                    player.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, player.position(), player.getRotationVector(), (ServerLevel) player.level(), 4,
                            player.getName().getString(), player.getDisplayName(), player.level().getServer(), player), "playsound target:minigun_rot player @s ~ ~ ~ 2 1");
                }
            } else if (usehand.getOrCreateTag().getDouble("rot") > 0) {
                usehand.getOrCreateTag().putDouble("rot", (usehand.getOrCreateTag().getDouble("rot") - 0.5));
            }
        }
        if (usehand.getItem() == TargetModItems.MINIGUN.get() && usehand.getOrCreateTag().getDouble("overheat") == 0
                && (player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).rifleammo > 0
                && !(player.getCooldowns().isOnCooldown(usehand.getItem())) && usehand.getOrCreateTag().getDouble("rot") >= 10) {
            usehand.getOrCreateTag().putDouble("heat", (usehand.getOrCreateTag().getDouble("heat") + 1));
            if (usehand.getOrCreateTag().getDouble("heat") >= 50.5) {
                usehand.getOrCreateTag().putDouble("overheat", 40);
                player.getCooldowns().addCooldown(usehand.getItem(), 40);
                if (!player.level().isClientSide() && player.getServer() != null) {
                    player.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, player.position(), player.getRotationVector(), (ServerLevel) player.level(), 4,
                            player.getName().getString(), player.getDisplayName(), player.level().getServer(), player), "playsound target:minigun_overheat player @s ~ ~ ~ 2 1");
                }
            }
            if (!player.level().isClientSide() && player.getServer() != null) {
                if (usehand.getOrCreateTag().getDouble("heat") <= 40) {
                    player.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, player.position(), player.getRotationVector(), (ServerLevel) player.level(), 4,
                            player.getName().getString(), player.getDisplayName(), player.level().getServer(), player), "playsound target:minigun_fire_1p player @s ~ ~ ~ 2 1");
                    player.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, player.position(), player.getRotationVector(), (ServerLevel) player.level(), 4,
                            player.getName().getString(), player.getDisplayName(), player.level().getServer(), player), "playsound target:minigun_fire_3p player @a ~ ~ ~ 4 1");
                    player.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, player.position(), player.getRotationVector(), (ServerLevel) player.level(), 4,
                            player.getName().getString(), player.getDisplayName(), player.level().getServer(), player), "playsound target:minigun_far player @a ~ ~ ~ 12 1");
                    player.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, player.position(), player.getRotationVector(), (ServerLevel) player.level(), 4,
                            player.getName().getString(), player.getDisplayName(), player.level().getServer(), player), "playsound target:minigun_veryfar player @a ~ ~ ~ 24 1");
                } else {
                    player.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, player.position(), player.getRotationVector(), (ServerLevel) player.level(), 4,
                            player.getName().getString(), player.getDisplayName(), player.level().getServer(), player), ("playsound target:minigun_fire_1p player @s ~ ~ ~ 2 " + (1 - 0.025 * Math.abs(40 - usehand.getOrCreateTag().getDouble("heat")))));
                    player.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, player.position(), player.getRotationVector(), (ServerLevel) player.level(), 4,
                            player.getName().getString(), player.getDisplayName(), player.level().getServer(), player), ("playsound target:minigun_fire_3p player @a ~ ~ ~ 4 " + (1 - 0.025 * Math.abs(40 - usehand.getOrCreateTag().getDouble("heat")))));
                    player.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, player.position(), player.getRotationVector(), (ServerLevel) player.level(), 4,
                            player.getName().getString(), player.getDisplayName(), player.level().getServer(), player), ("playsound target:minigun_far player @a ~ ~ ~ 12 " + (1 - 0.025 * Math.abs(40 - usehand.getOrCreateTag().getDouble("heat")))));
                    player.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, player.position(), player.getRotationVector(), (ServerLevel) player.level(), 4,
                            player.getName().getString(), player.getDisplayName(), player.level().getServer(), player), ("playsound target:minigun_veryfar player @a ~ ~ ~ 24 " + (1 - 0.025 * Math.abs(40 - usehand.getOrCreateTag().getDouble("heat")))));
                }
            }
            GunsTool.spawnBullet(player);

            player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                capability.rifleammo = player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables()).rifleammo - 1;
                capability.syncPlayerVariables(player);
            });

            usehand.getOrCreateTag().putDouble("fireanim", 2);
        }
    }
}