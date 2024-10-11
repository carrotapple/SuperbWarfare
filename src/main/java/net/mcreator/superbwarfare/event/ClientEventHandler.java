package net.mcreator.superbwarfare.event;

import net.mcreator.superbwarfare.ModUtils;
import net.mcreator.superbwarfare.config.client.DisplayConfig;
import net.mcreator.superbwarfare.entity.DroneEntity;
import net.mcreator.superbwarfare.entity.ICannonEntity;
import net.mcreator.superbwarfare.init.ModItems;
import net.mcreator.superbwarfare.init.ModMobEffects;
import net.mcreator.superbwarfare.init.ModTags;
import net.mcreator.superbwarfare.item.gun.GunItem;
import net.mcreator.superbwarfare.network.ModVariables;
import net.mcreator.superbwarfare.network.message.ShootMessage;
import net.mcreator.superbwarfare.perk.AmmoPerk;
import net.mcreator.superbwarfare.perk.Perk;
import net.mcreator.superbwarfare.perk.PerkHelper;
import net.mcreator.superbwarfare.tools.MillisTimer;
import net.mcreator.superbwarfare.tools.SeekTool;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.NetworkEvent;
import org.lwjgl.glfw.GLFW;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;

import java.util.function.Supplier;

import static net.mcreator.superbwarfare.entity.DroneEntity.ROT_X;
import static net.mcreator.superbwarfare.entity.DroneEntity.ROT_Z;
import static net.mcreator.superbwarfare.event.PlayerEventHandler.isProne;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientEventHandler {
    public static double zoomTime = 0;
    public static double zoomPos = 0;
    public static double zoomPosZ = 0;
    public static double swayTime = 0;
    public static double swayX = 0;
    public static double swayY = 0;
    public static double moveXTime = 0;
    public static double moveYTime = 0;
    public static double movePosX = 0;
    public static double movePosY = 0;
    public static double moveRotZ = 0;
    public static double movePosHorizon = 0;
    public static double velocityY = 0;

    public static double[] turnRot = {0, 0, 0};
    public static double[] cameraRot = {0, 0, 0};

    public static double fireRecoilTime = 0;
    public static double firePosTimer = 0;
    public static double fireRotTimer = 0;
    public static double firePos = 0;
    public static double firePosZ = 0;
    public static double fireRot = 0;

    public static double recoilTime = 0;

    public static double recoilHorizon = 0;

    public static boolean recoil = false;

    public static double recoilY = 0;
    public static double droneCameraRotX = 0;
    public static double droneCameraRotY = 0;
    public static double droneRotX = 0;
    public static double droneRotZ = 0;
    public static double breathTime = 0;

    public static double fov = 0;

    public static double pullTimer = 0;
    public static double bowTimer = 0;
    public static double handTimer = 0;
    public static double pullPos = 0;
    public static double bowPos = 0;
    public static double handPos = 0;
    public static double gunSpread = 0;
    public static double fireSpread = 0;
    public static double cantFireTime = 0;
    public static double lookDistance = 0;
    public static double cameraLocation = 0.6;

    public static double drawTime = 1;

    public static int shellIndex = 0;

    public static double[] shellIndexTime = {0, 0, 0, 0, 0};

    public static double[] randomShell = {0, 0, 0};
    public static MillisTimer clientTimer = new MillisTimer();

    @SubscribeEvent
    public static void handleWeaponTurn(RenderHandEvent event) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) {
            return;
        }
        float xRotOffset = Mth.lerp(event.getPartialTick(), player.xBobO, player.xBob);
        float yRotOffset = Mth.lerp(event.getPartialTick(), player.yBobO, player.yBob);
        float xRot = player.getViewXRot(event.getPartialTick()) - xRotOffset;
        float yRot = player.getViewYRot(event.getPartialTick()) - yRotOffset;
        turnRot[0] = Mth.clamp(0.05 * xRot, -5, 5) * (1 - 0.75 * zoomTime);
        turnRot[1] = Mth.clamp(0.05 * yRot, -10, 10) * (1 - 0.75 * zoomTime) + 1.5f * (Mth.DEG_TO_RAD * recoilHorizon) * (0.5 + 0.4 * fireSpread);
        turnRot[2] = Mth.clamp(0.1 * yRot, -10, 10) * (1 - zoomTime);

        droneCameraRotX = Mth.clamp(0.25f * xRot, -10, 10);
        droneCameraRotY = Mth.clamp(0.25f * yRot, -20, 10);
    }

    private static boolean notInGame() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return true;
        if (mc.getOverlay() != null) return true;
        if (mc.screen != null) return true;
        if (!mc.mouseHandler.isMouseGrabbed()) return true;
        return !mc.isWindowActive();
    }

    private static boolean isMoving() {
        Player player = Minecraft.getInstance().player;
        return Minecraft.getInstance().options.keyLeft.isDown()
                || Minecraft.getInstance().options.keyRight.isDown()
                || Minecraft.getInstance().options.keyUp.isDown()
                || Minecraft.getInstance().options.keyDown.isDown()
                || (player != null && player.isSprinting());
    }

    @SubscribeEvent
    public static void handleWeaponFire(TickEvent.RenderTickEvent event) {
        ClientLevel level = Minecraft.getInstance().level;
        Player player = Minecraft.getInstance().player;
        if (notInGame()) return;
        if (player == null) return;
        if (level == null) return;

        ItemStack stack = player.getMainHandItem();
        var perk = PerkHelper.getPerkByType(stack, Perk.Type.AMMO);

        // 精准度
        float times = Minecraft.getInstance().getDeltaFrameTime();

        double basicDev = stack.getOrCreateTag().getDouble("spread");
        double walk = isMoving() ? 0.3 * basicDev : 0;
        double sprint = player.isSprinting() ? 0.25 * basicDev : 0;
        double crouching = player.isCrouching() ? -0.15 * basicDev : 0;
        double prone = isProne(player) ? -0.3 * basicDev : 0;
        double jump = player.onGround() ? 0 * basicDev : 0.35 * basicDev;
        double ride = player.onGround() ? -0.25 * basicDev : 0;

        double zoomSpread;

        if (stack.is(ModTags.Items.SNIPER_RIFLE)) {
            zoomSpread = 1 - (0.995 * zoomTime);
        } else if (stack.is(ModTags.Items.SHOTGUN)) {
            if (perk instanceof AmmoPerk ammoPerk && ammoPerk.slug) {
                zoomSpread = 1 - (0.85 * zoomTime);
            } else {
                zoomSpread = 1 - (0.25 * zoomTime);
            }
        } else if (stack.is(ModItems.MINIGUN.get())) {
            zoomSpread = 1 - (0.25 * zoomTime);
        } else {
            zoomSpread = 1 - (0.9 * zoomTime);
        }

        double spread = stack.is(ModTags.Items.SHOTGUN) || stack.is(ModItems.MINIGUN.get()) ? 1.2 * zoomSpread * (basicDev + 0.2 * (walk + sprint + crouching + prone + jump + ride) + fireSpread) : zoomSpread * (0.7 * basicDev + walk + sprint + crouching + prone + jump + ride + 0.8 * fireSpread);

        if (gunSpread < spread) {
            gunSpread += 0.07 * Math.pow(spread - gunSpread, 2) * times;
        } else {
            gunSpread -= 0.07 * Math.pow(spread - gunSpread, 2) * times;
        }

        // 开火部分

        double weight = stack.getOrCreateTag().getDouble("weight");

        double speed = 1;

        if (weight == 0) {
            speed = 1.05;
        } else if (weight == 1) {
            speed = 0.85;
        } else if (weight == 2) {
            speed = 0.6;
        }

        if (player.getPersistentData().getDouble("noRun") == 0 && player.isSprinting() && GLFW.glfwGetMouseButton(Minecraft.getInstance().getWindow().getWindow(), GLFW.GLFW_MOUSE_BUTTON_RIGHT) != GLFW.GLFW_PRESS) {
            cantFireTime = Mth.clamp(cantFireTime + 3 * times, 0, 24);
        } else {
            cantFireTime = Mth.clamp(cantFireTime - 6 * speed * times, 0, 24);
        }


        if (GLFW.glfwGetMouseButton(Minecraft.getInstance().getWindow().getWindow(), GLFW.GLFW_MOUSE_BUTTON_LEFT) == GLFW.GLFW_PRESS
                && (player.getMainHandItem().is(ModTags.Items.NORMAL_GUN)
                && cantFireTime == 0
                && drawTime < 0.01
                && !notInGame()
                || (stack.is(ModItems.MINIGUN.get()) && !player.isSprinting() && stack.getOrCreateTag().getDouble("overheat") == 0 && !player.getCooldowns().isOnCooldown(stack.getItem()) && stack.getOrCreateTag().getDouble("minigun_rotation") >= 10
        ))) {

            double customRpm = 0;

            if (stack.getItem() == ModItems.DEVOTION.get()) {
                customRpm = stack.getOrCreateTag().getInt("customRpm");
            }

            if (stack.getItem() == ModItems.MINIGUN.get() && player.isInWater()) {
                customRpm = -0.25 * stack.getOrCreateTag().getDouble("rpm");
            }

            double rpm = stack.getOrCreateTag().getDouble("rpm") + customRpm;
            if (rpm == 0) {
                rpm = 600;
            }
            double rps = rpm / 60;

            // cooldown in ms
            double cooldown = 1000 / rps;


            if (!clientTimer.started()) {
                clientTimer.start();
                // 首发瞬间发射
                clientTimer.setProgress((long) (cooldown + 1));
            }

            if (clientTimer.getProgress() >= cooldown) {
                ModUtils.PACKET_HANDLER.sendToServer(new ShootMessage(spread));
                clientTimer.setProgress((long) (clientTimer.getProgress() - cooldown));
            }

        } else {
            clientTimer.stop();
            fireSpread = 0;
        }
    }

    @SubscribeEvent
    public static void handleWeaponBreathSway(TickEvent.RenderTickEvent event) {
        Player player = Minecraft.getInstance().player;
        if (player == null) return;
        if (!player.getMainHandItem().is(ModTags.Items.GUN)) return;

        float pose;
        float times = 2 * Minecraft.getInstance().getDeltaFrameTime();

        if (player.isCrouching() && player.getBbHeight() >= 1 && !isProne(player)) {
            pose = 0.85f;
        } else if (isProne(player)) {
            pose = player.getMainHandItem().getOrCreateTag().getDouble("bipod") == 1 ? 0 : 0.25f;
        } else {
            pose = 1;
        }

        if (!player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables()).breath &&
                player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables()).zoom) {
            float newPitch = (float) (player.getXRot() - 0.01f * Mth.sin((float) (0.03 * player.tickCount)) * pose * Mth.nextDouble(RandomSource.create(), 0.1, 1) * times);
            player.setXRot(newPitch);
            player.xRotO = player.getXRot();

            float newYaw = (float) (player.getYRot() - 0.005f * Mth.cos((float) (0.025 * (player.tickCount + 2 * Math.PI))) * pose * Mth.nextDouble(RandomSource.create(), 0.05, 1.25) * times);
            player.setYRot(newYaw);
            player.yRotO = player.getYRot();
        }
    }

    @SubscribeEvent
    public static void computeCameraAngles(ViewportEvent.ComputeCameraAngles event) {
        ClientLevel level = Minecraft.getInstance().level;
        Entity entity = event.getCamera().getEntity();
        handlePlayerCamera(event);
        if (level != null && entity instanceof LivingEntity living
                && living.getMainHandItem().is(ModItems.MONITOR.get())
                && living.getMainHandItem().getOrCreateTag().getBoolean("Using")
                && living.getMainHandItem().getOrCreateTag().getBoolean("Linked")) {
            handleDroneCamera(event, living);
        } else {
            var effect = Minecraft.getInstance().gameRenderer.currentEffect();
            if (effect != null && effect.getName().equals(ModUtils.MODID + ":shaders/post/scan_pincushion.json")) {
                Minecraft.getInstance().gameRenderer.shutdownEffect();
            }
        }
        if (level != null && entity instanceof LivingEntity living && living.getMainHandItem().is(ModTags.Items.GUN)) {
            handleWeaponSway(living);
            handleWeaponMove(living);
            handleWeaponZoom(living);
            handlePlayerBreath(living);
            handleWeaponFire(event, living);
            handleWeaponShell();
            handleGunRecoil();
            handleShockCamera(event, living);
            handleBowPullAnimation(living);
            handleWeaponDraw(living);
        }
    }

    private static void handleDroneCamera(ViewportEvent.ComputeCameraAngles event, LivingEntity entity) {
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
                Minecraft.getInstance().gameRenderer.loadEffect(ModUtils.loc("shaders/post/scan_pincushion.json"));
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

        if (event.getHand() == rightHand) {
            if (rightHandItem.is(ModTags.Items.GUN) && drawTime > 0.15) {
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
        if (entity.getMainHandItem().is(ModTags.Items.GUN) && entity instanceof Player player) {
            float times = 2 * Minecraft.getInstance().getDeltaFrameTime();
            double pose;

            if (player.isShiftKeyDown() && player.getBbHeight() >= 1 && isProne(player)) {
                pose = 0.85;
            } else if (isProne(player)) {
                pose = player.getMainHandItem().getOrCreateTag().getDouble("bipod") == 1 ? 0 : 0.25f;
            } else {
                pose = 1;
            }
            swayTime += 0.05 * times;

            swayX = pose * -0.008 * Math.sin(swayTime) * (1 - 0.95 * zoomTime);
            swayY = pose * 0.125 * Math.sin(swayTime - 1.585) * (1 - 0.95 * zoomTime) - 3 * moveRotZ;
        }
    }

    private static void handleWeaponMove(LivingEntity entity) {
        if (entity.getMainHandItem().is(ModTags.Items.GUN)) {
            float times = 3.7f * Minecraft.getInstance().getDeltaFrameTime();
            double moveSpeed = (float) Mth.clamp(entity.getDeltaMovement().horizontalDistanceSqr(), 0, 0.02);
            double onGround;
            if (entity.onGround()) {
                if (entity.isSprinting()) {
                    onGround = 1.35;
                } else {
                    onGround = 2.0;
                }
            } else {
                onGround = 0.001;
            }

            if (Minecraft.getInstance().options.keyUp.isDown() && firePosTimer == 0) {
                moveRotZ = Mth.clamp(moveRotZ + 0.007 * times, 0, 0.14) * (1 - zoomTime);
            } else {
                moveRotZ = Mth.clamp(moveRotZ - 0.007 * times, 0, 0.14) * (1 - zoomTime);
            }

            if (isMoving() && firePosTimer == 0) {
                if (moveYTime < 1.25) {
                    moveYTime += 1.2 * onGround * times * moveSpeed;
                } else {
                    moveYTime = 0.25;
                }

                if (moveXTime < 2) {
                    moveXTime += 1.2 * onGround * times * moveSpeed;
                } else {
                    moveXTime = 0;
                }

                movePosX = 0.2 * Math.sin(1 * Math.PI * moveXTime) * (1 - 0.95 * zoomTime);
                movePosY = -0.135 * Math.sin(2 * Math.PI * (moveYTime - 0.25)) * (1 - 0.95 * zoomTime);
            } else {
                if (moveYTime > 0.25) {
                    moveYTime -= 0.5 * times;
                } else {
                    moveYTime = 0.25;
                }

                if (moveXTime > 0) {
                    moveXTime -= 0.5 * times;
                } else {
                    moveXTime = 0;
                }

                if (movePosX > 0) {
                    movePosX -= 1.5 * (Math.pow(movePosX, 2) * times) * (1 - 0.75 * zoomTime);
                } else {
                    movePosX += 1.5 * (Math.pow(movePosX, 2) * times) * (1 - 0.75 * zoomTime);
                }

                if (movePosY > 0) {
                    movePosY -= 1.5 * (Math.pow(movePosY, 2) * times) * (1 - 0.75 * zoomTime);
                } else {
                    movePosY += 1.5 * (Math.pow(movePosY, 2) * times) * (1 - 0.75 * zoomTime);
                }
            }

            if (Minecraft.getInstance().options.keyRight.isDown()) {
                movePosHorizon = Mth.clamp(movePosHorizon + Math.pow(Math.abs(movePosHorizon) + 0.05, 2) * 0.2 * times, -0.5, 0.5) * (1 - zoomTime);
            } else if (Minecraft.getInstance().options.keyLeft.isDown()) {
                movePosHorizon = Mth.clamp(movePosHorizon - Math.pow(Math.abs(movePosHorizon) + 0.05, 2) * 0.2 * times, -0.5, 0.5) * (1 - zoomTime);
            }

            if (movePosHorizon < 0) {
                movePosHorizon += 4 * times * Math.pow(movePosHorizon, 2);
            } else {
                movePosHorizon -= 4 * times * Math.pow(movePosHorizon, 2);
            }

            movePosHorizon *= (1 - zoomTime);

            double velocity = entity.getDeltaMovement().y();

            if (-0.8 < velocity + 0.078 && velocity + 0.078 < 0.8) {
                if (velocityY < entity.getDeltaMovement().y() + 0.078) {
                    velocityY = Mth.clamp((velocityY + 0.55 * Math.pow((velocity + 0.078) - velocityY, 2)), -0.8, 0.8) * (1 - 0.8 * zoomTime);
                } else {
                    velocityY = Mth.clamp((velocityY - 0.55 * Math.pow((velocity + 0.078) - velocityY, 2)), -0.8, 0.8) * (1 - 0.8 * zoomTime);
                }
            }
        }
    }

    private static void handleWeaponZoom(LivingEntity entity) {
        if (!(entity instanceof Player player)) return;
        ItemStack stack = player.getMainHandItem();
        float times = 5 * Minecraft.getInstance().getDeltaFrameTime();
        double speed = stack.getOrCreateTag().getDouble("zoom_speed");
        if (GLFW.glfwGetMouseButton(Minecraft.getInstance().getWindow().getWindow(), GLFW.GLFW_MOUSE_BUTTON_RIGHT) == GLFW.GLFW_PRESS && !notInGame() && drawTime < 0.01) {
            zoomTime = Mth.clamp(zoomTime + 0.03 * speed * times, 0, 1);
        } else {
            zoomTime = Mth.clamp(zoomTime - 0.04 * speed * times, 0, 1);
        }
        zoomPos = 0.5 * Math.cos(Math.PI * Math.pow(Math.pow(zoomTime, 2) - 1, 2)) + 0.5;
        zoomPosZ = -Math.pow(2 * zoomTime - 1, 2) + 1;
    }

    public static void handleFireRecoilTimeMessage(double time, Supplier<NetworkEvent.Context> ctx) {
        if (ctx.get().getDirection().getReceptionSide() == LogicalSide.CLIENT) {
            fireRecoilTime = time;
            shellIndex++;

            shellIndexTime[shellIndex] = 0;

            randomShell[0] = (1 + 2 * Math.random());
            randomShell[1] = (1 + 2 * Math.random());
            randomShell[2] = (1 + 2 * Math.random());
        }
    }

    private static void handleWeaponFire(ViewportEvent.ComputeCameraAngles event, LivingEntity entity) {
        float times = 1.5f * Minecraft.getInstance().getDeltaFrameTime();
        double yaw = event.getYaw();
        double pitch = event.getPitch();
        double roll = event.getRoll();
        ItemStack stack = entity.getMainHandItem();
        double amplitude = 15000 * stack.getOrCreateTag().getDouble("recoil_y") * stack.getOrCreateTag().getDouble("recoil_x");

        if (fireRecoilTime > 0) {
            firePosTimer = 0.001;
            fireRotTimer = 0.001;
            firePosZ = 0.1;
            fireRecoilTime -= 7 * times;
            fireSpread += 0.1;
        }

        fireSpread = Mth.clamp(fireSpread - 0.1 * (Math.pow(fireSpread, 2) * times), 0, 100);
        firePosZ = Mth.clamp(firePosZ - 0.02 * times, 0, 0.6);

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
            if (recoilY > 0) {
                event.setYaw((float) (yaw - 1.3 * amplitude * (1 / 6.3 * (fireRotTimer - 0.5)) * Math.sin(6.3 * (fireRotTimer - 0.5)) * (3 - Math.pow(fireRotTimer, 2)) + 1 * Mth.clamp(0.3 - fireRotTimer, 0, 1) * (2 * Math.random() - 1)));
                event.setPitch((float) (pitch + 1.3 * amplitude * (1 / 6.3 * (fireRotTimer - 0.5)) * Math.sin(6.3 * (fireRotTimer - 0.5)) * (3 - Math.pow(fireRotTimer, 2)) + 1 * Mth.clamp(0.3 - fireRotTimer, 0, 1) * (2 * Math.random() - 1)));
                event.setRoll((float) (roll + 4.2 * amplitude * (1 / 6.3 * (fireRotTimer - 0.5)) * Math.sin(6.3 * (fireRotTimer - 0.5)) * (3 - Math.pow(fireRotTimer, 2)) + 3 * Mth.clamp(0.5 - fireRotTimer, 0, 0.5) * (2 * Math.random() - 1)));
            } else if (recoilY <= 0) {
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

    private static void handleWeaponShell() {
        Player player = Minecraft.getInstance().player;
        if (player == null) return;

        float times = Minecraft.getInstance().getDeltaFrameTime();

        shellIndex %= 5;

        for (int i = 0; i < 5; i++) {
            shellIndexTime[i] = Math.min(shellIndexTime[i] + 6 * times * ((50 - shellIndexTime[i]) / 50), 50);
        }
    }

    private static void handleGunRecoil() {
        Player player = Minecraft.getInstance().player;
        if (player == null) return;
        if (!player.getMainHandItem().is(ModTags.Items.GUN)) return;

        CompoundTag tag = player.getMainHandItem().getOrCreateTag();
        float times = Minecraft.getInstance().getDeltaFrameTime();
        float gunRecoilX = (float) tag.getDouble("recoil_x") * 80;
        float gunRecoilY = (float) tag.getDouble("recoil_y") * 60;

        if (fireRecoilTime > 0) {
            recoil = true;
        }

        if (recoil) {
            recoilTime = 0.01;
            recoilY = (float) (2 * Math.random() - 1) * gunRecoilY;
            recoil = false;
        }

        if (recoilHorizon > 0) {
            recoilHorizon -= (4 * Math.pow(recoilHorizon, 2) + recoilY) * times;
        } else {
            recoilHorizon += (4 * Math.pow(recoilHorizon, 2) + recoilY) * times;
        }
        recoilY = 0;

        // 计算后坐力
        float pose = 1;
        if (player.isShiftKeyDown() && player.getBbHeight() >= 1 && !isProne(player)) {
            pose = 0.7f;
        } else if (isProne(player)) {
            if (tag.getDouble("bipod") == 1) {
                pose = 0.1f;
            } else {
                pose = 0.5f;
            }
        }

        float newYaw = (float) (player.getYRot() - 0.6 * recoilHorizon * pose * times * (0.5 + fireSpread));
        player.setYRot(newYaw);
        player.yRotO = player.getYRot();

        double sinRes = 0;

        if (0 < recoilTime && recoilTime < 0.5) {
            float newPitch = player.getXRot() - 0.02f * gunRecoilX * times;
            player.setXRot(newPitch);
            player.xRotO = player.getXRot();
        }

        if (0 < recoilTime && recoilTime < 2) {
            recoilTime = recoilTime + 0.3 * times;
            sinRes = Math.sin(Math.PI * recoilTime);
        }

        if (2 <= recoilTime && recoilTime < 2.5) {
            recoilTime = recoilTime + 0.17 * times;
            sinRes = 0.4 * Math.sin(2 * Math.PI * recoilTime);
        }

        if (0 < recoilTime && recoilTime < 2.5) {
            float newPitch = (float) (player.getXRot() - 1.5 * pose * gunRecoilX * (sinRes + Mth.clamp(0.5 - recoilTime, 0, 0.5)) * times * (0.5 + fireSpread));
            player.setXRot(newPitch);
            player.xRotO = player.getXRot();
        }

        if (recoilTime >= 2.5) recoilTime = 0;
    }

    private static void handlePlayerBreath(LivingEntity entity) {
        float times = 4 * Minecraft.getInstance().getDeltaFrameTime();

        if ((entity.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables())).breath) {
            breathTime = Mth.clamp(breathTime + 0.06 * times, 0, 1);
        } else {
            breathTime = Mth.clamp(breathTime - 0.06 * times, 0, 1);
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
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            cameraRot[0] = boneRotX;
            cameraRot[1] = boneRotY;
            cameraRot[2] = boneRotZ;
        }
    }

    private static void handlePlayerCamera(ViewportEvent.ComputeCameraAngles event) {
        double yaw = event.getYaw();
        double pitch = event.getPitch();
        double roll = event.getRoll();
        LocalPlayer player = Minecraft.getInstance().player;

        if (GLFW.glfwGetKey(Minecraft.getInstance().getWindow().getWindow(), GLFW.GLFW_KEY_RIGHT) == GLFW.GLFW_PRESS) {
            cameraLocation = Mth.clamp(cameraLocation - 0.05 * Minecraft.getInstance().getDeltaFrameTime(), -0.6, 0.6);
        }

        if (GLFW.glfwGetKey(Minecraft.getInstance().getWindow().getWindow(), GLFW.GLFW_KEY_LEFT) == GLFW.GLFW_PRESS) {
            cameraLocation = Mth.clamp(cameraLocation + 0.05 * Minecraft.getInstance().getDeltaFrameTime(), -0.6, 0.6);
        }

        if (player == null) return;

        double range;
        Entity lookingEntity = SeekTool.seekEntity(player, player.level(), 520, 5);

        if (lookingEntity != null) {
            range = player.distanceTo(lookingEntity);
        } else {
            range = player.position().distanceTo((Vec3.atLowerCornerOf(player.level().clip(
                    new ClipContext(player.getEyePosition(), player.getEyePosition().add(player.getLookAngle().scale(520)),
                            ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, player)).getBlockPos())));
        }

        if (lookDistance < range) {
            lookDistance = Mth.clamp(lookDistance + 0.002 * Math.pow(range - lookDistance, 2) * Minecraft.getInstance().getDeltaFrameTime(), 0.01, 520);
        } else {
            lookDistance = Mth.clamp(lookDistance - 0.002 * Math.pow(range - lookDistance, 2) * Minecraft.getInstance().getDeltaFrameTime(), 0.01, 520);
        }

        double angle = 0;

        if (lookDistance != 0 && cameraLocation != 0) {
            angle = Math.atan(Mth.abs((float) cameraLocation) / (lookDistance + 2.9)) * Mth.RAD_TO_DEG;
        }

        if (player.getMainHandItem().is(ModTags.Items.GUN) || (player.getVehicle() != null && (player.getVehicle() instanceof ICannonEntity))) {
            event.setPitch((float) (pitch + cameraRot[0] + (DisplayConfig.CAMERA_ROTATE.get() ? 0.2 : 0) * turnRot[0] + 3 * velocityY));
            if (Minecraft.getInstance().options.getCameraType() == CameraType.THIRD_PERSON_BACK) {
                event.setYaw((float) (yaw + cameraRot[1] + (DisplayConfig.CAMERA_ROTATE.get() ? 0.8 : 0) * turnRot[1] - (cameraLocation > 0 ? 1 : -1) * angle * zoomPos));
            } else {
                event.setYaw((float) (yaw + cameraRot[1] + (DisplayConfig.CAMERA_ROTATE.get() ? 0.8 : 0) * turnRot[1]));
            }

            event.setRoll((float) (roll + cameraRot[2] + (DisplayConfig.CAMERA_ROTATE.get() ? 0.35 : 0) * turnRot[2]));
        }
    }

    private static void handleBowPullAnimation(LivingEntity entity) {
        float times = 4 * Minecraft.getInstance().getDeltaFrameTime();

        if ((entity.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables())).bowPull) {
            pullTimer = Math.min(pullTimer + 0.018 * times, 1);
            bowTimer = Math.min(bowTimer + 0.018 * times, 1);
            handTimer = Math.min(handTimer + 0.018 * times, 1);
            handPos = 0.5 * Math.cos(Math.PI * Math.pow(Math.pow(handTimer, 2) - 1, 2)) + 0.5;
        } else {
            pullTimer = Math.max(pullTimer - 0.009 * times, 0);
            bowTimer = Math.max(bowTimer - 1 * times, 0);
            handTimer = Math.max(handTimer - 0.04 * times, 0);
            if (handTimer > 0 && handTimer < 0.5) {
                handPos = 0.5 * Math.cos(Math.PI * Math.pow(Math.pow(handTimer, 2) - 1, 2)) + 0.5;
            }
        }
        pullPos = 0.5 * Math.cos(Math.PI * Math.pow(Math.pow(pullTimer, 2) - 1, 2)) + 0.5;
        bowPos = 0.5 * Math.cos(Math.PI * Math.pow(Math.pow(bowTimer, 2) - 1, 2)) + 0.5;
    }

    @SubscribeEvent
    public static void onFovUpdate(ViewportEvent.ComputeFov event) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player == null) {
            return;
        }

        ItemStack stack = player.getMainHandItem();

        if (stack.is(ModTags.Items.GUN)) {
            if (!event.usedConfiguredFov()) {
                return;
            }

            double p;
            if (stack.is(ModItems.BOCEK.get())) {
                p = (pullPos + 0.25) * zoomTime;
            } else {
                p = zoomPos;
            }

            double zoom = stack.getOrCreateTag().getDouble("zoom") + stack.getOrCreateTag().getDouble("custom_zoom");

            if (mc.options.getCameraType().isFirstPerson()) {
                event.setFOV(event.getFOV() / (1.0 + p * (zoom - 1)) * (1 - 0.4 * breathTime));
            } else if (mc.options.getCameraType() == CameraType.THIRD_PERSON_BACK)
                event.setFOV(event.getFOV() / (1.0 + p * 0.01) * (1 - 0.4 * breathTime));
            fov = event.getFOV();
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

    @SubscribeEvent
    public static void handleChangeSlot(LivingEquipmentChangeEvent event) {
        if (event.getEntity() instanceof Player && event.getSlot() == EquipmentSlot.MAINHAND) {
            ItemStack oldStack = event.getFrom();
            ItemStack newStack = event.getTo();

            if (newStack.getItem() != oldStack.getItem()
                    || newStack.getTag() == null || oldStack.getTag() == null
                    || !newStack.getTag().hasUUID("gun_uuid") || !oldStack.getTag().hasUUID("gun_uuid")
                    || !newStack.getTag().getUUID("gun_uuid").equals(oldStack.getTag().getUUID("gun_uuid"))
            ) {
                if (newStack.getItem() instanceof GunItem) {
                    drawTime = 1;
                }
            }
        }
    }

    private static void handleWeaponDraw(LivingEntity entity) {
        float times = Minecraft.getInstance().getDeltaFrameTime();
        ItemStack stack = entity.getMainHandItem();
        double weight = stack.getOrCreateTag().getDouble("weight");
        double speed = 1;

        if (weight == 0) {
            speed = 3;
        } else if (weight == 1) {
            speed = 2;
        } else if (weight == 2) {
            speed = 1.2;
        }

        drawTime = Math.max(drawTime - Math.max(0.2 * speed * times * drawTime, 0.0008), 0);
    }

    public static void handleShells(float x, float y, CoreGeoBone... shells) {
        for (int i = 0; i < shells.length; i++) {
            if (i >= 5) break;

            shells[i].setPosX((float) (-x * shellIndexTime[i]));
            shells[i].setPosY((float) (randomShell[0] * y * Math.sin(0.15 * shellIndexTime[i])));
            shells[i].setRotX((float) (randomShell[1] * shellIndexTime[i]));
            shells[i].setRotY((float) (randomShell[2] * shellIndexTime[i]));
        }
    }
}
