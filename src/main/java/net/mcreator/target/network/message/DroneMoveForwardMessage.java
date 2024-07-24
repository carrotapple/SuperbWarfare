package net.mcreator.target.network.message;

import net.mcreator.target.entity.DroneEntity;
import net.mcreator.target.init.TargetModItems;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class DroneMoveForwardMessage {
    private final boolean forward;

    public DroneMoveForwardMessage(boolean forward) {
        this.forward = forward;
    }

    public static DroneMoveForwardMessage decode(FriendlyByteBuf buffer) {
        return new DroneMoveForwardMessage(buffer.readBoolean());
    }

    public static void encode(DroneMoveForwardMessage message, FriendlyByteBuf buffer) {
        buffer.writeBoolean(message.forward);
    }

    public static void handler(DroneMoveForwardMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            if (context.getSender() != null) {
                pressAction(context.getSender(), message.forward);
            }
        });
        context.setPacketHandled(true);
    }

    public static void pressAction(Player player, boolean forward) {
        ItemStack stack = player.getMainHandItem();
        if (stack.is(TargetModItems.MONITOR.get()) && stack.getOrCreateTag().getBoolean("Using") && stack.getOrCreateTag().getBoolean("Linked")) {
            DroneEntity drone = player.level().getEntitiesOfClass(DroneEntity.class, player.getBoundingBox().inflate(512))
                    .stream().filter(e -> e.getStringUUID().equals(stack.getOrCreateTag().getString("LinkedDrone"))).findFirst().orElse(null);
            if (drone != null) {
                if (forward) {
                    drone.getPersistentData().putBoolean("forward",true);
                } else {
                    drone.getPersistentData().putBoolean("forward",false);
                }
            }
        }
    }
}
