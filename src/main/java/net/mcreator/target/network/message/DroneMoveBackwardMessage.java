package net.mcreator.target.network.message;

import net.mcreator.target.entity.DroneEntity;
import net.mcreator.target.init.TargetModItems;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class DroneMoveBackwardMessage {
    private final boolean backward;

    public DroneMoveBackwardMessage(boolean backward) {
        this.backward = backward;
    }

    public static DroneMoveBackwardMessage decode(FriendlyByteBuf buffer) {
        return new DroneMoveBackwardMessage(buffer.readBoolean());
    }

    public static void encode(DroneMoveBackwardMessage message, FriendlyByteBuf buffer) {
        buffer.writeBoolean(message.backward);
    }

    public static void handler(DroneMoveBackwardMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            if (context.getSender() != null) {
                pressAction(context.getSender(), message.backward);
            }
        });
        context.setPacketHandled(true);
    }

    public static void pressAction(Player player, boolean backward) {
        ItemStack stack = player.getMainHandItem();
        if (stack.is(TargetModItems.MONITOR.get()) && stack.getOrCreateTag().getBoolean("Using") && stack.getOrCreateTag().getBoolean("Linked")) {
            DroneEntity drone = player.level().getEntitiesOfClass(DroneEntity.class, player.getBoundingBox().inflate(512))
                    .stream().filter(e -> e.getStringUUID().equals(stack.getOrCreateTag().getString("LinkedDrone"))).findFirst().orElse(null);
            if (drone != null) {
                if (backward) {
                    drone.getPersistentData().putBoolean("backward",true);
                } else {
                    drone.getPersistentData().putBoolean("backward",false);
                }
            }
        }
    }
}
