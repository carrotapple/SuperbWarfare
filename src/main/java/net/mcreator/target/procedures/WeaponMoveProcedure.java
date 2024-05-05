package net.mcreator.target.procedures;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class WeaponMoveProcedure {
    private static ViewportEvent.ComputeCameraAngles _provider = null;

    private static void setAngles(float yaw, float pitch, float roll) {
        _provider.setYaw(yaw);
        _provider.setPitch(pitch);
        _provider.setRoll(roll);
    }

    @SubscribeEvent
    public static void computeCameraangles(ViewportEvent.ComputeCameraAngles event) {
        _provider = event;
        ClientLevel level = Minecraft.getInstance().level;
        Entity entity = _provider.getCamera().getEntity();
        if (level != null && entity != null) {
            Vec3 entPos = entity.getPosition((float) _provider.getPartialTick());
            execute(_provider, entity);
        }
    }

    public static void execute(Entity entity) {
        execute(null, entity);
    }

    private static void execute(@Nullable Event event, Entity entity) {
        if (entity == null)
            return;
        if ((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).is(ItemTags.create(new ResourceLocation("target:gun")))) {
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
}
