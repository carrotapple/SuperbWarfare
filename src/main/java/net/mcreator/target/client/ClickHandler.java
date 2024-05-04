package net.mcreator.target.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;
import net.minecraft.tags.ItemTags;
import net.minecraft.resources.ResourceLocation;

import net.mcreator.target.network.FireMessage;
import net.mcreator.target.TargetMod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClickHandler {
    private static boolean isInGame() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null)
            return false;
        if (mc.getOverlay() != null)
            return false;
        if (mc.screen != null)
            return false;
        if (!mc.mouseHandler.isMouseGrabbed())
            return false;
        return mc.isWindowActive();
    }

    @SubscribeEvent
    public static void onKeyReleased(InputEvent.MouseButton.Pre event) {
        if(!isInGame()) {
            return;
        }

        if (event.getAction() != InputConstants.RELEASE) {
            return;
        }

        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player == null) {
            return;
        }

        ItemStack heldItem = player.getMainHandItem();

		int button = event.getButton();
		if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
                TargetMod.PACKET_HANDLER.sendToServer(new FireMessage(1, 0));
        }
        
    }  

    @SubscribeEvent
    public static void onKeyPressed(InputEvent.MouseButton.Pre event) {
        if(!isInGame()) {
            return;
        }

        if (event.getAction() != InputConstants.PRESS) {
            return;
        }

        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player == null) {
            return;
        }

        ItemStack heldItem = player.getMainHandItem();

		int button = event.getButton();
		if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
 				if ((player.getMainHandItem()).is(ItemTags.create(new ResourceLocation("target:gun")))) {
                event.setCanceled(true);
                TargetMod.PACKET_HANDLER.sendToServer(new FireMessage(0, 0));
            }
        }      
    }

    @SubscribeEvent
    public static void onKeyPressed(InputEvent.Key event) {
        if(!isInGame()) {
            return;
        }

        if (event.getAction() != InputConstants.PRESS) {
            return;
        }

        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;

		int button = event.getKey();
		if (button == GLFW.GLFW_KEY_D) {
			player.getPersistentData().putDouble("mover", 1);
		}  
		if (button == GLFW.GLFW_KEY_A) {
			player.getPersistentData().putDouble("movel", 1);
        }
        if (button == GLFW.GLFW_KEY_W) {
			player.getPersistentData().putDouble("qian", 1);
        } 
        if (button == GLFW.GLFW_KEY_W) {
			player.getPersistentData().putDouble("tui", 1);
        } 
    }
    
    @SubscribeEvent
    public static void onKeyReleased(InputEvent.Key event) {
        if(!isInGame()) {
            return;
        }

        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;

        if (event.getAction() != InputConstants.RELEASE) {
            return;
        }

		int button = event.getKey();
		if (button == GLFW.GLFW_KEY_D) {
            player.getPersistentData().putDouble("mover", 0);
        }
        if (button == GLFW.GLFW_KEY_A) {
            player.getPersistentData().putDouble("movel", 0);
        } 
        if (button == GLFW.GLFW_KEY_W) {
			player.getPersistentData().putDouble("qian", 0);
        } 
        if (button == GLFW.GLFW_KEY_W) {
			player.getPersistentData().putDouble("tui", 0);
        }
    }
}