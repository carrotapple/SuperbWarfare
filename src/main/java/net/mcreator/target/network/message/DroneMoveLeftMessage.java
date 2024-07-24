package net.mcreator.target.network.message;

import net.mcreator.target.entity.DroneEntity;
import net.mcreator.target.init.TargetModItems;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class DroneMoveLeftMessage {
    private final boolean left;

    public DroneMoveLeftMessage(boolean left) {
        this.left = left;
    }

    public static DroneMoveLeftMessage decode(FriendlyByteBuf buffer) {
        return new DroneMoveLeftMessage(buffer.readBoolean());
    }

    public static void encode(DroneMoveLeftMessage message, FriendlyByteBuf buffer) {
        buffer.writeBoolean(message.left);
    }

    public static void handler(DroneMoveLeftMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            if (context.getSender() != null) {
                pressAction(context.getSender(), message.left);
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
                    drone.getPersistentData().putBoolean("left",true);
                } else {
                    drone.getPersistentData().putBoolean("left",false);
                }
            }
        }
    }
}
