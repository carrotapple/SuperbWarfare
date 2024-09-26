package net.mcreator.superbwarfare.network.message;

import net.mcreator.superbwarfare.client.gui.RangeHelper;
import net.mcreator.superbwarfare.init.ModSounds;
import net.mcreator.superbwarfare.tools.SoundTool;
import net.mcreator.superbwarfare.tools.TraceTool;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

import static net.mcreator.superbwarfare.entity.MortarEntity.PITCH;

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

            if (looking instanceof LivingEntity living){
                living.getEntityData().set(PITCH, (float)Mth.clamp(living.getEntityData().get(PITCH) + 0.5 * message.scroll,20,89));
                angle = living.getEntityData().get(PITCH);
            }

            player.displayClientMessage(Component.literal("Angle:" + new java.text.DecimalFormat("##.##").format(angle) + " Range:" + new java.text.DecimalFormat("##.#").format((int) RangeHelper.getRange(angle)) + "M"), true);
            SoundTool.playLocalSound(player, ModSounds.ADJUST_FOV.get(), 1f, 0.7f);
        });
        context.get().setPacketHandled(true);
    }

}
