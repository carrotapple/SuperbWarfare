package net.mcreator.superbwarfare.network.message;

import net.mcreator.superbwarfare.entity.DroneEntity;
import net.mcreator.superbwarfare.init.ModItems;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class DroneFireMessage {
    private final int type;

    public DroneFireMessage(int type) {
        this.type = type;
    }

    public DroneFireMessage(FriendlyByteBuf buffer) {
        this.type = buffer.readInt();
    }

    public static void encode(DroneFireMessage message, FriendlyByteBuf buffer) {
        buffer.writeInt(message.type);
    }

    public static void handler(DroneFireMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            if (context.getSender() != null) {
                Player player = context.getSender();

                ItemStack stack = player.getMainHandItem();
                if (stack.is(ModItems.MONITOR.get()) && stack.getOrCreateTag().getBoolean("Using") && stack.getOrCreateTag().getBoolean("Linked")) {
                    DroneEntity drone = player.level().getEntitiesOfClass(DroneEntity.class, player.getBoundingBox().inflate(512))
                            .stream().filter(e -> e.getStringUUID().equals(stack.getOrCreateTag().getString("LinkedDrone"))).findFirst().orElse(null);
                    if (drone != null) {
                        if (message.type == 0) {
                            drone.getPersistentData().putBoolean("firing", true);
                        }
                    }
                }
            }
        });
        context.setPacketHandled(true);
    }
}
