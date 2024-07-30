
package net.mcreator.superbwarfare.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.mcreator.superbwarfare.entity.RgoGrenadeEntity;
import net.mcreator.superbwarfare.entity.model.RgoGrenadeEntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class RgoGrenadeRenderer extends GeoEntityRenderer<RgoGrenadeEntity> {
	public RgoGrenadeRenderer(EntityRendererProvider.Context renderManager) {
		super(renderManager, new RgoGrenadeEntityModel());
		this.shadowRadius = 0.1f;
	}

	@Override
	public RenderType getRenderType(RgoGrenadeEntity animatable, ResourceLocation texture, MultiBufferSource bufferSource, float partialTick) {
		return RenderType.entityTranslucent(getTextureLocation(animatable));
	}

	@Override
	public void preRender(PoseStack poseStack, RgoGrenadeEntity entity, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green,
			float blue, float alpha) {
		float scale = 1f;
		this.scaleHeight = scale;
		this.scaleWidth = scale;
		super.preRender(poseStack, entity, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
	}

	@Override
	protected float getDeathMaxRotation(RgoGrenadeEntity entityLivingBaseIn) {
		return 0.0F;
	}
}
