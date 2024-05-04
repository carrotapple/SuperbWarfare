
package net.mcreator.target.client.screens;

import org.checkerframework.checker.units.qual.h;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.Minecraft;
import net.mcreator.target.init.TargetModAttributes;

import net.mcreator.target.network.TargetModVariables;

import net.mcreator.target.procedures.CrosshairXianShiYouXiNeiDieJiaCengProcedure;

import net.minecraft.client.gui.GuiGraphics;
import com.mojang.blaze3d.systems.RenderSystem;
import org.joml.Matrix4f;
import com.mojang.blaze3d.vertex.BufferUploader;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.platform.GlStateManager;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.Tesselator;

@Mod.EventBusSubscriber({Dist.CLIENT})
public class CrosshairOverlay {
	@SubscribeEvent(priority = EventPriority.NORMAL)
	public static void eventHandler(RenderGuiEvent.Pre event) {
		int w = event.getWindow().getGuiScaledWidth();
		int h = event.getWindow().getGuiScaledHeight();
		Level world = null;
		double x = 0;
		double y = 0;
		double z = 0;
		Player entity = Minecraft.getInstance().player;

		double spread = 0;
		spread = entity.getAttribute(TargetModAttributes.SPREAD.get()).getBaseValue();

		double hitind = 0;
		hitind = (entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).hitind;

		double headind = 0;
		headind = (entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).headind;

		double killind = 0;
		killind = (entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).killind;

		if (entity != null) {
			world = entity.level();
			x = entity.getX();
			y = entity.getY();
			z = entity.getZ();
		}
		RenderSystem.disableDepthTest();
		RenderSystem.depthMask(false);
		RenderSystem.enableBlend();
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		RenderSystem.setShaderColor(1, 1, 1, 1);
		if (CrosshairXianShiYouXiNeiDieJiaCengProcedure.execute(entity)) {
			preciseBlit(event.getGuiGraphics(), new ResourceLocation("target:textures/screens/point.png"), w / 2 + -7.5f, h / 2 + -8, 0, 0, 16, 16, 16, 16);

			preciseBlit(event.getGuiGraphics(), new ResourceLocation("target:textures/screens/rexheng.png"), w / 2 + -9.5f - 2.8f * (float)spread, h / 2 + -8, 0, 0, 16, 16, 16, 16);

			preciseBlit(event.getGuiGraphics(), new ResourceLocation("target:textures/screens/rexheng.png"), w / 2 + -6.5f + 2.8f * (float)spread, h / 2 + -8, 0, 0, 16, 16, 16, 16);

			preciseBlit(event.getGuiGraphics(), new ResourceLocation("target:textures/screens/rexshu.png"), w / 2 + -7.5f, h / 2 + -7 + 2.8f * (float)spread, 0, 0, 16, 16, 16, 16);

			preciseBlit(event.getGuiGraphics(), new ResourceLocation("target:textures/screens/rexshu.png"), w / 2 + -7.5f, h / 2 + -10 - 2.8f * (float)spread, 0, 0, 16, 16, 16, 16);
		}

		float ww = 0;
		float hh = 0;
		float m = 0;
		ww = w / 2 + -7.5f + (float)(2 * (Math.random() - 0.5f));
		hh = h / 2 + -8 + (float)(2 * (Math.random() - 0.5f));
		m = (float)((40 - killind)/5.5f);
		
		if (hitind > 0) {			
			preciseBlit(event.getGuiGraphics(), new ResourceLocation("target:textures/screens/hit_marker.png"), ww, hh, 0, 0, 16, 16, 16, 16);
		}

		if (headind > 0) {			
			preciseBlit(event.getGuiGraphics(), new ResourceLocation("target:textures/screens/headshotmark.png"), ww, hh, 0, 0, 16, 16, 16, 16);
		}

		if (killind > 0) {			
			preciseBlit(event.getGuiGraphics(), new ResourceLocation("target:textures/screens/kill_mark1.png"), w / 2 + -7.5f -2 + m, h / 2 + -8 -2 + m, 0, 0, 16, 16, 16, 16);

			preciseBlit(event.getGuiGraphics(), new ResourceLocation("target:textures/screens/kill_mark2.png"), w / 2 + -7.5f +2 - m, h / 2 + -8 -2 + m, 0, 0, 16, 16, 16, 16);

			preciseBlit(event.getGuiGraphics(), new ResourceLocation("target:textures/screens/kill_mark3.png"), w / 2 + -7.5f -2 + m, h / 2 + -8 +2 - m, 0, 0, 16, 16, 16, 16);

			preciseBlit(event.getGuiGraphics(), new ResourceLocation("target:textures/screens/kill_mark4.png"), w / 2 + -7.5f +2 - m, h / 2 + -8 +2 - m, 0, 0, 16, 16, 16, 16);
		}

		RenderSystem.depthMask(true);
		RenderSystem.defaultBlendFunc();
		RenderSystem.enableDepthTest();
		RenderSystem.disableBlend();
		RenderSystem.setShaderColor(1, 1, 1, 1);
	}

	private static void preciseBlit(GuiGraphics gui,ResourceLocation pAtlasLocation, float pX, float pY, float pUOffset, float pVOffset, float pWidth, float pHeight, float pTextureWidth, float pTextureHeight) {
   float pX1 = pX;
   float pX2 = pX + pWidth;
   float pY1 = pY;
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
   bufferbuilder.vertex(matrix4f, pX1, pY1, pBlitOffset).uv(pMinU, pMinV).endVertex();
   bufferbuilder.vertex(matrix4f, pX1, pY2, pBlitOffset).uv(pMinU, pMaxV).endVertex();
   bufferbuilder.vertex(matrix4f, pX2, pY2, pBlitOffset).uv(pMaxU, pMaxV).endVertex();
   bufferbuilder.vertex(matrix4f, pX2, pY1, pBlitOffset).uv(pMaxU, pMinV).endVertex();
   BufferUploader.drawWithShader(bufferbuilder.end());
}
}
