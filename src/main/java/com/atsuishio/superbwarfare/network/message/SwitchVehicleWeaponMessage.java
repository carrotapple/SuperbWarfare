package com.atsuishio.superbwarfare.network.message;

import com.atsuishio.superbwarfare.entity.vehicle.MultiWeaponVehicleEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SwitchVehicleWeaponMessage {

    private final double scroll;

    public SwitchVehicleWeaponMessage(double scroll) {
        this.scroll = scroll;
    }

    public static void encode(SwitchVehicleWeaponMessage message, FriendlyByteBuf byteBuf) {
        byteBuf.writeDouble(message.scroll);
    }

    public static SwitchVehicleWeaponMessage decode(FriendlyByteBuf byteBuf) {
        return new SwitchVehicleWeaponMessage(byteBuf.readDouble());
    }

    public static void handler(SwitchVehicleWeaponMessage message, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            ServerPlayer player = context.get().getSender();
            if (player == null) {
                return;
            }

            if (player.getVehicle() instanceof MultiWeaponVehicleEntity multiWeaponVehicle && multiWeaponVehicle.isDriver(player)) {
                multiWeaponVehicle.changeWeapon(Mth.clamp(message.scroll > 0 ? Mth.ceil(message.scroll) : Mth.floor(message.scroll), -1, 1));
            }

        });
        context.get().setPacketHandled(true);
    }

}
