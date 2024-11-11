package net.mcreator.superbwarfare.network.message;

import net.mcreator.superbwarfare.entity.DroneEntity;
import net.mcreator.superbwarfare.init.ModItems;
import net.mcreator.superbwarfare.tools.TraceTool;
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
                    DroneEntity drone = player.level().getEntitiesOfClass(DroneEntity.class, player.getBoundingBox().inflate(512))
                            .stream().filter(e -> e.getStringUUID().equals(stack.getOrCreateTag().getString("LinkedDrone"))).findFirst().orElse(null);
                    if (drone != null) {
                        if (!player.getOffhandItem().is(ModItems.FIRING_PARAMETERS.get())) {
                            drone.getPersistentData().putBoolean("firing", true);
                        } else {
                            ItemStack offStack = player.getOffhandItem();
                            boolean lookAtEntity = false;

                            Entity lookingEntity = TraceTool.findLookingEntity(drone, 520);

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

                            player.displayClientMessage(Component.translatable("des.superbwarfare.target.pos").withStyle(ChatFormatting.GRAY)
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
