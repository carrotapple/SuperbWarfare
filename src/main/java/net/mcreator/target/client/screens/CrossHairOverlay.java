package net.mcreator.target.client.screens;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.mcreator.target.init.TargetModItems;
import net.mcreator.target.init.TargetModTags;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static net.mcreator.target.tools.RenderTool.preciseBlit;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class CrossHairOverlay {
    public static int HIT_INDICATOR = 0;
    public static int HEAD_INDICATOR = 0;
    public static int KILL_INDICATOR = 0;

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void eventHandler(RenderGuiEvent.Pre event) {
        int w = event.getWindow().getGuiScaledWidth();
        int h = event.getWindow().getGuiScaledHeight();
        Player entity = Minecraft.getInstance().player;
        if (entity == null) {
            return;
        }

        double spread = entity.getPersistentData().getDouble("crosshair");

        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        RenderSystem.setShaderColor(1, 1, 1, 1);

        if (shouldRenderCrossHair(entity)) {
            preciseBlit(event.getGuiGraphics(), new ResourceLocation("target:textures/screens/point.png"), w / 2f - 7.5f, h / 2f - 8, 0, 0, 16, 16, 16, 16);
            preciseBlit(event.getGuiGraphics(), new ResourceLocation("target:textures/screens/rexheng.png"), w / 2f - 9.5f - 2.8f * (float) spread, h / 2f - 8, 0, 0, 16, 16, 16, 16);
            preciseBlit(event.getGuiGraphics(), new ResourceLocation("target:textures/screens/rexheng.png"), w / 2f - 6.5f + 2.8f * (float) spread, h / 2f - 8, 0, 0, 16, 16, 16, 16);
            preciseBlit(event.getGuiGraphics(), new ResourceLocation("target:textures/screens/rexshu.png"), w / 2f - 7.5f, h / 2f - 7 + 2.8f * (float) spread, 0, 0, 16, 16, 16, 16);
            preciseBlit(event.getGuiGraphics(), new ResourceLocation("target:textures/screens/rexshu.png"), w / 2f - 7.5f, h / 2f - 10 - 2.8f * (float) spread, 0, 0, 16, 16, 16, 16);
        }

        float ww = w / 2f - 7.5f + (float) (2 * (Math.random() - 0.5f));
        float hh = h / 2f - 8 + (float) (2 * (Math.random() - 0.5f));
        float m = (40 - KILL_INDICATOR * 5) / 5.5f;

        if (HIT_INDICATOR > 0) {
            preciseBlit(event.getGuiGraphics(), new ResourceLocation("target:textures/screens/hit_marker.png"), ww, hh, 0, 0, 16, 16, 16, 16);
        }

        if (HEAD_INDICATOR > 0) {
            preciseBlit(event.getGuiGraphics(), new ResourceLocation("target:textures/screens/headshotmark.png"), ww, hh, 0, 0, 16, 16, 16, 16);
        }

        if (KILL_INDICATOR > 0) {
            preciseBlit(event.getGuiGraphics(), new ResourceLocation("target:textures/screens/kill_mark1.png"), w / 2f - 7.5f - 2 + m, h / 2f - 8 - 2 + m, 0, 0, 16, 16, 16, 16);
            preciseBlit(event.getGuiGraphics(), new ResourceLocation("target:textures/screens/kill_mark2.png"), w / 2f - 7.5f + 2 - m, h / 2f - 8 - 2 + m, 0, 0, 16, 16, 16, 16);
            preciseBlit(event.getGuiGraphics(), new ResourceLocation("target:textures/screens/kill_mark3.png"), w / 2f - 7.5f - 2 + m, h / 2f - 8 + 2 - m, 0, 0, 16, 16, 16, 16);
            preciseBlit(event.getGuiGraphics(), new ResourceLocation("target:textures/screens/kill_mark4.png"), w / 2f - 7.5f + 2 - m, h / 2f - 8 + 2 - m, 0, 0, 16, 16, 16, 16);
        }

        RenderSystem.depthMask(true);
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        RenderSystem.disableBlend();
        RenderSystem.setShaderColor(1, 1, 1, 1);
    }

    private static boolean shouldRenderCrossHair(Player player) {
        if (player == null) return false;

        if (player.isSpectator()) return false;
        if (!player.getMainHandItem().is(TargetModTags.Items.GUN) || !(player.getPersistentData().getDouble("zoom_animation_time") < 6))
            return false;

        return !(player.getMainHandItem().getItem() == TargetModItems.M_79.get())
                && Minecraft.getInstance().options.getCameraType() == CameraType.FIRST_PERSON;
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }

        handleRenderDamageIndicator();
    }

    private static void handleRenderDamageIndicator() {
        HEAD_INDICATOR = Math.max(0, HEAD_INDICATOR - 1);
        HIT_INDICATOR = Math.max(0, HIT_INDICATOR - 1);
        KILL_INDICATOR = Math.max(0, KILL_INDICATOR - 1);
    }
}
