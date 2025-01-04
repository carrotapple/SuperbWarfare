package com.atsuishio.superbwarfare.mixins;

import com.atsuishio.superbwarfare.entity.vehicle.VehicleEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Code based on @Luke100000's ImmersiveAircraft
 */
@Mixin(value = Player.class, priority = 1145)
public abstract class PlayerEntityMixin extends Entity {

    public PlayerEntityMixin(EntityType<?> type, Level world) {
        super(type, world);
    }

    @Inject(method = "wantsToStopRiding", at = @At("HEAD"), cancellable = true)
    void shouldDismountInjection(CallbackInfoReturnable<Boolean> cir) {
        if (this.getRootVehicle() instanceof VehicleEntity) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "updatePlayerPose()V", at = @At("TAIL"))
    void updatePostInjection(CallbackInfo ci) {
        if (getRootVehicle() instanceof VehicleEntity) {
            this.setPose(Pose.STANDING);
        }
    }
}
