package net.mcreator.superbwarfare.network.message;

import net.mcreator.superbwarfare.entity.ICannonEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class VehicleFireMessage {
    private final int type;

    public VehicleFireMessage(int type) {
        this.type = type;
    }

    public VehicleFireMessage(FriendlyByteBuf buffer) {
        this.type = buffer.readInt();
    }

    public static void encode(VehicleFireMessage message, FriendlyByteBuf buffer) {
        buffer.writeInt(message.type);
    }

    public static void handler(VehicleFireMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            if (context.getSender() != null) {
                pressAction(context.getSender(), message.type);
            }
        });
        context.setPacketHandled(true);
    }

    public static void pressAction(Player player, int type) {
        Level level = player.level();

        if (!level.isLoaded(player.blockPosition())) {
            return;
        }

        if (player.getVehicle() instanceof ICannonEntity entity) {
            entity.cannonShoot(player);
        }
    }
}
