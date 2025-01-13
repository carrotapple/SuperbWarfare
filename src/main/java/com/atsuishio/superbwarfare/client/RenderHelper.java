package com.atsuishio.superbwarfare.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import javax.annotation.Nullable;

public class RenderHelper {

    public static void preciseBlit(GuiGraphics gui, ResourceLocation pAtlasLocation, float pX, float pY, float pUOffset, float pVOffset, float pWidth, float pHeight, float pTextureWidth, float pTextureHeight) {
        preciseBlit(gui, pAtlasLocation, pX, pY, 0, pUOffset, pVOffset, pWidth, pHeight, pTextureWidth, pTextureHeight);
    }

    public static void preciseBlit(GuiGraphics gui, ResourceLocation pAtlasLocation, float pX, float pY, float pBlitOffset, float pUOffset, float pVOffset, float pWidth, float pHeight, float pTextureWidth, float pTextureHeight) {
        float pX2 = pX + pWidth;
        float pY2 = pY + pHeight;

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

    /**
     * Codes based on @Xjqsh
     */
    @Nullable
    public static Vec3 worldToScreen(Vec3 pos, Vec3 cameraPos) {
        Minecraft minecraft = Minecraft.getInstance();
        Frustum frustum = minecraft.levelRenderer.getFrustum();

        Vector3f relativePos = pos.subtract(cameraPos).toVector3f();
        Vector3f transformedPos = frustum.matrix.transformProject(relativePos.x, relativePos.y, relativePos.z, new Vector3f());

        double scaleFactor = minecraft.getWindow().getGuiScale();
        float guiScaleMul = 0.5f / (float) scaleFactor;

        Vector3f screenPos = transformedPos.mul(1.0f, -1.0f, 1.0f).add(1.0f, 1.0f, 0.0f)
                .mul(guiScaleMul * minecraft.getWindow().getWidth(), guiScaleMul * minecraft.getWindow().getHeight(), 1.0f);

        return transformedPos.z < 1.0f ? new Vec3(screenPos.x, screenPos.y, transformedPos.z) : null;
    }

}
