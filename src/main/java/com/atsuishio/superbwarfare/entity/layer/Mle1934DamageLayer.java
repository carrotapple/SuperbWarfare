package com.atsuishio.superbwarfare.entity.layer;

import com.atsuishio.superbwarfare.ModUtils;
import com.atsuishio.superbwarfare.config.server.CannonConfig;
import com.atsuishio.superbwarfare.entity.Mle1934Entity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public class Mle1934DamageLayer extends GeoRenderLayer<Mle1934Entity> {

    private static final ResourceLocation LAYER = ModUtils.loc("textures/entity/mle1934_damage.png");

    public Mle1934DamageLayer(GeoRenderer<Mle1934Entity> entityRenderer) {
        super(entityRenderer);
    }

    @Override
    public void render(PoseStack poseStack, Mle1934Entity animatable, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        RenderType glowRenderType = RenderType.entityTranslucent(LAYER);
        float heal = Mth.clamp((0.3f * CannonConfig.MLE1934_HP.get() - animatable.getEntityData().get(Mle1934Entity.HEALTH)) * 0.000007f * CannonConfig.MLE1934_HP.get(), 0.1f, 1);
        getRenderer().reRender(getDefaultBakedModel(animatable), poseStack, bufferSource, animatable, glowRenderType, bufferSource.getBuffer(glowRenderType), partialTick, packedLight, OverlayTexture.NO_OVERLAY, 1, 1, 1, heal);
    }
}
