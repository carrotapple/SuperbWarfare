package net.mcreator.target.mixins;

import net.mcreator.target.init.TargetModTags;
import net.mcreator.target.network.TargetModVariables;
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

        ItemStack stack = mc.player.getMainHandItem();

        boolean flag = false;
        float sens = 0.01f;
        float fov = (float) player.getPersistentData().getDouble("fov");

        float original_fov = mc.options.fov().get();

        if (mc.player != null && !mc.player.getMainHandItem().isEmpty() && mc.options.getCameraType() == CameraType.FIRST_PERSON) {
            if (stack.is(TargetModTags.Items.GUN) && (player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).zooming) {
                additionalAdsSensitivity = Mth.clamp(1.5F * fov / original_fov, 0.25F, 0.8F);

                flag = true;
            }
        }
        return original * additionalAdsSensitivity * (1.0 - sens * (flag ? 1 : 0));
    }
}
