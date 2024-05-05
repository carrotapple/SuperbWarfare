package net.mcreator.target.procedures;

import net.mcreator.target.network.TargetModVariables;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
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
public class WeaponfireProcedure {
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
            execute(_provider, entity, _provider.getPitch(), _provider.getRoll(), _provider.getYaw());
        }
    }

    public static void execute(Entity entity, double pitch, double roll, double yaw) {
        execute(null, entity, pitch, roll, yaw);
    }

    private static void execute(@Nullable Event event, Entity entity, double pitch, double roll, double yaw) {
        if (entity == null)
            return;
        double pose = 0;
        double amplitude = 0;
        float fps = Minecraft.getInstance().getFps();
        if (fps <= 0) {
            fps = 1f;
        }
        float times = 45f / fps;
        amplitude = 15000 * (entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getOrCreateTag().getDouble("recoily")
                * (entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getOrCreateTag().getDouble("recoilx");
        if (entity.isShiftKeyDown() && entity.getBbHeight() >= 1 && entity.getPersistentData().getDouble("prone") == 0) {
            pose = 0.9;
        } else if (entity.getPersistentData().getDouble("prone") > 0) {
            if ((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getOrCreateTag().getDouble("bipod") == 1) {
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
            if ((entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).recoilhorizon == 1) {
                setAngles((float) (yaw - 0.2 * amplitude * ((-18.34) * Math.pow(entity.getPersistentData().getDouble("firetime"), 2) + 8.58 * entity.getPersistentData().getDouble("firetime") + 0.7 * (2 * Math.random() - 1))),
                        (float) (pitch + 0.2 * amplitude * ((-18.34) * Math.pow(entity.getPersistentData().getDouble("firetime"), 2) + 8.58 * entity.getPersistentData().getDouble("firetime") + 0.7 * (2 * Math.random() - 1))),
                        (float) (roll + amplitude * ((-18.34) * Math.pow(entity.getPersistentData().getDouble("firetime"), 2) + 8.58 * entity.getPersistentData().getDouble("firetime") + 0.7 * (2 * Math.random() - 1))));
            } else if ((entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).recoilhorizon == -1) {
                setAngles((float) (yaw - 0.2 * amplitude * ((-18.34) * Math.pow(entity.getPersistentData().getDouble("firetime"), 2) + 8.58 * entity.getPersistentData().getDouble("firetime") + 0.7 * (2 * Math.random() - 1))),
                        (float) (pitch + 0.2 * amplitude * ((-18.34) * Math.pow(entity.getPersistentData().getDouble("firetime"), 2) + 8.58 * entity.getPersistentData().getDouble("firetime") + 0.7 * (2 * Math.random() - 1))),
                        (float) (roll - amplitude * ((-18.34) * Math.pow(entity.getPersistentData().getDouble("firetime"), 2) + 8.58 * entity.getPersistentData().getDouble("firetime") + 0.7 * (2 * Math.random() - 1))));
            }
        }
        if (0.2 <= entity.getPersistentData().getDouble("firetime") && entity.getPersistentData().getDouble("firetime") < 1) {
            entity.getPersistentData().putDouble("firepos",
                    (pose * (3.34 * Math.pow(entity.getPersistentData().getDouble("firetime"), 2) - 5.5 * entity.getPersistentData().getDouble("firetime") + 2.167 + entity.getPersistentData().getDouble("firepos2"))));
            if ((entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).recoilhorizon == 1) {
                setAngles((float) (yaw - 0.2 * amplitude * (3.34 * Math.pow(entity.getPersistentData().getDouble("firetime"), 2) - 5.5 * entity.getPersistentData().getDouble("firetime") + 2.167 + 0.7 * (2 * Math.random() - 1))),
                        (float) (pitch + 0.2 * amplitude * (3.34 * Math.pow(entity.getPersistentData().getDouble("firetime"), 2) - 5.5 * entity.getPersistentData().getDouble("firetime") + 2.167 + 0.7 * (2 * Math.random() - 1))),
                        (float) (roll + amplitude * (3.34 * Math.pow(entity.getPersistentData().getDouble("firetime"), 2) - 5.5 * entity.getPersistentData().getDouble("firetime") + 2.167 + 0.7 * (2 * Math.random() - 1))));
            } else if ((entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).recoilhorizon == -1) {
                setAngles((float) (yaw + 0.2 * amplitude * (3.34 * Math.pow(entity.getPersistentData().getDouble("firetime"), 2) - 5.5 * entity.getPersistentData().getDouble("firetime") + 2.167 + 0.7 * (2 * Math.random() - 1))),
                        (float) (pitch - 0.2 * amplitude * (3.34 * Math.pow(entity.getPersistentData().getDouble("firetime"), 2) - 5.5 * entity.getPersistentData().getDouble("firetime") + 2.167 + 0.7 * (2 * Math.random() - 1))),
                        (float) (roll - amplitude * (3.34 * Math.pow(entity.getPersistentData().getDouble("firetime"), 2) - 5.5 * entity.getPersistentData().getDouble("firetime") + 2.167 + 0.7 * (2 * Math.random() - 1))));
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
}
