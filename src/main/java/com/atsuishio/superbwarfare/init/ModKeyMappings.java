package com.atsuishio.superbwarfare.init;

import com.atsuishio.superbwarfare.ModUtils;
import com.atsuishio.superbwarfare.network.message.BreathMessage;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModKeyMappings {

    public static final KeyMapping RELOAD = new KeyMapping("key.superbwarfare.reload", GLFW.GLFW_KEY_R, "key.categories.superbwarfare");
    public static final KeyMapping FIRE_MODE = new KeyMapping("key.superbwarfare.fire_mode", GLFW.GLFW_KEY_N, "key.categories.superbwarfare");
    public static final KeyMapping SENSITIVITY_INCREASE = new KeyMapping("key.superbwarfare.sensitivity_increase", GLFW.GLFW_KEY_PAGE_UP, "key.categories.superbwarfare");
    public static final KeyMapping SENSITIVITY_REDUCE = new KeyMapping("key.superbwarfare.sensitivity_reduce", GLFW.GLFW_KEY_PAGE_DOWN, "key.categories.superbwarfare");
    public static final KeyMapping INTERACT = new KeyMapping("key.superbwarfare.interact", GLFW.GLFW_KEY_X, "key.categories.superbwarfare");
    public static final KeyMapping DISMOUNT = new KeyMapping("key.superbwarfare.dismount", GLFW.GLFW_KEY_LEFT_ALT, "key.categories.superbwarfare");

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

    public static final KeyMapping CONFIG = new KeyMapping("key.superbwarfare.config", KeyConflictContext.IN_GAME,
            KeyModifier.ALT, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_O, "key.categories.superbwarfare");

    public static final KeyMapping EDIT_MODE = new KeyMapping("key.superbwarfare.edit_mode", GLFW.GLFW_KEY_H, "key.categories.superbwarfare");
    public static final KeyMapping EDIT_SCOPE = new KeyMapping("key.superbwarfare.edit_scope", GLFW.GLFW_KEY_UP, "key.categories.superbwarfare");
    public static final KeyMapping EDIT_BARREL = new KeyMapping("key.superbwarfare.edit_barrel", GLFW.GLFW_KEY_LEFT, "key.categories.superbwarfare");
    public static final KeyMapping EDIT_MAGAZINE = new KeyMapping("key.superbwarfare.edit_magazine", GLFW.GLFW_KEY_DOWN, "key.categories.superbwarfare");
    public static final KeyMapping EDIT_STOCK = new KeyMapping("key.superbwarfare.edit_stock", GLFW.GLFW_KEY_RIGHT, "key.categories.superbwarfare");
    public static final KeyMapping EDIT_GRIP = new KeyMapping("key.superbwarfare.edit_grip", KeyConflictContext.IN_GAME,
            KeyModifier.SHIFT, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_DOWN, "key.categories.superbwarfare");

    public static final KeyMapping FIRE = new KeyMapping("key.superbwarfare.fire", InputConstants.Type.MOUSE, GLFW.GLFW_MOUSE_BUTTON_LEFT, "key.categories.superbwarfare");
    public static final KeyMapping HOLD_ZOOM = new KeyMapping("key.superbwarfare.hold_zoom", InputConstants.Type.MOUSE, GLFW.GLFW_MOUSE_BUTTON_RIGHT, "key.categories.superbwarfare");
    public static final KeyMapping SWITCH_ZOOM = new KeyMapping("key.superbwarfare.switch_zoom", GLFW.GLFW_KEY_UNKNOWN, "key.categories.superbwarfare");
    public static final KeyMapping RELEASE_DECOY = new KeyMapping("key.superbwarfare.release_decoy", GLFW.GLFW_KEY_X, "key.categories.superbwarfare");

    @SubscribeEvent
    public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(RELOAD);
        event.register(FIRE_MODE);
        event.register(SENSITIVITY_INCREASE);
        event.register(SENSITIVITY_REDUCE);
        event.register(INTERACT);
        event.register(DISMOUNT);
        event.register(BREATH);
        event.register(CONFIG);
        event.register(EDIT_MODE);
        event.register(EDIT_SCOPE);
        event.register(EDIT_BARREL);
        event.register(EDIT_MAGAZINE);
        event.register(EDIT_STOCK);
        event.register(EDIT_GRIP);
        event.register(FIRE);
        event.register(HOLD_ZOOM);
        event.register(SWITCH_ZOOM);
        event.register(RELEASE_DECOY);
    }

    @Mod.EventBusSubscriber(value = Dist.CLIENT)
    public static class KeyEventListener {
        @SubscribeEvent
        public static void onClientTick(TickEvent.ClientTickEvent event) {
            if (Minecraft.getInstance().screen == null) {
                BREATH.consumeClick();
            }
        }
    }
}
