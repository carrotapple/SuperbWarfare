package com.atsuishio.superbwarfare.network.message;

import com.atsuishio.superbwarfare.network.ClientPacketHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ClientMotionSyncMessage {

    public final int id;
    public final double x;
    public final double y;
    public final double z;

    public ClientMotionSyncMessage(Entity entity) {
        this(entity.getId(), entity.getDeltaMovement());
    }

    public ClientMotionSyncMessage(int id, Vec3 motion) {
        this.id = id;
        this.x = motion.x;
        this.y = motion.y;
        this.z = motion.z;
    }

    public static void encode(ClientMotionSyncMessage message, FriendlyByteBuf buffer) {
        buffer.writeVarInt(message.id);
        buffer.writeDouble(message.x);
        buffer.writeDouble(message.y);
        buffer.writeDouble(message.z);
    }

    public static ClientMotionSyncMessage decode(FriendlyByteBuf buffer) {
        int id = buffer.readVarInt();
        double x = buffer.readDouble();
        double y = buffer.readDouble();
        double z = buffer.readDouble();
        return new ClientMotionSyncMessage(id, new Vec3(x, y, z));
    }

    public static void handler(ClientMotionSyncMessage message, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT,
                () -> () -> ClientPacketHandler.handleClientSyncMotion(message, ctx)));
        ctx.get().setPacketHandled(true);
    }
}
