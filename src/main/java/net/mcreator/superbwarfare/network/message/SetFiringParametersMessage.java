package net.mcreator.superbwarfare.network.message;

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

public class SetFiringParametersMessage {

    private final int type;

    public SetFiringParametersMessage(int type) {
        this.type = type;
    }

    public static SetFiringParametersMessage decode(FriendlyByteBuf buffer) {
        return new SetFiringParametersMessage(buffer.readInt());
    }

    public static void encode(SetFiringParametersMessage message, FriendlyByteBuf buffer) {
        buffer.writeInt(message.type);
    }

    public static void handler(SetFiringParametersMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            if (context.getSender() != null) {
                Player player = context.getSender();

                ItemStack stack = player.getOffhandItem();
                boolean lookAtEntity = false;
                Entity lookingEntity = TraceTool.findLookingEntity(player, 520);

                Vec3 looking = Vec3.atLowerCornerOf(player.level().clip(new ClipContext(player.getEyePosition(), player.getEyePosition().add(player.getLookAngle().scale(512)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, player)).getBlockPos());

                if (lookingEntity != null) {
                    lookAtEntity = true;
                }

                if (lookAtEntity) {
                    stack.getOrCreateTag().putInt("TargetX", (int) lookingEntity.getX());
                    stack.getOrCreateTag().putInt("TargetY", (int) lookingEntity.getY());
                    stack.getOrCreateTag().putInt("TargetZ", (int) lookingEntity.getZ());
                } else {
                    stack.getOrCreateTag().putInt("TargetX", (int) looking.x());
                    stack.getOrCreateTag().putInt("TargetY", (int) looking.y());
                    stack.getOrCreateTag().putInt("TargetZ", (int) looking.z());
                }

                player.displayClientMessage(Component.translatable("des.superbwarfare.target.pos").withStyle(ChatFormatting.GRAY)
                        .append(Component.literal("[" + stack.getOrCreateTag().getInt("TargetX")
                                + "," + stack.getOrCreateTag().getInt("TargetY")
                                + "," + stack.getOrCreateTag().getInt("TargetZ") + "]")), true);
            }
        });
        context.setPacketHandled(true);
    }
}
