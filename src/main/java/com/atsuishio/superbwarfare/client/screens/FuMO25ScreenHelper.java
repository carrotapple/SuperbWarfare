package com.atsuishio.superbwarfare.client.screens;

import com.atsuishio.superbwarfare.menu.FuMO25Menu;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class FuMO25ScreenHelper {

    public static List<Entity> entities = new ArrayList<>();

    @SubscribeEvent
    public static void onContainerClosed(PlayerContainerEvent.Close event) {
        if (event.getContainer() instanceof FuMO25Menu menu && event.getEntity() instanceof ServerPlayer serverPlayer) {
            serverPlayer.displayClientMessage(Component.literal("Closed!"), false);
        }
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.side != LogicalSide.CLIENT) return;
        if (event.phase != TickEvent.Phase.END) return;
        Player player = Minecraft.getInstance().player;
        if (player == null) return;
        var menu = player.containerMenu;
        if (!(menu instanceof FuMO25Menu fuMO25Menu)) return;


    }
}
