package net.mcreator.target.network.message;

import net.mcreator.target.entity.DroneEntity;
import net.mcreator.target.init.TargetModItems;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class DroneMoveUpMessage {
    private final boolean up;

    public DroneMoveUpMessage(boolean up) {
        this.up = up;
    }

    public static DroneMoveUpMessage decode(FriendlyByteBuf buffer) {
        return new DroneMoveUpMessage(buffer.readBoolean());
    }

    public static void encode(DroneMoveUpMessage message, FriendlyByteBuf buffer) {
        buffer.writeBoolean(message.up);
    }

    public static void handler(DroneMoveUpMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            if (context.getSender() != null) {
                pressAction(context.getSender(), message.up);
            }
        });
        context.setPacketHandled(true);
    }

    public static void pressAction(Player player, boolean up) {
        ItemStack stack = player.getMainHandItem();
        if (stack.is(TargetModItems.MONITOR.get()) && stack.getOrCreateTag().getBoolean("Using") && stack.getOrCreateTag().getBoolean("Linked")) {
            DroneEntity drone = player.level().getEntitiesOfClass(DroneEntity.class, player.getBoundingBox().inflate(512))
                    .stream().filter(e -> e.getStringUUID().equals(stack.getOrCreateTag().getString("LinkedDrone"))).findFirst().orElse(null);
            if (drone != null) {
                if (up) {
                    drone.getPersistentData().putBoolean("up",true);
                } else {
                    drone.getPersistentData().putBoolean("up",false);
                }
            }
        }
    }
}
