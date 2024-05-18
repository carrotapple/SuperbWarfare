package net.mcreator.target.network;

import net.mcreator.target.network.message.GunsDataMessage;
import net.mcreator.target.tools.GunsTool;
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

    public static void handlePlayerKillMessage(Player attacker, Entity target, Supplier<NetworkEvent.Context> ctx) {
        if (ctx.get().getDirection().getReceptionSide() == LogicalSide.CLIENT) {
            System.out.println(attacker + " killed " + target);
        }
    }
}
