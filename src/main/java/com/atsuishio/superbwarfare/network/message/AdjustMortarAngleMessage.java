package com.atsuishio.superbwarfare.network.message;

import com.atsuishio.superbwarfare.client.gui.RangeHelper;
import com.atsuishio.superbwarfare.entity.MortarEntity;
import com.atsuishio.superbwarfare.init.ModSounds;
import com.atsuishio.superbwarfare.tools.FormatTool;
import com.atsuishio.superbwarfare.tools.SoundTool;
import com.atsuishio.superbwarfare.tools.TraceTool;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class AdjustMortarAngleMessage {

    private final double scroll;

    public AdjustMortarAngleMessage(double scroll) {
        this.scroll = scroll;
    }

    public static void encode(AdjustMortarAngleMessage message, FriendlyByteBuf byteBuf) {
        byteBuf.writeDouble(message.scroll);
    }

    public static AdjustMortarAngleMessage decode(FriendlyByteBuf byteBuf) {
        return new AdjustMortarAngleMessage(byteBuf.readDouble());
    }

    public static void handler(AdjustMortarAngleMessage message, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            ServerPlayer player = context.get().getSender();
            if (player == null) {
                return;
            }

            Entity looking = TraceTool.findLookingEntity(player, 6);
            if (looking == null) return;

            double angle = 0;

            if (looking instanceof MortarEntity mortar) {
                mortar.getEntityData().set(MortarEntity.PITCH, (float) Mth.clamp(mortar.getEntityData().get(MortarEntity.PITCH) + 0.5 * message.scroll, 20, 89));
                angle = mortar.getEntityData().get(MortarEntity.PITCH);
            }

            player.displayClientMessage(Component.translatable("tips.superbwarfare.mortar.angle",
                    FormatTool.format2D(angle),
                    FormatTool.format1D((int) RangeHelper.getRange(angle)), "m"), true);
            SoundTool.playLocalSound(player, ModSounds.ADJUST_FOV.get(), 1f, 0.7f);
        });
        context.get().setPacketHandled(true);
    }
}
