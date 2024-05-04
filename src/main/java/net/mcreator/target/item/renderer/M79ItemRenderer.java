package net.mcreator.target.item.renderer;

import software.bernie.geckolib.util.RenderUtils;
import software.bernie.geckolib.renderer.GeoItemRenderer;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.cache.object.BakedGeoModel;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.Minecraft;

import net.mcreator.target.utils.AnimUtils;
import net.mcreator.target.item.model.M79ItemModel;
import net.mcreator.target.item.M79Item;

import net.mcreator.target.item.layer.M79Layer;

import java.util.Set;
import java.util.HashSet;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack;

public class M79ItemRenderer extends GeoItemRenderer<M79Item> {
	public M79ItemRenderer() {
		super(new M79ItemModel());
		this.addRenderLayer(new M79Layer(this));
	}

	@Override
	public RenderType getRenderType(M79Item animatable, ResourceLocation texture, MultiBufferSource bufferSource, float partialTick) {
		return RenderType.entityTranslucent(getTextureLocation(animatable));
	}

	private static final float SCALE_RECIPROCAL = 1.0f / 16.0f;
	protected boolean renderArms = false;
	protected MultiBufferSource currentBuffer;
	protected RenderType renderType;
	public ItemDisplayContext transformType;
	protected M79Item animatable;
	private final Set<String> hiddenBones = new HashSet<>();
	private final Set<String> suppressedBones = new HashSet<>();

	@Override
	public void renderByItem(ItemStack stack, ItemDisplayContext transformType, PoseStack matrixStack, MultiBufferSource bufferIn, int combinedLightIn, int p_239207_6_) {
		this.transformType = transformType;
		if (this.animatable != null)
			this.animatable.getTransformType(transformType);
		super.renderByItem(stack, transformType, matrixStack, bufferIn, combinedLightIn, p_239207_6_);
	}

	@Override
	public void actuallyRender(PoseStack matrixStackIn, M79Item animatable, BakedGeoModel model, RenderType type, MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, boolean isRenderer, float partialTicks, int packedLightIn,
			int packedOverlayIn, float red, float green, float blue, float alpha) {
		this.currentBuffer = renderTypeBuffer;
		this.renderType = type;
		this.animatable = animatable;
		super.actuallyRender(matrixStackIn, animatable, model, type, renderTypeBuffer, vertexBuilder, isRenderer, partialTicks, packedLightIn, packedOverlayIn, red, green, blue, alpha);
		if (this.renderArms) {
			this.renderArms = false;
		}
	}

	@Override
	public ResourceLocation getTextureLocation(M79Item instance) {
		return super.getTextureLocation(instance);
	}
}
