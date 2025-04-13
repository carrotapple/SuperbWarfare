package com.atsuishio.superbwarfare.client;

import com.atsuishio.superbwarfare.event.ClientEventHandler;
import com.atsuishio.superbwarfare.item.gun.data.GunData;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationProcessor;

public class AnimationHelper {

    public static void renderPartOverBone(ModelPart model, GeoBone bone, PoseStack stack, VertexConsumer buffer, int packedLightIn, int packedOverlayIn, float alpha) {
        renderPartOverBone(model, bone, stack, buffer, packedLightIn, packedOverlayIn, 1.0f, 1.0f, 1.0f, alpha);
    }

    public static void renderPartOverBone(ModelPart model, GeoBone bone, PoseStack stack, VertexConsumer buffer, int packedLightIn, int packedOverlayIn, float r, float g, float b, float a) {
        setupModelFromBone(model, bone);
        model.render(stack, buffer, packedLightIn, packedOverlayIn, r, g, b, a);
    }

    public static void setupModelFromBone(ModelPart model, GeoBone bone) {
        model.setPos(bone.getPivotX(), bone.getPivotY(), bone.getPivotZ());
        model.xRot = 0.0f;
        model.yRot = 0.0f;
        model.zRot = 0.0f;
    }

    public static void renderPartOverBone2(ModelPart model, GeoBone bone, PoseStack stack, VertexConsumer buffer, int packedLightIn, int packedOverlayIn, float alpha) {
        renderPartOverBone2(model, bone, stack, buffer, packedLightIn, packedOverlayIn, 1.0f, 1.0f, 1.0f, alpha);
    }

    public static void renderPartOverBone2(ModelPart model, GeoBone bone, PoseStack stack, VertexConsumer buffer, int packedLightIn, int packedOverlayIn, float r, float g, float b, float a) {
        setupModelFromBone2(model, bone);
        model.render(stack, buffer, packedLightIn, packedOverlayIn, r, g, b, a);
    }

    public static void setupModelFromBone2(ModelPart model, GeoBone bone) {
        model.setPos(bone.getPivotX(), bone.getPivotY() + 7, bone.getPivotZ());
        model.xRot = 0.0f;
        model.yRot = 180 * Mth.DEG_TO_RAD;
        model.zRot = 180 * Mth.DEG_TO_RAD;
    }

    public static void handleShellsAnimation(AnimationProcessor<?> animationProcessor, float x, float y) {
        CoreGeoBone shell1 = animationProcessor.getBone("shell1");
        CoreGeoBone shell2 = animationProcessor.getBone("shell2");
        CoreGeoBone shell3 = animationProcessor.getBone("shell3");
        CoreGeoBone shell4 = animationProcessor.getBone("shell4");
        CoreGeoBone shell5 = animationProcessor.getBone("shell5");

        ClientEventHandler.handleShells(x, y, shell1, shell2, shell3, shell4, shell5);
    }

    public static void handleReloadShakeAnimation(ItemStack stack, CoreGeoBone main, CoreGeoBone camera, float roll, float pitch) {
        if (GunData.from(stack).reload.time() > 0) {
            main.setRotX(roll * main.getRotX());
            main.setRotY(roll * main.getRotY());
            main.setRotZ(roll * main.getRotZ());
            main.setPosX(pitch * main.getPosX());
            main.setPosY(pitch * main.getPosY());
            main.setPosZ(pitch * main.getPosZ());
            camera.setRotX(roll * camera.getRotX());
            camera.setRotY(roll * camera.getRotY());
            camera.setRotZ(roll * camera.getRotZ());
        }
    }
}
