package net.mcreator.target.network.message;

import net.mcreator.target.entity.*;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
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

    public static void buffer(VehicleFireMessage message, FriendlyByteBuf buffer) {
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
        Level world = player.level();

        if (!world.isLoaded(player.blockPosition())) {
            return;
        }

        if (player.getVehicle() != null && player.getVehicle() instanceof Mk42Entity) {
            Entity cannon = player.getVehicle();
            cannon.getPersistentData().putBoolean("firing",true);
            if (type == 0) {
                cannon.getPersistentData().putBoolean("firing",true);
            } else if (type == 1) {
                cannon.getPersistentData().putBoolean("firing",false);
            }
        }
    }
}
