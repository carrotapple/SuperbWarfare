package net.mcreator.superbwarfare.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.mcreator.superbwarfare.ModUtils;
import net.mcreator.superbwarfare.entity.ICannonEntity;
import net.mcreator.superbwarfare.entity.MortarEntity;
import net.mcreator.superbwarfare.init.ModItems;
import net.mcreator.superbwarfare.init.ModMobEffects;
import net.mcreator.superbwarfare.init.ModSounds;
import net.mcreator.superbwarfare.init.ModTags;
import net.mcreator.superbwarfare.item.common.ammo.CannonShellItem;
import net.mcreator.superbwarfare.network.ModVariables;
import net.mcreator.superbwarfare.network.message.*;
import net.mcreator.superbwarfare.tools.TraceTool;
import net.minecraft.client.Minecraft;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
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
        }
        if (button == GLFW.GLFW_MOUSE_BUTTON_RIGHT) {
            if (player.hasEffect(ModMobEffects.SHOCK.get())) {
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
            if (player.hasEffect(ModMobEffects.SHOCK.get())) {
                event.setCanceled(true);
                return;
            }
            if (stack.is(ModItems.MONITOR.get())) {
                event.setCanceled(true);
                ModUtils.PACKET_HANDLER.sendToServer(new DroneFireMessage(0));
            }
            if (player.getVehicle() != null && player.getVehicle() instanceof ICannonEntity && player.getMainHandItem().getItem() instanceof CannonShellItem) {
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
            if (player.hasEffect(ModMobEffects.SHOCK.get())) {
                event.setCanceled(true);
                return;
            }
            if (player.getMainHandItem().is(ModTags.Items.GUN) || (player.isPassenger() && player.getVehicle() instanceof ICannonEntity)) {
                event.setCanceled(true);
                ModUtils.PACKET_HANDLER.sendToServer(new ZoomMessage(0));
            }
        }

        if (button == GLFW.GLFW_MOUSE_BUTTON_MIDDLE) {
            if (player.hasEffect(ModMobEffects.SHOCK.get())) {
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

        if (stack.is(ModTags.Items.GUN) && player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables()).zoom) {
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

    @SubscribeEvent
    public static void onKeyPressed(InputEvent.Key event) {
        if (notInGame()) return;

        Player player = Minecraft.getInstance().player;
        if (player == null) return;
        if (player.isSpectator()) return;

        setKeyState(event);

        int key = event.getKey();
        if (key == Minecraft.getInstance().options.keyJump.getKey().getValue()) {
            handleDoubleJump(player);
        }
    }

    private static void setKeyState(InputEvent.Key event) {
        int key = event.getKey();
        int state;
        if (event.getAction() == InputConstants.PRESS) {
            state = 1;
        } else if (event.getAction() == InputConstants.RELEASE) {
            state = 0;
        } else {
            return;
        }

        Player player = Minecraft.getInstance().player;
        if (player == null) return;

        handleDroneMove(key, state, player);
    }

    private static void handleDroneMove(int key, int state, Player player) {
        ItemStack stack = player.getMainHandItem();
        if (!stack.is(ModItems.MONITOR.get())) return;
        if (!stack.getOrCreateTag().getBoolean("Using")) return;
        if (!stack.getOrCreateTag().getBoolean("Linked")) return;

        var options = Minecraft.getInstance().options;

        if (key == options.keyLeft.getKey().getValue()) {
            ModUtils.PACKET_HANDLER.sendToServer(new DroneMovementMessage(0, state == 1));
        } else if (key == options.keyRight.getKey().getValue()) {
            ModUtils.PACKET_HANDLER.sendToServer(new DroneMovementMessage(1, state == 1));
        } else if (key == options.keyUp.getKey().getValue()) {
            ModUtils.PACKET_HANDLER.sendToServer(new DroneMovementMessage(2, state == 1));
        } else if (key == options.keyDown.getKey().getValue()) {
            ModUtils.PACKET_HANDLER.sendToServer(new DroneMovementMessage(3, state == 1));
        } else if (key == options.keyJump.getKey().getValue()) {
            ModUtils.PACKET_HANDLER.sendToServer(new DroneMovementMessage(4, state == 1));
        } else if (key == options.keyShift.getKey().getValue()) {
            ModUtils.PACKET_HANDLER.sendToServer(new DroneMovementMessage(5, state == 1));
        }
    }

    private static void handleDoubleJump(Player player) {
        Level level = player.level();
        double x = player.getX();
        double y = player.getY();
        double z = player.getZ();

        if (!level.isLoaded(player.blockPosition())) {
            return;
        }

        if (player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables()).playerDoubleJump) {
            player.setDeltaMovement(new Vec3(player.getLookAngle().x, 0.8, player.getLookAngle().z));
            level.playLocalSound(x, y, z, ModSounds.DOUBLE_JUMP.get(), SoundSource.BLOCKS, 1, 1, false);

            ModUtils.PACKET_HANDLER.sendToServer(new DoubleJumpMessage(false));
        }
    }

}