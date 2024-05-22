package net.mcreator.target.event;

import net.mcreator.target.init.TargetModMobEffects;
import net.mcreator.target.init.TargetModTags;
import net.mcreator.target.network.TargetModVariables;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientEventHandler {

    @SubscribeEvent
    public static void computeCameraAngles(ViewportEvent.ComputeCameraAngles event) {
        ClientLevel level = Minecraft.getInstance().level;
        Entity entity = event.getCamera().getEntity();
        if (level != null && entity instanceof LivingEntity living) {
            handleWeaponSway(living);
            handleWeaponMove(living);
            handleWeaponZoom(living);
            handleWeaponFire(event, living);
            handleShockCamera(event, living);
            PlayerCameraShake(event, living);
            handleBowPullAnimation(living);
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
            data.putDouble("x", (pose * -0.008 * Math.sin(data.getDouble("sway_time")) * (1 - 0.9 * data.getDouble("zoom_time"))));
            data.putDouble("y", (pose * 0.125 * Math.sin(data.getDouble("sway_time") - 1.585) * (1 - 0.9 * data.getDouble("zoom_time"))));
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
            double move_speed = (float) entity.getDeltaMovement().horizontalDistanceSqr();
            double on_ground;
            if (entity.onGround()) {
                if (entity.isSprinting()) {
                    on_ground = 1.2;
                } else {
                    on_ground = 2.4;
                }
            } else {
                on_ground = 0.001;
            }

            if ((data.getDouble("move_left") == 1
                    || data.getDouble("move_right") == 1
                    || data.getDouble("move_forward") == 1
                    || data.getDouble("move_backward") == 1) && data.getDouble("firetime") == 0) {

                if (data.getDouble("gun_moveY_time") < 1.25) {
                    data.putDouble("gun_moveY_time", data.getDouble("gun_moveY_time") + on_ground * times * move_speed);
                } else {
                    data.putDouble("gun_moveY_time", 0.25);
                }

                if (data.getDouble("gun_moveX_time") < 2) {
                    data.putDouble("gun_moveX_time", data.getDouble("gun_moveX_time") + on_ground * times * move_speed);
                } else {
                    data.putDouble("gun_moveX_time", 0);
                }

                data.putDouble("gun_move_posY", -0.135 * Math.sin(2 * Math.PI * (data.getDouble("gun_moveY_time") - 0.25)) * (1 - 0.75 * data.getDouble("zoom_time")));

                data.putDouble("gun_move_posX", 0.2 * Math.sin(1 * Math.PI * data.getDouble("gun_moveX_time")) * (1 - 0.75 * data.getDouble("zoom_time")));

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
                    data.putDouble("gun_move_posX", data.getDouble("gun_move_posX") - 1.5 * (Math.pow(data.getDouble("gun_move_posX"), 2) * times) * (1 - 0.5 * data.getDouble("zoom_time")));
                } else {
                    data.putDouble("gun_move_posX", data.getDouble("gun_move_posX") + 1.5 * (Math.pow(data.getDouble("gun_move_posX"), 2) * times) * (1 - 0.5 * data.getDouble("zoom_time")));
                }

                if (data.getDouble("gun_move_posY") > 0) {
                    data.putDouble("gun_move_posY", data.getDouble("gun_move_posY") - 1.5 * (Math.pow(data.getDouble("gun_move_posY"), 2) * times) * (1 - 0.5 * data.getDouble("zoom_time")));
                } else {
                    data.putDouble("gun_move_posY", data.getDouble("gun_move_posY") + 1.5 * (Math.pow(data.getDouble("gun_move_posY"), 2) * times) * (1 - 0.5 * data.getDouble("zoom_time")));
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
            if (data.getDouble("turnr") == 1) {
                data.putDouble("turntimeyaw", (data.getDouble("turntimeyaw") + 0.08 * times * Math.pow(data.getDouble("amplitudeyaw"), 2)));
            }
            if (data.getDouble("turnl") == 1) {
                data.putDouble("turntimeyaw", (data.getDouble("turntimeyaw") - 0.08 * times * Math.pow(data.getDouble("amplitudeyaw"), 2)));
            }
            if (data.getDouble("turntimeyaw") > 1) {
                data.putDouble("turntimeyaw", 1);
            }
            if (data.getDouble("turntimeyaw") < -1) {
                data.putDouble("turntimeyaw", (-1));
            }
            if (data.getDouble("turntimeyaw") >= 0) {
                if (data.getDouble("turnr") == 0) {
                    data.putDouble("turntimeyaw", (data.getDouble("turntimeyaw") - 0.02 * times));
                }
            }
            if (data.getDouble("turntimeyaw") < 0) {
                if (data.getDouble("turnl") == 0) {
                    data.putDouble("turntimeyaw", (data.getDouble("turntimeyaw") + 0.02 * times));
                }
            }
            if (data.getDouble("amplitudeyaw") < Math.abs(data.getDouble("r1") - data.getDouble("r2"))) {
                data.putDouble("amplitudeyaw", (data.getDouble("amplitudeyaw")
                        + 0.005 * Math.sin(0.5 * Math.PI * (Math.abs(data.getDouble("r1") - data.getDouble("r2")) - data.getDouble("amplitudeyaw")))));
            } else {
                data.putDouble("amplitudeyaw", (data.getDouble("amplitudeyaw")
                        - 0.005 * Math.sin(0.5 * Math.PI * (Math.abs(data.getDouble("r1") - data.getDouble("r2")) - data.getDouble("amplitudeyaw")))));
            }
            if (data.getDouble("amplitudeyaw") > 0) {
                data.putDouble("amplitudeyaw", (data.getDouble("amplitudeyaw") - 0.01 * Math.pow(data.getDouble("amplitudeyaw"), 2)));
            } else {
                data.putDouble("amplitudeyaw", (data.getDouble("amplitudeyaw") + 0.01 * Math.pow(data.getDouble("amplitudeyaw"), 2)));
            }
            data.putDouble("yaw", (0.04 * Math.tan(0.25 * Math.PI * data.getDouble("turntimeyaw")) * (1 - 1 * data.getDouble("zoom_time"))));
            if (data.getDouble("turnu") == 1) {
                data.putDouble("turntimepitch", (data.getDouble("turntimepitch") + 0.02 * times));
            }
            if (data.getDouble("turnd") == 1) {
                data.putDouble("turntimepitch", (data.getDouble("turntimepitch") - 0.02 * times));
            }
            if (data.getDouble("turntimepitch") > 1) {
                data.putDouble("turntimepitch", 1);
            }
            if (data.getDouble("turntimepitch") < -1) {
                data.putDouble("turntimepitch", (-1));
            }
            if (data.getDouble("turntimepitch") >= 0) {
                if (data.getDouble("turnu") == 0) {
                    data.putDouble("turntimepitch", (data.getDouble("turntimepitch") - 0.04 * times));
                }
            }
            if (data.getDouble("turntimepitch") < 0) {
                if (data.getDouble("turnd") == 0) {
                    data.putDouble("turntimepitch", (data.getDouble("turntimepitch") + 0.04 * times));
                }
            }
            if (data.getDouble("amplitudepitch") < Math.abs(data.getDouble("p1") - data.getDouble("p2"))) {
                data.putDouble("amplitudepitch", (data.getDouble("amplitudepitch")
                        + 0.00001 * Math.pow(Math.abs(data.getDouble("p1") - data.getDouble("p2")) - data.getDouble("amplitudepitch"), 2)));
            } else {
                data.putDouble("amplitudepitch", (data.getDouble("amplitudepitch")
                        - 0.00001 * Math.pow(Math.abs(data.getDouble("p1") - data.getDouble("p2")) - data.getDouble("amplitudepitch"), 2)));
            }
            if (data.getDouble("amplitudepitch") > 0) {
                data.putDouble("amplitudepitch", (data.getDouble("amplitudepitch") - 0.01 * Math.pow(data.getDouble("amplitudepitch"), 2)));
            } else {
                data.putDouble("amplitudepitch", (data.getDouble("amplitudepitch") + 0.01 * Math.pow(data.getDouble("amplitudepitch"), 2)));
            }
            data.putDouble("gun_pitch",
                    ((0.15 * data.getDouble("amplitudepitch") * Math.tan(0.25 * Math.PI * data.getDouble("turntimepitch")) * (1 - 0.8 * data.getDouble("zoom_time"))
                            - 0.05 * data.getDouble("vy")) * (1 - 1 * data.getDouble("zoom_time"))));
            if (data.getDouble("firetime") == 0) {
                data.putDouble("rottime", (data.getDouble("rottime") + 1));
                if (data.getDouble("rottime") >= 3) {
                    data.putDouble("rottime", 0);
                }
                if (data.getDouble("rottime") == 1) {
                    data.putDouble("r1", (entity.getYRot()));
                    data.putDouble("p1", (entity.getXRot()));
                }
                if (data.getDouble("rottime") == 2) {
                    data.putDouble("r2", (entity.getYRot()));
                    data.putDouble("p2", (entity.getXRot()));
                }
                if (0 > data.getDouble("r1") - data.getDouble("r2")) {
                    data.putDouble("rot", (data.getDouble("rot") - 0.01));
                } else if (0 < data.getDouble("r1") - data.getDouble("r2")) {
                    data.putDouble("rot", (data.getDouble("rot") + 0.01));
                } else if (0 == data.getDouble("r1") - data.getDouble("r2")) {
                    data.putDouble("rot", 0);
                }
                if (0 > data.getDouble("p1") - data.getDouble("p2")) {
                    data.putDouble("pit", (data.getDouble("pit") - 0.01));
                } else if (0 < data.getDouble("p1") - data.getDouble("p2")) {
                    data.putDouble("pit", (data.getDouble("pit") + 0.01));
                } else if (0 == data.getDouble("p1") - data.getDouble("p2")) {
                    data.putDouble("pit", 0);
                }
                if (data.getDouble("rot") < 0) {
                    data.putDouble("rot", (data.getDouble("rot") + 2 * times * Math.pow(data.getDouble("rot"), 2)));
                    if (data.getDouble("rot") < -0.04) {
                        data.putDouble("turnr", 1);
                        data.putDouble("turnl", 0);
                    }
                } else if (data.getDouble("rot") > 0) {
                    data.putDouble("rot", (data.getDouble("rot") - 2 * times * Math.pow(data.getDouble("rot"), 2)));
                    if (data.getDouble("rot") > 0.04) {
                        data.putDouble("turnl", 1);
                        data.putDouble("turnr", 0);
                    }
                } else {
                    data.putDouble("rot", 0);
                    data.putDouble("turnl", 0);
                    data.putDouble("turnr", 0);
                }
                if (data.getDouble("pit") < 0) {
                    data.putDouble("pit", (data.getDouble("pit") + 2 * times * Math.pow(data.getDouble("pit"), 2)));
                    if (data.getDouble("pit") < -0.034) {
                        data.putDouble("turnu", 1);
                        data.putDouble("turnd", 0);
                    }
                } else if (data.getDouble("pit") > 0) {
                    data.putDouble("pit", (data.getDouble("pit") - 2 * times * Math.pow(data.getDouble("pit"), 2)));
                    if (data.getDouble("pit") > 0.034) {
                        data.putDouble("turnd", 1);
                        data.putDouble("turnu", 0);
                    }
                } else {
                    data.putDouble("pit", 0);
                    data.putDouble("turnd", 0);
                    data.putDouble("turnu", 0);
                }
            } else {
                data.putDouble("pit", 0);
                data.putDouble("turnl", 0);
                data.putDouble("turnr", 0);
                data.putDouble("turnd", 0);
                data.putDouble("turnu", 0);
            }
            double velocity = entity.getDeltaMovement().y();

            if (-0.8 < velocity + 0.078 && velocity + 0.078 < 0.8) {
                if (data.getDouble("vy") < entity.getDeltaMovement().y() + 0.078) {
                    data.putDouble("vy",
                            ((data.getDouble("vy") + 0.5 * Math.pow((velocity + 0.078) - data.getDouble("vy"), 2)) * (1 - 0.4 * data.getDouble("zoom_time"))));
                } else {
                    data.putDouble("vy",
                            ((data.getDouble("vy") - 0.5 * Math.pow((velocity + 0.078) - data.getDouble("vy"), 2)) * (1 - 0.4 * data.getDouble("zoom_time"))));
                }
            }
            if (data.getDouble("vy") > 0.8) {
                data.putDouble("vy", 0.8);
            }
            if (data.getDouble("vy") < -0.8) {
                data.putDouble("vy", (-0.8));
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
                        (data.getDouble("zoom_time") + entity.getMainHandItem().getOrCreateTag().getDouble("zoom_speed") * 0.02 * times));
            } else {
                data.putDouble("zoom_time", 1);
            }
        } else {
            if (data.getDouble("zoom_time") > 0) {
                data.putDouble("zoom_time", (data.getDouble("zoom_time") - 0.02 * times));
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

        double pose;
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
        if (entity.isShiftKeyDown() && entity.getBbHeight() >= 1 && data.getDouble("prone") == 0) {
            pose = 0.9;
        } else if (data.getDouble("prone") > 0) {
            if (stack.getOrCreateTag().getDouble("bipod") == 1) {
                pose = 0.75;
            } else {
                pose = 0.8;
            }
        } else {
            pose = 1;
        }

        var capability = entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null);
        if (capability.orElse(new TargetModVariables.PlayerVariables()).firing > 0) {
            data.putDouble("firetime", 0.2);
            if (0.3 > data.getDouble("firepos2")) {
                data.putDouble("firepos2", (data.getDouble("firepos2") + 0.04 * times));
            }
        }
        if (0 < data.getDouble("firepos2")) {
            data.putDouble("firepos2", (data.getDouble("firepos2") - 0.02 * times));
        } else {
            data.putDouble("firepos2", 0);
        }
        if (0 < data.getDouble("firetime")) {
            data.putDouble("firetime", (data.getDouble("firetime") + 0.075 * times));
        }
        if (0 < data.getDouble("firetime") && data.getDouble("firetime") < 0.2) {
            data.putDouble("fire_pos",
                    (pose * ((-18.34) * Math.pow(data.getDouble("firetime"), 2) + 8.58 * data.getDouble("firetime") + data.getDouble("firepos2"))));
            if ((capability.orElse(new TargetModVariables.PlayerVariables())).recoilHorizon == 1) {
                event.setYaw((float) (yaw - 0.2 * amplitude * ((-18.34) * Math.pow(data.getDouble("firetime"), 2) + 8.58 * data.getDouble("firetime") + 0.7 * (2 * Math.random() - 1))));
                event.setPitch((float) (pitch + 0.2 * amplitude * ((-18.34) * Math.pow(data.getDouble("firetime"), 2) + 8.58 * data.getDouble("firetime") + 0.7 * (2 * Math.random() - 1))));
                event.setRoll((float) (roll + amplitude * ((-18.34) * Math.pow(data.getDouble("firetime"), 2) + 8.58 * data.getDouble("firetime") + 0.7 * (2 * Math.random() - 1))));
            } else if ((capability.orElse(new TargetModVariables.PlayerVariables())).recoilHorizon == -1) {
                event.setYaw((float) (yaw - 0.2 * amplitude * ((-18.34) * Math.pow(data.getDouble("firetime"), 2) + 8.58 * data.getDouble("firetime") + 0.7 * (2 * Math.random() - 1))));
                event.setPitch((float) (pitch + 0.2 * amplitude * ((-18.34) * Math.pow(data.getDouble("firetime"), 2) + 8.58 * data.getDouble("firetime") + 0.7 * (2 * Math.random() - 1))));
                event.setRoll((float) (roll - amplitude * ((-18.34) * Math.pow(data.getDouble("firetime"), 2) + 8.58 * data.getDouble("firetime") + 0.7 * (2 * Math.random() - 1))));
            }
        }
        if (0.2 <= data.getDouble("firetime") && data.getDouble("firetime") < 1) {
            data.putDouble("fire_pos",
                    (pose * (3.34 * Math.pow(data.getDouble("firetime"), 2) - 5.5 * data.getDouble("firetime") + 2.167 + data.getDouble("firepos2"))));
            if ((capability.orElse(new TargetModVariables.PlayerVariables())).recoilHorizon == 1) {
                event.setYaw((float) (yaw - 0.2 * amplitude * (3.34 * Math.pow(data.getDouble("firetime"), 2) - 5.5 * data.getDouble("firetime") + 2.167 + 0.7 * (2 * Math.random() - 1))));
                event.setPitch((float) (pitch + 0.2 * amplitude * (3.34 * Math.pow(data.getDouble("firetime"), 2) - 5.5 * data.getDouble("firetime") + 2.167 + 0.7 * (2 * Math.random() - 1))));
                event.setRoll((float) (roll + amplitude * (3.34 * Math.pow(data.getDouble("firetime"), 2) - 5.5 * data.getDouble("firetime") + 2.167 + 0.7 * (2 * Math.random() - 1))));
            } else if ((capability.orElse(new TargetModVariables.PlayerVariables())).recoilHorizon == -1) {
                event.setYaw((float) (yaw + 0.2 * amplitude * (3.34 * Math.pow(data.getDouble("firetime"), 2) - 5.5 * data.getDouble("firetime") + 2.167 + 0.7 * (2 * Math.random() - 1))));
                event.setPitch((float) (pitch - 0.2 * amplitude * (3.34 * Math.pow(data.getDouble("firetime"), 2) - 5.5 * data.getDouble("firetime") + 2.167 + 0.7 * (2 * Math.random() - 1))));
                event.setRoll((float) (roll - amplitude * (3.34 * Math.pow(data.getDouble("firetime"), 2) - 5.5 * data.getDouble("firetime") + 2.167 + 0.7 * (2 * Math.random() - 1))));
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
    }

    private static void handleShockCamera(ViewportEvent.ComputeCameraAngles event, LivingEntity entity) {
        if (entity.hasEffect(TargetModMobEffects.SHOCK.get()) && Minecraft.getInstance().options.getCameraType() == CameraType.FIRST_PERSON) {
            event.setYaw(Minecraft.getInstance().gameRenderer.getMainCamera().getYRot());
            event.setPitch(Minecraft.getInstance().gameRenderer.getMainCamera().getXRot());
            event.setRoll((float) Mth.nextDouble(RandomSource.create(), 8, 12));
        }
    }

    private static void PlayerCameraShake(ViewportEvent.ComputeCameraAngles event, LivingEntity entity) {
        var data = entity.getPersistentData();
        double yaw = event.getYaw();
        double pitch = event.getPitch();
        double roll = event.getRoll();

        event.setPitch((float) (pitch + data.getDouble("camera_rot_x")));

        event.setYaw((float) (yaw + data.getDouble("camera_rot_y")));

        event.setRoll((float) (roll + data.getDouble("camera_rot_z")));

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
        if (!event.usedConfiguredFov()) {
            return;
        }

        Player player = Minecraft.getInstance().player;
        if (player == null) {
            return;
        }

        ItemStack stack = player.getMainHandItem();

        double p = player.getPersistentData().getDouble("zoom_pos");
        double zoom = stack.getOrCreateTag().getDouble("zoom");

        if (stack.is(TargetModTags.Items.GUN)) {
            event.setFOV(event.getFOV() / (1.0 + p * (zoom - 1)));
            player.getPersistentData().putDouble("fov", event.getFOV());
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

        if (mc.player.getMainHandItem().is(TargetModTags.Items.GUN)) {
            event.setCanceled(true);
        }
    }
}


