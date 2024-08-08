package net.mcreator.superbwarfare.entity.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.mcreator.superbwarfare.ModUtils;
import net.mcreator.superbwarfare.entity.ProjectileEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public class ProjectileEntityLayer extends GeoRenderLayer<ProjectileEntity> {
    private static final ResourceLocation LAYER = new ResourceLocation(ModUtils.MODID, "textures/entity/projectile_entity.png");

    public ProjectileEntityLayer(GeoRenderer<ProjectileEntity> entityRenderer) {
        super(entityRenderer);
    }

    // TODO 解决RGB颜色问题
    @Override
    public void render(PoseStack poseStack, ProjectileEntity animatable, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        RenderType glowRenderType = RenderType.eyes(LAYER);
        getRenderer().reRender(getDefaultBakedModel(animatable), poseStack, bufferSource, animatable, glowRenderType,
                bufferSource.getBuffer(glowRenderType), partialTick, packedLight, OverlayTexture.NO_OVERLAY,
                animatable.getEntityData().get(ProjectileEntity.COLOR_R),
                animatable.getEntityData().get(ProjectileEntity.COLOR_G),
                animatable.getEntityData().get(ProjectileEntity.COLOR_B),
                0.8f);
    }
}
