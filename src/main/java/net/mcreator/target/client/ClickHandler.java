package net.mcreator.target.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.mcreator.target.TargetMod;
import net.mcreator.target.client.gui.RangeHelper;
import net.mcreator.target.entity.MortarEntity;
import net.mcreator.target.entity.Target1Entity;
import net.mcreator.target.entity.TargetEntity;
import net.mcreator.target.init.*;
import net.mcreator.target.item.gun.GunItem;
import net.mcreator.target.network.TargetModVariables;
import net.mcreator.target.network.message.*;
import net.mcreator.target.tools.SoundTool;
import net.mcreator.target.tools.TraceTool;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
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
        }
        if (button == GLFW.GLFW_MOUSE_BUTTON_RIGHT) {
            if (Minecraft.getInstance().player.hasEffect(TargetModMobEffects.SHOCK.get())) {
                event.setCanceled(true);
            }
            if (player.getMainHandItem().is(TargetModTags.Items.GUN)) {
                TargetMod.PACKET_HANDLER.sendToServer(new ZoomMessage(1));
            }
        }
    }

    @SubscribeEvent
    public static void onButtonPressed(InputEvent.MouseButton.Pre event) {
        if (notInGame()) return;
        if (event.getAction() != InputConstants.PRESS) return;

        Player player = Minecraft.getInstance().player;
        if (player == null) return;

        int button = event.getButton();

        if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
            if (Minecraft.getInstance().player.hasEffect(TargetModMobEffects.SHOCK.get())) {
                event.setCanceled(true);
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
            if (player.getMainHandItem().is(TargetModTags.Items.GUN)) {
                if (!TargetModKeyMappings.INTERACT.isDown()) {
                    event.setCanceled(true);
                }
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
        ItemStack stack = player.getMainHandItem();
        var tag = stack.getOrCreateTag();
        double scroll = event.getScrollDelta();

        if (notInGame()) return;
        if (player == null) return;

        if (stack.is(TargetModTags.Items.GUN) && (player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).zoom) {
            if (tag.getDouble("min_zoom") != 0 && tag.getDouble("max_zoom") != 0) {
                TargetMod.PACKET_HANDLER.sendToServer(new AdjustZoomFovMessage(scroll));
            }

            double min_zoom = tag.getDouble("min_zoom") - tag.getDouble("zoom");
            double max_zoom = tag.getDouble("max_zoom") - tag.getDouble("zoom");
            if (tag.getDouble("custom_zoom") > min_zoom && tag.getDouble("custom_zoom") < max_zoom) {
                SoundTool.playLocalSound(player, TargetModSounds.ADJUST_FOV.get(), 1f, 0.7f);
            }

            event.setCanceled(true);
        }

        Entity looking = TraceTool.findLookingEntity(player, 6);
        if (looking == null) return;
        if (looking instanceof MortarEntity && player.isShiftKeyDown()){

            TargetMod.PACKET_HANDLER.sendToServer(new AdjustMortarAngleMessage(scroll));
            double angle = 0;
            var range = -looking.getXRot();
            if (looking instanceof LivingEntity living){
                angle = living.getAttribute(TargetModAttributes.MORTAR_PITCH.get()).getBaseValue();
            }
            SoundTool.playLocalSound(player, TargetModSounds.ADJUST_FOV.get(), 1f, 0.7f);
            player.displayClientMessage(Component.literal("Angle:" + new java.text.DecimalFormat("##.#").format(angle) + " Range:" + new java.text.DecimalFormat("##.#").format((int) RangeHelper.getRange(range)) + "M"), true);

            event.setCanceled(true);
        }


        if (Minecraft.getInstance().player.hasEffect(TargetModMobEffects.SHOCK.get())) {
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
    }

    @SubscribeEvent
    public static void onKeyReleased(InputEvent.Key event) {
        if (notInGame()) return;
        if (event.getAction() != InputConstants.RELEASE) return;
        setKeyState(event.getKey(), 0);
    }
}