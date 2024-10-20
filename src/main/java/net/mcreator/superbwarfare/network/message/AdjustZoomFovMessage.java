package net.mcreator.superbwarfare.network.message;

import net.mcreator.superbwarfare.init.ModSounds;
import net.mcreator.superbwarfare.init.ModTags;
import net.mcreator.superbwarfare.tools.SoundTool;
import net.minecraft.network.FriendlyByteBuf;
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
            if (!stack.is(ModTags.Items.GUN)) {
                return;
            }
            var tag = stack.getOrCreateTag();

            if (!tag.getBoolean("CanAdjustZoomFov")) return;

            double min_zoom = tag.getDouble("MinZoom") - 1.25;
            double max_zoom = tag.getDouble("MaxZoom") - 1.25;
            tag.putDouble("CustomZoom", Mth.clamp(tag.getDouble("CustomZoom") + 0.5 * message.scroll, min_zoom, max_zoom));
            if (tag.getDouble("CustomZoom") > min_zoom && tag.getDouble("CustomZoom") < max_zoom) {
                SoundTool.playLocalSound(player, ModSounds.ADJUST_FOV.get(), 1f, 0.7f);
            }
        });
        context.get().setPacketHandled(true);
    }

}
