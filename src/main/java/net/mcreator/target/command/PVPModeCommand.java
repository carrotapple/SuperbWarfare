
package net.mcreator.target.command;

import net.mcreator.target.network.TargetModVariables;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class PVPModeCommand {
    @SubscribeEvent
    public static void registerCommand(RegisterCommandsEvent event) {
        event.getDispatcher().register(Commands.literal("target:pvpmode").requires(s -> s.hasPermission(4)).executes(arguments -> {
            Level world = arguments.getSource().getUnsidedLevel();
            Entity entity = arguments.getSource().getEntity();
            if (entity == null && world instanceof ServerLevel server) {
                entity = FakePlayerFactory.getMinecraft(server);
            }

            var mapVariables = TargetModVariables.MapVariables.get(world);
            mapVariables.pvpMode = !mapVariables.pvpMode;
            mapVariables.syncData(world);

            if (entity instanceof Player player && !player.level().isClientSide()) {
                player.displayClientMessage(Component.translatable("commands.pvp_mode." + (mapVariables.pvpMode ? "on" : "off")), false);
            }
            return 0;
        }));
    }
}
