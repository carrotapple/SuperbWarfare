package com.atsuishio.superbwarfare.network.message;

import com.atsuishio.superbwarfare.entity.IArmedVehicleEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class VehicleFireMessage {

    private final int type;

    public VehicleFireMessage(int type) {
        this.type = type;
    }

    public static VehicleFireMessage decode(FriendlyByteBuf buffer) {
        return new VehicleFireMessage(buffer.readInt());
    }

    public static void encode(VehicleFireMessage message, FriendlyByteBuf buffer) {
        buffer.writeInt(message.type);
    }

    public static void handler(VehicleFireMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            if (context.getSender() != null) {
                var player = context.getSender();

                if (player.getVehicle() instanceof IArmedVehicleEntity iVehicle) {
                    iVehicle.vehicleShoot(player);
                }
            }
        });
        context.setPacketHandled(true);
    }

}
