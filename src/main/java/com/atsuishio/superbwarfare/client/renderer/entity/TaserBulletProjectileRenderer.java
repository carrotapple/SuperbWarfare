package com.atsuishio.superbwarfare.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.atsuishio.superbwarfare.ModUtils;
import com.atsuishio.superbwarfare.client.model.entity.ModelTaserRod;
import com.atsuishio.superbwarfare.entity.projectile.TaserBulletProjectileEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class TaserBulletProjectileRenderer extends EntityRenderer<TaserBulletProjectileEntity> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(ModUtils.MODID, "textures/entity/taser_rod.png");
    private final ModelTaserRod<TaserBulletProjectileEntity> model;

    public TaserBulletProjectileRenderer(EntityRendererProvider.Context context) {
        super(context);
        model = new ModelTaserRod<>(context.bakeLayer(ModelTaserRod.LAYER_LOCATION));
    }

    @Override
    public void render(TaserBulletProjectileEntity entityIn, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferIn, int packedLightIn) {
        VertexConsumer vb = bufferIn.getBuffer(RenderType.entityCutout(this.getTextureLocation(entityIn)));
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTicks, entityIn.yRotO, entityIn.getYRot()) - 90));
        poseStack.mulPose(Axis.ZP.rotationDegrees(90 + Mth.lerp(partialTicks, entityIn.xRotO, entityIn.getXRot())));
        model.renderToBuffer(poseStack, vb, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 0.0625f);
        poseStack.popPose();
        super.render(entityIn, entityYaw, partialTicks, poseStack, bufferIn, packedLightIn);
    }

    @Override
    public ResourceLocation getTextureLocation(TaserBulletProjectileEntity entity) {
        return TEXTURE;
    }
}
