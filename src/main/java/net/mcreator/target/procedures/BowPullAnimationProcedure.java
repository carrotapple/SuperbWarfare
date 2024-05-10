package net.mcreator.target.procedures;

import net.mcreator.target.network.TargetModVariables;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class BowPullAnimationProcedure {

    @SubscribeEvent
    public static void computeCameraAngles(ViewportEvent.ComputeCameraAngles event) {
        ClientLevel level = Minecraft.getInstance().level;
        Entity entity = event.getCamera().getEntity();
        if (level != null) {
            execute(entity);
        }
    }

    private static void execute(Entity entity) {
        float fps = Minecraft.getInstance().getFps();
        if (fps <= 0) {
            fps = 1f;
        }
        float times = 90f / fps;
        CompoundTag persistentData = entity.getPersistentData();
        if ((entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).bowpull) {
            if (persistentData.getDouble("pulltime") < 1) {
                persistentData.putDouble("pulltime", (persistentData.getDouble("pulltime") + 0.014 * times));
            } else {
                persistentData.putDouble("pulltime", 1);
            }
        } else {
            if (persistentData.getDouble("pulltime") > 0) {
                persistentData.putDouble("pulltime", (persistentData.getDouble("pulltime") - 0.009 * times));
            } else {
                persistentData.putDouble("pulltime", 0);
            }
        }
        persistentData.putDouble("pullpos", (0.5 * Math.cos(Math.PI * Math.pow(Math.pow(persistentData.getDouble("pulltime"), 2) - 1, 2)) + 0.5));
        if ((entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).bowpull) {
            if (persistentData.getDouble("bowtime") < 1) {
                persistentData.putDouble("bowtime", (persistentData.getDouble("bowtime") + 0.014 * times));
            } else {
                persistentData.putDouble("bowtime", 1);
            }
        } else {
            if (persistentData.getDouble("bowtime") > 0) {
                persistentData.putDouble("bowtime", (persistentData.getDouble("bowtime") - 1 * times));
            } else {
                persistentData.putDouble("bowtime", 0);
            }
        }
        persistentData.putDouble("bowpos", (0.5 * Math.cos(Math.PI * Math.pow(Math.pow(persistentData.getDouble("bowtime"), 2) - 1, 2)) + 0.5));
        if ((entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).bowpull) {
            if (persistentData.getDouble("handtime") < 1) {
                persistentData.putDouble("handtime", (persistentData.getDouble("handtime") + 0.014 * times));
            } else {
                persistentData.putDouble("handtime", 1);
            }
            persistentData.putDouble("handpos", (0.5 * Math.cos(Math.PI * Math.pow(Math.pow(persistentData.getDouble("handtime"), 2) - 1, 2)) + 0.5));
        } else {
            if (persistentData.getDouble("handtime") > 0) {
                persistentData.putDouble("handtime", (persistentData.getDouble("handtime") - 0.04 * times));
            } else {
                persistentData.putDouble("handtime", 0);
            }
            if (persistentData.getDouble("handtime") > 0 && persistentData.getDouble("handtime") < 0.5) {
                persistentData.putDouble("handpos", (0.5 * Math.cos(Math.PI * Math.pow(Math.pow(persistentData.getDouble("handtime"), 2) - 1, 2)) + 0.5));
            }
        }
    }
}
