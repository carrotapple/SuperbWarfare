package com.atsuishio.superbwarfare.client.overlay;

import com.atsuishio.superbwarfare.ModUtils;
import com.atsuishio.superbwarfare.entity.vehicle.Lav150Entity;
import com.atsuishio.superbwarfare.entity.vehicle.MobileVehicleEntity;
import com.atsuishio.superbwarfare.event.ClientEventHandler;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.joml.Math;

import java.text.DecimalFormat;

import static com.atsuishio.superbwarfare.client.RenderHelper.preciseBlit;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class LandArmorHudOverlay {
    private static float scopeScale = 1;
    private static final ResourceLocation FRAME = ModUtils.loc("textures/screens/land/tv_frame.png");

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void eventHandler(RenderGuiEvent.Pre event) {
        int w = event.getWindow().getGuiScaledWidth();
        int h = event.getWindow().getGuiScaledHeight();

        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        Camera camera = mc.gameRenderer.getMainCamera();
        Vec3 cameraPos = camera.getPosition();
        GuiGraphics guiGraphics = event.getGuiGraphics();
        PoseStack poseStack = guiGraphics.pose();

        if (player == null) return;
        if (player.getVehicle() instanceof Lav150Entity lav150 && player.getVehicle() instanceof MobileVehicleEntity mobileVehicle && lav150.isDriver(player)) {
            poseStack.pushPose();

            poseStack.translate(-6 * ClientEventHandler.turnRot[1],-6 * ClientEventHandler.turnRot[0],0);
            RenderSystem.disableDepthTest();
            RenderSystem.depthMask(false);
            RenderSystem.enableBlend();
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            RenderSystem.setShaderColor(1, 1, 1, 1);

            scopeScale = Mth.lerp(event.getPartialTick(), scopeScale, 1F);
            float f = (float) Math.min(w, h);
            float f1 = Math.min((float) w / f, (float) h / f) * scopeScale;
            float i = Mth.floor(f * f1);
            float j = Mth.floor(f * f1);
            float k = ((w - i) / 2);
            float l = ((h - j) / 2);

            if (Minecraft.getInstance().options.getCameraType() == CameraType.FIRST_PERSON) {
                int addW = (w / h) * 48;
                int addH = (w / h) * 27;
                preciseBlit(guiGraphics, FRAME, (float) -addW / 2, (float) -addH / 2, 0, 0.0F, w + addW, h + addH, w + addW, h + addH);
                preciseBlit(guiGraphics, ModUtils.loc("textures/screens/helicopter/heli_base.png"), k, l, 0, 0.0F, i, j, i, j);
                preciseBlit(guiGraphics, ModUtils.loc("textures/screens/compass.png"), (float) w / 2 - 128, (float) 6, 128 + ((float) 64 / 45 * player.getYRot()), 0, 256, 16, 512, 16);
                preciseBlit(guiGraphics, ModUtils.loc("textures/screens/helicopter/speed_frame.png"), (float) w / 2 - 144, (float) h / 2 - 6, 0, 0, 50, 18, 50, 18);
                guiGraphics.drawString(Minecraft.getInstance().font, Component.literal(new DecimalFormat("##").format(length(mobileVehicle.getDeltaMovement().x, mobileVehicle.getDeltaMovement().y, mobileVehicle.getDeltaMovement().z) * 72) + "KM/H"),
                        w / 2 - 140, h / 2, 0x66FF00, false);
                if (mobileVehicle.getEnergy() < 0.02 * mobileVehicle.getMaxEnergy()) {
                    guiGraphics.drawString(Minecraft.getInstance().font, Component.literal( "NO POWER!"),
                            w / 2 - 144, h / 2 + 14, -65536, false);
                } else if (mobileVehicle.getEnergy() < 0.2 * mobileVehicle.getMaxEnergy()) {
                    guiGraphics.drawString(Minecraft.getInstance().font, Component.literal( "LOW POWER"),
                            w / 2 - 144, h / 2 + 14, 0xFF6B00, false);
                }

            }

            poseStack.popPose();
        } else {
            scopeScale = 0.7f;
        }
    }



    public static double length(double x, double y, double z) {
        return Math.sqrt(x * x + y * y + z * z);
    }
}
