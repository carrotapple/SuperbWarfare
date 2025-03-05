package com.atsuishio.superbwarfare.mixins;

import com.atsuishio.superbwarfare.ModUtils;
import com.atsuishio.superbwarfare.entity.vehicle.base.VehicleEntity;
import com.atsuishio.superbwarfare.entity.vehicle.base.WeaponVehicleEntity;
import com.atsuishio.superbwarfare.network.message.ChangeVehicleSeatMessage;
import com.atsuishio.superbwarfare.network.message.SwitchVehicleWeaponMessage;
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
     * 在可切换座位的载具上，按下shift+数字键时切换座位
     * 在有武器的载具上，按下数字键时切换武器
     */
    @Inject(method = "handleKeybinds()V", at = @At("HEAD"), cancellable = true)
    private void handleKeybinds(CallbackInfo ci) {
        if (player == null || !(player.getVehicle() instanceof VehicleEntity vehicle)) return;

        var index = -1;
        for (int i = 0; i < 9; ++i) {
            if (options.keyHotbarSlots[i].consumeClick()) {
                index = i;
                break;
            }
        }
        if (index == -1) return;

        // shift+数字键 座位更改
        if (vehicle.getMaxPassengers() > 1
                && Screen.hasShiftDown()
                && index < vehicle.getMaxPassengers()
                && vehicle.getNthEntity(index) == null
        ) {
            ModUtils.PACKET_HANDLER.sendToServer(new ChangeVehicleSeatMessage(index));
            vehicle.changeSeat(player, index);
            ci.cancel();
            return;
        }

        var seatIndex = vehicle.getSeatIndex(player);

        if (vehicle instanceof WeaponVehicleEntity weaponVehicle && weaponVehicle.banHand(player)) {
            ci.cancel();

            // 数字键 武器切换
            if (!Screen.hasShiftDown()
                    && weaponVehicle.hasWeapon(seatIndex)
                    && weaponVehicle.getWeaponType(seatIndex) != index) {
                ModUtils.PACKET_HANDLER.sendToServer(new SwitchVehicleWeaponMessage(seatIndex, index, false));
            }
        }
    }
}
