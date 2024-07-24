package net.mcreator.target.network.message;

import net.mcreator.target.entity.DroneEntity;
import net.mcreator.target.init.TargetModItems;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class DroneMoveRightMessage {
    private final boolean right;

    public DroneMoveRightMessage(boolean right) {
        this.right = right;
    }

    public static DroneMoveRightMessage decode(FriendlyByteBuf buffer) {
        return new DroneMoveRightMessage(buffer.readBoolean());
    }

    public static void encode(DroneMoveRightMessage message, FriendlyByteBuf buffer) {
        buffer.writeBoolean(message.right);
    }

    public static void handler(DroneMoveRightMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            if (context.getSender() != null) {
                pressAction(context.getSender(), message.right);
            }
        });
        context.setPacketHandled(true);
    }

    public static void pressAction(Player player, boolean left) {
        ItemStack stack = player.getMainHandItem();
        if (stack.is(TargetModItems.MONITOR.get()) && stack.getOrCreateTag().getBoolean("Using") && stack.getOrCreateTag().getBoolean("Linked")) {
            DroneEntity drone = player.level().getEntitiesOfClass(DroneEntity.class, player.getBoundingBox().inflate(512))
                    .stream().filter(e -> e.getStringUUID().equals(stack.getOrCreateTag().getString("LinkedDrone"))).findFirst().orElse(null);
            if (drone != null) {
                if (left) {
                    drone.getPersistentData().putBoolean("right",true);
                } else {
                    drone.getPersistentData().putBoolean("right",false);
                }
            }
        }
    }
}
