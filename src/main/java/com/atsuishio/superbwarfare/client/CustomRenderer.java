package com.atsuishio.superbwarfare.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.Item;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class CustomRenderer<T extends Item & GeoAnimatable> extends GeoItemRenderer<T> {

    public CustomRenderer(GeoModel<T> model) {
        super(model);
    }

    @Override
    public void renderChildBones(PoseStack poseStack, T animatable, GeoBone bone, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        if (bone.isHidingChildren())
            return;

        for (GeoBone childBone : bone.getChildBones()) {
            if (childBone.getName().endsWith("_illuminated")) {
                var type = ModRenderTypes.GUN_ILLUMINATED.apply(this.getTextureLocation(animatable));
                renderRecursively(poseStack, animatable, childBone, type, bufferSource, bufferSource.getBuffer(type), isReRender, partialTick, packedLight, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
            } else {
                renderRecursively(poseStack, animatable, childBone, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
            }
        }
    }
}
