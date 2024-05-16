package net.mcreator.target.command;

import net.mcreator.target.init.TargetModSounds;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class MediumExpCommand {
    @SubscribeEvent
    public static void registerCommand(RegisterCommandsEvent event) {
        event.getDispatcher().register(Commands.literal("target:mediumexp")
                .executes(arguments -> {
                    Level world = arguments.getSource().getUnsidedLevel();
                    double x = arguments.getSource().getPosition().x();
                    double y = arguments.getSource().getPosition().y();
                    double z = arguments.getSource().getPosition().z();

                    if (!world.isClientSide()) {
                        world.playSound(null, BlockPos.containing(x, y + 1, z), TargetModSounds.EXP.get(), SoundSource.BLOCKS, 8, 1);
                        world.playSound(null, BlockPos.containing(x, y + 1, z), TargetModSounds.EXPFAR.get(), SoundSource.BLOCKS, 16, 1);
                        world.playSound(null, BlockPos.containing(x, y + 1, z), TargetModSounds.EXPVERYFAR.get(), SoundSource.BLOCKS, 32, 1);
                    } else {
                        world.playLocalSound(x, (y + 1), z, TargetModSounds.EXP.get(), SoundSource.BLOCKS, 24, 1, false);
                        world.playLocalSound(x, (y + 1), z, TargetModSounds.EXPFAR.get(), SoundSource.BLOCKS, 24, 1, false);
                        world.playLocalSound(x, (y + 1), z, TargetModSounds.EXPVERYFAR.get(), SoundSource.BLOCKS, 64, 1, false);
                    }
                    if (world instanceof ServerLevel server) {
                        server.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, (y + 1), z), Vec2.ZERO, server, 4, "", Component.literal(""), server.getServer(), null).withSuppressedOutput(),
                                "particle minecraft:campfire_cosy_smoke ~ ~ ~ 0.4 1 0.4 0.02 80 force");
                        server.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, (y + 1), z), Vec2.ZERO, server, 4, "", Component.literal(""), server.getServer(), null).withSuppressedOutput(),
                                "particle minecraft:large_smoke ~ ~1 ~ 0.4 1 0.4 0.02 80 force");
                        server.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, (y + 1), z), Vec2.ZERO, server, 4, "", Component.literal(""), server.getServer(), null).withSuppressedOutput(),
                                "particle minecraft:campfire_cosy_smoke ~ ~ ~ 2 0.001 2 0.01 80 force");
                        server.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, (y + 1), z), Vec2.ZERO, server, 4, "", Component.literal(""), server.getServer(), null).withSuppressedOutput(),
                                "particle target:firestar ~ ~ ~ 0 0 0 0.2 80 force");
                    }
                    return 0;
                }));
    }
}
