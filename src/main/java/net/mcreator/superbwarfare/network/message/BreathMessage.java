package net.mcreator.superbwarfare.network.message;

import net.mcreator.superbwarfare.network.ModVariables;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class BreathMessage {
    private final boolean type;

    public BreathMessage(boolean type) {
        this.type = type;
    }

    public static BreathMessage decode(FriendlyByteBuf buffer) {
        return new BreathMessage(buffer.readBoolean());
    }

    public static void encode(BreathMessage message, FriendlyByteBuf buffer) {
        buffer.writeBoolean(message.type);
    }

    public static void handler(BreathMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();

            if (player != null) {
                Level level = player.level();

                if (!level.isLoaded(player.blockPosition())) {
                    return;
                }

                var cap = player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null);

                if (message.type && !cap.orElse(new ModVariables.PlayerVariables()).breathExhaustion && cap.orElse(new ModVariables.PlayerVariables()).zoom &&
                        player.getPersistentData().getDouble("NoBreath") == 0) {
                    cap.ifPresent(capability -> {
                        capability.breath = true;
                        capability.syncPlayerVariables(player);
                    });
                }

                if (!message.type) {
                    cap.ifPresent(capability -> {
                        capability.breath = false;
                        capability.syncPlayerVariables(player);
                    });
                }
            }
        });
        context.setPacketHandled(true);
    }
}
