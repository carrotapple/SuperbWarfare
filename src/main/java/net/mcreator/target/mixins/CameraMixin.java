package net.mcreator.target.mixins;

import net.mcreator.target.entity.DroneEntity;
import net.mcreator.target.entity.Mk42Entity;
import net.mcreator.target.init.TargetModItems;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Camera.class)
public abstract class CameraMixin {
    @Shadow(aliases = "Lnet/minecraft/client/Camera;setRotation(FF)V")
    protected abstract void setRotation(float p_90573_, float p_90574_);

    @Shadow(aliases = "Lnet/minecraft/client/Camera;setPosition(DDD)V")
    protected abstract void setPosition(double p_90585_, double p_90586_, double p_90587_);

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Camera;setRotation(FF)V", ordinal = 0),
            method = "setup(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/world/entity/Entity;ZZF)V",
            cancellable = true)
    private void onSetup(BlockGetter level, Entity entity, boolean detached, boolean mirrored, float partialTicks, CallbackInfo info) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;

        if (player != null) {
            ItemStack stack = player.getMainHandItem();

            if (stack.is(TargetModItems.MONITOR.get()) && stack.getOrCreateTag().getBoolean("Using") && stack.getOrCreateTag().getBoolean("Linked")) {
                DroneEntity drone = player.level().getEntitiesOfClass(DroneEntity.class, player.getBoundingBox().inflate(512))
                        .stream().filter(e -> e.getStringUUID().equals(stack.getOrCreateTag().getString("LinkedDrone"))).findFirst().orElse(null);

                if (drone != null) {
                    setRotation(drone.getViewYRot(partialTicks), drone.getViewXRot(partialTicks));
                    setPosition(Mth.lerp(partialTicks, drone.xo, drone.getX()) + 0.3 * drone.getLookAngle().x, Mth.lerp(partialTicks, drone.yo, drone.getY()) + 0.08, Mth.lerp(partialTicks, drone.zo, drone.getZ()) + 0.3 * drone.getLookAngle().z);
                    info.cancel();
                }
            }
        }
    }

    @Inject(method = "setup", at = @At("TAIL"))
    public void ia$setup(BlockGetter area, Entity entity, boolean thirdPerson, boolean inverseView, float tickDelta, CallbackInfo ci) {
        if (thirdPerson && entity.getVehicle() instanceof Mk42Entity) {
            move(-getMaxZoom(8), 1.0, 0.0);
        }
    }

    @Shadow
    protected abstract void move(double x, double y, double z);

    @Shadow
    protected abstract double getMaxZoom(double desiredCameraDistance);
}