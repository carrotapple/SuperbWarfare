
package net.mcreator.target.command;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.mcreator.target.network.TargetModVariables;
import net.mcreator.target.tools.GunInfo;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.command.EnumArgument;

@Mod.EventBusSubscriber
public class SetAmmoCommand {
    @SubscribeEvent
    public static void registerCommand(RegisterCommandsEvent event) {
        // mojang你看看你写的是个牛魔Builder😅
        event.getDispatcher().register(Commands.literal("ammo").requires(s -> s.hasPermission(4))
                .then(Commands.literal("get").then(Commands.argument("type", EnumArgument.enumArgument(GunInfo.Type.class)).executes(context -> {
                    var player = context.getSource().getPlayer();
                    if (player == null) return 0;

                    var type = context.getArgument("type", GunInfo.Type.class);

                    var value = player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).map(c ->
                            switch (type) {
                                case HANDGUN -> c.handgunAmmo;
                                case RIFLE -> c.rifleAmmo;
                                case SHOTGUN -> c.shotgunAmmo;
                                case SNIPER -> c.sniperAmmo;
                            }
                    ).orElse(0d);
                    context.getSource().sendSuccess(() -> Component.literal("Current " + type.name + " ammo: " + value), true);
                    return 0;
                })))
                .then(Commands.literal("set").then(Commands.argument("type", EnumArgument.enumArgument(GunInfo.Type.class)).then(Commands.argument("value", IntegerArgumentType.integer(0, 2147483647)).executes(context -> {
                    var player = context.getSource().getPlayer();
                    if (player == null) return 0;

                    var type = context.getArgument("type", GunInfo.Type.class);
                    var value = IntegerArgumentType.getInteger(context, "value");

                    player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                        switch (type) {
                            case HANDGUN -> capability.handgunAmmo = value;
                            case RIFLE -> capability.rifleAmmo = value;
                            case SHOTGUN -> capability.shotgunAmmo = value;
                            case SNIPER -> capability.sniperAmmo = value;
                        }
                        capability.syncPlayerVariables(player);
                    });
                    context.getSource().sendSuccess(() -> Component.literal("Set " + type.name + " ammo to: " + value), true);
                    return 0;
                }))))
                .then(Commands.literal("add").then(Commands.argument("type", EnumArgument.enumArgument(GunInfo.Type.class)).then(Commands.argument("value", IntegerArgumentType.integer(0, 2147483647)).executes(context -> {
                    var player = context.getSource().getPlayer();
                    if (player == null) return 0;
                    var type = context.getArgument("type", GunInfo.Type.class);
                    var value = IntegerArgumentType.getInteger(context, "value");

                    player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                        switch (type) {
                            case HANDGUN -> capability.handgunAmmo += value;
                            case RIFLE -> capability.rifleAmmo += value;
                            case SHOTGUN -> capability.shotgunAmmo += value;
                            case SNIPER -> capability.sniperAmmo += value;
                        }
                        capability.syncPlayerVariables(player);
                    });
                    context.getSource().sendSuccess(() -> Component.literal("Added " + type.name + " ammo of amount " + value), true);
                    return 0;
                }))))
        );
    }
}
