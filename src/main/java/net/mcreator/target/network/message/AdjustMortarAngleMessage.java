package net.mcreator.target.network.message;

import net.mcreator.target.client.gui.RangeHelper;
import net.mcreator.target.entity.MortarEntity;
import net.mcreator.target.init.TargetModAttributes;
import net.mcreator.target.init.TargetModSounds;
import net.mcreator.target.init.TargetModTags;
import net.mcreator.target.tools.SoundTool;
import net.mcreator.target.tools.TraceTool;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
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

            if (looking instanceof LivingEntity living){
                living.getAttribute(TargetModAttributes.MORTAR_PITCH.get()).setBaseValue(Mth.clamp(living.getAttribute(TargetModAttributes.MORTAR_PITCH.get()).getBaseValue() + message.scroll,20,89));
            }

        });
        context.get().setPacketHandled(true);
    }

}
