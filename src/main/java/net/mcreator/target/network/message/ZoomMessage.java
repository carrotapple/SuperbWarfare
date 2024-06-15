package net.mcreator.target.network.message;

import net.mcreator.target.network.TargetModVariables;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ZoomMessage {
    private final int type;

    public ZoomMessage(int type) {
        this.type = type;
    }

    public ZoomMessage(FriendlyByteBuf buffer) {
        this.type = buffer.readInt();
    }

    public static void buffer(ZoomMessage message, FriendlyByteBuf buffer) {
        buffer.writeInt(message.type);
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
            entity.getPersistentData().putDouble("zoom_animation_time", 0);

        }
    }
}
