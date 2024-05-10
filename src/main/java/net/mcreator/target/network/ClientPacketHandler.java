package net.mcreator.target.network;

import net.mcreator.target.tools.GunsTool;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ClientPacketHandler {

    public static void handleGunsDataMessage(GunsDataMessage message,  Supplier<NetworkEvent.Context> ctx) {
        if (ctx.get().getDirection().getReceptionSide() == LogicalSide.CLIENT) {
            GunsTool.gunsData = message.gunsData;
        }
    }
}
