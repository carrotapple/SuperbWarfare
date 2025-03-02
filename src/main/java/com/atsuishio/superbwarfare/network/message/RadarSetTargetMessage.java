package com.atsuishio.superbwarfare.network.message;

import com.atsuishio.superbwarfare.menu.FuMO25Menu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class RadarSetTargetMessage {

    private final byte mode;

    public RadarSetTargetMessage(byte mode) {
        this.mode = mode;
    }

    public static void encode(RadarSetTargetMessage message, FriendlyByteBuf buffer) {
        buffer.writeByte(message.mode);
    }

    public static RadarSetTargetMessage decode(FriendlyByteBuf buffer) {
        return new RadarSetTargetMessage(buffer.readByte());
    }

    public static void handler(RadarSetTargetMessage message, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player == null) return;

            AbstractContainerMenu menu = player.containerMenu;
            if (menu instanceof FuMO25Menu fuMO25Menu) {
                if (!player.containerMenu.stillValid(player)) {
                    return;
                }
                System.out.println(123);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
