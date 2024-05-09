package net.mcreator.target.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ZoomMessage {
    private final int type;
    private final int pressedMs;

    public ZoomMessage(int type, int pressedMs) {
        this.type = type;
        this.pressedMs = pressedMs;
    }

    public ZoomMessage(FriendlyByteBuf buffer) {
        this.type = buffer.readInt();
        this.pressedMs = buffer.readInt();
    }

    public static void buffer(ZoomMessage message, FriendlyByteBuf buffer) {
        buffer.writeInt(message.type);
        buffer.writeInt(message.pressedMs);
    }

    public static void handler(ZoomMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            if (context.getSender() != null) {
                pressAction(context.getSender(), message.type);
            }
        });
        context.setPacketHandled(true);
    }

    public static void pressAction(Player entity, int type) {
        Level world = entity.level();

        if (!world.hasChunkAt(entity.blockPosition())) {
            return;
        }

        if (type == 0) {
            entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                capability.zoom = true;
                capability.syncPlayerVariables(entity);
            });
        }
        if (type == 1) {
            entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                capability.zoom = false;
                capability.syncPlayerVariables(entity);
            });
            entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                capability.zooming = false;
                capability.syncPlayerVariables(entity);
            });
            entity.getPersistentData().putDouble("zoom_time", 0);

        }
    }
}
