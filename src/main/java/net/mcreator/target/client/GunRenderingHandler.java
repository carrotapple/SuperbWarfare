//package net.mcreator.target.client;
//
//import com.mojang.blaze3d.vertex.PoseStack;
//import com.mojang.math.Axis;
//import net.minecraft.client.Minecraft;
//import net.minecraft.util.Mth;
//import net.minecraft.world.entity.player.Player;
//import net.minecraftforge.api.distmarker.Dist;
//import net.minecraftforge.client.event.RenderHandEvent;
//import net.minecraftforge.eventbus.api.SubscribeEvent;
//import net.minecraftforge.fml.common.Mod;
//
//@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
//public class GunRenderingHandler {
//
//    @SubscribeEvent
//    public static void onRenderOverlay(RenderHandEvent event) {
//        PoseStack poseStack = event.getPoseStack();
//        poseStack.pushPose();
//        /* Applies custom bobbing animations */
//        applyBobbingTransforms(poseStack, event.getPartialTick());
//
//        poseStack.popPose();
//    }
//
//    private static void applyBobbingTransforms(PoseStack poseStack, float partialTicks) {
//        Minecraft mc = Minecraft.getInstance();
//        if (mc.options.bobView().get() && mc.getCameraEntity() instanceof Player player) {
//            float deltaDistanceWalked = player.walkDist - player.walkDistO;
//            float distanceWalked = -(player.walkDist + deltaDistanceWalked * partialTicks);
//            float bobbing = Mth.lerp(partialTicks, player.oBob, player.bob);
//
//            /* Reverses the original bobbing rotations and translations so it can be controlled */
//            poseStack.mulPose(Axis.XP.rotationDegrees(-(Math.abs(Mth.cos(distanceWalked * (float) Math.PI - 0.2F) * bobbing) * 5.0F)));
//            poseStack.mulPose(Axis.ZP.rotationDegrees(-(Mth.sin(distanceWalked * (float) Math.PI) * bobbing * 3.0F)));
//            poseStack.translate(-(Mth.sin(distanceWalked * (float) Math.PI) * bobbing * 0.5F), -(-Math.abs(Mth.cos(distanceWalked * (float) Math.PI) * bobbing)), 0.0D);
//        }
//    }
//}