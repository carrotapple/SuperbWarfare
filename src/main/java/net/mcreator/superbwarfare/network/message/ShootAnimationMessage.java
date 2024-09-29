package net.mcreator.superbwarfare.network.message;

import net.mcreator.superbwarfare.event.ClientEventHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ShootAnimationMessage {
    public double time;

    public ShootAnimationMessage(double time) {
        this.time = time;
    }

    public static void encode(ShootAnimationMessage message, FriendlyByteBuf buffer) {
        buffer.writeDouble(message.time);
    }

    public static ShootAnimationMessage decode(FriendlyByteBuf buffer) {
        return new ShootAnimationMessage(buffer.readDouble());
    }

    public static void handle(ShootAnimationMessage message, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT,
                () -> () -> ClientEventHandler.handleFireRecoilTimeMessage(message.time, context)));
        context.get().setPacketHandled(true);
    }
}
