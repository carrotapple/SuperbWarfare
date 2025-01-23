package com.atsuishio.superbwarfare.network.message;

import com.atsuishio.superbwarfare.entity.vehicle.DroneEntity;
import com.atsuishio.superbwarfare.init.ModItems;
import com.atsuishio.superbwarfare.tools.EntityFindUtil;
import com.atsuishio.superbwarfare.tools.SeekTool;
import net.minecraft.ChatFormatting;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class DroneFireMessage {

    private final int type;

    public DroneFireMessage(int type) {
        this.type = type;
    }

    public static DroneFireMessage decode(FriendlyByteBuf buffer) {
        return new DroneFireMessage(buffer.readInt());
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
                    DroneEntity drone = EntityFindUtil.findDrone(player.level(), stack.getOrCreateTag().getString("LinkedDrone"));
                    if (drone != null) {
                        if (!player.getOffhandItem().is(ModItems.FIRING_PARAMETERS.get())) {
                            drone.fire = true;
                        } else {
                            ItemStack offStack = player.getOffhandItem();
                            boolean lookAtEntity = false;

                            Entity lookingEntity = SeekTool.seekLivingEntity(drone, drone.level(), 512, 2);
                            Vec3 looking = Vec3.atLowerCornerOf(player.level().clip(new ClipContext(drone.getEyePosition(), drone.getEyePosition().add(drone.getLookAngle().scale(512)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, player)).getBlockPos());

                            if (lookingEntity != null) {
                                lookAtEntity = true;
                            }

                            if (lookAtEntity) {
                                offStack.getOrCreateTag().putInt("TargetX", (int) lookingEntity.getX());
                                offStack.getOrCreateTag().putInt("TargetY", (int) lookingEntity.getY());
                                offStack.getOrCreateTag().putInt("TargetZ", (int) lookingEntity.getZ());
                            } else {
                                offStack.getOrCreateTag().putInt("TargetX", (int) looking.x());
                                offStack.getOrCreateTag().putInt("TargetY", (int) looking.y());
                                offStack.getOrCreateTag().putInt("TargetZ", (int) looking.z());
                            }

                            player.displayClientMessage(Component.translatable("tips.superbwarfare.mortar.target_pos").withStyle(ChatFormatting.GRAY)
                                    .append(Component.literal("[" + offStack.getOrCreateTag().getInt("TargetX")
                                            + "," + offStack.getOrCreateTag().getInt("TargetY")
                                            + "," + offStack.getOrCreateTag().getInt("TargetZ") + "]")), true);
                        }
                    }
                }
            }
        });
        context.setPacketHandled(true);
    }
}
