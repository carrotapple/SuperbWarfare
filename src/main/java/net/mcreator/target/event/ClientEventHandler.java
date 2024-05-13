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
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class ClientEventHandler {

    @SubscribeEvent
    public static void computeCameraAngles(ViewportEvent.ComputeCameraAngles event) {
        ClientLevel level = Minecraft.getInstance().level;
        Entity entity = event.getCamera().getEntity();
        if (level != null && entity instanceof LivingEntity living) {
            handleWeaponMove(living);
            handleWeaponZoom(living);
            handleWeaponFire(event, living);
            handleShockCamera(event, living);
            handleBowPullAnimation(living);
        }
    }

    private static void handleWeaponMove(LivingEntity entity) {
        if (entity.getMainHandItem().is(TargetModTags.Items.GUN)) {
            float fps = Minecraft.getInstance().getFps();
            if (fps <= 0) {
                fps = 1f;
            }
            float times = 90f / fps;
            if (entity.getPersistentData().getDouble("move") < 0) {
                entity.getPersistentData().putDouble("move", ((entity.getPersistentData().getDouble("move") + 1 * times * Math.pow(entity.getPersistentData().getDouble("move"), 2) * (1 - 1 * entity.getPersistentData().getDouble("zoomtime")))
                        * (1 - 1 * entity.getPersistentData().getDouble("zoomtime"))));
            } else {
                entity.getPersistentData().putDouble("move", ((entity.getPersistentData().getDouble("move") - 1 * times * Math.pow(entity.getPersistentData().getDouble("move"), 2) * (1 - 1 * entity.getPersistentData().getDouble("zoomtime")))
                        * (1 - 1 * entity.getPersistentData().getDouble("zoomtime"))));
            }
            if (entity.getPersistentData().getDouble("movel") == 1) {
                entity.getPersistentData().putDouble("move",
                        ((entity.getPersistentData().getDouble("move") + Math.pow(Math.abs(entity.getPersistentData().getDouble("move")) + 0.05, 2) * 0.2 * times * (1 - 0.1 * entity.getPersistentData().getDouble("zoomtime")))
                                * (1 - 0.1 * entity.getPersistentData().getDouble("zoomtime"))));
            } else if (entity.getPersistentData().getDouble("mover") == 1) {
                entity.getPersistentData().putDouble("move",
                        ((entity.getPersistentData().getDouble("move") - Math.pow(Math.abs(entity.getPersistentData().getDouble("move")) + 0.05, 2) * 0.2 * times * (1 - 0.1 * entity.getPersistentData().getDouble("zoomtime")))
                                * (1 - 0.1 * entity.getPersistentData().getDouble("zoomtime"))));
            }
            if (entity.getPersistentData().getDouble("turnr") == 1) {
                entity.getPersistentData().putDouble("turntimeyaw", (entity.getPersistentData().getDouble("turntimeyaw") + 0.08 * times * Math.pow(entity.getPersistentData().getDouble("amplitudeyaw"), 2)));
            }
            if (entity.getPersistentData().getDouble("turnl") == 1) {
                entity.getPersistentData().putDouble("turntimeyaw", (entity.getPersistentData().getDouble("turntimeyaw") - 0.08 * times * Math.pow(entity.getPersistentData().getDouble("amplitudeyaw"), 2)));
            }
            if (entity.getPersistentData().getDouble("turntimeyaw") > 1) {
                entity.getPersistentData().putDouble("turntimeyaw", 1);
            }
            if (entity.getPersistentData().getDouble("turntimeyaw") < -1) {
                entity.getPersistentData().putDouble("turntimeyaw", (-1));
            }
            if (entity.getPersistentData().getDouble("turntimeyaw") >= 0) {
                if (entity.getPersistentData().getDouble("turnr") == 0) {
                    entity.getPersistentData().putDouble("turntimeyaw", (entity.getPersistentData().getDouble("turntimeyaw") - 0.02 * times));
                }
            }
            if (entity.getPersistentData().getDouble("turntimeyaw") < 0) {
                if (entity.getPersistentData().getDouble("turnl") == 0) {
                    entity.getPersistentData().putDouble("turntimeyaw", (entity.getPersistentData().getDouble("turntimeyaw") + 0.02 * times));
                }
            }
            if (entity.getPersistentData().getDouble("amplitudeyaw") < Math.abs(entity.getPersistentData().getDouble("r1") - entity.getPersistentData().getDouble("r2"))) {
                entity.getPersistentData().putDouble("amplitudeyaw", (entity.getPersistentData().getDouble("amplitudeyaw")
                        + 0.005 * Math.sin(0.5 * Math.PI * (Math.abs(entity.getPersistentData().getDouble("r1") - entity.getPersistentData().getDouble("r2")) - entity.getPersistentData().getDouble("amplitudeyaw")))));
            } else {
                entity.getPersistentData().putDouble("amplitudeyaw", (entity.getPersistentData().getDouble("amplitudeyaw")
                        - 0.005 * Math.sin(0.5 * Math.PI * (Math.abs(entity.getPersistentData().getDouble("r1") - entity.getPersistentData().getDouble("r2")) - entity.getPersistentData().getDouble("amplitudeyaw")))));
            }
            if (entity.getPersistentData().getDouble("amplitudeyaw") > 0) {
                entity.getPersistentData().putDouble("amplitudeyaw", (entity.getPersistentData().getDouble("amplitudeyaw") - 0.01 * Math.pow(entity.getPersistentData().getDouble("amplitudeyaw"), 2)));
            } else {
                entity.getPersistentData().putDouble("amplitudeyaw", (entity.getPersistentData().getDouble("amplitudeyaw") + 0.01 * Math.pow(entity.getPersistentData().getDouble("amplitudeyaw"), 2)));
            }
            entity.getPersistentData().putDouble("yaw", (0.04 * Math.tan(0.25 * Math.PI * entity.getPersistentData().getDouble("turntimeyaw")) * (1 - 1 * entity.getPersistentData().getDouble("zoomtime"))));
            if (entity.getPersistentData().getDouble("turnu") == 1) {
                entity.getPersistentData().putDouble("turntimepitch", (entity.getPersistentData().getDouble("turntimepitch") + 0.02 * times));
            }
            if (entity.getPersistentData().getDouble("turnd") == 1) {
                entity.getPersistentData().putDouble("turntimepitch", (entity.getPersistentData().getDouble("turntimepitch") - 0.02 * times));
            }
            if (entity.getPersistentData().getDouble("turntimepitch") > 1) {
                entity.getPersistentData().putDouble("turntimepitch", 1);
            }
            if (entity.getPersistentData().getDouble("turntimepitch") < -1) {
                entity.getPersistentData().putDouble("turntimepitch", (-1));
            }
            if (entity.getPersistentData().getDouble("turntimepitch") >= 0) {
                if (entity.getPersistentData().getDouble("turnu") == 0) {
                    entity.getPersistentData().putDouble("turntimepitch", (entity.getPersistentData().getDouble("turntimepitch") - 0.04 * times));
                }
            }
            if (entity.getPersistentData().getDouble("turntimepitch") < 0) {
                if (entity.getPersistentData().getDouble("turnd") == 0) {
                    entity.getPersistentData().putDouble("turntimepitch", (entity.getPersistentData().getDouble("turntimepitch") + 0.04 * times));
                }
            }
            if (entity.getPersistentData().getDouble("amplitudepitch") < Math.abs(entity.getPersistentData().getDouble("p1") - entity.getPersistentData().getDouble("p2"))) {
                entity.getPersistentData().putDouble("amplitudepitch", (entity.getPersistentData().getDouble("amplitudepitch")
                        + 0.00001 * Math.pow(Math.abs(entity.getPersistentData().getDouble("p1") - entity.getPersistentData().getDouble("p2")) - entity.getPersistentData().getDouble("amplitudepitch"), 2)));
            } else {
                entity.getPersistentData().putDouble("amplitudepitch", (entity.getPersistentData().getDouble("amplitudepitch")
                        - 0.00001 * Math.pow(Math.abs(entity.getPersistentData().getDouble("p1") - entity.getPersistentData().getDouble("p2")) - entity.getPersistentData().getDouble("amplitudepitch"), 2)));
            }
            if (entity.getPersistentData().getDouble("amplitudepitch") > 0) {
                entity.getPersistentData().putDouble("amplitudepitch", (entity.getPersistentData().getDouble("amplitudepitch") - 0.01 * Math.pow(entity.getPersistentData().getDouble("amplitudepitch"), 2)));
            } else {
                entity.getPersistentData().putDouble("amplitudepitch", (entity.getPersistentData().getDouble("amplitudepitch") + 0.01 * Math.pow(entity.getPersistentData().getDouble("amplitudepitch"), 2)));
            }
            entity.getPersistentData().putDouble("gunpitch",
                    ((0.15 * entity.getPersistentData().getDouble("amplitudepitch") * Math.tan(0.25 * Math.PI * entity.getPersistentData().getDouble("turntimepitch")) * (1 - 0.8 * entity.getPersistentData().getDouble("zoomtime"))
                            - 0.05 * entity.getPersistentData().getDouble("vy")) * (1 - 1 * entity.getPersistentData().getDouble("zoomtime"))));
            if (entity.getPersistentData().getDouble("firetime") == 0) {
                entity.getPersistentData().putDouble("rottime", (entity.getPersistentData().getDouble("rottime") + 1));
                if (entity.getPersistentData().getDouble("rottime") >= 3) {
                    entity.getPersistentData().putDouble("rottime", 0);
                }
                if (entity.getPersistentData().getDouble("rottime") == 1) {
                    entity.getPersistentData().putDouble("r1", (entity.getYRot()));
                    entity.getPersistentData().putDouble("p1", (entity.getXRot()));
                }
                if (entity.getPersistentData().getDouble("rottime") == 2) {
                    entity.getPersistentData().putDouble("r2", (entity.getYRot()));
                    entity.getPersistentData().putDouble("p2", (entity.getXRot()));
                }
                if (0 > entity.getPersistentData().getDouble("r1") - entity.getPersistentData().getDouble("r2")) {
                    entity.getPersistentData().putDouble("rot", (entity.getPersistentData().getDouble("rot") - 0.01));
                } else if (0 < entity.getPersistentData().getDouble("r1") - entity.getPersistentData().getDouble("r2")) {
                    entity.getPersistentData().putDouble("rot", (entity.getPersistentData().getDouble("rot") + 0.01));
                } else if (0 == entity.getPersistentData().getDouble("r1") - entity.getPersistentData().getDouble("r2")) {
                    entity.getPersistentData().putDouble("rot", 0);
                }
                if (0 > entity.getPersistentData().getDouble("p1") - entity.getPersistentData().getDouble("p2")) {
                    entity.getPersistentData().putDouble("pit", (entity.getPersistentData().getDouble("pit") - 0.01));
                } else if (0 < entity.getPersistentData().getDouble("p1") - entity.getPersistentData().getDouble("p2")) {
                    entity.getPersistentData().putDouble("pit", (entity.getPersistentData().getDouble("pit") + 0.01));
                } else if (0 == entity.getPersistentData().getDouble("p1") - entity.getPersistentData().getDouble("p2")) {
                    entity.getPersistentData().putDouble("pit", 0);
                }
                if (entity.getPersistentData().getDouble("rot") < 0) {
                    entity.getPersistentData().putDouble("rot", (entity.getPersistentData().getDouble("rot") + 2 * times * Math.pow(entity.getPersistentData().getDouble("rot"), 2)));
                    if (entity.getPersistentData().getDouble("rot") < -0.04) {
                        entity.getPersistentData().putDouble("turnr", 1);
                        entity.getPersistentData().putDouble("turnl", 0);
                    }
                } else if (entity.getPersistentData().getDouble("rot") > 0) {
                    entity.getPersistentData().putDouble("rot", (entity.getPersistentData().getDouble("rot") - 2 * times * Math.pow(entity.getPersistentData().getDouble("rot"), 2)));
                    if (entity.getPersistentData().getDouble("rot") > 0.04) {
                        entity.getPersistentData().putDouble("turnl", 1);
                        entity.getPersistentData().putDouble("turnr", 0);
                    }
                } else {
                    entity.getPersistentData().putDouble("rot", 0);
                    entity.getPersistentData().putDouble("turnl", 0);
                    entity.getPersistentData().putDouble("turnr", 0);
                }
                if (entity.getPersistentData().getDouble("pit") < 0) {
                    entity.getPersistentData().putDouble("pit", (entity.getPersistentData().getDouble("pit") + 2 * times * Math.pow(entity.getPersistentData().getDouble("pit"), 2)));
                    if (entity.getPersistentData().getDouble("pit") < -0.034) {
                        entity.getPersistentData().putDouble("turnu", 1);
                        entity.getPersistentData().putDouble("turnd", 0);
                    }
                } else if (entity.getPersistentData().getDouble("pit") > 0) {
                    entity.getPersistentData().putDouble("pit", (entity.getPersistentData().getDouble("pit") - 2 * times * Math.pow(entity.getPersistentData().getDouble("pit"), 2)));
                    if (entity.getPersistentData().getDouble("pit") > 0.034) {
                        entity.getPersistentData().putDouble("turnd", 1);
                        entity.getPersistentData().putDouble("turnu", 0);
                    }
                } else {
                    entity.getPersistentData().putDouble("pit", 0);
                    entity.getPersistentData().putDouble("turnd", 0);
                    entity.getPersistentData().putDouble("turnu", 0);
                }
            } else {
                entity.getPersistentData().putDouble("pit", 0);
                entity.getPersistentData().putDouble("turnl", 0);
                entity.getPersistentData().putDouble("turnr", 0);
                entity.getPersistentData().putDouble("turnd", 0);
                entity.getPersistentData().putDouble("turnu", 0);
            }
            if (-0.8 < entity.getDeltaMovement().y() + 0.078 && entity.getDeltaMovement().y() + 0.078 < 0.8) {
                if (entity.getPersistentData().getDouble("vy") < entity.getDeltaMovement().y() + 0.078) {
                    entity.getPersistentData().putDouble("vy",
                            ((entity.getPersistentData().getDouble("vy") + 2 * Math.pow((entity.getDeltaMovement().y() + 0.078) - entity.getPersistentData().getDouble("vy"), 2)) * (1 - 1 * entity.getPersistentData().getDouble("zoomtime"))));
                } else {
                    entity.getPersistentData().putDouble("vy",
                            ((entity.getPersistentData().getDouble("vy") - 2 * Math.pow((entity.getDeltaMovement().y() + 0.078) - entity.getPersistentData().getDouble("vy"), 2)) * (1 - 1 * entity.getPersistentData().getDouble("zoomtime"))));
                }
            }
            if (entity.getPersistentData().getDouble("vy") > 0.8) {
                entity.getPersistentData().putDouble("vy", 0.8);
            }
            if (entity.getPersistentData().getDouble("vy") < -0.8) {
                entity.getPersistentData().putDouble("vy", (-0.8));
            }
        }
    }

    private static void handleWeaponZoom(LivingEntity entity) {
        float fps = Minecraft.getInstance().getFps();
        if (fps <= 0) {
            fps = 1f;
        }
        float times = 110f / fps;
        if ((entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).zooming) {
            if (entity.getPersistentData().getDouble("zoomtime") < 1) {
                entity.getPersistentData().putDouble("zoomtime",
                        (entity.getPersistentData().getDouble("zoomtime") + entity.getMainHandItem().getOrCreateTag().getDouble("zoomspeed") * 0.02 * times));
            } else {
                entity.getPersistentData().putDouble("zoomtime", 1);
            }
        } else {
            if (entity.getPersistentData().getDouble("zoomtime") > 0) {
                entity.getPersistentData().putDouble("zoomtime", (entity.getPersistentData().getDouble("zoomtime") - 0.02 * times));
            } else {
                entity.getPersistentData().putDouble("zoomtime", 0);
            }
        }
        entity.getPersistentData().putDouble("zoompos", (0.5 * Math.cos(Math.PI * Math.pow(Math.pow(entity.getPersistentData().getDouble("zoomtime"), 2) - 1, 2)) + 0.5));
        entity.getPersistentData().putDouble("zoomposz", (-Math.pow(2 * entity.getPersistentData().getDouble("zoomtime") - 1, 2) + 1));
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
        amplitude = 15000 * stack.getOrCreateTag().getDouble("recoily")
                * stack.getOrCreateTag().getDouble("recoilx");
        if (entity.isShiftKeyDown() && entity.getBbHeight() >= 1 && entity.getPersistentData().getDouble("prone") == 0) {
            pose = 0.9;
        } else if (entity.getPersistentData().getDouble("prone") > 0) {
            if (stack.getOrCreateTag().getDouble("bipod") == 1) {
                pose = 0.75;
            } else {
                pose = 0.8;
            }
        } else {
            pose = 1;
        }

        if ((entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).firing > 0) {
            entity.getPersistentData().putDouble("firetime", 0.2);
            if (0.3 > entity.getPersistentData().getDouble("firepos2")) {
                entity.getPersistentData().putDouble("firepos2", (entity.getPersistentData().getDouble("firepos2") + 0.04 * times));
            }
        }
        if (0 < entity.getPersistentData().getDouble("firepos2")) {
            entity.getPersistentData().putDouble("firepos2", (entity.getPersistentData().getDouble("firepos2") - 0.02 * times));
        } else {
            entity.getPersistentData().putDouble("firepos2", 0);
        }
        if (0 < entity.getPersistentData().getDouble("firetime")) {
            entity.getPersistentData().putDouble("firetime", (entity.getPersistentData().getDouble("firetime") + 0.075 * times));
        }
        if (0 < entity.getPersistentData().getDouble("firetime") && entity.getPersistentData().getDouble("firetime") < 0.2) {
            entity.getPersistentData().putDouble("firepos",
                    (pose * ((-18.34) * Math.pow(entity.getPersistentData().getDouble("firetime"), 2) + 8.58 * entity.getPersistentData().getDouble("firetime") + entity.getPersistentData().getDouble("firepos2"))));
            if ((entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).recoilHorizon == 1) {
                event.setYaw((float) (yaw - 0.2 * amplitude * ((-18.34) * Math.pow(entity.getPersistentData().getDouble("firetime"), 2) + 8.58 * entity.getPersistentData().getDouble("firetime") + 0.7 * (2 * Math.random() - 1))));
                event.setPitch((float) (pitch + 0.2 * amplitude * ((-18.34) * Math.pow(entity.getPersistentData().getDouble("firetime"), 2) + 8.58 * entity.getPersistentData().getDouble("firetime") + 0.7 * (2 * Math.random() - 1))));
                event.setRoll((float) (roll + amplitude * ((-18.34) * Math.pow(entity.getPersistentData().getDouble("firetime"), 2) + 8.58 * entity.getPersistentData().getDouble("firetime") + 0.7 * (2 * Math.random() - 1))));
            } else if ((entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).recoilHorizon == -1) {
                event.setYaw((float) (yaw - 0.2 * amplitude * ((-18.34) * Math.pow(entity.getPersistentData().getDouble("firetime"), 2) + 8.58 * entity.getPersistentData().getDouble("firetime") + 0.7 * (2 * Math.random() - 1))));
                event.setPitch((float) (pitch + 0.2 * amplitude * ((-18.34) * Math.pow(entity.getPersistentData().getDouble("firetime"), 2) + 8.58 * entity.getPersistentData().getDouble("firetime") + 0.7 * (2 * Math.random() - 1))));
                event.setRoll((float) (roll - amplitude * ((-18.34) * Math.pow(entity.getPersistentData().getDouble("firetime"), 2) + 8.58 * entity.getPersistentData().getDouble("firetime") + 0.7 * (2 * Math.random() - 1))));
            }
        }
        if (0.2 <= entity.getPersistentData().getDouble("firetime") && entity.getPersistentData().getDouble("firetime") < 1) {
            entity.getPersistentData().putDouble("firepos",
                    (pose * (3.34 * Math.pow(entity.getPersistentData().getDouble("firetime"), 2) - 5.5 * entity.getPersistentData().getDouble("firetime") + 2.167 + entity.getPersistentData().getDouble("firepos2"))));
            if ((entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).recoilHorizon == 1) {
                event.setYaw((float) (yaw - 0.2 * amplitude * (3.34 * Math.pow(entity.getPersistentData().getDouble("firetime"), 2) - 5.5 * entity.getPersistentData().getDouble("firetime") + 2.167 + 0.7 * (2 * Math.random() - 1))));
                event.setPitch((float) (pitch + 0.2 * amplitude * (3.34 * Math.pow(entity.getPersistentData().getDouble("firetime"), 2) - 5.5 * entity.getPersistentData().getDouble("firetime") + 2.167 + 0.7 * (2 * Math.random() - 1))));
                event.setRoll((float) (roll + amplitude * (3.34 * Math.pow(entity.getPersistentData().getDouble("firetime"), 2) - 5.5 * entity.getPersistentData().getDouble("firetime") + 2.167 + 0.7 * (2 * Math.random() - 1))));
            } else if ((entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).recoilHorizon == -1) {
                event.setYaw((float) (yaw + 0.2 * amplitude * (3.34 * Math.pow(entity.getPersistentData().getDouble("firetime"), 2) - 5.5 * entity.getPersistentData().getDouble("firetime") + 2.167 + 0.7 * (2 * Math.random() - 1))));
                event.setPitch((float) (pitch - 0.2 * amplitude * (3.34 * Math.pow(entity.getPersistentData().getDouble("firetime"), 2) - 5.5 * entity.getPersistentData().getDouble("firetime") + 2.167 + 0.7 * (2 * Math.random() - 1))));
                event.setRoll((float) (roll - amplitude * (3.34 * Math.pow(entity.getPersistentData().getDouble("firetime"), 2) - 5.5 * entity.getPersistentData().getDouble("firetime") + 2.167 + 0.7 * (2 * Math.random() - 1))));
            }
        }
        if (0 <= entity.getPersistentData().getDouble("firetime") && entity.getPersistentData().getDouble("firetime") <= 0.25) {
            entity.getPersistentData().putDouble("boltpos", (-Math.pow(8 * entity.getPersistentData().getDouble("firetime") - 1, 2) + 1));
        }
        if (0.25 < entity.getPersistentData().getDouble("firetime") && entity.getPersistentData().getDouble("firetime") < 1) {
            entity.getPersistentData().putDouble("boltpos", 0);
        }
        if (entity.getPersistentData().getDouble("firetime") >= 1) {
            entity.getPersistentData().putDouble("firetime", 0);
        }
    }

    private static void handleShockCamera(ViewportEvent.ComputeCameraAngles event, LivingEntity entity) {
        if (entity.hasEffect(TargetModMobEffects.SHOCK.get()) && Minecraft.getInstance().options.getCameraType() == CameraType.FIRST_PERSON) {
            event.setYaw(Minecraft.getInstance().gameRenderer.getMainCamera().getYRot());
            event.setPitch(Minecraft.getInstance().gameRenderer.getMainCamera().getXRot());
            event.setRoll((float) Mth.nextDouble(RandomSource.create(), 8, 12));
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

}
