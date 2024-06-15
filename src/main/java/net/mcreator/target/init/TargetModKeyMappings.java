package net.mcreator.target.init;

import net.mcreator.target.TargetMod;
import net.mcreator.target.network.message.DoubleJumpMessage;
import net.mcreator.target.network.message.FireModeMessage;
import net.mcreator.target.network.message.ReloadMessage;
import net.mcreator.target.network.message.SensitivityMessage;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = {Dist.CLIENT})
public class TargetModKeyMappings {
    public static final KeyMapping RELOAD = new KeyMapping("key.target.reload", GLFW.GLFW_KEY_R, "key.categories.target") {
        private boolean isDownOld = false;

        @Override
        public void setDown(boolean isDown) {
            super.setDown(isDown);
            if (isDownOld != isDown && isDown && Minecraft.getInstance().player != null) {
                TargetMod.PACKET_HANDLER.sendToServer(new ReloadMessage(0));
                ReloadMessage.pressAction(Minecraft.getInstance().player, 0);
            }
            isDownOld = isDown;
        }
    };

    public static final KeyMapping DOUBLE_JUMP = new KeyMapping("key.target.double_jump", GLFW.GLFW_KEY_SPACE, "key.categories.target") {
        private boolean isDownOld = false;

        @Override
        public void setDown(boolean isDown) {
            super.setDown(isDown);
            if (isDownOld != isDown && isDown) {
                TargetMod.PACKET_HANDLER.sendToServer(new DoubleJumpMessage(0));
                if (Minecraft.getInstance().player != null) {
                    DoubleJumpMessage.pressAction(Minecraft.getInstance().player, 0);
                }
            }
            isDownOld = isDown;
        }
    };

    public static final KeyMapping FIRE_MODE = new KeyMapping("key.target.fire_mode", GLFW.GLFW_KEY_N, "key.categories.target") {
        private boolean isDownOld = false;

        @Override
        public void setDown(boolean isDown) {
            super.setDown(isDown);
            if (isDownOld != isDown && isDown) {
                TargetMod.PACKET_HANDLER.sendToServer(new FireModeMessage(0));
                FireModeMessage.pressAction(Minecraft.getInstance().player, 0);
            }
            isDownOld = isDown;
        }
    };

    public static final KeyMapping SENSITIVITY_INCREASE = new KeyMapping("key.target.sensitivity_increase", GLFW.GLFW_KEY_PAGE_UP, "key.categories.target") {
        private boolean isDownOld = false;

        @Override
        public void setDown(boolean isDown) {
            super.setDown(isDown);
            if (isDownOld != isDown && isDown) {
                TargetMod.PACKET_HANDLER.sendToServer(new SensitivityMessage(true));
            }
            isDownOld = isDown;
        }
    };

    public static final KeyMapping SENSITIVITY_REDUCE = new KeyMapping("key.target.sensitivity_reduce", GLFW.GLFW_KEY_PAGE_DOWN, "key.categories.target") {
        private boolean isDownOld = false;

        @Override
        public void setDown(boolean isDown) {
            super.setDown(isDown);
            if (isDownOld != isDown && isDown) {
                TargetMod.PACKET_HANDLER.sendToServer(new SensitivityMessage(false));
            }
            isDownOld = isDown;
        }
    };

    public static final KeyMapping INTERACT = new KeyMapping("key.target.interact", GLFW.GLFW_KEY_X, "key.categories.target") {
        private boolean isDownOld = false;

        @Override
        public void setDown(boolean isDown) {
            super.setDown(isDown);
            if (isDownOld != isDown && isDown) {
            }
            isDownOld = isDown;
        }
    };

    @SubscribeEvent
    public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(RELOAD);
        event.register(DOUBLE_JUMP);
        event.register(FIRE_MODE);
        event.register(SENSITIVITY_INCREASE);
        event.register(SENSITIVITY_REDUCE);
        event.register(INTERACT);
    }

    @Mod.EventBusSubscriber({Dist.CLIENT})
    public static class KeyEventListener {
        @SubscribeEvent
        public static void onClientTick(TickEvent.ClientTickEvent event) {
            if (Minecraft.getInstance().screen == null) {
                RELOAD.consumeClick();
                DOUBLE_JUMP.consumeClick();
                FIRE_MODE.consumeClick();
                SENSITIVITY_INCREASE.consumeClick();
                SENSITIVITY_REDUCE.consumeClick();
                INTERACT.consumeClick();
            }
        }
    }
}
