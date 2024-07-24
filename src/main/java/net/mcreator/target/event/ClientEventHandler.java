package net.mcreator.target.event;

import net.mcreator.target.entity.DroneEntity;
import net.mcreator.target.entity.Mk42Entity;
import net.mcreator.target.init.TargetModAttributes;
import net.mcreator.target.init.TargetModItems;
import net.mcreator.target.init.TargetModMobEffects;
import net.mcreator.target.init.TargetModTags;
import net.mcreator.target.network.TargetModVariables;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.nbt.CompoundTag;
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

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientEventHandler {

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
        data.putDouble("xRot", Mth.clamp(0.05 * xRot, -5, 5) * (1 - 0.75 * data.getDouble("zoom_time")));
        data.putDouble("yRot", Mth.clamp(0.05 * yRot, -10, 10) * (1 - 0.75 * data.getDouble("zoom_time")));
        data.putDouble("zRot", Mth.clamp(0.1 * yRot, -10, 10) * (1 - data.getDouble("zoom_time")));

        data.putDouble("Cannon_xRot", Mth.clamp(0.2 * xRot, -3, 3));
        data.putDouble("Cannon_yRot", Mth.clamp(1 * yRot, -15, 15));
    }

    @SubscribeEvent
    public static void computeCameraAngles(ViewportEvent.ComputeCameraAngles event) {
        ClientLevel level = Minecraft.getInstance().level;
        Entity entity = event.getCamera().getEntity();
        if (level != null && entity instanceof LivingEntity living && entity.isPassenger() && entity.getVehicle() instanceof Mk42Entity) {
            handleCannonCamera(event, living);
        }
        if (level != null && entity instanceof LivingEntity living && living.getMainHandItem().is(TargetModTags.Items.GUN)) {
            handleWeaponCrossHair(living);
            handleWeaponSway(living);
            handleWeaponMove(living);
            handleWeaponZoom(living);
            handleWeaponFire(event, living);
            handleShockCamera(event, living);
            handlePlayerCameraShake(event, living);
            handleBowPullAnimation(living);
        }
    }

    private static void handleCannonCamera(ViewportEvent.ComputeCameraAngles event, LivingEntity entity) {
        var data = entity.getPersistentData();
        double yaw = event.getYaw();
        double pitch = event.getPitch();
        double roll = event.getRoll();

        event.setPitch((float) (pitch + 1 * data.getDouble("Cannon_xRot") + data.getDouble("cannon_camera_rot_x")));
        event.setYaw((float) (yaw + 1 * data.getDouble("Cannon_yRot") + data.getDouble("cannon_camera_rot_y")));
        event.setRoll((float) (roll + data.getDouble("cannon_camera_rot_z")));

    }

    @SubscribeEvent
    public static void onRenderHand(RenderHandEvent event) {
        Player player = Minecraft.getInstance().player;
        if (player == null) return;

        InteractionHand hand = Minecraft.getInstance().options.mainHand().get() == HumanoidArm.RIGHT ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
        ItemStack stack = player.getMainHandItem();

        if (event.getHand() == hand) {
            if (player.getUseItem().is(TargetModTags.Items.GUN)) {
                event.setCanceled(true);
            }
        }

        if (stack.is(TargetModItems.MONITOR.get()) && stack.getOrCreateTag().getBoolean("Using") && stack.getOrCreateTag().getBoolean("Linked")) {

            DroneEntity drone = player.level().getEntitiesOfClass(DroneEntity.class, player.getBoundingBox().inflate(512))
                    .stream().filter(e -> e.getStringUUID().equals(stack.getOrCreateTag().getString("LinkedDrone"))).findFirst().orElse(null);

            if (drone != null) {
                event.setCanceled(true);
            }
        }
    }

    private static void handleWeaponCrossHair(LivingEntity entity) {
        if (entity.getMainHandItem().is(TargetModTags.Items.GUN)) {
            float fps = Minecraft.getInstance().getFps();
            if (fps <= 30) {
                fps = 30f;
            }
            float times = 90f / fps;
            var data = entity.getPersistentData();
            double spread = entity.getAttributeBaseValue(TargetModAttributes.SPREAD.get());

            if (data.getDouble("crosshair") > spread) {
                data.putDouble("crosshair", data.getDouble("crosshair") - 0.05 * Math.pow(spread - data.getDouble("crosshair"), 2) * times);
            } else {
                data.putDouble("crosshair", data.getDouble("crosshair") + 0.05 * Math.pow(spread - data.getDouble("crosshair"), 2) * times);
            }
        }
    }

    private static void handleWeaponSway(LivingEntity entity) {
        if (entity.getMainHandItem().is(TargetModTags.Items.GUN)) {
            float fps = Minecraft.getInstance().getFps();
            if (fps <= 30) {
                fps = 30f;
            }
            float times = 90f / fps;
            double pose;
            var data = entity.getPersistentData();

            if (entity.isShiftKeyDown() && entity.getBbHeight() >= 1 && data.getDouble("prone") == 0) {
                pose = 0.85;
            } else if (data.getDouble("prone") > 0) {
                if (entity.getMainHandItem().getOrCreateTag().getDouble("bipod") == 1) {
                    pose = 0;
                } else {
                    pose = 0.25;
                }
            } else {
                pose = 1;
            }

            data.putDouble("sway_time", data.getDouble("sway_time") + 0.015 * times);
            data.putDouble("x", (pose * -0.008 * Math.sin(data.getDouble("sway_time")) * (1 - 0.95 * data.getDouble("zoom_time"))));
            data.putDouble("y", (pose * 0.125 * Math.sin(data.getDouble("sway_time") - 1.585) * (1 - 0.95 * data.getDouble("zoom_time"))) - 3 * data.getDouble("gun_move_rotZ"));
        }
    }

    private static void handleWeaponMove(LivingEntity entity) {
        if (entity.getMainHandItem().is(TargetModTags.Items.GUN)) {
            float fps = Minecraft.getInstance().getFps();
            if (fps <= 30) {
                fps = 30f;
            }

            float times = 90f / fps;
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

            if (data.getDouble("move_forward") == 1 && data.getDouble("firetime") == 0 && data.getDouble("zoom_time") == 0) {
                if (data.getDouble("gun_move_rotZ") < 0.14) {
                    data.putDouble("gun_move_rotZ", data.getDouble("gun_move_rotZ") + 0.007 * times);
                }
            } else {
                if (data.getDouble("gun_move_rotZ") > 0) {
                    data.putDouble("gun_move_rotZ", data.getDouble("gun_move_rotZ") - 0.007 * times);
                } else {
                    data.putDouble("gun_move_rotZ", 0);
                }
            }

            if ((data.getDouble("move_left") == 1
                    || data.getDouble("move_right") == 1
                    || data.getDouble("move_forward") == 1
                    || data.getDouble("move_backward") == 1) && data.getDouble("firetime") == 0) {

                if (data.getDouble("gun_moveY_time") < 1.25) {
                    data.putDouble("gun_moveY_time", data.getDouble("gun_moveY_time") + 1.2 * on_ground * times * move_speed);
                } else {
                    data.putDouble("gun_moveY_time", 0.25);
                }

                if (data.getDouble("gun_moveX_time") < 2) {
                    data.putDouble("gun_moveX_time", data.getDouble("gun_moveX_time") + 1.2 * on_ground * times * move_speed);
                } else {
                    data.putDouble("gun_moveX_time", 0);
                }

                data.putDouble("gun_move_posY", -0.135 * Math.sin(2 * Math.PI * (data.getDouble("gun_moveY_time") - 0.25)) * (1 - 0.95 * data.getDouble("zoom_time")));

                data.putDouble("gun_move_posX", 0.2 * Math.sin(1 * Math.PI * data.getDouble("gun_moveX_time")) * (1 - 0.95 * data.getDouble("zoom_time")));

            } else {
                if (data.getDouble("gun_moveY_time") > 0.25) {
                    data.putDouble("gun_moveY_time", data.getDouble("gun_moveY_time") - 0.5 * times);
                } else {
                    data.putDouble("gun_moveY_time", 0.25);
                }

                if (data.getDouble("gun_moveX_time") > 0) {
                    data.putDouble("gun_moveX_time", data.getDouble("gun_moveX_time") - 0.5 * times);
                } else {
                    data.putDouble("gun_moveX_time", 0);
                }

                if (data.getDouble("gun_move_posX") > 0) {
                    data.putDouble("gun_move_posX", data.getDouble("gun_move_posX") - 1.5 * (Math.pow(data.getDouble("gun_move_posX"), 2) * times) * (1 - 0.75 * data.getDouble("zoom_time")));
                } else {
                    data.putDouble("gun_move_posX", data.getDouble("gun_move_posX") + 1.5 * (Math.pow(data.getDouble("gun_move_posX"), 2) * times) * (1 - 0.75 * data.getDouble("zoom_time")));
                }

                if (data.getDouble("gun_move_posY") > 0) {
                    data.putDouble("gun_move_posY", data.getDouble("gun_move_posY") - 1.5 * (Math.pow(data.getDouble("gun_move_posY"), 2) * times) * (1 - 0.75 * data.getDouble("zoom_time")));
                } else {
                    data.putDouble("gun_move_posY", data.getDouble("gun_move_posY") + 1.5 * (Math.pow(data.getDouble("gun_move_posY"), 2) * times) * (1 - 0.75 * data.getDouble("zoom_time")));
                }

            }

            if (data.getDouble("move") < 0) {
                data.putDouble("move", ((data.getDouble("move") + 1 * times * Math.pow(data.getDouble("move"), 2) * (1 - 0.6 * data.getDouble("zoom_time")))
                        * (1 - 1 * data.getDouble("zoom_time"))));
            } else {
                data.putDouble("move", ((data.getDouble("move") - 1 * times * Math.pow(data.getDouble("move"), 2) * (1 - 0.6 * data.getDouble("zoom_time")))
                        * (1 - 1 * data.getDouble("zoom_time"))));
            }
            if (data.getDouble("move_right") == 1) {
                data.putDouble("move",
                        ((data.getDouble("move") + Math.pow(Math.abs(data.getDouble("move")) + 0.05, 2) * 0.2 * times * (1 - 0.1 * data.getDouble("zoom_time")))
                                * (1 - 0.1 * data.getDouble("zoom_time"))));
            } else if (data.getDouble("move_left") == 1) {
                data.putDouble("move",
                        ((data.getDouble("move") - Math.pow(Math.abs(data.getDouble("move")) + 0.05, 2) * 0.2 * times * (1 - 0.1 * data.getDouble("zoom_time")))
                                * (1 - 0.1 * data.getDouble("zoom_time"))));
            }

            double velocity = entity.getDeltaMovement().y();

            if (-0.8 < velocity + 0.078 && velocity + 0.078 < 0.8) {
                if (data.getDouble("vy") < entity.getDeltaMovement().y() + 0.078) {
                    data.putDouble("vy", Mth.clamp(((data.getDouble("vy") + 0.35 * Math.pow((velocity + 0.078) - data.getDouble("vy"), 2)) * (1 - 0.8 * data.getDouble("zoom_time"))), -0.8, 0.8));
                } else {
                    data.putDouble("vy", Mth.clamp(((data.getDouble("vy") - 0.35 * Math.pow((velocity + 0.078) - data.getDouble("vy"), 2)) * (1 - 0.8 * data.getDouble("zoom_time"))), -0.8, 0.8));
                }
            }
        }
    }

    private static void handleWeaponZoom(LivingEntity entity) {
        float fps = Minecraft.getInstance().getFps();
        if (fps <= 0) {
            fps = 1f;
        }
        float times = 110f / fps;
        var data = entity.getPersistentData();

        if ((entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).zooming) {
            if (data.getDouble("zoom_time") < 1) {
                data.putDouble("zoom_time",
                        (data.getDouble("zoom_time") + entity.getMainHandItem().getOrCreateTag().getDouble("zoom_speed") * 0.03 * times));
            } else {
                data.putDouble("zoom_time", 1);
            }
        } else {
            if (data.getDouble("zoom_time") > 0) {
                data.putDouble("zoom_time", (data.getDouble("zoom_time") - 0.04 * times));
            } else {
                data.putDouble("zoom_time", 0);
            }
        }
        data.putDouble("zoom_pos", (0.5 * Math.cos(Math.PI * Math.pow(Math.pow(data.getDouble("zoom_time"), 2) - 1, 2)) + 0.5));
        data.putDouble("zoom_pos_z", (-Math.pow(2 * data.getDouble("zoom_time") - 1, 2) + 1));
    }

    private static void handleWeaponFire(ViewportEvent.ComputeCameraAngles event, LivingEntity entity) {
        double yaw = event.getYaw();
        double pitch = event.getPitch();
        double roll = event.getRoll();

        double amplitude;
        float fps = Minecraft.getInstance().getFps();
        if (fps <= 0) {
            fps = 1f;
        }

        ItemStack stack = entity.getMainHandItem();

        float times = 45f / fps;
        amplitude = 15000 * stack.getOrCreateTag().getDouble("recoil_y")
                * stack.getOrCreateTag().getDouble("recoil_x");
        var data = entity.getPersistentData();

        var capability = entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null);
        if (capability.orElse(new TargetModVariables.PlayerVariables()).firing > 0) {
            data.putDouble("firetime", 0.001);
            data.putDouble("fire_rotx_time", 0.001);
            data.putDouble("firepos2", 0.1);
        }

        data.putDouble("firepos2", Mth.clamp(data.getDouble("firepos2") - 0.01 * times, 0, 0.6));

        if (0 < data.getDouble("firetime")) {
            data.putDouble("firetime", (data.getDouble("firetime") + 0.25 * (1.1 - data.getDouble("firetime")) * times));
        }
        if (0 < data.getDouble("firetime") && data.getDouble("firetime") < 0.454) {
            data.putDouble("fire_pos",
                    ((-18.34) * Math.pow(data.getDouble("firetime"), 2) + 8.58 * data.getDouble("firetime") + data.getDouble("firepos2")));
        }
        if (0.454 <= data.getDouble("firetime") && data.getDouble("firetime") < 1) {
            data.putDouble("fire_pos",
                    (4.34 * Math.pow(data.getDouble("firetime"), 2) - 6.5 * data.getDouble("firetime") + 2.167 + data.getDouble("firepos2")));
        }


        if (0 < data.getDouble("fire_rotx_time") && data.getDouble("fire_rotx_time") < 1.732) {
            data.putDouble("fire_rotx_time", (data.getDouble("fire_rotx_time") + 0.18 * (1.9 - data.getDouble("fire_rotx_time")) * times));
        }

        if (0 < data.getDouble("fire_rotx_time") && data.getDouble("fire_rotx_time") < 1.732) {
            data.putDouble("fire_rot",
                    (1 / 6.3 * (data.getDouble("fire_rotx_time") - 0.5)) * Math.sin(6.3 * (data.getDouble("fire_rotx_time") - 0.5)) * (3 - Math.pow(data.getDouble("fire_rotx_time"), 2)));
            if ((capability.orElse(new TargetModVariables.PlayerVariables())).recoilHorizon > 0) {
                event.setYaw((float) (yaw - 1.3 * amplitude * (1 / 6.3 * (data.getDouble("fire_rotx_time") - 0.5)) * Math.sin(6.3 * (data.getDouble("fire_rotx_time") - 0.5)) * (3 - Math.pow(data.getDouble("fire_rotx_time"), 2)) + 1 * Mth.clamp(0.3 - data.getDouble("fire_rotx_time"), 0, 1) * (2 * Math.random() - 1)));
                event.setPitch((float) (pitch + 1.3 * amplitude * (1 / 6.3 * (data.getDouble("fire_rotx_time") - 0.5)) * Math.sin(6.3 * (data.getDouble("fire_rotx_time") - 0.5)) * (3 - Math.pow(data.getDouble("fire_rotx_time"), 2)) + 1 * Mth.clamp(0.3 - data.getDouble("fire_rotx_time"), 0, 1) * (2 * Math.random() - 1)));
                event.setRoll((float) (roll + 4.2 * amplitude * (1 / 6.3 * (data.getDouble("fire_rotx_time") - 0.5)) * Math.sin(6.3 * (data.getDouble("fire_rotx_time") - 0.5)) * (3 - Math.pow(data.getDouble("fire_rotx_time"), 2)) + 3 * Mth.clamp(0.5 - data.getDouble("fire_rotx_time"), 0, 0.5) * (2 * Math.random() - 1)));
            } else if ((capability.orElse(new TargetModVariables.PlayerVariables())).recoilHorizon <= 0) {
                event.setYaw((float) (yaw + 1.3 * amplitude * (1 / 6.3 * (data.getDouble("fire_rotx_time") - 0.5)) * Math.sin(6.3 * (data.getDouble("fire_rotx_time") - 0.5)) * (3 - Math.pow(data.getDouble("fire_rotx_time"), 2)) + 1 * Mth.clamp(0.3 - data.getDouble("fire_rotx_time"), 0, 1) * (2 * Math.random() - 1)));
                event.setPitch((float) (pitch - 1.3 * amplitude * (1 / 6.3 * (data.getDouble("fire_rotx_time") - 0.5)) * Math.sin(6.3 * (data.getDouble("fire_rotx_time") - 0.5)) * (3 - Math.pow(data.getDouble("fire_rotx_time"), 2)) + 1 * Mth.clamp(0.3 - data.getDouble("fire_rotx_time"), 0, 1) * (2 * Math.random() - 1)));
                event.setRoll((float) (roll - 4.2 * amplitude * (1 / 6.3 * (data.getDouble("fire_rotx_time") - 0.5)) * Math.sin(6.3 * (data.getDouble("fire_rotx_time") - 0.5)) * (3 - Math.pow(data.getDouble("fire_rotx_time"), 2)) + 3 * Mth.clamp(0.5 - data.getDouble("fire_rotx_time"), 0, 0.5) * (2 * Math.random() - 1)));
            }
        }
        if (0 <= data.getDouble("firetime") && data.getDouble("firetime") <= 0.25) {
            data.putDouble("boltpos", (-Math.pow(8 * data.getDouble("firetime") - 1, 2) + 1));
        }
        if (0.25 < data.getDouble("firetime") && data.getDouble("firetime") < 1) {
            data.putDouble("boltpos", 0);
        }
        if (data.getDouble("firetime") >= 1) {
            data.putDouble("firetime", 0);
        }
        if (data.getDouble("fire_rotx_time") >= 1.732) {
            data.putDouble("fire_rotx_time", 0);
            data.putDouble("fire_rot", 0);
        }
    }

    private static void handleShockCamera(ViewportEvent.ComputeCameraAngles event, LivingEntity entity) {
        if (entity.hasEffect(TargetModMobEffects.SHOCK.get()) && Minecraft.getInstance().options.getCameraType() == CameraType.FIRST_PERSON) {
            event.setYaw(Minecraft.getInstance().gameRenderer.getMainCamera().getYRot() + (float) Mth.nextDouble(RandomSource.create(), -3, 3));
            event.setPitch(Minecraft.getInstance().gameRenderer.getMainCamera().getXRot() + (float) Mth.nextDouble(RandomSource.create(), -3, 3));
            event.setRoll((float) Mth.nextDouble(RandomSource.create(), 8, 12));
        }
    }

    private static void handlePlayerCameraShake(ViewportEvent.ComputeCameraAngles event, LivingEntity entity) {
        var data = entity.getPersistentData();
        double yaw = event.getYaw();
        double pitch = event.getPitch();
        double roll = event.getRoll();
        if (entity.getMainHandItem().is(TargetModTags.Items.GUN)) {

            event.setPitch((float) (pitch + data.getDouble("camera_rot_x") + 0.2 * data.getDouble("xRot") + 3 * data.getDouble("vy")));

            event.setYaw((float) (yaw + data.getDouble("camera_rot_y") + 0.8 * data.getDouble("yRot")));

            event.setRoll((float) (roll + data.getDouble("camera_rot_z") + 0.35 * data.getDouble("zRot")));
        }
    }

    private static void handleBowPullAnimation(LivingEntity entity) {
        float fps = Minecraft.getInstance().getFps();
        if (fps <= 0) {
            fps = 1f;
        }

        float times = 90f / fps;
        CompoundTag persistentData = entity.getPersistentData();

        if ((entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).bowPull) {
            persistentData.putDouble("pulltime", Math.min(persistentData.getDouble("pulltime") + 0.014 * times, 1));
            persistentData.putDouble("bowtime", Math.min(persistentData.getDouble("bowtime") + 0.014 * times, 1));
            persistentData.putDouble("handtime", Math.min(persistentData.getDouble("handtime") + 0.014 * times, 1));
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

        if (stack.is(TargetModTags.Items.GUN)) {
            if (!event.usedConfiguredFov()) {
                return;
            }

            double p = player.getPersistentData().getDouble("zoom_pos");
            double zoom = stack.getOrCreateTag().getDouble("zoom") + stack.getOrCreateTag().getDouble("custom_zoom");

            event.setFOV(event.getFOV() / (1.0 + p * (zoom - 1)));
            player.getPersistentData().putDouble("fov", event.getFOV());
            return;
        }
        if (player.isPassenger() && player.getVehicle() instanceof Mk42Entity) {
            if ((player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).zoom) {
                event.setFOV(event.getFOV() / 5);
            }
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

        if (mc.player.getMainHandItem().is(TargetModTags.Items.GUN) || (mc.player.getVehicle() != null && mc.player.getVehicle() instanceof Mk42Entity)) {
            event.setCanceled(true);
        }

        ItemStack stack = mc.player.getMainHandItem();
        if (stack.is(TargetModItems.MONITOR.get()) && stack.getOrCreateTag().getBoolean("Using") && stack.getOrCreateTag().getBoolean("Linked")) {
            event.setCanceled(true);
        }
    }
}


