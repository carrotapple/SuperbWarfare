package net.mcreator.superbwarfare.mixins;

import net.mcreator.superbwarfare.entity.Mk42Entity;
import net.mcreator.superbwarfare.entity.Mle1934Entity;
import net.mcreator.superbwarfare.init.ModMobEffects;
import net.mcreator.superbwarfare.init.ModTags;
import net.mcreator.superbwarfare.network.ModVariables;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
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
        float additionalAdsSensitivity = 1.0F;
        Minecraft mc = Minecraft.getInstance();
        Player player = Minecraft.getInstance().player;

        if (player == null) {
            return original;
        }

        if (player.hasEffect(ModMobEffects.SHOCK.get()) && !player.isSpectator()) {
            return 0;
        }

        ItemStack stack = mc.player.getMainHandItem();

        if (player.getVehicle() != null && (player.getVehicle() instanceof Mk42Entity || player.getVehicle() instanceof Mle1934Entity)) {
            if ((player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables())).zoom) {
                return 0.12;
            } else {
                return 0.23;
            }
        }

        if (!stack.is(ModTags.Items.GUN)) {
            return original;
        }

        boolean flag = false;
        float sens = 0.2f;
        float fov = (float) player.getPersistentData().getDouble("fov");
        float customSens = (float) stack.getOrCreateTag().getInt("sensitivity");

        float originalFov = mc.options.fov().get();

        if (!player.getMainHandItem().isEmpty() && mc.options.getCameraType() == CameraType.FIRST_PERSON) {
            if ((player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables())).zooming) {
                additionalAdsSensitivity = (float) Mth.clamp((1 + 0.1f * customSens) * (1.25F * fov / originalFov) * (1 + 0.2f * Math.pow((originalFov / fov), 1.25)), 0.125F, 2F);
            } else {
                additionalAdsSensitivity = Mth.clamp((1 + 0.1f * customSens) * 1.25F, 0.125F, 2F);
            }
            flag = true;
        }

        return original * additionalAdsSensitivity * (1.0 - sens * (flag ? 1 : 0));
    }
}
