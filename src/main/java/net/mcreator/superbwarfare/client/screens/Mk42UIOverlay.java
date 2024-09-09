package net.mcreator.superbwarfare.client.screens;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.mcreator.superbwarfare.ModUtils;
import net.mcreator.superbwarfare.entity.ICannonEntity;
import net.mcreator.superbwarfare.item.gun.GunItem;
import net.mcreator.superbwarfare.tools.RenderTool;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class Mk42UIOverlay {
    public static final int TEXTURE_WIDTH = 771;
    public static final int TEXTURE_HEIGHT = 1006;

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void eventHandler(RenderGuiEvent.Pre event) {
        int w = event.getWindow().getGuiScaledWidth();
        int h = event.getWindow().getGuiScaledHeight();
        Player player = Minecraft.getInstance().player;
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        RenderSystem.setShaderColor(1, 1, 1, 1);
        if (shouldRenderCrossHair(player)) {
            RenderTool.preciseBlit(event.getGuiGraphics(),
                    new ResourceLocation(ModUtils.MODID, "textures/screens/mk_42_rex.png"),
                    (float) w / 2 - (float) TEXTURE_WIDTH / 10f, (float) h / 2 - (float) TEXTURE_HEIGHT / 10f,
                    0, 0, TEXTURE_WIDTH / 5f, TEXTURE_HEIGHT / 5f, TEXTURE_WIDTH / 5f, TEXTURE_HEIGHT / 5f);
        }
        RenderSystem.depthMask(true);
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        RenderSystem.disableBlend();
        RenderSystem.setShaderColor(1, 1, 1, 1);
    }

    private static boolean shouldRenderCrossHair(Player player) {
        if (player == null) return false;
        return !player.isSpectator()
                && !(player.getMainHandItem().getItem() instanceof GunItem)
                && (player.getVehicle() != null && (player.getVehicle() instanceof ICannonEntity))
                && GLFW.glfwGetMouseButton(Minecraft.getInstance().getWindow().getWindow(), GLFW.GLFW_MOUSE_BUTTON_RIGHT) == GLFW.GLFW_PRESS;
    }
}
