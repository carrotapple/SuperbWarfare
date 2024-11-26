package com.atsuishio.superbwarfare.client.layer;

import com.atsuishio.superbwarfare.ModUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.atsuishio.superbwarfare.item.gun.sniper.SvdItem;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public class SvdLayer extends GeoRenderLayer<SvdItem> {
    private static final ResourceLocation LAYER = new ResourceLocation(ModUtils.MODID, "textures/item/svd_e.png");

    public SvdLayer(GeoRenderer<SvdItem> entityRenderer) {
        super(entityRenderer);
    }

    @Override
    public void render(PoseStack poseStack, SvdItem animatable, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        RenderType glowRenderType = RenderType.eyes(LAYER);
        getRenderer().reRender(getDefaultBakedModel(animatable), poseStack, bufferSource, animatable, glowRenderType, bufferSource.getBuffer(glowRenderType), partialTick, packedLight, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
    }
}
