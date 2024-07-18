package net.mcreator.target.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.mcreator.target.entity.MortarEntity;
import net.mcreator.target.entity.layer.MortarLayer;
import net.mcreator.target.entity.model.MortarModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class MortarRenderer extends GeoEntityRenderer<MortarEntity> {
    public MortarRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new MortarModel());
        this.shadowRadius = 0f;
        this.addRenderLayer(new MortarLayer(this));
    }

    @Override
    public RenderType getRenderType(MortarEntity animatable, ResourceLocation texture, MultiBufferSource bufferSource, float partialTick) {
        return RenderType.entityTranslucent(getTextureLocation(animatable));
    }

    @Override
    public void preRender(PoseStack poseStack, MortarEntity entity, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green,
                          float blue, float alpha) {
        float scale = 1f;
        this.scaleHeight = scale;
        this.scaleWidth = scale;
        super.preRender(poseStack, entity, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    protected float getDeathMaxRotation(MortarEntity entityLivingBaseIn) {
        return 0.0F;
    }
}
