
package com.atsuishio.superbwarfare.client.renderer.entity;

import com.atsuishio.superbwarfare.client.model.entity.DroneModel;
import com.atsuishio.superbwarfare.entity.vehicle.DroneEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class DroneRenderer extends GeoEntityRenderer<DroneEntity> {
	public DroneRenderer(EntityRendererProvider.Context renderManager) {
		super(renderManager, new DroneModel());
		this.shadowRadius = 0.2f;
	}

	@Override
	public RenderType getRenderType(DroneEntity animatable, ResourceLocation texture, MultiBufferSource bufferSource, float partialTick) {
		return RenderType.entityTranslucent(getTextureLocation(animatable));
	}

	@Override
	public void preRender(PoseStack poseStack, DroneEntity entity, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green,
			float blue, float alpha) {
		float scale = 1f;
		this.scaleHeight = scale;
		this.scaleWidth = scale;
		super.preRender(poseStack, entity, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
	}

	@Override
	public void render(DroneEntity entityIn, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferIn, int packedLightIn) {
		poseStack.pushPose();
		poseStack.mulPose(Axis.YP.rotationDegrees(-entityIn.getYaw(partialTicks)));
		poseStack.mulPose(Axis.XP.rotationDegrees(entityIn.getBodyPitch(partialTicks)));
		poseStack.mulPose(Axis.ZP.rotationDegrees(entityIn.getRoll(partialTicks)));
		super.render(entityIn, entityYaw, partialTicks, poseStack, bufferIn, packedLightIn);
		poseStack.popPose();
	}

	@Override
	public void renderRecursively(PoseStack poseStack, DroneEntity animatable, GeoBone bone, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		String name = bone.getName();
		if (name.equals("wingFL")) {
			bone.setRotY(bone.getRotY() + 2);
		}
		if (name.equals("wingFR")) {
			bone.setRotY(bone.getRotY() + 2);
		}
		if (name.equals("wingBL")) {
			bone.setRotY(bone.getRotY() + 2);
		}
		if (name.equals("wingBR")) {
			bone.setRotY(bone.getRotY() + 2);
		}
		super.renderRecursively(poseStack, animatable, bone, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
	}
}
