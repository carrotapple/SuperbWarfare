package com.atsuishio.superbwarfare.network.message;

import com.atsuishio.superbwarfare.entity.vehicle.base.MobileVehicleEntity;
import com.atsuishio.superbwarfare.init.ModItems;
import com.atsuishio.superbwarfare.tools.EntityFindUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class VehicleMovementMessage {

    private final int direction;
    private final boolean clicked;

    public VehicleMovementMessage(int direction, boolean clicked) {
        this.direction = direction;
        this.clicked = clicked;
    }

    public static VehicleMovementMessage decode(FriendlyByteBuf buffer) {
        return new VehicleMovementMessage(buffer.readInt(), buffer.readBoolean());
    }

    public static void encode(VehicleMovementMessage message, FriendlyByteBuf buffer) {
        buffer.writeInt(message.direction);
        buffer.writeBoolean(message.clicked);
    }

    public static void handler(VehicleMovementMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            if (context.getSender() != null) {
                Player player = context.getSender();

                var vehicle = player.getVehicle();
                ItemStack stack = player.getMainHandItem();
                if (vehicle instanceof MobileVehicleEntity mobileVehicleEntity) {
                    switch (message.direction) {
                        case 0:
                            mobileVehicleEntity.leftInputDown = message.clicked;
                            break;
                        case 1:
                            mobileVehicleEntity.rightInputDown = message.clicked;
                            break;
                        case 2:
                            mobileVehicleEntity.forwardInputDown = message.clicked;
                            break;
                        case 3:
                            mobileVehicleEntity.backInputDown = message.clicked;
                            break;
                        case 4:
                            mobileVehicleEntity.upInputDown = message.clicked;
                            break;
                        case 5:
                            mobileVehicleEntity.downInputDown = message.clicked;
                            break;
                        case 6:
                            mobileVehicleEntity.decoyInputDown = message.clicked;
                            break;
                    }
                } else if (stack.is(ModItems.MONITOR.get()) && stack.getOrCreateTag().getBoolean("Using") && stack.getOrCreateTag().getBoolean("Linked")) {
                    var drone = EntityFindUtil.findDrone(player.level(), stack.getOrCreateTag().getString("LinkedDrone"));
                    if (drone != null) {
                        switch (message.direction) {
                            case 0:
                                drone.leftInputDown = message.clicked;
                                break;
                            case 1:
                                drone.rightInputDown = message.clicked;
                                break;
                            case 2:
                                drone.forwardInputDown = message.clicked;
                                break;
                            case 3:
                                drone.backInputDown = message.clicked;
                                break;
                            case 4:
                                drone.upInputDown = message.clicked;
                                break;
                            case 5:
                                drone.downInputDown = message.clicked;
                                break;
                        }
                    }
                }
            }
        });
        context.setPacketHandled(true);
    }
}
