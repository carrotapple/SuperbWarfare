package net.mcreator.superbwarfare.network;

import net.mcreator.superbwarfare.client.screens.CrossHairOverlay;
import net.mcreator.superbwarfare.client.screens.DroneUIOverlay;
import net.mcreator.superbwarfare.event.KillMessageHandler;
import net.mcreator.superbwarfare.network.message.ClientIndicatorMessage;
import net.mcreator.superbwarfare.network.message.GunsDataMessage;
import net.mcreator.superbwarfare.tools.GunsTool;
import net.mcreator.superbwarfare.tools.PlayerKillRecord;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ClientPacketHandler {

    public static void handleGunsDataMessage(GunsDataMessage message, Supplier<NetworkEvent.Context> ctx) {
        if (ctx.get().getDirection().getReceptionSide() == LogicalSide.CLIENT) {
            GunsTool.gunsData = message.gunsData;
        }
    }

    public static void handlePlayerKillMessage(Player attacker, Entity target, boolean headshot, ResourceKey<DamageType> damageType, Supplier<NetworkEvent.Context> ctx) {
        if (ctx.get().getDirection().getReceptionSide() == LogicalSide.CLIENT) {
            if (KillMessageHandler.QUEUE.size() >= KillMessageHandler.MAX_SIZE) {
                KillMessageHandler.QUEUE.poll();
            }
            KillMessageHandler.QUEUE.offer(new PlayerKillRecord(attacker, target, attacker.getMainHandItem(), headshot, damageType));
        }
    }

    public static void handleClientIndicatorMessage(ClientIndicatorMessage message, Supplier<NetworkEvent.Context> ctx) {
        if (ctx.get().getDirection().getReceptionSide() == LogicalSide.CLIENT) {
            switch (message.type) {
                case 1 -> CrossHairOverlay.HEAD_INDICATOR = message.value;
                case 2 -> CrossHairOverlay.KILL_INDICATOR = message.value;
                default -> CrossHairOverlay.HIT_INDICATOR = message.value;
            }
        }
    }

    public static void handleSimulationDistanceMessage(int distance, Supplier<NetworkEvent.Context> ctx) {
        if (ctx.get().getDirection().getReceptionSide() == LogicalSide.CLIENT) {
            DroneUIOverlay.MAX_DISTANCE = distance * 16;
        }
    }
}
