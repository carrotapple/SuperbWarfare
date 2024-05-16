package net.mcreator.target.network;

import net.mcreator.target.procedures.PlayerReloadProcedure;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ReloadMessage {
    private final int type;

    public ReloadMessage(int type) {
        this.type = type;
    }

    public ReloadMessage(FriendlyByteBuf buffer) {
        this.type = buffer.readInt();
    }

    public static void buffer(ReloadMessage message, FriendlyByteBuf buffer) {
        buffer.writeInt(message.type);
    }

    public static void handler(ReloadMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            if (context.getSender() != null) {
                pressAction(context.getSender(), message.type);
            }
        });
        context.setPacketHandled(true);
    }

    public static void pressAction(Player entity, int type) {
        Level world = entity.level();

        if (!world.hasChunkAt(entity.blockPosition()))
            return;
        if (type == 0) {

            PlayerReloadProcedure.execute(entity);
        }
    }
}
