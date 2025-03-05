package com.atsuishio.superbwarfare.mixins;

import com.atsuishio.superbwarfare.ModUtils;
import com.atsuishio.superbwarfare.entity.vehicle.base.VehicleEntity;
import com.atsuishio.superbwarfare.network.message.ChangeVehicleSeatMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(Minecraft.class)
public class MinecraftMixin {

    @Shadow
    @Nullable
    public LocalPlayer player;

    @Shadow
    @Final
    public Options options;

    /**
     * 未按住shift且在可切换座位的载具上时，禁用快捷栏切换，发送切换座位消息
     */
    @Inject(method = "handleKeybinds()V", at = @At("HEAD"), cancellable = true)
    private void handleKeybinds(CallbackInfo ci) {
        if (player != null && player.getVehicle() instanceof VehicleEntity vehicle && vehicle.getMaxPassengers() > 1
                && !Screen.hasShiftDown()) {
            for (int i = 0; i < 9; ++i) {
                if (options.keyHotbarSlots[i].consumeClick()) {
                    ci.cancel();

                    if (i < vehicle.getMaxPassengers() && vehicle.getNthEntity(i) == null) {
                        ModUtils.PACKET_HANDLER.sendToServer(new ChangeVehicleSeatMessage(i));
                        vehicle.changeSeat(player, i);
                    }
                }
            }
        }
    }
}
