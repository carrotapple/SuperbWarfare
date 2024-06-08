package net.mcreator.target.mixins;

import net.mcreator.target.init.TargetModMobEffects;
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
        boolean shock = false;
        float sens = 0.2f;
        float fov = (float) player.getPersistentData().getDouble("fov");
        float custom_sens = (float) stack.getOrCreateTag().getInt("sensitivity");

        float original_fov = mc.options.fov().get();

        if (!player.getMainHandItem().isEmpty() && mc.options.getCameraType() == CameraType.FIRST_PERSON) {
            if (stack.is(TargetModTags.Items.GUN)) {
                if ((player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).zooming) {
                    additionalAdsSensitivity = (float) Mth.clamp((1 + 0.1f * custom_sens) * (1.25F * fov / original_fov) * (1 + 0.2f * Math.pow((original_fov / fov), 1.25)), 0.125F, 2F);
                } else {
                    additionalAdsSensitivity = Mth.clamp((1 + 0.1f * custom_sens) * 1.25F, 0.125F, 2F);
                }
                flag = true;
            } else {
                return original;
            }
        }

        if (Minecraft.getInstance().player.hasEffect(TargetModMobEffects.SHOCK.get()) && !Minecraft.getInstance().player.isSpectator()) {
            shock = true;
        }

        return original * additionalAdsSensitivity * (1.0 - sens * (flag ? 1 : 0)) * (shock ? 0 : 1);
    }
}
