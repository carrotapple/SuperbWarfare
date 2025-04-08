package com.atsuishio.superbwarfare.client.overlay;

import com.atsuishio.superbwarfare.ModUtils;
import com.atsuishio.superbwarfare.client.RenderHelper;
import com.atsuishio.superbwarfare.entity.vehicle.Yx100Entity;
import com.atsuishio.superbwarfare.event.ClientEventHandler;
import com.atsuishio.superbwarfare.tools.SeekTool;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.joml.Math;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class Yx100SwarmDroneHudOverlay {
    private static final ResourceLocation FRAME = ModUtils.loc("textures/screens/frame/frame.png");
    private static final ResourceLocation FRAME_LOCK = ModUtils.loc("textures/screens/frame/frame_lock.png");

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void eventHandler(RenderGuiEvent.Pre event) {
        int w = event.getWindow().getGuiScaledWidth();
        int h = event.getWindow().getGuiScaledHeight();
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        GuiGraphics guiGraphics = event.getGuiGraphics();
        PoseStack poseStack = guiGraphics.pose();

        if (!shouldRenderCrossHair(player)) return;

        Entity cannon = player.getVehicle();
        if (cannon == null) return;

        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        RenderSystem.setShaderColor(1, 1, 1, 1);

        if (player.getVehicle() instanceof Yx100Entity yx100 && yx100.banHand(player)) {
            if (Minecraft.getInstance().options.getCameraType() == CameraType.FIRST_PERSON) {
                float fovAdjust = (float) 70 / Minecraft.getInstance().options.fov().get();

                float f = (float) Math.min(w, h);
                float f1 = Math.min((float) w / f, (float) h / f) * fovAdjust;
                int i = Mth.floor(f * f1);
                int j = Mth.floor(f * f1);
                int k = (w - i) / 2;
                int l = (h - j) / 2;
                RenderHelper.preciseBlit(guiGraphics, ModUtils.loc("textures/screens/land/lav_missile_cross.png"), k, l, 0, 0.0F, i, j, i, j);
                VehicleHudOverlay.renderKillIndicator(guiGraphics, w, h);
                Entity naerestEntity = SeekTool.seekLivingEntity(player, player.level(), 384, 6);

                float fovAdjust2 = (float) (Minecraft.getInstance().options.fov().get() / 30) - 1;

                double zoom = 1;

                if (ClientEventHandler.zoomVehicle) {
                    zoom = Minecraft.getInstance().options.fov().get() / ClientEventHandler.fov + 0.05 * fovAdjust2;
                }

                if (naerestEntity != null) {
                    Vec3 playerVec = new Vec3(Mth.lerp(event.getPartialTick(), player.xo, player.getX()), Mth.lerp(event.getPartialTick(), player.yo + player.getEyeHeight(), player.getEyeY()), Mth.lerp(event.getPartialTick(), player.zo, player.getZ()));
                    Vec3 pos = new Vec3(Mth.lerp(event.getPartialTick(), naerestEntity.xo, naerestEntity.getX()), Mth.lerp(event.getPartialTick(), naerestEntity.yo + naerestEntity.getEyeHeight(), naerestEntity.getEyeY()), Mth.lerp(event.getPartialTick(), naerestEntity.zo, naerestEntity.getZ()));
                    Vec3 lookAngle = player.getLookAngle().normalize().scale(pos.distanceTo(playerVec) * (1 - 1.0 / zoom));

                    var cPos = playerVec.add(lookAngle);
                    Vec3 point = RenderHelper.worldToScreen(pos, cPos);
                    if (point == null) return;

                    poseStack.pushPose();
                    float x = (float) point.x;
                    float y = (float) point.y;

                    RenderHelper.blit(poseStack, FRAME_LOCK, x - 12, y - 12, 0, 0, 24, 24, 24, 24, 1f);
                    poseStack.popPose();
                }
            }
        }
    }

    private static boolean shouldRenderCrossHair(Player player) {
        if (player == null) return false;
        return !player.isSpectator()
                && player.getVehicle() instanceof Yx100Entity yx100 && yx100.getNthEntity(2) == player;
    }
}
