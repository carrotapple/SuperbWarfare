package com.atsuishio.superbwarfare.client.overlay;

import com.atsuishio.superbwarfare.ModUtils;
import com.atsuishio.superbwarfare.client.RenderHelper;
import com.atsuishio.superbwarfare.entity.vehicle.SpeedboatEntity;
import com.atsuishio.superbwarfare.entity.vehicle.Yx100Entity;
import com.atsuishio.superbwarfare.entity.vehicle.base.LandArmorEntity;
import com.atsuishio.superbwarfare.entity.vehicle.base.MobileVehicleEntity;
import com.atsuishio.superbwarfare.entity.vehicle.base.WeaponVehicleEntity;
import com.atsuishio.superbwarfare.event.ClientEventHandler;
import com.atsuishio.superbwarfare.init.ModItems;
import com.atsuishio.superbwarfare.tools.FormatTool;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
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

import static com.atsuishio.superbwarfare.client.RenderHelper.preciseBlit;
import static com.atsuishio.superbwarfare.client.overlay.VehicleHudOverlay.renderKillIndicator3P;
import static com.atsuishio.superbwarfare.entity.vehicle.Yx100Entity.MACHINE_GUN_HEAT;
import static com.atsuishio.superbwarfare.entity.vehicle.Yx100Entity.MG_AMMO;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class VehicleMgHudOverlay {

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void eventHandler(RenderGuiEvent.Pre event) {
        int w = event.getWindow().getGuiScaledWidth();
        int h = event.getWindow().getGuiScaledHeight();
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        GuiGraphics guiGraphics = event.getGuiGraphics();
        PoseStack poseStack = guiGraphics.pose();
        Camera camera = mc.gameRenderer.getMainCamera();
        Vec3 cameraPos = camera.getPosition();

        if (!shouldRenderCrossHair(player)) return;

        Entity cannon = player.getVehicle();
        if (cannon == null) return;

        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        RenderSystem.setShaderColor(1, 1, 1, 1);

        if (player.getVehicle() instanceof LandArmorEntity iLand && iLand instanceof WeaponVehicleEntity && iLand instanceof MobileVehicleEntity mobileVehicle) {
            if (Minecraft.getInstance().options.getCameraType() == CameraType.FIRST_PERSON || ClientEventHandler.zoomVehicle) {
                float fovAdjust = (float) 70 / Minecraft.getInstance().options.fov().get();

                float f = (float) Math.min(w, h);
                float f1 = Math.min((float) w / f, (float) h / f) * fovAdjust;
                int i = Mth.floor(f * f1);
                int j = Mth.floor(f * f1);
                int k = (w - i) / 2;
                int l = (h - j) / 2;
                RenderHelper.preciseBlit(guiGraphics, ModUtils.loc("textures/screens/cannon/cannon_crosshair_notzoom.png"), k, l, 0, 0.0F, i, j, i, j);
                VehicleHudOverlay.renderKillIndicator(guiGraphics, w, h);

            } else if (Minecraft.getInstance().options.getCameraType() == CameraType.THIRD_PERSON_BACK && !ClientEventHandler.zoomVehicle) {
                Vec3 p = RenderHelper.worldToScreen(new Vec3(Mth.lerp(event.getPartialTick(), player.xo, player.getX()), Mth.lerp(event.getPartialTick(), player.yo + player.getEyeHeight(), player.getEyeY()), Mth.lerp(event.getPartialTick(), player.zo, player.getZ())).add(iLand.getGunVec(event.getPartialTick()).scale(192)), cameraPos);
                // 第三人称准星
                if (p != null) {
                    poseStack.pushPose();
                    float x = (float) p.x;
                    float y = (float) p.y;

                    poseStack.pushPose();
                    preciseBlit(guiGraphics, ModUtils.loc("textures/screens/drone.png"), x - 12, y - 12, 0, 0, 24, 24, 24, 24);
                    renderKillIndicator3P(guiGraphics, x - 7.5f + (float) (2 * (Math.random() - 0.5f)), y - 7.5f + (float) (2 * (Math.random() - 0.5f)));

                    poseStack.pushPose();

                    poseStack.translate(x, y, 0);
                    poseStack.scale(0.75f, 0.75f, 1);


                    //YX-100
                    if (player.getVehicle() instanceof Yx100Entity yx100) {
                        double heat = yx100.getEntityData().get(MACHINE_GUN_HEAT) / 100.0F;
                        guiGraphics.drawString(mc.font, Component.literal(".50 HMG " + (player.getInventory().hasAnyMatching(s -> s.is(ModItems.CREATIVE_AMMO_BOX.get())) ? "∞" : yx100.getEntityData().get(MG_AMMO))), 30, -9, Mth.hsvToRgb(0F, (float) heat, 1.0F), false);
                    }

                    double heal = 1 - mobileVehicle.getHealth() / mobileVehicle.getMaxHealth();

                    guiGraphics.drawString(Minecraft.getInstance().font, Component.literal("HP " +
                            FormatTool.format0D(100 * mobileVehicle.getHealth() / mobileVehicle.getMaxHealth())), 30, 1, Mth.hsvToRgb(0F, (float) heal, 1.0F), false);

                    poseStack.popPose();
                    poseStack.popPose();
                    poseStack.popPose();
                }
            }

        }
    }

    private static boolean shouldRenderCrossHair(Player player) {
        if (player == null) return false;
        return !player.isSpectator()
                && (player.getVehicle() instanceof SpeedboatEntity
                || player.getVehicle() instanceof Yx100Entity yx100 && yx100.getNthEntity(1) == player);
    }
}
