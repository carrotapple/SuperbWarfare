package net.mcreator.superbwarfare.client.screens;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.mcreator.superbwarfare.ModUtils;
import net.mcreator.superbwarfare.init.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class JavelinHudOverlay {

    private static float scopeScale = 1;

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void eventHandler(RenderGuiEvent.Pre event) {
        int w = event.getWindow().getGuiScaledWidth();
        int h = event.getWindow().getGuiScaledHeight();
        Player player = Minecraft.getInstance().player;
        if (player != null) {
            ItemStack stack = player.getMainHandItem();

            if ((stack.getItem() == ModItems.JAVELIN.get() && !stack.getOrCreateTag().getBoolean("HoloHidden")) && Minecraft.getInstance().options.getCameraType().isFirstPerson() && GLFW.glfwGetMouseButton(Minecraft.getInstance().getWindow().getWindow(), GLFW.GLFW_MOUSE_BUTTON_RIGHT) == GLFW.GLFW_PRESS) {
                RenderSystem.disableDepthTest();
                RenderSystem.depthMask(false);
                RenderSystem.enableBlend();
                RenderSystem.setShader(GameRenderer::getPositionTexShader);
                RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
                RenderSystem.setShaderColor(1, 1, 1, 1);
                float deltaFrame = Minecraft.getInstance().getDeltaFrameTime();
                scopeScale = Mth.lerp(0.5F * deltaFrame, scopeScale, 1.35F);
                float f = (float)Math.min(w, h);
                float f1 = Math.min((float)w / f, (float)h / f) * scopeScale;
                int i = Mth.floor(f * f1);
                int j = Mth.floor(f * f1);
                int k = (w - i) / 2;
                int l = (h - j) / 2;
                int i1 = k + i;
                int j1 = l + j;
                event.getGuiGraphics().blit(new ResourceLocation(ModUtils.MODID, "textures/screens/javelin/javelin_hud.png"), k, l, -90, 0.0F, 0.0F, i, j, i, j);
                event.getGuiGraphics().blit(new ResourceLocation(ModUtils.MODID, stack.getOrCreateTag().getBoolean("TopMode") ? "textures/screens/javelin/top.png" : "textures/screens/javelin/dir.png"), k, l, -90, 0.0F, 0.0F, i, j, i, j);
                event.getGuiGraphics().blit(new ResourceLocation(ModUtils.MODID, stack.getOrCreateTag().getInt("ammo") > 0 ? "textures/screens/javelin/missile_green.png" : "textures/screens/javelin/missile_red.png"), k, l, -90, 0.0F, 0.0F, i, j, i, j);
                if (stack.getOrCreateTag().getInt("SeekTime") > 1 && stack.getOrCreateTag().getInt("SeekTime") < 20) {
                    event.getGuiGraphics().blit(new ResourceLocation(ModUtils.MODID, "textures/screens/javelin/seek.png"), k, l, -90, 0.0F, 0.0F, i, j, i, j);
                }
                event.getGuiGraphics().fill(RenderType.guiOverlay(), 0, l, k, j1, -90, -16777216);
                event.getGuiGraphics().fill(RenderType.guiOverlay(), i1, l, w, j1, -90, -16777216);
                RenderSystem.depthMask(true);
                RenderSystem.defaultBlendFunc();
                RenderSystem.enableDepthTest();
                RenderSystem.disableBlend();
                RenderSystem.setShaderColor(1, 1, 1, 1);
            } else {
                scopeScale = 1;
            }
        }
    }
}
