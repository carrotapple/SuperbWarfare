
package net.mcreator.target.command;

import org.checkerframework.checker.units.qual.s;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.common.util.FakePlayerFactory;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.Direction;
import net.minecraft.commands.Commands;
import net.minecraft.world.entity.player.Player;

import net.minecraft.network.chat.Component;
import net.minecraft.world.level.LevelAccessor;

import net.mcreator.target.network.TargetModVariables;

@Mod.EventBusSubscriber
public class PvPmodeCommand {
    @SubscribeEvent
    public static void registerCommand(RegisterCommandsEvent event) {
        event.getDispatcher().register(Commands.literal("target:pvpmode").requires(s -> s.hasPermission(4)).executes(arguments -> {
            Level world = arguments.getSource().getUnsidedLevel();
            double x = arguments.getSource().getPosition().x();
            double y = arguments.getSource().getPosition().y();
            double z = arguments.getSource().getPosition().z();
            Entity entity = arguments.getSource().getEntity();
            if (entity == null && world instanceof ServerLevel _servLevel)
                entity = FakePlayerFactory.getMinecraft(_servLevel);
            Direction direction = Direction.DOWN;
            if (entity != null)
                direction = entity.getDirection();

            if (TargetModVariables.MapVariables.get(world).pvpmode == true) {
                TargetModVariables.MapVariables.get(world).pvpmode = false;
                TargetModVariables.MapVariables.get(world).syncData(world);
                if (entity instanceof Player _player && !_player.level().isClientSide())
                    _player.displayClientMessage(Component.literal("PVPMODE:OFF"), false);

            } else {
                TargetModVariables.MapVariables.get(world).pvpmode = true;
                TargetModVariables.MapVariables.get(world).syncData(world);
                if (entity instanceof Player _player && !_player.level().isClientSide())
                    _player.displayClientMessage(Component.literal("PVPMODE:ON"), false);

            }
            return 0;
        }));
    }
}
