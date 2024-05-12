package net.mcreator.target.client.screens;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.mcreator.target.init.TargetModAttributes;
import net.mcreator.target.init.TargetModItems;
import net.mcreator.target.network.TargetModVariables;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.joml.Matrix4f;

@Mod.EventBusSubscriber({Dist.CLIENT})
public class CrosshairOverlay {
    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void eventHandler(RenderGuiEvent.Pre event) {
        int w = event.getWindow().getGuiScaledWidth();
        int h = event.getWindow().getGuiScaledHeight();
        Player entity = Minecraft.getInstance().player;

        double spread = entity.getAttribute(TargetModAttributes.SPREAD.get()).getBaseValue();

        double hitind = (entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).hitIndicator;

        double headind = (entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).headIndicator;

        double killind = (entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).killIndicator;

        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        RenderSystem.setShaderColor(1, 1, 1, 1);
        if (shouldRenderCrosshair(entity)) {
            preciseBlit(event.getGuiGraphics(), new ResourceLocation("target:textures/screens/point.png"), w / 2 + -7.5f, h / 2 + -8, 0, 0, 16, 16, 16, 16);

            preciseBlit(event.getGuiGraphics(), new ResourceLocation("target:textures/screens/rexheng.png"), w / 2 + -9.5f - 2.8f * (float) spread, h / 2 + -8, 0, 0, 16, 16, 16, 16);

            preciseBlit(event.getGuiGraphics(), new ResourceLocation("target:textures/screens/rexheng.png"), w / 2 + -6.5f + 2.8f * (float) spread, h / 2 + -8, 0, 0, 16, 16, 16, 16);

            preciseBlit(event.getGuiGraphics(), new ResourceLocation("target:textures/screens/rexshu.png"), w / 2 + -7.5f, h / 2 + -7 + 2.8f * (float) spread, 0, 0, 16, 16, 16, 16);

            preciseBlit(event.getGuiGraphics(), new ResourceLocation("target:textures/screens/rexshu.png"), w / 2 + -7.5f, h / 2 + -10 - 2.8f * (float) spread, 0, 0, 16, 16, 16, 16);
        }

        float ww = w / 2 - 7.5f + (float) (2 * (Math.random() - 0.5f));
        float hh = h / 2 - 8 + (float) (2 * (Math.random() - 0.5f));
        float m = (float) ((40 - killind) / 5.5f);

        if (hitind > 0) {
            preciseBlit(event.getGuiGraphics(), new ResourceLocation("target:textures/screens/hit_marker.png"), ww, hh, 0, 0, 16, 16, 16, 16);
        }

        if (headind > 0) {
            preciseBlit(event.getGuiGraphics(), new ResourceLocation("target:textures/screens/headshotmark.png"), ww, hh, 0, 0, 16, 16, 16, 16);
        }

        if (killind > 0) {
            preciseBlit(event.getGuiGraphics(), new ResourceLocation("target:textures/screens/kill_mark1.png"), w / 2 + -7.5f - 2 + m, h / 2 + -8 - 2 + m, 0, 0, 16, 16, 16, 16);

            preciseBlit(event.getGuiGraphics(), new ResourceLocation("target:textures/screens/kill_mark2.png"), w / 2 + -7.5f + 2 - m, h / 2 + -8 - 2 + m, 0, 0, 16, 16, 16, 16);

            preciseBlit(event.getGuiGraphics(), new ResourceLocation("target:textures/screens/kill_mark3.png"), w / 2 + -7.5f - 2 + m, h / 2 + -8 + 2 - m, 0, 0, 16, 16, 16, 16);

            preciseBlit(event.getGuiGraphics(), new ResourceLocation("target:textures/screens/kill_mark4.png"), w / 2 + -7.5f + 2 - m, h / 2 + -8 + 2 - m, 0, 0, 16, 16, 16, 16);
        }

        RenderSystem.depthMask(true);
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        RenderSystem.disableBlend();
        RenderSystem.setShaderColor(1, 1, 1, 1);
    }

    private static void preciseBlit(GuiGraphics gui, ResourceLocation pAtlasLocation, float pX, float pY, float pUOffset, float pVOffset, float pWidth, float pHeight, float pTextureWidth, float pTextureHeight) {
        float pX2 = pX + pWidth;
        float pY2 = pY + pHeight;
        float pBlitOffset = 0;

        float pMinU = pUOffset / pTextureWidth;
        float pMaxU = (pUOffset + pWidth) / pTextureWidth;
        float pMinV = pVOffset / pTextureHeight;
        float pMaxV = (pVOffset + pHeight) / pTextureHeight;

        RenderSystem.setShaderTexture(0, pAtlasLocation);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        Matrix4f matrix4f = gui.pose().last().pose();
        BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        bufferbuilder.vertex(matrix4f, pX, pY, pBlitOffset).uv(pMinU, pMinV).endVertex();
        bufferbuilder.vertex(matrix4f, pX, pY2, pBlitOffset).uv(pMinU, pMaxV).endVertex();
        bufferbuilder.vertex(matrix4f, pX2, pY2, pBlitOffset).uv(pMaxU, pMaxV).endVertex();
        bufferbuilder.vertex(matrix4f, pX2, pY, pBlitOffset).uv(pMaxU, pMinV).endVertex();
        BufferUploader.drawWithShader(bufferbuilder.end());
    }

    private static boolean shouldRenderCrosshair(Player player) {
        if (player == null) return false;

        if (player.isSpectator()) return false;
        if (!player.getMainHandItem().is(ItemTags.create(new ResourceLocation("target:gun")))
                || !(player.getPersistentData().getDouble("zoom_time") < 7)
        ) return false;

        return !(player.getMainHandItem().getItem() == TargetModItems.M_79.get())
                && Minecraft.getInstance().options.getCameraType() == CameraType.FIRST_PERSON;
    }
}
