package com.atsuishio.superbwarfare.client.layer;

import com.atsuishio.superbwarfare.ModUtils;
import com.atsuishio.superbwarfare.entity.SpeedboatEntityMobile;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

import static com.atsuishio.superbwarfare.entity.SpeedboatEntityMobile.HEAT;

public class SpeedBoatHeatLayer extends GeoRenderLayer<SpeedboatEntityMobile> {
    private static final ResourceLocation LAYER = ModUtils.loc("textures/entity/speedboat_heat.png");

    public SpeedBoatHeatLayer(GeoRenderer<SpeedboatEntityMobile> entityRenderer) {
        super(entityRenderer);
    }

    @Override
    public void render(PoseStack poseStack, SpeedboatEntityMobile animatable, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        RenderType glowRenderType = RenderType.eyes(LAYER);
        float heat = animatable.getEntityData().get(HEAT) < 20 ? 0 : animatable.getEntityData().get(HEAT) - 20;
        getRenderer().reRender(getDefaultBakedModel(animatable), poseStack, bufferSource, animatable, glowRenderType, bufferSource.getBuffer(glowRenderType), partialTick, packedLight, OverlayTexture.NO_OVERLAY, heat / 80, heat / 80, heat / 80, 1);
    }
}

