package net.mcreator.superbwarfare.mixins;

import net.mcreator.superbwarfare.entity.DroneEntity;
import net.mcreator.superbwarfare.entity.Mk42Entity;
import net.mcreator.superbwarfare.entity.Mle1934Entity;
import net.mcreator.superbwarfare.event.ClientEventHandler;
import net.mcreator.superbwarfare.init.ModItems;
import net.mcreator.superbwarfare.init.ModTags;
import net.minecraft.client.Camera;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import org.joml.Vector3d;
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

            if (stack.is(ModItems.MONITOR.get()) && stack.getOrCreateTag().getBoolean("Using") && stack.getOrCreateTag().getBoolean("Linked")) {
                DroneEntity drone = player.level().getEntitiesOfClass(DroneEntity.class, player.getBoundingBox().inflate(512))
                        .stream().filter(e -> e.getStringUUID().equals(stack.getOrCreateTag().getString("LinkedDrone"))).findFirst().orElse(null);

                if (drone != null) {
                    float yRot = drone.getYRot();
                    if (yRot < 0) {
                        yRot += 360;
                    }
                    yRot = yRot + 90 % 360;

                    var CameraPos = new Vector3d(0.20375, 0.103125, 0);
                    CameraPos.rotateZ(-drone.getXRot() * Mth.DEG_TO_RAD);
                    CameraPos.rotateY(-yRot * Mth.DEG_TO_RAD);

                    setRotation(drone.getViewYRot(partialTicks), drone.getViewXRot(partialTicks));
                    setPosition(Mth.lerp(partialTicks, drone.xo + CameraPos.x, drone.getX() + CameraPos.x), Mth.lerp(partialTicks, drone.yo + CameraPos.y, drone.getY() + CameraPos.y), Mth.lerp(partialTicks, drone.zo + CameraPos.z, drone.getZ() + CameraPos.z));
                    info.cancel();
                }
            }
        }
    }

    @Inject(method = "setup", at = @At("TAIL"))
    public void ia$setup(BlockGetter area, Entity entity, boolean thirdPerson, boolean inverseView, float tickDelta, CallbackInfo ci) {
        if (thirdPerson && entity.getVehicle() instanceof Mk42Entity) {
            move(-getMaxZoom(8), 1.0, 0.0);
            return;
        }
        if (thirdPerson && entity.getVehicle() instanceof Mle1934Entity) {
            move(-getMaxZoom(10), 1.3, 0.0);
            return;
        }
        if (Minecraft.getInstance().options.getCameraType() == CameraType.THIRD_PERSON_BACK && entity instanceof Player player && player.getMainHandItem().is(ModTags.Items.GUN)) {
            move(-getMaxZoom(-2.9 * Math.max(ClientEventHandler.pullPos, ClientEventHandler.zoomPos)), 0, -ClientEventHandler.cameraLocation * Math.max(ClientEventHandler.pullPos, ClientEventHandler.zoomPos));
        }

    }

    @Shadow
    protected abstract void move(double x, double y, double z);

    @Shadow
    protected abstract double getMaxZoom(double desiredCameraDistance);
}