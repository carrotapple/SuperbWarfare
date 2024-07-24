package net.mcreator.target.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.mcreator.target.TargetMod;
import net.mcreator.target.client.gui.RangeHelper;
import net.mcreator.target.entity.Mk42Entity;
import net.mcreator.target.entity.MortarEntity;
import net.mcreator.target.init.TargetModItems;
import net.mcreator.target.init.TargetModKeyMappings;
import net.mcreator.target.init.TargetModMobEffects;
import net.mcreator.target.init.TargetModTags;
import net.mcreator.target.network.TargetModVariables;
import net.mcreator.target.network.message.*;
import net.mcreator.target.tools.TraceTool;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
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
            TargetMod.PACKET_HANDLER.sendToServer(new FireMessage(1));
            TargetMod.PACKET_HANDLER.sendToServer(new VehicleFireMessage(1));
        }
        if (button == GLFW.GLFW_MOUSE_BUTTON_RIGHT) {
            if (Minecraft.getInstance().player.hasEffect(TargetModMobEffects.SHOCK.get())) {
                event.setCanceled(true);
            }

            TargetMod.PACKET_HANDLER.sendToServer(new ZoomMessage(1));
        }
    }

    @SubscribeEvent
    public static void onButtonPressed(InputEvent.MouseButton.Pre event) {
        if (notInGame()) return;
        if (event.getAction() != InputConstants.PRESS) return;

        Player player = Minecraft.getInstance().player;
        if (player == null) return;
        ItemStack stack = player.getMainHandItem();

        int button = event.getButton();

        if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
            if (Minecraft.getInstance().player.hasEffect(TargetModMobEffects.SHOCK.get())) {
                event.setCanceled(true);
                return;
            }
            if (stack.is(TargetModItems.MONITOR.get())) {
                event.setCanceled(true);
                TargetMod.PACKET_HANDLER.sendToServer(new DroneFireMessage(0));
            }
            if (player.getVehicle() != null && player.getVehicle() instanceof Mk42Entity) {
                event.setCanceled(true);
                TargetMod.PACKET_HANDLER.sendToServer(new VehicleFireMessage(0));
                return;
            }
            if (player.getMainHandItem().is(TargetModTags.Items.GUN)) {
                event.setCanceled(true);
                TargetMod.PACKET_HANDLER.sendToServer(new FireMessage(0));
            }
        }

        if (button == GLFW.GLFW_MOUSE_BUTTON_RIGHT) {
            if (Minecraft.getInstance().player.hasEffect(TargetModMobEffects.SHOCK.get())) {
                event.setCanceled(true);
            }
            if (player.getMainHandItem().is(TargetModTags.Items.GUN) || (player.isPassenger() && player.getVehicle() instanceof Mk42Entity)) {
                event.setCanceled(true);
                TargetMod.PACKET_HANDLER.sendToServer(new ZoomMessage(0));
            }
        }

        if (button == GLFW.GLFW_MOUSE_BUTTON_MIDDLE) {
            if (Minecraft.getInstance().player.hasEffect(TargetModMobEffects.SHOCK.get())) {
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

        if (stack.is(TargetModTags.Items.GUN) && (player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).zoom) {
            var tag = stack.getOrCreateTag();
            if (tag.getDouble("min_zoom") != 0 && tag.getDouble("max_zoom") != 0) {
                TargetMod.PACKET_HANDLER.sendToServer(new AdjustZoomFovMessage(scroll));
            }
            event.setCanceled(true);
        }

        if (player.hasEffect(TargetModMobEffects.SHOCK.get())) {
            event.setCanceled(true);
        }

        Entity looking = TraceTool.findLookingEntity(player, 6);
        if (looking == null) return;
        if (looking instanceof MortarEntity && player.isShiftKeyDown()) {
            TargetMod.PACKET_HANDLER.sendToServer(new AdjustMortarAngleMessage(scroll));
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
        if (event.getAction() != InputConstants.PRESS) return;
        setKeyState(event.getKey(), 1);

        int button = event.getKey();
        if (button == GLFW.GLFW_KEY_A) {
            TargetMod.PACKET_HANDLER.sendToServer(new DroneMoveLeftMessage(true));
        }
        if (button == GLFW.GLFW_KEY_D) {
            TargetMod.PACKET_HANDLER.sendToServer(new DroneMoveRightMessage(true));
        }
        if (button == GLFW.GLFW_KEY_W) {
            TargetMod.PACKET_HANDLER.sendToServer(new DroneMoveForwardMessage(true));
        }
        if (button == GLFW.GLFW_KEY_S) {
            TargetMod.PACKET_HANDLER.sendToServer(new DroneMoveBackwardMessage(true));
        }
        if (button == GLFW.GLFW_KEY_SPACE) {
            TargetMod.PACKET_HANDLER.sendToServer(new DroneMoveUpMessage(true));
        }
        if (button == GLFW.GLFW_KEY_LEFT_CONTROL) {
            TargetMod.PACKET_HANDLER.sendToServer(new DroneMoveDownMessage(true));
        }
    }

    @SubscribeEvent
    public static void onKeyReleased(InputEvent.Key event) {
        if (notInGame()) return;
        if (event.getAction() != InputConstants.RELEASE) return;
        setKeyState(event.getKey(), 0);

        int button = event.getKey();
        if (button == GLFW.GLFW_KEY_A) {
            TargetMod.PACKET_HANDLER.sendToServer(new DroneMoveLeftMessage(false));
        }
        if (button == GLFW.GLFW_KEY_D) {
            TargetMod.PACKET_HANDLER.sendToServer(new DroneMoveRightMessage(false));
        }
        if (button == GLFW.GLFW_KEY_W) {
            TargetMod.PACKET_HANDLER.sendToServer(new DroneMoveForwardMessage(false));
        }
        if (button == GLFW.GLFW_KEY_S) {
            TargetMod.PACKET_HANDLER.sendToServer(new DroneMoveBackwardMessage(false));
        }
        if (button == GLFW.GLFW_KEY_SPACE) {
            TargetMod.PACKET_HANDLER.sendToServer(new DroneMoveUpMessage(false));
        }
        if (button == GLFW.GLFW_KEY_LEFT_CONTROL) {
            TargetMod.PACKET_HANDLER.sendToServer(new DroneMoveDownMessage(false));
        }
    }
}