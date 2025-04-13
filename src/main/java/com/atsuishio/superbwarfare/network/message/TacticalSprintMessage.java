package com.atsuishio.superbwarfare.network.message;

import com.atsuishio.superbwarfare.network.ModVariables;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record TacticalSprintMessage(boolean sprint) {

    public static void encode(TacticalSprintMessage message, FriendlyByteBuf buffer) {
        buffer.writeBoolean(message.sprint);
    }

    public static TacticalSprintMessage decode(FriendlyByteBuf buffer) {
        return new TacticalSprintMessage(buffer.readBoolean());
    }

    public static void handler(TacticalSprintMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player == null) return;
            player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                capability.tacticalSprint = message.sprint;
                capability.sync(player);
            });
        });
        context.setPacketHandled(true);
    }
}
