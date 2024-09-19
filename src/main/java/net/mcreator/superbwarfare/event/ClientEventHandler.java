package net.mcreator.superbwarfare.event;

import net.mcreator.superbwarfare.entity.DroneEntity;
import net.mcreator.superbwarfare.entity.ICannonEntity;
import net.mcreator.superbwarfare.init.ModItems;
import net.mcreator.superbwarfare.init.ModMobEffects;
import net.mcreator.superbwarfare.init.ModTags;
import net.mcreator.superbwarfare.network.ModVariables;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

import static net.mcreator.superbwarfare.entity.DroneEntity.ROT_X;
import static net.mcreator.superbwarfare.entity.DroneEntity.ROT_Z;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientEventHandler {
    private static double zoomTimer = 0;
    private static double zoomPos = 0;
    private static double zoomPosZ = 0;
    private static double swayTimer = 0;
    private static double swayX = 0;
    private static double swayY = 0;
    private static double moveXTimer = 0;
    private static double moveYTimer = 0;
    private static double movePosX = 0;
    private static double movePosY = 0;
    private static double moveRotZ = 0;
    private static double movePosHorizon = 0;
    private static double velocityY = 0;
    private static double turnRotX = 0;
    private static double turnRotY = 0;
    private static double turnRotZ = 0;
    private static double cameraRotX = 0;
    private static double cameraRotY = 0;
    private static double cameraRotZ = 0;
    private static double firePosTimer = 0;
    private static double fireRotTimer = 0;
    private static double firePos = 0;
    private static double firePosZ = 0;
    private static double fireRot = 0;
    private static double droneCameraRotX = 0;
    private static double droneCameraRotY = 0;
    private static double droneRotX = 0;
    private static double droneRotZ = 0;


    @SubscribeEvent
    public static void handleWeaponTurn(RenderHandEvent event) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) {
            return;
        }
        var data = player.getPersistentData();
        float xRotOffset = Mth.lerp(event.getPartialTick(), player.xBobO, player.xBob);
        float yRotOffset = Mth.lerp(event.getPartialTick(), player.yBobO, player.yBob);
        float xRot = player.getViewXRot(event.getPartialTick()) - xRotOffset;
        float yRot = player.getViewYRot(event.getPartialTick()) - yRotOffset;
        turnRotX = Mth.clamp(0.05 * xRot, -5, 5) * (1 - 0.75 * zoomTimer);
        turnRotY = Mth.clamp(0.05 * yRot, -10, 10) * (1 - 0.75 * zoomTimer);
        turnRotZ = Mth.clamp(0.1 * yRot, -10, 10) * (1 - zoomTimer);

        droneCameraRotX = Mth.clamp(0.25f * xRot, -10, 10);
        droneCameraRotY = Mth.clamp(0.25f * yRot, -20, 10);
    }

    public static double getTurnRotX() {
        return turnRotX;
    }

    public static double getTurnRotY() {
        return turnRotY;
    }

    public static double getTurnRotZ() {
        return turnRotZ;
    }

    @SubscribeEvent
    public static void computeCameraAngles(ViewportEvent.ComputeCameraAngles event) {
        ClientLevel level = Minecraft.getInstance().level;
        Entity entity = event.getCamera().getEntity();

        handlePlayerCameraShake(event);

        if (level != null && entity instanceof LivingEntity living
                && living.getMainHandItem().is(ModItems.MONITOR.get())
                && living.getMainHandItem().getOrCreateTag().getBoolean("Using")
                && living.getMainHandItem().getOrCreateTag().getBoolean("Linked")) {
            handleDroneCamera(event, living);
        } else {
            if (Minecraft.getInstance().gameRenderer.currentEffect() != null && Minecraft.getInstance().gameRenderer.currentEffect().getName().equals("superbwarfare:shaders/post/scan_pincushion.json")) {
                Minecraft.getInstance().gameRenderer.shutdownEffect();
            }
        }
        if (level != null && entity instanceof LivingEntity living && living.getMainHandItem().is(ModTags.Items.GUN)) {
            handleWeaponSway(living);
            handleWeaponMove(living);
            handleWeaponZoom();
            handlePlayerBreath(living);
            handleWeaponFire(event, living);
            handleShockCamera(event, living);
            handleBowPullAnimation(living);
        }
    }

    private static void handleDroneCamera(ViewportEvent.ComputeCameraAngles event, LivingEntity entity) {
        var data = entity.getPersistentData();
        ItemStack stack = entity.getMainHandItem();
        double pitch = event.getPitch();
        double roll = event.getRoll();

        DroneEntity drone = entity.level().getEntitiesOfClass(DroneEntity.class, entity.getBoundingBox().inflate(512))
                .stream().filter(e -> e.getStringUUID().equals(stack.getOrCreateTag().getString("LinkedDrone"))).findFirst().orElse(null);

        if (drone != null) {

            if (droneRotZ > drone.getEntityData().get(ROT_Z)) {
                droneRotZ = Mth.clamp(droneRotZ - 0.3 * Math.pow(drone.getEntityData().get(ROT_Z) - droneRotZ, 2), drone.getEntityData().get(ROT_Z), Double.POSITIVE_INFINITY);
            } else {
                droneRotZ = Mth.clamp(droneRotZ + 0.3 * Math.pow(drone.getEntityData().get(ROT_Z) - droneRotZ, 2), Double.NEGATIVE_INFINITY, drone.getEntityData().get(ROT_Z));
            }

            if (droneRotX > drone.getEntityData().get(ROT_X)) {
                droneRotX = Mth.clamp(droneRotX - 0.2 * Math.pow(drone.getEntityData().get(ROT_X) - droneRotX, 2), drone.getEntityData().get(ROT_X), Double.POSITIVE_INFINITY);
            } else {
                droneRotX = Mth.clamp(droneRotX + 0.2 * Math.pow(drone.getEntityData().get(ROT_X) - droneRotX, 2), Double.NEGATIVE_INFINITY, drone.getEntityData().get(ROT_X));
            }

            event.setPitch((float) (pitch + droneCameraRotX - 0.15f * Mth.RAD_TO_DEG * droneRotZ));
            event.setRoll((float) (roll + droneCameraRotY - 0.5f * Mth.RAD_TO_DEG * droneRotX));
        }

        if (drone != null && stack.getOrCreateTag().getBoolean("Using")) {
            if (Minecraft.getInstance().gameRenderer.currentEffect() == null) {
                Minecraft.getInstance().gameRenderer.loadEffect(new ResourceLocation("superbwarfare:shaders/post/scan_pincushion.json"));
            }
        }
    }


    private static void handleCannonScreen(LivingEntity entity) {
        if (entity.level().isClientSide() && entity instanceof Player player) {
            ItemStack stack = player.getMainHandItem();
            DroneEntity drone = entity.level().getEntitiesOfClass(DroneEntity.class, entity.getBoundingBox().inflate(512))
                    .stream().filter(e -> e.getStringUUID().equals(stack.getOrCreateTag().getString("LinkedDrone"))).findFirst().orElse(null);

            if ((drone != null && player.getMainHandItem().is(ModItems.MONITOR.get()) && stack.getOrCreateTag().getBoolean("Using"))
                    || (player.isPassenger() && player.getVehicle() instanceof ICannonEntity && GLFW.glfwGetMouseButton(Minecraft.getInstance().getWindow().getWindow(), GLFW.GLFW_MOUSE_BUTTON_RIGHT) == GLFW.GLFW_PRESS && !stack.is(ModTags.Items.GUN))) {
                if (Minecraft.getInstance().gameRenderer.currentEffect() == null) {
                    Minecraft.getInstance().gameRenderer.loadEffect(new ResourceLocation("minecraft:shaders/post/scan_pincushion.json"));
                }
            } else {
                Minecraft.getInstance().gameRenderer.shutdownEffect();
            }
        }
    }

    @SubscribeEvent
    public static void onRenderHand(RenderHandEvent event) {
        Player player = Minecraft.getInstance().player;
        if (player == null) return;

        InteractionHand leftHand = Minecraft.getInstance().options.mainHand().get() == HumanoidArm.RIGHT ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
        InteractionHand rightHand = Minecraft.getInstance().options.mainHand().get() == HumanoidArm.RIGHT ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;

        ItemStack rightHandItem = player.getItemInHand(rightHand);

        if (event.getHand() == leftHand) {
            if (rightHandItem.is(ModTags.Items.GUN)) {
                event.setCanceled(true);
            }
        }

        ItemStack stack = player.getMainHandItem();
        if (stack.is(ModItems.MONITOR.get()) && stack.getOrCreateTag().getBoolean("Using") && stack.getOrCreateTag().getBoolean("Linked")) {
            player.level().getEntitiesOfClass(DroneEntity.class, player.getBoundingBox().inflate(512))
                    .stream().filter(e -> e.getStringUUID().equals(stack.getOrCreateTag().getString("LinkedDrone"))).findFirst().ifPresent(drone -> event.setCanceled(true));
        }
    }
    
    private static void handleWeaponSway(LivingEntity entity) {
        if (entity.getMainHandItem().is(ModTags.Items.GUN)) {
            float times = 2 * Minecraft.getInstance().getDeltaFrameTime();
            double pose;
            var data = entity.getPersistentData();

            if (entity.isShiftKeyDown() && entity.getBbHeight() >= 1 && data.getDouble("prone") == 0) {
                pose = 0.85;
            } else if (data.getDouble("prone") > 0) {
                pose = entity.getMainHandItem().getOrCreateTag().getDouble("bipod") == 1 ? 0 : 0.25f;
            } else {
                pose = 1;
            }

            swayTimer += 0.05 * times;
            swayX = pose * -0.008 * Math.sin(swayTimer) * (1 - 0.95 * zoomTimer);
            swayY = pose * 0.125 * Math.sin(swayTimer - 1.585) * (1 - 0.95 * zoomTimer) - 3 * moveRotZ;
        }
    }

    public static double getSwayX() {
        return swayX;
    }

    public static double getSwayY() {
        return swayY;
    }

    private static void handleWeaponMove(LivingEntity entity) {
        if (entity.getMainHandItem().is(ModTags.Items.GUN)) {
            float times = 4.5f * Minecraft.getInstance().getDeltaFrameTime();
            var data = entity.getPersistentData();
            double move_speed = (float) Mth.clamp(entity.getDeltaMovement().horizontalDistanceSqr(), 0, 0.02);
            double on_ground;
            if (entity.onGround()) {
                if (entity.isSprinting()) {
                    on_ground = 1.0;
                } else {
                    on_ground = 2.0;
                }
            } else {
                on_ground = 0.001;
            }

            if (Minecraft.getInstance().options.keyUp.isDown() && firePosTimer == 0 && zoomTimer == 0) {
                moveRotZ = Mth.clamp(moveRotZ + 0.007 * times,0,0.14);
            } else {
                moveRotZ = Mth.clamp(moveRotZ - 0.007 * times,0,0.14);
            }

            if ((Minecraft.getInstance().options.keyLeft.isDown()
                    || Minecraft.getInstance().options.keyRight.isDown()
                    || Minecraft.getInstance().options.keyUp.isDown()
                    || Minecraft.getInstance().options.keyDown.isDown()) && firePosTimer == 0) {

                if (moveYTimer < 1.25) {
                    moveYTimer += 1.2 * on_ground * times * move_speed;
                } else {
                    moveYTimer = 0.25;
                }

                if (moveXTimer < 2) {
                    moveXTimer += 1.2 * on_ground * times * move_speed;
                } else {
                    moveXTimer = 0;
                }

                movePosX= 0.2 * Math.sin(1 * Math.PI * moveXTimer) * (1 - 0.95 * zoomTimer);
                movePosY = -0.135 * Math.sin(2 * Math.PI * (moveYTimer - 0.25)) * (1 - 0.95 * zoomTimer);

            } else {
                if (moveYTimer > 0.25) {
                    moveYTimer -= 0.5 * times;
                } else {
                    moveYTimer = 0.25;
                }

                if (moveXTimer > 0) {
                    moveXTimer -= 0.5 * times;
                } else {
                    moveXTimer = 0;
                }

                if (movePosX > 0) {
                    movePosX -= 1.5 * (Math.pow(movePosX, 2) * times) * (1 - 0.75 * zoomTimer);
                } else {
                    movePosX += 1.5 * (Math.pow(movePosX, 2) * times) * (1 - 0.75 * zoomTimer);
                }

                if (movePosY > 0) {
                    movePosY -= 1.5 * (Math.pow(movePosY, 2) * times) * (1 - 0.75 * zoomTimer);
                } else {
                    movePosY += 1.5 * (Math.pow(movePosY, 2) * times) * (1 - 0.75 * zoomTimer);
                }
            }

            if (movePosHorizon < 0) {
                movePosHorizon += 2 * times * Math.pow(movePosHorizon, 2);
            } else {
                movePosHorizon -= 2 * times * Math.pow(movePosHorizon, 2);
            }
            if (Minecraft.getInstance().options.keyRight.isDown()) {
                movePosHorizon = Mth.clamp(movePosHorizon + Math.pow(Math.abs(movePosHorizon) + 0.05, 2) * 0.2 * times, -0.5,0.5) * (1 - zoomTimer);
            } else if (Minecraft.getInstance().options.keyLeft.isDown()) {
                movePosHorizon = Mth.clamp(movePosHorizon - Math.pow(Math.abs(movePosHorizon) + 0.05, 2) * 0.2 * times, -0.5,0.5) * (1 - zoomTimer);
            }

            double velocity = entity.getDeltaMovement().y();

            if (-0.8 < velocity + 0.078 && velocity + 0.078 < 0.8) {
                if (velocityY < entity.getDeltaMovement().y() + 0.078) {
                    velocityY = Mth.clamp((velocityY + 0.55 * Math.pow((velocity + 0.078) - velocityY , 2)), -0.8, 0.8) * (1 - 0.8 * zoomTimer);
                } else {
                    velocityY = Mth.clamp((velocityY - 0.55 * Math.pow((velocity + 0.078) - velocityY , 2)), -0.8, 0.8) * (1 - 0.8 * zoomTimer);
                }
            }
        }
    }

    public static double getMoveRotZ() {
        return moveRotZ;
    }

    public static double getMovePosX() {
        return movePosX;
    }

    public static double getMovePosY() {
        return movePosY;
    }

    public static double getMovePosHorizon() {
        return movePosHorizon;
    }

    public static double getVelocityY() {
        return velocityY;
    }

    private static void handleWeaponZoom() {
        float times = 5 * Minecraft.getInstance().getDeltaFrameTime();
        if (GLFW.glfwGetMouseButton(Minecraft.getInstance().getWindow().getWindow(), GLFW.GLFW_MOUSE_BUTTON_RIGHT) == GLFW.GLFW_PRESS) {
            zoomTimer = Mth.clamp(zoomTimer + 0.03 * times,0,1);
        } else {
            zoomTimer = Mth.clamp(zoomTimer - 0.04 * times,0,1);
        }
        zoomPos = 0.5 * Math.cos(Math.PI * Math.pow(Math.pow(zoomTimer, 2) - 1, 2)) + 0.5;
        zoomPosZ = -Math.pow(2 * zoomTimer - 1, 2) + 1;
    }

    public static double getZoomTime() {
        return zoomTimer;
    }

    public static double getZoomPos() {
        return zoomPos;
    }

    public static double getZoomPosZ() {
        return zoomPosZ;
    }

    private static void handleWeaponFire(ViewportEvent.ComputeCameraAngles event, LivingEntity entity) {
        float times = 1.5f * Minecraft.getInstance().getDeltaFrameTime();
        double yaw = event.getYaw();
        double pitch = event.getPitch();
        double roll = event.getRoll();
        double amplitude;
        ItemStack stack = entity.getMainHandItem();
        amplitude = 15000 * stack.getOrCreateTag().getDouble("recoil_y")
                * stack.getOrCreateTag().getDouble("recoil_x");

        var capability = entity.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null);
        if (capability.orElse(new ModVariables.PlayerVariables()).firing > 0) {
            firePosTimer = 0.001;
            fireRotTimer = 0.001;
            firePosZ = 0.1;
        }

        firePosZ = Mth.clamp(firePosZ - 0.01 * times, 0, 0.6);

        if (0 < firePosTimer) {
            firePosTimer += 0.25 * (1.1 - firePosTimer) * times;
        }
        if (0 < firePosTimer && firePosTimer < 0.454) {
            firePos = (-18.34) * Math.pow(firePosTimer, 2) + 8.58 * firePosTimer + firePosZ;
        }
        if (0.454 <= firePosTimer && firePosTimer < 1) {
            firePos = 4.34 * Math.pow(firePosTimer, 2) - 6.5 * firePosTimer + 2.167 + firePosZ;
        }

        if (0 < fireRotTimer && fireRotTimer < 1.732) {
            fireRotTimer += 0.18 * (1.9 - fireRotTimer) * times;
        }

        if (0 < fireRotTimer && fireRotTimer < 1.732) {
            fireRot = 1 / 6.3 * (fireRotTimer - 0.5) * Math.sin(6.3 * (fireRotTimer - 0.5)) * (3 - Math.pow(fireRotTimer, 2));
            if ((capability.orElse(new ModVariables.PlayerVariables())).recoilHorizon > 0) {
                event.setYaw((float) (yaw - 1.3 * amplitude * (1 / 6.3 * (fireRotTimer - 0.5)) * Math.sin(6.3 * (fireRotTimer - 0.5)) * (3 - Math.pow(fireRotTimer, 2)) + 1 * Mth.clamp(0.3 - fireRotTimer, 0, 1) * (2 * Math.random() - 1)));
                event.setPitch((float) (pitch + 1.3 * amplitude * (1 / 6.3 * (fireRotTimer - 0.5)) * Math.sin(6.3 * (fireRotTimer - 0.5)) * (3 - Math.pow(fireRotTimer, 2)) + 1 * Mth.clamp(0.3 - fireRotTimer, 0, 1) * (2 * Math.random() - 1)));
                event.setRoll((float) (roll + 4.2 * amplitude * (1 / 6.3 * (fireRotTimer - 0.5)) * Math.sin(6.3 * (fireRotTimer - 0.5)) * (3 - Math.pow(fireRotTimer, 2)) + 3 * Mth.clamp(0.5 - fireRotTimer, 0, 0.5) * (2 * Math.random() - 1)));
            } else if ((capability.orElse(new ModVariables.PlayerVariables())).recoilHorizon <= 0) {
                event.setYaw((float) (yaw + 1.3 * amplitude * (1 / 6.3 * (fireRotTimer - 0.5)) * Math.sin(6.3 * (fireRotTimer - 0.5)) * (3 - Math.pow(fireRotTimer, 2)) + 1 * Mth.clamp(0.3 - fireRotTimer, 0, 1) * (2 * Math.random() - 1)));
                event.setPitch((float) (pitch - 1.3 * amplitude * (1 / 6.3 * (fireRotTimer - 0.5)) * Math.sin(6.3 * (fireRotTimer - 0.5)) * (3 - Math.pow(fireRotTimer, 2)) + 1 * Mth.clamp(0.3 - fireRotTimer, 0, 1) * (2 * Math.random() - 1)));
                event.setRoll((float) (roll - 4.2 * amplitude * (1 / 6.3 * (fireRotTimer - 0.5)) * Math.sin(6.3 * (fireRotTimer - 0.5)) * (3 - Math.pow(fireRotTimer, 2)) + 3 * Mth.clamp(0.5 - fireRotTimer, 0, 0.5) * (2 * Math.random() - 1)));
            }
        }

        if (firePosTimer >= 1) {
            firePosTimer = 0;
        }
        if (fireRotTimer >= 1.732) {
            fireRotTimer = 0;
            fireRot = 0;
        }
    }

    public static double getFirePos() {
        return firePos;
    }

    public static double getFireRot() {
        return fireRot;
    }

    private static void handlePlayerBreath(LivingEntity entity) {
        float times = 4 * Minecraft.getInstance().getDeltaFrameTime();
        var data = entity.getPersistentData();

        if ((entity.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables())).breath) {
            data.putDouble("BreathTime", Mth.clamp(data.getDouble("BreathTime") + 0.06 * times,0,1));
        } else {
            data.putDouble("BreathTime", Mth.clamp(data.getDouble("BreathTime") - 0.06 * times,0,1));
        }
    }

    private static void handleShockCamera(ViewportEvent.ComputeCameraAngles event, LivingEntity entity) {
        if (entity.hasEffect(ModMobEffects.SHOCK.get()) && Minecraft.getInstance().options.getCameraType() == CameraType.FIRST_PERSON) {
            event.setYaw(Minecraft.getInstance().gameRenderer.getMainCamera().getYRot() + (float) Mth.nextDouble(RandomSource.create(), -3, 3));
            event.setPitch(Minecraft.getInstance().gameRenderer.getMainCamera().getXRot() + (float) Mth.nextDouble(RandomSource.create(), -3, 3));
            event.setRoll((float) Mth.nextDouble(RandomSource.create(), 8, 12));
        }
    }

    public static void shake(double boneRotX, double boneRotY, double boneRotZ) {
        cameraRotX = boneRotX;
        cameraRotY = boneRotY;
        cameraRotZ = boneRotZ;
    }

    private static void handlePlayerCameraShake(ViewportEvent.ComputeCameraAngles event) {
        double yaw = event.getYaw();
        double pitch = event.getPitch();
        double roll = event.getRoll();
        event.setPitch((float) (pitch + cameraRotX + 0.2 * turnRotX + 3 * velocityY));
        event.setYaw((float) (yaw + cameraRotY + 0.8 * turnRotY));
        event.setRoll((float) (roll + cameraRotZ + 0.35 * turnRotZ));
    }

    private static void handleBowPullAnimation(LivingEntity entity) {
        float times = 4 * Minecraft.getInstance().getDeltaFrameTime();
        CompoundTag persistentData = entity.getPersistentData();

        if ((entity.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables())).bowPull) {
            persistentData.putDouble("pulltime", Math.min(persistentData.getDouble("pulltime") + 0.018 * times, 1));
            persistentData.putDouble("bowtime", Math.min(persistentData.getDouble("bowtime") + 0.018 * times, 1));
            persistentData.putDouble("handtime", Math.min(persistentData.getDouble("handtime") + 0.018 * times, 1));
            persistentData.putDouble("handpos", (0.5 * Math.cos(Math.PI * Math.pow(Math.pow(persistentData.getDouble("handtime"), 2) - 1, 2)) + 0.5));
        } else {
            persistentData.putDouble("pulltime", Math.max(persistentData.getDouble("pulltime") - 0.009 * times, 0));
            persistentData.putDouble("bowtime", Math.max(persistentData.getDouble("bowtime") - 1 * times, 0));
            persistentData.putDouble("handtime", Math.max(persistentData.getDouble("handtime") - 0.04 * times, 0));
            if (persistentData.getDouble("handtime") > 0 && persistentData.getDouble("handtime") < 0.5) {
                persistentData.putDouble("handpos", (0.5 * Math.cos(Math.PI * Math.pow(Math.pow(persistentData.getDouble("handtime"), 2) - 1, 2)) + 0.5));
            }
        }

        persistentData.putDouble("pullpos", (0.5 * Math.cos(Math.PI * Math.pow(Math.pow(persistentData.getDouble("pulltime"), 2) - 1, 2)) + 0.5));
        persistentData.putDouble("bowpos", (0.5 * Math.cos(Math.PI * Math.pow(Math.pow(persistentData.getDouble("bowtime"), 2) - 1, 2)) + 0.5));
    }

    @SubscribeEvent
    public static void onFovUpdate(ViewportEvent.ComputeFov event) {
        Player player = Minecraft.getInstance().player;
        if (player == null) {
            return;
        }

        ItemStack stack = player.getMainHandItem();

        if (stack.is(ModTags.Items.GUN)) {
            if (!event.usedConfiguredFov()) {
                return;
            }

            double p = zoomPos;
            double zoom = stack.getOrCreateTag().getDouble("zoom") + stack.getOrCreateTag().getDouble("custom_zoom");

            event.setFOV(event.getFOV() / (1.0 + p * (zoom - 1)) * (1 - 0.4 * player.getPersistentData().getDouble("BreathTime")));
            player.getPersistentData().putDouble("fov", event.getFOV());
            return;
        }
        if (player.isPassenger() && player.getVehicle() instanceof ICannonEntity && GLFW.glfwGetMouseButton(Minecraft.getInstance().getWindow().getWindow(), GLFW.GLFW_MOUSE_BUTTON_RIGHT) == GLFW.GLFW_PRESS && !stack.is(ModTags.Items.GUN)) {
            event.setFOV(event.getFOV() / 5);
        }
    }

    @SubscribeEvent
    public static void handleRenderCrossHair(RenderGuiOverlayEvent.Pre event) {
        if (event.getOverlay() != VanillaGuiOverlay.CROSSHAIR.type()) {
            return;
        }

        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) {
            return;
        }

        if (!mc.options.getCameraType().isFirstPerson()) {
            return;
        }

        if (mc.player.getMainHandItem().is(ModTags.Items.GUN) || (mc.player.getVehicle() != null && mc.player.getVehicle() instanceof ICannonEntity)) {
            event.setCanceled(true);
        }

        ItemStack stack = mc.player.getMainHandItem();
        if (stack.is(ModItems.MONITOR.get()) && stack.getOrCreateTag().getBoolean("Using") && stack.getOrCreateTag().getBoolean("Linked")) {
            event.setCanceled(true);
        }
    }
}


