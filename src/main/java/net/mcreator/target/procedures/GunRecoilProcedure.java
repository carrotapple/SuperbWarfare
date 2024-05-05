package net.mcreator.target.procedures;

import net.mcreator.target.network.TargetModVariables;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber
public class GunRecoilProcedure {
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            execute(event, event.player);
        }
    }

    public static void execute(Entity entity) {
        execute(null, entity);
    }

    private static void execute(@Nullable Event event, Entity entity) {


        float recoilx = ((float) (entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getOrCreateTag().getDouble("recoilx"));
        float recoily = ((float) (entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getOrCreateTag().getDouble("recoily"));
        float recoilyaw = ((float) (entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).recoilhorizon);

        double[] recoilTimer = {0};
        double totalTime = 100;
        int sleepTime = 2;
        double recoilDuration = totalTime / sleepTime;
        Runnable recoilRunnable = () -> {
            while (recoilTimer[0] < recoilDuration) {

                float rx;
                float ry;

                if (entity.isShiftKeyDown() && entity.getBbHeight() >= 1 && entity.getPersistentData().getDouble("prone") == 0) {
                    rx = 0.7f;
                    ry = 0.8f;
                } else if (entity.getPersistentData().getDouble("prone") > 0) {
                    if ((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getOrCreateTag().getDouble("bipod") == 1) {
                        rx = 0.05f;
                        ry = 0.1f;
                    } else {
                        rx = 0.5f;
                        ry = 0.7f;
                    }
                } else {
                    rx = 1f;
                    ry = 1f;
                }

                if ((entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).recoil >= 1) {
                    {
                        double _setval = 0;
                        entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                            capability.recoil = _setval;
                            capability.syncPlayerVariables(entity);
                        });
                    }
                }

                if ((entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).recoil > 0) {
                    {
                        double _setval = (entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).recoil + 0.0025;
                        entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                            capability.recoil = _setval;
                            capability.syncPlayerVariables(entity);
                        });
                    }

                    float newPitch = ((float) (entity.getXRot() - 1.5f * recoily * ry * (Math.sin(2 * Math.PI * (1.03f * (entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).recoil - 0.032047110911)) + 0.2)));
                    entity.setXRot(newPitch);
                    entity.xRotO = entity.getXRot();

                    float newYaw = ((float) (entity.getYRot() - 1.0f * recoilyaw * recoilx * rx * (Math.sin(2 * Math.PI * (1.03f * (entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).recoil - 0.032047110911)) + 0.2)));
                    entity.setYRot(newYaw);
                    entity.yRotO = entity.getYRot();

                }

                recoilTimer[0]++;
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        Thread recoilThread = new Thread(recoilRunnable);
        recoilThread.start();
    }
}
