package net.mcreator.target.mixins;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * From Fast Projectile Fix
 * <br>
 * Author: Roki27
 */
@Mixin(ClientboundSetEntityMotionPacket.class)
public class ClientboundSetEntityMotionPacketMixin {
    @Mutable
    @Final
    @Shadow
    private int id;

    @Mutable
    @Final
    @Shadow
    private int xa;

    @Mutable
    @Final
    @Shadow
    private int ya;

    @Mutable
    @Final
    @Shadow
    private int za;

    @Inject(method = "Lnet/minecraft/network/protocol/game/ClientboundSetEntityMotionPacket;" +
            "<init>(ILnet/minecraft/world/phys/Vec3;)V", at = @At(value = "RETURN"))
    public void init(int entityId, Vec3 motion, CallbackInfo ci) {
        this.xa = (int) (motion.x * 8000.0D);
        this.ya = (int) (motion.y * 8000.0D);
        this.za = (int) (motion.z * 8000.0D);
    }

    @Redirect(method = "<init>(Lnet/minecraft/network/FriendlyByteBuf;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/network/FriendlyByteBuf;readShort()S"))
    public short readShort(FriendlyByteBuf instance) {
        return 0;
    }

    @Inject(method = "<init>(Lnet/minecraft/network/FriendlyByteBuf;)V", at = @At(value = "RETURN"), cancellable = true)
    public void read(FriendlyByteBuf buf, CallbackInfo ci) {
        ci.cancel();
        this.xa = buf.readInt();
        this.ya = buf.readInt();
        this.za = buf.readInt();
    }

    @Inject(method = "Lnet/minecraft/network/protocol/game/ClientboundSetEntityMotionPacket;" +
            "write(Lnet/minecraft/network/FriendlyByteBuf;)V", at = @At(value = "HEAD"), cancellable = true)
    public void write(FriendlyByteBuf buf, CallbackInfo ci) {
        ci.cancel();
        buf.writeVarInt(this.id);
        buf.writeInt(this.xa);
        buf.writeInt(this.ya);
        buf.writeInt(this.za);
    }
}
