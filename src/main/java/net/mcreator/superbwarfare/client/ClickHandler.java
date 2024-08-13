package net.mcreator.superbwarfare.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.mcreator.superbwarfare.ModUtils;
import net.mcreator.superbwarfare.entity.Mk42Entity;
import net.mcreator.superbwarfare.entity.Mle1934Entity;
import net.mcreator.superbwarfare.entity.MortarEntity;
import net.mcreator.superbwarfare.init.ModItems;
import net.mcreator.superbwarfare.init.ModMobEffects;
import net.mcreator.superbwarfare.init.ModTags;
import net.mcreator.superbwarfare.network.ModVariables;
import net.mcreator.superbwarfare.network.message.*;
import net.mcreator.superbwarfare.tools.TraceTool;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClickHandler {
    private static boolean notInGame() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return true;
        if (mc.getOverlay() != null) return true;
        if (mc.screen != null) return true;
        if (!mc.mouseHandler.isMouseGrabbed()) return true;
        return !mc.isWindowActive();
    }

    @SubscribeEvent
    public static void onButtonReleased(InputEvent.MouseButton.Pre event) {
        if (notInGame()) return;
        if (event.getAction() != InputConstants.RELEASE) return;

        Player player = Minecraft.getInstance().player;
        if (player == null) return;

        int button = event.getButton();
        if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
            ModUtils.PACKET_HANDLER.sendToServer(new FireMessage(1));
            ModUtils.PACKET_HANDLER.sendToServer(new VehicleFireMessage(0));
        }
        if (button == GLFW.GLFW_MOUSE_BUTTON_RIGHT) {
            if (Minecraft.getInstance().player.hasEffect(ModMobEffects.SHOCK.get())) {
                event.setCanceled(true);
            }

            ModUtils.PACKET_HANDLER.sendToServer(new ZoomMessage(1));
        }
    }

    @SubscribeEvent
    public static void onButtonPressed(InputEvent.MouseButton.Pre event) {
        if (notInGame()) return;
        if (event.getAction() != InputConstants.PRESS) return;

        Player player = Minecraft.getInstance().player;
        if (player == null) return;

        if (player.isSpectator()) return;

        ItemStack stack = player.getMainHandItem();

        int button = event.getButton();

        if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
            if (Minecraft.getInstance().player.hasEffect(ModMobEffects.SHOCK.get())) {
                event.setCanceled(true);
                return;
            }
            if (stack.is(ModItems.MONITOR.get())) {
                event.setCanceled(true);
                ModUtils.PACKET_HANDLER.sendToServer(new DroneFireMessage(0));
            }
            if (player.getVehicle() != null && (player.getVehicle() instanceof Mk42Entity || player.getVehicle() instanceof Mle1934Entity)) {
                event.setCanceled(true);
                ModUtils.PACKET_HANDLER.sendToServer(new VehicleFireMessage(0));
                return;
            }
            if (player.getMainHandItem().is(ModTags.Items.GUN)) {
                event.setCanceled(true);
                ModUtils.PACKET_HANDLER.sendToServer(new FireMessage(0));
            }
        }

        if (button == GLFW.GLFW_MOUSE_BUTTON_RIGHT) {
            if (Minecraft.getInstance().player.hasEffect(ModMobEffects.SHOCK.get())) {
                event.setCanceled(true);
            }
            if (player.getMainHandItem().is(ModTags.Items.GUN) || (player.isPassenger() && (player.getVehicle() instanceof Mk42Entity || player.getVehicle() instanceof Mle1934Entity))) {
                event.setCanceled(true);
                ModUtils.PACKET_HANDLER.sendToServer(new ZoomMessage(0));
            }
        }

        if (button == GLFW.GLFW_MOUSE_BUTTON_MIDDLE) {
            if (Minecraft.getInstance().player.hasEffect(ModMobEffects.SHOCK.get())) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void onMouseScrolling(InputEvent.MouseScrollingEvent event) {
        Player player = Minecraft.getInstance().player;

        if (notInGame()) return;
        if (player == null) return;

        ItemStack stack = player.getMainHandItem();

        double scroll = event.getScrollDelta();

        if (stack.is(ModTags.Items.GUN) && (player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables())).zoom) {
            var tag = stack.getOrCreateTag();
            if (tag.getDouble("min_zoom") != 0 && tag.getDouble("max_zoom") != 0) {
                ModUtils.PACKET_HANDLER.sendToServer(new AdjustZoomFovMessage(scroll));
            }
            event.setCanceled(true);
        }

        if (player.hasEffect(ModMobEffects.SHOCK.get())) {
            event.setCanceled(true);
        }

        Entity looking = TraceTool.findLookingEntity(player, 6);
        if (looking == null) return;
        if (looking instanceof MortarEntity && player.isShiftKeyDown()) {
            ModUtils.PACKET_HANDLER.sendToServer(new AdjustMortarAngleMessage(scroll));
            event.setCanceled(true);
        }
    }

    private static void setKeyState(int key, int state) {
        Player player = Minecraft.getInstance().player;
        if (player == null) return;

        var data = player.getPersistentData();
        switch (key) {
            case GLFW.GLFW_KEY_D -> data.putDouble("move_left", state);
            case GLFW.GLFW_KEY_A -> data.putDouble("move_right", state);
            case GLFW.GLFW_KEY_W -> data.putDouble("move_forward", state);
            case GLFW.GLFW_KEY_S -> data.putDouble("move_backward", state);
        }
    }

    @SubscribeEvent
    public static void onKeyPressed(InputEvent.Key event) {
        if (notInGame()) return;

        boolean clicked;
        if (event.getAction() == InputConstants.PRESS) {
            clicked = true;
        } else if (event.getAction() == InputConstants.RELEASE) {
            clicked = false;
        } else {
            return;
        }

        setKeyState(event.getKey(), clicked ? 1 : 0);
    }

}