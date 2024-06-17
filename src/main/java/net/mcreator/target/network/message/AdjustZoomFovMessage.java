package net.mcreator.target.network.message;

import net.mcreator.target.init.TargetModSounds;
import net.mcreator.target.init.TargetModTags;
import net.mcreator.target.tools.SoundTool;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class AdjustZoomFovMessage {
    private final double scroll;

    public AdjustZoomFovMessage(double scroll) {
        this.scroll = scroll;
    }

    public static void encode(AdjustZoomFovMessage message, FriendlyByteBuf byteBuf) {
        byteBuf.writeDouble(message.scroll);
    }

    public static AdjustZoomFovMessage decode(FriendlyByteBuf byteBuf) {
        return new AdjustZoomFovMessage(byteBuf.readDouble());
    }

    public static void handler(AdjustZoomFovMessage message, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            ServerPlayer player = context.get().getSender();
            if (player == null) {
                return;
            }

            ItemStack stack = player.getMainHandItem();
            var tag = stack.getOrCreateTag();
            if (!stack.is(TargetModTags.Items.GUN)) {
                return;
            }

            double min_zoom = tag.getDouble("min_zoom") - tag.getDouble("zoom");
            double max_zoom = tag.getDouble("max_zoom") - tag.getDouble("zoom");
            tag.putDouble("custom_zoom", Mth.clamp(tag.getDouble("custom_zoom") + 0.5 * message.scroll, min_zoom,max_zoom));
            if (tag.getDouble("custom_zoom") > min_zoom && tag.getDouble("custom_zoom") < max_zoom) {
                SoundTool.playLocalSound(player, TargetModSounds.ADJUST_FOV.get(), 1f, 0.7f);
            }
        });
        context.get().setPacketHandled(true);
    }

}
