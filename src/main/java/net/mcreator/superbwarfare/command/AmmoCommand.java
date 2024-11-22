
package net.mcreator.superbwarfare.command;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.mcreator.superbwarfare.network.ModVariables;
import net.mcreator.superbwarfare.tools.GunInfo;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraftforge.server.command.EnumArgument;

import java.util.Objects;

public class AmmoCommand {
    public static LiteralArgumentBuilder<CommandSourceStack> get() {
        // mojang你看看你写的是个牛魔Builder😅
        return Commands.literal("ammo").requires(s -> s.hasPermission(0))
                .then(Commands.literal("get").then(Commands.argument("player", EntityArgument.player()).then(Commands.argument("type", EnumArgument.enumArgument(GunInfo.Type.class)).executes(context -> {
                    var player = EntityArgument.getPlayer(context, "player");

                    // 权限不足时，只允许玩家查询自己的弹药数量
                    if (context.getSource().isPlayer() && !context.getSource().hasPermission(2)) {
                        if (!Objects.requireNonNull(context.getSource().getPlayer()).getUUID().equals(player.getUUID())) {
                            context.getSource().sendFailure(Component.translatable("commands.ammo.no_permission"));
                            return 0;
                        }
                    }

                    var type = context.getArgument("type", GunInfo.Type.class);

                    var value = player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).map(c ->
                            switch (type) {
                                case HANDGUN -> c.handgunAmmo;
                                case RIFLE -> c.rifleAmmo;
                                case SHOTGUN -> c.shotgunAmmo;
                                case SNIPER -> c.sniperAmmo;
                            }
                    ).orElse(0);
                    context.getSource().sendSuccess(() -> Component.translatable("commands.ammo.get", Component.translatable(type.translatableKey), value), true);
                    return 0;
                }))))
                .then(Commands.literal("set").requires(s -> s.hasPermission(2)).then(Commands.argument("players", EntityArgument.players()).then(Commands.argument("type", EnumArgument.enumArgument(GunInfo.Type.class)).then(Commands.argument("value", IntegerArgumentType.integer(0)).executes(context -> {
                    var players = EntityArgument.getPlayers(context, "players");
                    var type = context.getArgument("type", GunInfo.Type.class);
                    var value = IntegerArgumentType.getInteger(context, "value");

                    for (var player : players) {
                        player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                            switch (type) {
                                case HANDGUN -> capability.handgunAmmo = value;
                                case RIFLE -> capability.rifleAmmo = value;
                                case SHOTGUN -> capability.shotgunAmmo = value;
                                case SNIPER -> capability.sniperAmmo = value;
                            }
                            capability.syncPlayerVariables(player);
                        });
                    }

                    context.getSource().sendSuccess(() -> Component.translatable("commands.ammo.set", Component.translatable(type.translatableKey), value, players.size()), true);
                    return 0;
                })))))
                .then(Commands.literal("add").requires(s -> s.hasPermission(2)).then(Commands.argument("players", EntityArgument.players()).then(Commands.argument("type", EnumArgument.enumArgument(GunInfo.Type.class)).then(Commands.argument("value", IntegerArgumentType.integer(0)).executes(context -> {
                    var players = EntityArgument.getPlayers(context, "players");
                    var type = context.getArgument("type", GunInfo.Type.class);
                    var value = IntegerArgumentType.getInteger(context, "value");

                    for (var player : players) {
                        player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                            switch (type) {
                                case HANDGUN -> capability.handgunAmmo += value;
                                case RIFLE -> capability.rifleAmmo += value;
                                case SHOTGUN -> capability.shotgunAmmo += value;
                                case SNIPER -> capability.sniperAmmo += value;
                            }
                            // 迫真溢出检测
                            if (capability.handgunAmmo < 0) capability.handgunAmmo = Integer.MAX_VALUE;
                            if (capability.rifleAmmo < 0) capability.rifleAmmo = Integer.MAX_VALUE;
                            if (capability.shotgunAmmo < 0) capability.shotgunAmmo = Integer.MAX_VALUE;
                            if (capability.sniperAmmo < 0) capability.sniperAmmo = Integer.MAX_VALUE;

                            capability.syncPlayerVariables(player);
                        });
                    }

                    context.getSource().sendSuccess(() -> Component.translatable("commands.ammo.add", Component.translatable(type.translatableKey), value, players.size()), true);
                    return 0;
                })))));
    }
}
