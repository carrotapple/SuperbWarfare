package com.atsuishio.superbwarfare.mixins;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Entity.class)
public class EntityMixin {

    // TODO 优化后续逻辑
    @Redirect(method = "turn(DD)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;setXRot(F)V", ordinal = 1))
    public void turn(Entity instance, float pXRot) {
        if (instance instanceof Player player) {
            player.setXRot(player.getXRot());
            while (player.getXRot() > 180F) {
                player.setXRot(player.getXRot() - 360F);
            }
            while (player.getYRot() <= -180F) {
                player.setXRot(player.getXRot() + 360F);
            }
        } else {
            instance.setXRot(Mth.clamp(instance.getXRot(), -90.0F, 90.0F));
        }
    }
}
