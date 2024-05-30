package net.mcreator.target.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.mcreator.target.TargetMod;
import net.mcreator.target.init.TargetModTags;
import net.mcreator.target.network.message.FireMessage;
import net.minecraft.client.Minecraft;
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
    public static void onKeyReleased(InputEvent.MouseButton.Pre event) {
        if (notInGame()) return;
        if (event.getAction() != InputConstants.RELEASE) return;

        Player player = Minecraft.getInstance().player;
        if (player == null) return;

        int button = event.getButton();
        if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
            TargetMod.PACKET_HANDLER.sendToServer(new FireMessage(1));
        }
    }

    @SubscribeEvent
    public static void onKeyPressed(InputEvent.MouseButton.Pre event) {
        if (notInGame()) return;
        if (event.getAction() != InputConstants.PRESS) return;

        Player player = Minecraft.getInstance().player;
        if (player == null) return;

        int button = event.getButton();
        if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT && player.getMainHandItem().is(TargetModTags.Items.GUN)) {
            event.setCanceled(true);
            TargetMod.PACKET_HANDLER.sendToServer(new FireMessage(0));
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
        Player player = Minecraft.getInstance().player;
        ItemStack stack = player.getMainHandItem();
        int button = event.getKey();
        var tag = stack.getOrCreateTag();

        if (notInGame()) return;
        if (event.getAction() != InputConstants.PRESS) return;
        setKeyState(event.getKey(), 1);


        if (stack.is(TargetModTags.Items.GUN)){
            if (button == GLFW.GLFW_KEY_PAGE_UP) {
                TargetMod.PACKET_HANDLER.sendToServer(new FireMessage(0));
                tag.putDouble("sensitivity", tag.getDouble("sensitivity") + 1);
            }
            if (button == GLFW.GLFW_KEY_PAGE_DOWN) {
                tag.putDouble("sensitivity", tag.getDouble("sensitivity") - 1);
            }
        }
    }

    @SubscribeEvent
    public static void onKeyReleased(InputEvent.Key event) {
        if (notInGame()) return;
        if (event.getAction() != InputConstants.RELEASE) return;
        setKeyState(event.getKey(), 0);
    }
}