package net.mcreator.superbwarfare.network.message;

import net.mcreator.superbwarfare.init.ModItems;
import net.mcreator.superbwarfare.tools.EntityFindUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class DroneMovementMessage {
    private final int direction;
    private final boolean clicked;

    public DroneMovementMessage(int direction, boolean clicked) {
        this.direction = direction;
        this.clicked = clicked;
    }

    public static DroneMovementMessage decode(FriendlyByteBuf buffer) {
        return new DroneMovementMessage(buffer.readInt(), buffer.readBoolean());
    }

    public static void encode(DroneMovementMessage message, FriendlyByteBuf buffer) {
        buffer.writeInt(message.direction);
        buffer.writeBoolean(message.clicked);
    }

    public static void handler(DroneMovementMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            if (context.getSender() != null) {
                Player player = context.getSender();

                ItemStack stack = player.getMainHandItem();

                if (stack.is(ModItems.MONITOR.get()) && stack.getOrCreateTag().getBoolean("Using") && stack.getOrCreateTag().getBoolean("Linked")) {
                    var drone = EntityFindUtil.findDrone(player.level(), stack.getOrCreateTag().getString("LinkedDrone"));
                    if (drone != null) {
                        switch (message.direction) {
                            case 0:
                                drone.getPersistentData().putBoolean("left", message.clicked);
                                break;
                            case 1:
                                drone.getPersistentData().putBoolean("right", message.clicked);
                                break;
                            case 2:
                                drone.getPersistentData().putBoolean("forward", message.clicked);
                                break;
                            case 3:
                                drone.getPersistentData().putBoolean("backward", message.clicked);
                                break;
                            case 4:
                                drone.getPersistentData().putBoolean("up", message.clicked);
                                break;
                            case 5:
                                drone.getPersistentData().putBoolean("down", message.clicked);
                                break;
                        }
                    }
                }
            }
        });
        context.setPacketHandled(true);
    }
}
