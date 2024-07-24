package net.mcreator.target.network.message;

import net.mcreator.target.entity.DroneEntity;
import net.mcreator.target.init.TargetModItems;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class DroneMoveDownMessage {
    private final boolean down;

    public DroneMoveDownMessage(boolean down) {
        this.down = down;
    }

    public static DroneMoveDownMessage decode(FriendlyByteBuf buffer) {
        return new DroneMoveDownMessage(buffer.readBoolean());
    }

    public static void encode(DroneMoveDownMessage message, FriendlyByteBuf buffer) {
        buffer.writeBoolean(message.down);
    }

    public static void handler(DroneMoveDownMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            if (context.getSender() != null) {
                pressAction(context.getSender(), message.down);
            }
        });
        context.setPacketHandled(true);
    }

    public static void pressAction(Player player, boolean down) {
        ItemStack stack = player.getMainHandItem();
        if (stack.is(TargetModItems.MONITOR.get()) && stack.getOrCreateTag().getBoolean("Using") && stack.getOrCreateTag().getBoolean("Linked")) {
            DroneEntity drone = player.level().getEntitiesOfClass(DroneEntity.class, player.getBoundingBox().inflate(512))
                    .stream().filter(e -> e.getStringUUID().equals(stack.getOrCreateTag().getString("LinkedDrone"))).findFirst().orElse(null);
            if (drone != null) {
                if (down) {
                    drone.getPersistentData().putBoolean("down",true);
                } else {
                    drone.getPersistentData().putBoolean("down",false);
                }
            }
        }
    }
}
