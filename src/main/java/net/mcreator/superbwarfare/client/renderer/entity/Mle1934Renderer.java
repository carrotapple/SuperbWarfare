package net.mcreator.superbwarfare.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.mcreator.superbwarfare.entity.Mle1934Entity;
import net.mcreator.superbwarfare.entity.layer.Mle1934Layer;
import net.mcreator.superbwarfare.entity.model.Mle1934Model;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class Mle1934Renderer extends GeoEntityRenderer<Mle1934Entity> {
    public Mle1934Renderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new Mle1934Model());
        this.shadowRadius = 3f;
        this.addRenderLayer(new Mle1934Layer(this));
    }

    @Override
    public RenderType getRenderType(Mle1934Entity animatable, ResourceLocation texture, MultiBufferSource bufferSource, float partialTick) {
        return RenderType.entityTranslucent(getTextureLocation(animatable));
    }

    @Override
    public void preRender(PoseStack poseStack, Mle1934Entity entity, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green,
                          float blue, float alpha) {
        float scale = 1f;
        this.scaleHeight = scale;
        this.scaleWidth = scale;
        super.preRender(poseStack, entity, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    protected float getDeathMaxRotation(Mle1934Entity entityLivingBaseIn) {
        return 0.0F;
    }
}
