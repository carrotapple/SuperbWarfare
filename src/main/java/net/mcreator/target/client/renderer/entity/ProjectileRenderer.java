package net.mcreator.target.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.mcreator.target.client.model.entity.ModelBullet;
import net.mcreator.target.entity.ProjectileEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class ProjectileRenderer extends EntityRenderer<ProjectileEntity> {
    private static final ResourceLocation texture = new ResourceLocation("target:textures/entity/bullet_tex.png");
    private static final ResourceLocation empty_texture = new ResourceLocation("target:textures/entity/empty.png");
    private final ModelBullet<ProjectileEntity> model;

    public ProjectileRenderer(EntityRendererProvider.Context context) {
        super(context);
        model = new ModelBullet<>(context.bakeLayer(ModelBullet.LAYER_LOCATION));
    }

    protected int getBlockLightLevel(ProjectileEntity pEntity, BlockPos pPos) {
        return 15;
    }

    @Override
    public void render(ProjectileEntity entityIn, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferIn, int packedLightIn) {
        VertexConsumer vb = bufferIn.getBuffer(RenderType.eyes(this.getTextureLocation(entityIn)));
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTicks, entityIn.yRotO, entityIn.getYRot()) - 90));
        poseStack.mulPose(Axis.ZP.rotationDegrees(90 + Mth.lerp(partialTicks, entityIn.xRotO, entityIn.getXRot())));
        model.renderToBuffer(poseStack, vb, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 0.0625f);
        poseStack.popPose();
        super.render(entityIn, entityYaw, partialTicks, poseStack, bufferIn, packedLightIn);
    }

    @Override
    public ResourceLocation getTextureLocation(ProjectileEntity entity) {
        if (entity.tickCount > 1){
            return texture;
        }
        return empty_texture;
    }
}
