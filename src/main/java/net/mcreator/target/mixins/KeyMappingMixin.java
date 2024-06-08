package net.mcreator.target.mixins;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.KeyboardInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.mcreator.target.init.TargetModMobEffects;

@Mixin(KeyboardInput.class)
public abstract class KeyMappingMixin extends Input {

    @Unique
    private static long virtuarealcraft$counter = 0;

    @Shadow
    private static float calculateImpulse(boolean pInput, boolean pOtherInput) {
        return 0;
    }

    // 按键修改mixin
    @Inject(method = "tick", at = @At("RETURN"))
    public void tick(boolean pIsSneaking, float pSneakingSpeedMultiplier, CallbackInfo ci) {
        if (Minecraft.getInstance().player == null || !Minecraft.getInstance().player.hasEffect(TargetModMobEffects.SHOCK.get())
                || Minecraft.getInstance().player.isSpectator()) {
            return;
        }

        var moveKeyDown = this.up | this.down | this.left | this.right;

        this.up = false;
        this.down = false;

        virtuarealcraft$counter++;
        var reserveLeftAndRight = virtuarealcraft$counter % 3 == 0;
        this.left &= reserveLeftAndRight;
        this.right &= reserveLeftAndRight;

        this.shiftKeyDown = false;

        this.forwardImpulse = Math.min(calculateImpulse(moveKeyDown, false) + 0.6f, 1);
        this.leftImpulse *= 0.3f;
    }
}
