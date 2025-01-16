package com.atsuishio.superbwarfare.mixins;

import com.atsuishio.superbwarfare.entity.DroneEntity;
import com.atsuishio.superbwarfare.entity.vehicle.*;
import com.atsuishio.superbwarfare.event.ClientEventHandler;
import com.atsuishio.superbwarfare.init.ModItems;
import com.atsuishio.superbwarfare.init.ModTags;
import com.atsuishio.superbwarfare.tools.EntityFindUtil;
import com.mojang.math.Axis;
import net.minecraft.client.Camera;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import org.joml.Math;
import org.joml.Matrix4f;
import org.joml.Vector3d;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
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
        LocalPlayer player = mc.player;

        if (player != null) {
            ItemStack stack = player.getMainHandItem();

            if ((player.getVehicle() != null && player.getVehicle() instanceof SpeedboatEntity boat && boat.getFirstPassenger() == player) && ClientEventHandler.zoomVehicle && stack.is(ItemStack.EMPTY.getItem())) {
                float yRot = boat.getYRot();
                if (yRot < 0) {
                    yRot += 360;
                }
                yRot = yRot + 90 % 360;
                var CameraPos = new Vector3d(-0.57, 3.3, 0);
                CameraPos.rotateZ(-boat.getXRot() * Mth.DEG_TO_RAD);
                CameraPos.rotateY(-yRot * Mth.DEG_TO_RAD);

                setRotation(Mth.lerp(partialTicks, player.yBobO, player.yBob), Mth.lerp(partialTicks, player.xBobO, player.xBob));
                setPosition(Mth.lerp(partialTicks, boat.xo + CameraPos.x, boat.getX() + CameraPos.x), Mth.lerp(partialTicks, boat.yo + CameraPos.y, boat.getY() + CameraPos.y), Mth.lerp(partialTicks, boat.zo + CameraPos.z, boat.getZ() + CameraPos.z));
                info.cancel();

                return;
            }

            if ((player.getVehicle() instanceof Lav150Entity lav150 && lav150.getFirstPassenger() == player) && Minecraft.getInstance().options.getCameraType() == CameraType.FIRST_PERSON) {

                setRotation(-Mth.lerp(partialTicks, lav150.turretYRotO - lav150.yRotO, lav150.getTurretYRot() - lav150.getYRot()), Mth.lerp(partialTicks, lav150.turretXRotO - lav150.xRotO, lav150.getTurretXRot() - lav150.getXRot()));
                setPosition(Mth.lerp(partialTicks, player.xo, player.getX()), Mth.lerp(partialTicks, player.yo + player.getEyeHeight(), player.getEyeY()), Mth.lerp(partialTicks, player.zo, player.getZ()));
                info.cancel();

                return;
            }

            if (stack.is(ModItems.MONITOR.get()) && stack.getOrCreateTag().getBoolean("Using") && stack.getOrCreateTag().getBoolean("Linked")) {
                DroneEntity drone = EntityFindUtil.findDrone(player.level(), stack.getOrCreateTag().getString("LinkedDrone"));
                if (drone != null) {
                    Matrix4f transform = superbWarfare$getVehicleTransform(drone, partialTicks);
                    float x0 = 0f;
                    float y0 = 0.075f;
                    float z0 = 0.18f;

                    Vector4f worldPosition = superbWarfare$transformPosition(transform, x0, y0, z0);

                    setRotation(drone.getYaw(partialTicks), drone.getPitch(partialTicks));
                    setPosition(worldPosition.x, worldPosition.y, worldPosition.z);
                    info.cancel();
                }
            }
        }
    }

    @Unique
    private static Matrix4f superbWarfare$getVehicleTransform(DroneEntity vehicle, float ticks) {
        Matrix4f transform = new Matrix4f();
        transform.translate((float) Mth.lerp(ticks, vehicle.xo, vehicle.getX()), (float) Mth.lerp(ticks, vehicle.yo, vehicle.getY()), (float) Mth.lerp(ticks, vehicle.zo, vehicle.getZ()));
        transform.rotate(Axis.YP.rotationDegrees(-vehicle.getYaw(ticks)));
        transform.rotate(Axis.XP.rotationDegrees(vehicle.getBodyPitch(ticks)));
        transform.rotate(Axis.ZP.rotationDegrees(vehicle.getRoll(ticks)));
        return transform;
    }

    @Unique
    private static Vector4f superbWarfare$transformPosition(Matrix4f transform, float x, float y, float z) {
        return transform.transform(new Vector4f(x, y, z, 1));
    }

    @Inject(method = "setup", at = @At("TAIL"))
    public void ia$setup(BlockGetter area, Entity entity, boolean thirdPerson, boolean inverseView, float tickDelta, CallbackInfo ci) {
        if (thirdPerson && entity.getVehicle() instanceof Mk42Entity) {
            move(-getMaxZoom(8), 1, 0.0);
            return;
        }
        if (thirdPerson && entity.getVehicle() instanceof Mle1934Entity) {
            move(-getMaxZoom(10), 1.3, 0.0);
            return;
        }
        if (thirdPerson && entity.getVehicle() instanceof AnnihilatorEntity) {
            move(-getMaxZoom(16), 1.3, 0.0);
            return;
        }
        if (thirdPerson && entity.getVehicle() instanceof SpeedboatEntity && !ClientEventHandler.zoomVehicle) {
            move(-getMaxZoom(3), 1, 0.0);
            return;
        }
        if (thirdPerson && entity.getVehicle() instanceof Ah6Entity) {
            move(-getMaxZoom(7), 1, -2.7);
            return;
        }

        if (thirdPerson && entity.getVehicle() instanceof Lav150Entity && !ClientEventHandler.zoomVehicle) {
            move(-getMaxZoom(2.5), 0.75, 0.0);
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