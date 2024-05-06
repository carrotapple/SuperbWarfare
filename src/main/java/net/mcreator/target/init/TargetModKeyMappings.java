
/*
 *	MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.target.init;

import com.mojang.blaze3d.platform.InputConstants;
import net.mcreator.target.TargetMod;
import net.mcreator.target.network.DoubleJumpMessage;
import net.mcreator.target.network.FireModeMessage;
import net.mcreator.target.network.ReloadMessage;
import net.mcreator.target.network.ZoomMessage;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
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
            if (isDownOld != isDown && isDown) {
                TargetMod.PACKET_HANDLER.sendToServer(new ReloadMessage(0, 0));
                ReloadMessage.pressAction(Minecraft.getInstance().player, 0, 0);
            }
            isDownOld = isDown;
        }
    };
    public static final KeyMapping DOUBLEJUMPINPUT = new KeyMapping("key.target.doublejumpinput", GLFW.GLFW_KEY_SPACE, "key.categories.target") {
        private boolean isDownOld = false;

        @Override
        public void setDown(boolean isDown) {
            super.setDown(isDown);
            if (isDownOld != isDown && isDown) {
                TargetMod.PACKET_HANDLER.sendToServer(new DoubleJumpMessage(0, 0));
                DoubleJumpMessage.pressAction(Minecraft.getInstance().player, 0, 0);
            }
            isDownOld = isDown;
        }
    };
    public static final KeyMapping FIREMODE = new KeyMapping("key.target.firemode", GLFW.GLFW_KEY_N, "key.categories.target") {
        private boolean isDownOld = false;

        @Override
        public void setDown(boolean isDown) {
            super.setDown(isDown);
            if (isDownOld != isDown && isDown) {
                TargetMod.PACKET_HANDLER.sendToServer(new FireModeMessage(0, 0));
                FireModeMessage.pressAction(Minecraft.getInstance().player, 0, 0);
            }
            isDownOld = isDown;
        }
    };
    public static final KeyMapping ZOOM = new KeyMapping("key.target.zoom", InputConstants.Type.MOUSE, GLFW.GLFW_MOUSE_BUTTON_RIGHT, "key.categories.target") {
        private boolean isDownOld = false;

        @Override
        public void setDown(boolean isDown) {
            super.setDown(isDown);
            if (isDownOld != isDown && isDown) {
                TargetMod.PACKET_HANDLER.sendToServer(new ZoomMessage(0, 0));
                ZoomMessage.pressAction(Minecraft.getInstance().player, 0, 0);
                ZOOM_LASTPRESS = System.currentTimeMillis();
            } else if (isDownOld != isDown) {
                int dt = (int) (System.currentTimeMillis() - ZOOM_LASTPRESS);
                TargetMod.PACKET_HANDLER.sendToServer(new ZoomMessage(1, dt));
                ZoomMessage.pressAction(Minecraft.getInstance().player, 1, dt);
            }
            isDownOld = isDown;
        }
    };
    private static long ZOOM_LASTPRESS = 0;

    @SubscribeEvent
    public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(RELOAD);
        event.register(DOUBLEJUMPINPUT);
        event.register(FIREMODE);
        event.register(ZOOM);
    }

    @Mod.EventBusSubscriber({Dist.CLIENT})
    public static class KeyEventListener {
        @SubscribeEvent
        public static void onClientTick(TickEvent.ClientTickEvent event) {
            if (Minecraft.getInstance().screen == null) {
                RELOAD.consumeClick();
                DOUBLEJUMPINPUT.consumeClick();
                FIREMODE.consumeClick();
                ZOOM.consumeClick();
            }
        }
    }
}
