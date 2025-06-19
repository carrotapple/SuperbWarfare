package com.atsuishio.superbwarfare.network.message.receive;

import com.atsuishio.superbwarfare.Mod;
import com.atsuishio.superbwarfare.event.ClientEventHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

import java.util.function.Supplier;

public class ShakeClientMessage {

    public double time;
    public double radius;
    public double amplitude;
    public double x;
    public double y;
    public double z;

    public ShakeClientMessage(double time, double radius, double amplitude, double x, double y, double z) {
        this.time = time;
        this.radius = radius;
        this.amplitude = amplitude;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static void encode(ShakeClientMessage message, FriendlyByteBuf buffer) {
        buffer.writeDouble(message.time);
        buffer.writeDouble(message.radius);
        buffer.writeDouble(message.amplitude);
        buffer.writeDouble(message.x);
        buffer.writeDouble(message.y);
        buffer.writeDouble(message.z);
    }

    public static ShakeClientMessage decode(FriendlyByteBuf buffer) {
        return new ShakeClientMessage(buffer.readDouble(), buffer.readDouble(), buffer.readDouble(), buffer.readDouble(), buffer.readDouble(), buffer.readDouble());
    }

    public static void handle(ShakeClientMessage message, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT,
                () -> () -> ClientEventHandler.handleShakeClient(message.time, message.radius, message.amplitude, message.x, message.y, message.z, context)));
        context.get().setPacketHandled(true);
    }

    public static void sendToNearbyPlayers(Level level, double x, double y, double z, double sendRadius, double time, double radius, double amplitude) {
        var center = new Vec3(x, y, z);

        for (var serverPlayer : level.getEntitiesOfClass(ServerPlayer.class, new AABB(center, center).inflate(sendRadius), e -> true)) {
            Mod.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new ShakeClientMessage(time, radius, amplitude, x, y, z));
        }
    }

    public static void sendToNearbyPlayers(Entity source, double sendRadius, double time, double radius, double amplitude) {
        sendToNearbyPlayers(source.level(), source.getX(), source.getY(), source.getZ(), sendRadius, time, radius, amplitude);
    }
}
