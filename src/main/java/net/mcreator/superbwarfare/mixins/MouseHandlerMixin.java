package net.mcreator.superbwarfare.mixins;

import net.mcreator.superbwarfare.entity.ICannonEntity;
import net.mcreator.superbwarfare.event.ClientEventHandler;
import net.mcreator.superbwarfare.init.ModMobEffects;
import net.mcreator.superbwarfare.init.ModTags;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.lwjgl.glfw.GLFW;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

/**
 * Author: MrCrayfish
 */
@Mixin(MouseHandler.class)
public class MouseHandlerMixin {

    @ModifyVariable(method = "turnPlayer()V", at = @At(value = "STORE", opcode = Opcodes.DSTORE), ordinal = 2)
    private double sensitivity(double original) {
        Minecraft mc = Minecraft.getInstance();
        Player player = Minecraft.getInstance().player;

        if (player == null) {
            return original;
        }

        if (player.hasEffect(ModMobEffects.SHOCK.get()) && !player.isSpectator()) {
            return 0;
        }

        ItemStack stack = mc.player.getMainHandItem();

        if (player.getVehicle() != null && player.getVehicle() instanceof ICannonEntity && !stack.is(ModTags.Items.GUN)) {
            if (GLFW.glfwGetMouseButton(Minecraft.getInstance().getWindow().getWindow(), GLFW.GLFW_MOUSE_BUTTON_RIGHT) == GLFW.GLFW_PRESS) {
                return 0.15;
            } else {
                return 0.3;
            }
        }

        if (!stack.is(ModTags.Items.GUN)) {
            return original;
        }

        double zoom = stack.getOrCreateTag().getDouble("zoom") + stack.getOrCreateTag().getDouble("custom_zoom");
        float customSens = (float) stack.getOrCreateTag().getInt("sensitivity");


        if (!player.getMainHandItem().isEmpty() && mc.options.getCameraType() == CameraType.FIRST_PERSON) {
            return original / Math.max((1 + (0.25 * (zoom - (0.2 * customSens)) * ClientEventHandler.zoomTime)), 0.1);
        }

        return original;
    }
}
