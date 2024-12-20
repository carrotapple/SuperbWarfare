package com.atsuishio.superbwarfare.client.renderer.block;

import com.atsuishio.superbwarfare.ModUtils;
import com.atsuishio.superbwarfare.block.entity.FuMO25BlockEntity;
import com.atsuishio.superbwarfare.client.model.block.FuMO25Model;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@SuppressWarnings("rawtypes")
@OnlyIn(Dist.CLIENT)
public class FuMO25BlockEntityRenderer implements BlockEntityRenderer<FuMO25BlockEntity> {

    public static final ResourceLocation TEXTURE = ModUtils.loc("textures/entity/fumo_25.png");
    private final FuMO25Model model;

    public FuMO25BlockEntityRenderer(BlockEntityRendererProvider.Context pContext) {
        this.model = new FuMO25Model(pContext.bakeLayer(FuMO25Model.LAYER_LOCATION));
    }

    @Override
    public void render(FuMO25BlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay) {
        pPoseStack.pushPose();

        pPoseStack.translate(0.5f, 1.8f, 0.5f);

        VertexConsumer vertexconsumer = ItemRenderer.getFoilBufferDirect(pBuffer, this.model.renderType(TEXTURE), false, false);
        this.model.render(pPoseStack, vertexconsumer, 0xffffff, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F, true);
        pPoseStack.popPose();
    }

}
