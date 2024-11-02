package net.mcreator.superbwarfare.entity.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.mcreator.superbwarfare.ModUtils;
import net.mcreator.superbwarfare.entity.Mk42Entity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public class Mk42DamageLayer extends GeoRenderLayer<Mk42Entity> {

    private static final ResourceLocation LAYER = ModUtils.loc("textures/entity/sherman_damage.png");

    public Mk42DamageLayer(GeoRenderer<Mk42Entity> entityRenderer) {
        super(entityRenderer);
    }

    @Override
    public void render(PoseStack poseStack, Mk42Entity animatable, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        RenderType glowRenderType = RenderType.entityTranslucent(LAYER);
        float heal = Mth.clamp((300 - animatable.getEntityData().get(Mk42Entity.HEALTH)) * 0.0033f, 0.1f, 1);
        getRenderer().reRender(getDefaultBakedModel(animatable), poseStack, bufferSource, animatable, glowRenderType, bufferSource.getBuffer(glowRenderType), partialTick, packedLight, OverlayTexture.NO_OVERLAY, 1, 1, 1, heal);
    }
}
