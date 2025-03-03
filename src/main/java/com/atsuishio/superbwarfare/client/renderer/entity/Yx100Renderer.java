package com.atsuishio.superbwarfare.client.renderer.entity;

import com.atsuishio.superbwarfare.client.model.entity.Yx100Model;
import com.atsuishio.superbwarfare.entity.vehicle.Yx100Entity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

import static com.atsuishio.superbwarfare.entity.vehicle.Yx100Entity.YAW;

public class Yx100Renderer extends GeoEntityRenderer<Yx100Entity> {

    public Yx100Renderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new Yx100Model());
//        this.addRenderLayer(new Yx100Layer(this));
    }

    @Override
    public RenderType getRenderType(Yx100Entity animatable, ResourceLocation texture, MultiBufferSource bufferSource, float partialTick) {
        return RenderType.entityTranslucent(getTextureLocation(animatable));
    }

    @Override
    public void preRender(PoseStack poseStack, Yx100Entity entity, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green,
                          float blue, float alpha) {
        float scale = 1f;
        this.scaleHeight = scale;
        this.scaleWidth = scale;
        super.preRender(poseStack, entity, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public void render(Yx100Entity entityIn, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferIn, int packedLightIn) {
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(-Mth.lerp(partialTicks, entityIn.yRotO, entityIn.getYRot())));
        super.render(entityIn, entityYaw, partialTicks, poseStack, bufferIn, packedLightIn);
        poseStack.popPose();
    }

    @Override
    public void renderRecursively(PoseStack poseStack, Yx100Entity animatable, GeoBone bone, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        String name = bone.getName();
        for (int i = 0; i < 8; i++) {
            if (name.equals("wheelL" + i)) {
                bone.setRotX(1.5f * Mth.lerp(partialTick, animatable.leftWheelRotO, animatable.getLeftWheelRot()));
            }
            if (name.equals("wheelR" + i)) {
                bone.setRotX(1.5f * Mth.lerp(partialTick, animatable.rightWheelRotO, animatable.getRightWheelRot()));
            }
        }

        if (name.equals("turret")) {
            bone.setRotY(Mth.lerp(partialTick, animatable.turretYRotO, animatable.getTurretYRot()) * Mth.DEG_TO_RAD);
        }
        if (name.equals("barrel")) {
            bone.setRotX(-Mth.lerp(partialTick, animatable.turretXRotO, animatable.getTurretXRot()) * Mth.DEG_TO_RAD);
        }

        if (name.equals("flare")) {
            bone.setRotZ((float) (0.5 * (Math.random() - 0.5)));
        }
        if (name.equals("flare2")) {
            bone.setRotZ((float) (0.5 * (Math.random() - 0.5)));
        }

        if (name.equals("leida") && animatable.getEnergy() > 0) {
            bone.setRotY((System.currentTimeMillis() % 36000000) / 300f);
        }

        if (name.equals("base")) {

            float a = animatable.getEntityData().get(YAW);
            float r = (Mth.abs(a) - 90f) / 90f;

            bone.setPosZ(r * Mth.lerp(partialTick, (float) animatable.recoilShakeO, (float) animatable.getRecoilShake()) * 1.5f);
            bone.setRotX(r * Mth.lerp(partialTick, (float) animatable.recoilShakeO, (float) animatable.getRecoilShake()) * Mth.DEG_TO_RAD);

            float r2;

            if (Mth.abs(a) <= 90f) {
                r2 = a / 90f;
            } else {
                if (a < 0) {
                    r2 = (180f + a) / 90f;
                } else {
                    r2 = (180f - a) / 90f;
                }
            }

            bone.setPosX(r2 * Mth.lerp(partialTick, (float) animatable.recoilShakeO, (float) animatable.getRecoilShake()) * 1f);
            bone.setRotZ(r2 * Mth.lerp(partialTick, (float) animatable.recoilShakeO, (float) animatable.getRecoilShake()) * Mth.DEG_TO_RAD * 1.75f);
        }

        super.renderRecursively(poseStack, animatable, bone, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
