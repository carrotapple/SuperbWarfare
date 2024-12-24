package com.atsuishio.superbwarfare.network.message;

import com.atsuishio.superbwarfare.init.ModItems;
import com.atsuishio.superbwarfare.init.ModSounds;
import com.atsuishio.superbwarfare.init.ModTags;
import com.atsuishio.superbwarfare.tools.GunsTool;
import com.atsuishio.superbwarfare.tools.SoundTool;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.text.DecimalFormat;
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

            if (stack.is(ModItems.MINIGUN.get())) {
                double minRpm = 300;
                double maxRpm = 2400;

                GunsTool.setGunIntTag(stack, "RPM", (int) Mth.clamp(GunsTool.getGunIntTag(stack, "RPM", 0) + 50 * message.scroll, minRpm, maxRpm));
                if (GunsTool.getGunIntTag(stack, "RPM", 0) == 1150) {
                    GunsTool.setGunIntTag(stack, "RPM", 1145);
                }

                if (GunsTool.getGunIntTag(stack, "RPM", 0) == 1195) {
                    GunsTool.setGunIntTag(stack, "RPM", 1200);
                }

                if (GunsTool.getGunIntTag(stack, "RPM", 0) == 1095) {
                    GunsTool.setGunIntTag(stack, "RPM", 1100);
                }
                player.displayClientMessage(Component.literal("RPM: " + new DecimalFormat("##").format(GunsTool.getGunIntTag(stack, "RPM", 0))), true);
                int rpm = GunsTool.getGunIntTag(stack, "RPM", 0);
                if (rpm > minRpm && rpm < maxRpm) {
                    SoundTool.playLocalSound(player, ModSounds.ADJUST_FOV.get(), 1f, 0.7f);
                }
            } else {
                double minZoom = GunsTool.getGunDoubleTag(stack, "MinZoom", 0) - 1.25;
                double maxZoom = GunsTool.getGunDoubleTag(stack, "MaxZoom", 0) - 1.25;
                tag.putDouble("CustomZoom", Mth.clamp(tag.getDouble("CustomZoom") + 0.5 * message.scroll, minZoom, maxZoom));
                if (tag.getDouble("CustomZoom") > minZoom && tag.getDouble("CustomZoom") < maxZoom) {
                    SoundTool.playLocalSound(player, ModSounds.ADJUST_FOV.get(), 1f, 0.7f);
                }
            }
        });
        context.get().setPacketHandled(true);
    }

}
