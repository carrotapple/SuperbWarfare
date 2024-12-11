package com.atsuishio.superbwarfare.network.message;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class VehicleMovementMessage {
    private final int direction;
    private final boolean clicked;

    public VehicleMovementMessage(int direction, boolean clicked) {
        this.direction = direction;
        this.clicked = clicked;
    }

    public static VehicleMovementMessage decode(FriendlyByteBuf buffer) {
        return new VehicleMovementMessage(buffer.readInt(), buffer.readBoolean());
    }

    public static void encode(VehicleMovementMessage message, FriendlyByteBuf buffer) {
        buffer.writeInt(message.direction);
        buffer.writeBoolean(message.clicked);
    }

    public static void handler(VehicleMovementMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            if (context.getSender() != null) {
                Player player = context.getSender();

                var vehicle = player.getVehicle();
                if (vehicle != null) {
                    switch (message.direction) {
                        case 0:
                            vehicle.getPersistentData().putBoolean("left", message.clicked);
                            break;
                        case 1:
                            vehicle.getPersistentData().putBoolean("right", message.clicked);
                            break;
                        case 2:
                            vehicle.getPersistentData().putBoolean("forward", message.clicked);
                            break;
                        case 3:
                            vehicle.getPersistentData().putBoolean("backward", message.clicked);
                            break;
                    }
                }
            }
        });
        context.setPacketHandled(true);
    }
}
