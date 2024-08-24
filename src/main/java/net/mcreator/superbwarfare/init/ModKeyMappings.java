package net.mcreator.superbwarfare.init;

import net.mcreator.superbwarfare.ModUtils;
import net.mcreator.superbwarfare.network.message.*;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModKeyMappings {
    public static final KeyMapping RELOAD = new KeyMapping("key.superbwarfare.reload", GLFW.GLFW_KEY_R, "key.categories.superbwarfare") {
        private boolean isDownOld = false;

        @Override
        public void setDown(boolean isDown) {
            super.setDown(isDown);
            if (isDownOld != isDown && isDown && Minecraft.getInstance().player != null) {
                ModUtils.PACKET_HANDLER.sendToServer(new ReloadMessage(0));
            }
            isDownOld = isDown;
        }
    };

    public static final KeyMapping FIRE_MODE = new KeyMapping("key.superbwarfare.fire_mode", GLFW.GLFW_KEY_N, "key.categories.superbwarfare") {
        private boolean isDownOld = false;

        @Override
        public void setDown(boolean isDown) {
            super.setDown(isDown);
            if (isDownOld != isDown && isDown) {
                ModUtils.PACKET_HANDLER.sendToServer(new FireModeMessage(0));
                FireModeMessage.pressAction(Minecraft.getInstance().player, 0);
            }
            isDownOld = isDown;
        }
    };

    public static final KeyMapping SENSITIVITY_INCREASE = new KeyMapping("key.superbwarfare.sensitivity_increase", GLFW.GLFW_KEY_PAGE_UP, "key.categories.superbwarfare") {
        private boolean isDownOld = false;

        @Override
        public void setDown(boolean isDown) {
            super.setDown(isDown);
            if (isDownOld != isDown && isDown) {
                ModUtils.PACKET_HANDLER.sendToServer(new SensitivityMessage(true));
            }
            isDownOld = isDown;
        }
    };

    public static final KeyMapping SENSITIVITY_REDUCE = new KeyMapping("key.superbwarfare.sensitivity_reduce", GLFW.GLFW_KEY_PAGE_DOWN, "key.categories.superbwarfare") {
        private boolean isDownOld = false;

        @Override
        public void setDown(boolean isDown) {
            super.setDown(isDown);
            if (isDownOld != isDown && isDown) {
                ModUtils.PACKET_HANDLER.sendToServer(new SensitivityMessage(false));
            }
            isDownOld = isDown;
        }
    };

    public static final KeyMapping INTERACT = new KeyMapping("key.superbwarfare.interact", GLFW.GLFW_KEY_X, "key.categories.superbwarfare") {
        private boolean isDownOld = false;

        @Override
        public void setDown(boolean isDown) {
            super.setDown(isDown);
            if (isDownOld != isDown && isDown) {
                ModUtils.PACKET_HANDLER.sendToServer(new InteractMessage(0));
                if (Minecraft.getInstance().player != null) {
                    InteractMessage.pressAction(Minecraft.getInstance().player, 0);
                }
            }
            isDownOld = isDown;
        }
    };

    public static final KeyMapping FORWARD = new KeyMapping("key.superbwarfare.forward", GLFW.GLFW_KEY_W, "key.categories.superbwarfare") {
        private boolean isDownOld = false;

        @Override
        public void setDown(boolean isDown) {
            super.setDown(isDown);
            if (isDownOld != isDown && isDown) {
                ModUtils.PACKET_HANDLER.sendToServer(new DroneMovementMessage(2, true));
            } else if (isDownOld != isDown) {
                ModUtils.PACKET_HANDLER.sendToServer(new DroneMovementMessage(2, false));
            }
            isDownOld = isDown;
        }
    };
    public static final KeyMapping BACKWARD = new KeyMapping("key.superbwarfare.backward", GLFW.GLFW_KEY_S, "key.categories.superbwarfare") {
        private boolean isDownOld = false;

        @Override
        public void setDown(boolean isDown) {
            super.setDown(isDown);
            if (isDownOld != isDown && isDown) {
                ModUtils.PACKET_HANDLER.sendToServer(new DroneMovementMessage(3, true));
            } else if (isDownOld != isDown) {
                ModUtils.PACKET_HANDLER.sendToServer(new DroneMovementMessage(3, false));
            }
            isDownOld = isDown;
        }
    };
    public static final KeyMapping LEFT = new KeyMapping("key.superbwarfare.left", GLFW.GLFW_KEY_A, "key.categories.superbwarfare") {
        private boolean isDownOld = false;

        @Override
        public void setDown(boolean isDown) {
            super.setDown(isDown);
            if (isDownOld != isDown && isDown) {
                ModUtils.PACKET_HANDLER.sendToServer(new DroneMovementMessage(0, true));
            } else if (isDownOld != isDown) {
                ModUtils.PACKET_HANDLER.sendToServer(new DroneMovementMessage(0, false));
            }
            isDownOld = isDown;
        }
    };
    public static final KeyMapping RIGHT = new KeyMapping("key.superbwarfare.right", GLFW.GLFW_KEY_D, "key.categories.superbwarfare") {
        private boolean isDownOld = false;

        @Override
        public void setDown(boolean isDown) {
            super.setDown(isDown);
            if (isDownOld != isDown && isDown) {
                ModUtils.PACKET_HANDLER.sendToServer(new DroneMovementMessage(1, true));
            } else if (isDownOld != isDown) {
                ModUtils.PACKET_HANDLER.sendToServer(new DroneMovementMessage(1, false));
            }
            isDownOld = isDown;
        }
    };
    public static final KeyMapping UP = new KeyMapping("key.superbwarfare.up", GLFW.GLFW_KEY_SPACE, "key.categories.superbwarfare") {
        private boolean isDownOld = false;

        @Override
        public void setDown(boolean isDown) {
            super.setDown(isDown);
            if (isDownOld != isDown && isDown) {
                ModUtils.PACKET_HANDLER.sendToServer(new DroneMovementMessage(4, true));
            } else if (isDownOld != isDown) {
                ModUtils.PACKET_HANDLER.sendToServer(new DroneMovementMessage(4, false));
            }
            isDownOld = isDown;
        }
    };
    public static final KeyMapping DOWN = new KeyMapping("key.superbwarfare.down", GLFW.GLFW_KEY_LEFT_SHIFT, "key.categories.superbwarfare") {
        private boolean isDownOld = false;

        @Override
        public void setDown(boolean isDown) {
            super.setDown(isDown);
            if (isDownOld != isDown && isDown) {
                ModUtils.PACKET_HANDLER.sendToServer(new DroneMovementMessage(5, true));
            } else if (isDownOld != isDown) {
                ModUtils.PACKET_HANDLER.sendToServer(new DroneMovementMessage(5, false));
            }
            isDownOld = isDown;
        }
    };

    public static final KeyMapping BREATH = new KeyMapping("key.superbwarfare.breath", GLFW.GLFW_KEY_LEFT_CONTROL, "key.categories.superbwarfare") {
        private boolean isDownOld = false;

        @Override
        public void setDown(boolean isDown) {
            super.setDown(isDown);
            if (isDownOld != isDown && isDown) {
                ModUtils.PACKET_HANDLER.sendToServer(new BreathMessage(true));
            } else if (isDownOld != isDown) {
                ModUtils.PACKET_HANDLER.sendToServer(new BreathMessage(false));
            }
            isDownOld = isDown;
        }
    };

    @SubscribeEvent
    public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(RELOAD);
        event.register(FIRE_MODE);
        event.register(SENSITIVITY_INCREASE);
        event.register(SENSITIVITY_REDUCE);
        event.register(INTERACT);
        event.register(FORWARD);
        event.register(BACKWARD);
        event.register(LEFT);
        event.register(RIGHT);
        event.register(UP);
        event.register(DOWN);
        event.register(BREATH);
    }

    @Mod.EventBusSubscriber({Dist.CLIENT})
    public static class KeyEventListener {
        @SubscribeEvent
        public static void onClientTick(TickEvent.ClientTickEvent event) {
            if (Minecraft.getInstance().screen == null) {
                RELOAD.consumeClick();
                FIRE_MODE.consumeClick();
                SENSITIVITY_INCREASE.consumeClick();
                SENSITIVITY_REDUCE.consumeClick();
                INTERACT.consumeClick();
                FORWARD.consumeClick();
                BACKWARD.consumeClick();
                LEFT.consumeClick();
                RIGHT.consumeClick();
                UP.consumeClick();
                DOWN.consumeClick();
                BREATH.consumeClick();
            }
        }
    }
}
