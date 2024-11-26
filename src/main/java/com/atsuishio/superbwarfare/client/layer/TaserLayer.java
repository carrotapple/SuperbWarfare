package com.atsuishio.superbwarfare.client.layer;

import com.atsuishio.superbwarfare.ModUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.atsuishio.superbwarfare.item.gun.special.TaserItem;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public class TaserLayer extends GeoRenderLayer<TaserItem> {
    private static final ResourceLocation LAYER = new ResourceLocation(ModUtils.MODID, "textures/item/tasergun_e.png");

    public TaserLayer(GeoRenderer<TaserItem> itemGeoRenderer) {
        super(itemGeoRenderer);
    }

    @Override
    public void render(PoseStack poseStack, TaserItem animatable, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int light, int packedOverlay) {

        RenderType glowRenderType = RenderType.entityTranslucent(LAYER);
        getRenderer().reRender(getDefaultBakedModel(animatable), poseStack, bufferSource, animatable, glowRenderType, bufferSource.getBuffer(glowRenderType), partialTick, 255, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
    }
}
