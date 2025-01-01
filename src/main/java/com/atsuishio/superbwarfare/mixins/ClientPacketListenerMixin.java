package com.atsuishio.superbwarfare.mixins;

import com.atsuishio.superbwarfare.entity.VehicleEntity;
import com.atsuishio.superbwarfare.init.ModKeyMappings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetPassengersPacket;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Code based on @Luke100000's ImmersiveAircraft
 */
@Mixin(ClientPacketListener.class)
public class ClientPacketListenerMixin {

    @Shadow
    @Final
    private Minecraft minecraft;

    @Shadow
    private ClientLevel level;

    @Inject(method = "handleSetEntityPassengersPacket(Lnet/minecraft/network/protocol/game/ClientboundSetPassengersPacket;)V", at = @At("TAIL"))
    public void handleSetEntityPassengersPacket(ClientboundSetPassengersPacket pPacket, CallbackInfo ci) {
        Entity entity = this.level.getEntity(pPacket.getVehicle());
        if (entity == null) return;

        var player = this.minecraft.player;
        assert player != null;

        boolean hasIndirectPassenger = entity.hasIndirectPassenger(this.minecraft.player);
        for (int i : pPacket.getPassengers()) {
            Entity passenger = this.level.getEntity(i);
            if (passenger != null && (passenger == this.minecraft.player || hasIndirectPassenger)) {
                if (entity instanceof VehicleEntity) {
                    this.minecraft.player.yRotO = entity.getYRot();
                    this.minecraft.player.setYRot(entity.getYRot());
                    this.minecraft.player.setYHeadRot(entity.getYRot());

                    Component component = Component.translatable("mount.onboard", ModKeyMappings.DISMOUNT.getTranslatedKeyMessage());
                    this.minecraft.gui.setOverlayMessage(component, false);
                    this.minecraft.getNarrator().sayNow(component);
                }
            }
        }
    }
}
