package net.mcreator.target.network.message;

import net.mcreator.target.init.TargetModTags;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SensitivityMessage {
    private final boolean add;

    public SensitivityMessage(boolean add) {
        this.add = add;
    }

    public static void encode(SensitivityMessage message, FriendlyByteBuf byteBuf) {
        byteBuf.writeBoolean(message.add);
    }

    public static SensitivityMessage decode(FriendlyByteBuf byteBuf) {
        return new SensitivityMessage(byteBuf.readBoolean());
    }

    public static void handler(SensitivityMessage message, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            ServerPlayer player = context.get().getSender();
            if (player == null) {
                return;
            }

            ItemStack stack = player.getMainHandItem();
            if (!stack.is(TargetModTags.Items.GUN)) {
                return;
            }

            if (message.add) {
                stack.getOrCreateTag().putInt("sensitivity", Math.min(10, stack.getOrCreateTag().getInt("sensitivity") + 1));
            } else {
                stack.getOrCreateTag().putInt("sensitivity", Math.max(-10, stack.getOrCreateTag().getInt("sensitivity") - 1));
            }
            player.displayClientMessage(Component.translatable("des.target.sensitivity", stack.getOrCreateTag().getInt("sensitivity")), true);
        });
        context.get().setPacketHandled(true);
    }

}
