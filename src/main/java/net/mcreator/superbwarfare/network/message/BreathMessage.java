package net.mcreator.superbwarfare.network.message;

import net.mcreator.superbwarfare.init.ModSounds;
import net.mcreator.superbwarfare.network.ModVariables;
import net.mcreator.superbwarfare.tools.SoundTool;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class BreathMessage {
    private final boolean type;

    public BreathMessage(boolean type) {
        this.type = type;
    }

    public BreathMessage(FriendlyByteBuf buffer) {
        this.type = buffer.readBoolean();
    }

    public static void buffer(BreathMessage message, FriendlyByteBuf buffer) {
        buffer.writeBoolean(message.type);
    }

    public static void handler(BreathMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            if (context.getSender() != null) {
                pressAction(context.getSender(), message.type);
            }
        });
        context.setPacketHandled(true);
    }

    public static void pressAction(Player entity, boolean type) {
        Level world = entity.level();

        if (!world.isLoaded(entity.blockPosition())) {
            return;
        }

        var cap = entity.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null);

        if (type && !cap.orElse(new ModVariables.PlayerVariables()).breathExhaustion && cap.orElse(new ModVariables.PlayerVariables()).zooming && entity.getPersistentData().getDouble("NoBreath") == 0) {
            cap.ifPresent(capability -> {
                capability.breath = true;
                capability.syncPlayerVariables(entity);
            });
            if (entity instanceof ServerPlayer serverPlayer) {
                SoundTool.playLocalSound(serverPlayer, ModSounds.BREATH_IN.get(), 1, 1);
            }
            entity.getPersistentData().putInt("NoBreath", 20);
        }

        if (!type) {
            cap.ifPresent(capability -> {
                capability.breath = false;
                capability.syncPlayerVariables(entity);
            });
        }
    }
}
