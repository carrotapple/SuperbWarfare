package com.atsuishio.superbwarfare.mixins;

import com.atsuishio.superbwarfare.entity.vehicle.Ah6Entity;
import com.atsuishio.superbwarfare.entity.vehicle.IArmedVehicleEntity;
import com.atsuishio.superbwarfare.entity.vehicle.ICannonEntity;
import com.atsuishio.superbwarfare.entity.vehicle.Lav150Entity;
import com.atsuishio.superbwarfare.event.ClientEventHandler;
import com.atsuishio.superbwarfare.init.ModItems;
import com.atsuishio.superbwarfare.init.ModMobEffects;
import com.atsuishio.superbwarfare.init.ModTags;
import com.atsuishio.superbwarfare.tools.GunsTool;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import static com.atsuishio.superbwarfare.event.ClientEventHandler.droneFovLerp;
import static com.atsuishio.superbwarfare.event.ClientEventHandler.vehicleFovLerp;

/**
 * Author: MrCrayfish
 */
@Mixin(MouseHandler.class)
public class MouseHandlerMixin {

    @ModifyVariable(method = "turnPlayer()V", at = @At(value = "STORE", opcode = Opcodes.DSTORE), ordinal = 2)
    private double sensitivity(double original) {
        Minecraft mc = Minecraft.getInstance();
        Player player = Minecraft.getInstance().player;

        if (player == null) return original;

        if (player.hasEffect(ModMobEffects.SHOCK.get()) && !player.isSpectator()) {
            return 0;
        }

        ItemStack stack = mc.player.getMainHandItem();

        if (player.getVehicle() instanceof ICannonEntity) {
            return ClientEventHandler.zoom ? 0.15 : 0.3;
        }

        if (player.getVehicle() instanceof Ah6Entity ah6Entity && !ah6Entity.onGround() && ah6Entity.getFirstPassenger() == player) {
            return 0.24;
        }

        if (player.getVehicle() instanceof Lav150Entity) {
            return ClientEventHandler.zoom ? 0.23 : 0.28;
        }

        if (stack.is(ModItems.MONITOR.get()) && stack.getOrCreateTag().getBoolean("Using") && stack.getOrCreateTag().getBoolean("Linked")) {
            return 0.33 / (1 + 0.08 * (droneFovLerp - 1));
        }

        if (player.getVehicle() instanceof IArmedVehicleEntity iVehicle && iVehicle.isDriver(player) && ClientEventHandler.zoom) {
            return 0.33 / (1 + 0.08 * (vehicleFovLerp - 1));
        }

        if (!stack.is(ModTags.Items.GUN)) {
            return original;
        }

        double zoom = 1.25 + GunsTool.getGunDoubleTag(stack, "CustomZoom", 0);
        float customSens = (float) stack.getOrCreateTag().getInt("sensitivity");

        if (!player.getMainHandItem().isEmpty() && mc.options.getCameraType() == CameraType.FIRST_PERSON) {
            return original / Math.max((1 + (0.2 * (zoom - (0.3 * customSens)) * ClientEventHandler.zoomTime)), 0.1);
        }

        return original;
    }
}
