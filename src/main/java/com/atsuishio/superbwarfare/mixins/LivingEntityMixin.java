package com.atsuishio.superbwarfare.mixins;

import com.atsuishio.superbwarfare.init.ModDamageTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Unique
    private DamageSource target$source;

    @Inject(method = "hurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;knockback(DDD)V"))
    private void capture(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        this.target$source = source;
    }

    @ModifyArg(method = "hurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;knockback(DDD)V"), index = 0)
    private double modifyApplyKnockbackArgs(double original) {
        if (this.target$source.is(ModDamageTypes.GUN_FIRE) || this.target$source.is(ModDamageTypes.GUN_FIRE_HEADSHOT)
                || this.target$source.is(ModDamageTypes.SHOCK) || this.target$source.is(ModDamageTypes.GUN_FIRE_ABSOLUTE)
                || this.target$source.is(ModDamageTypes.GUN_FIRE_HEADSHOT_ABSOLUTE) || this.target$source.is(ModDamageTypes.BURN)) {
            return 0.05 * original;
        }
        return original;
    }
}