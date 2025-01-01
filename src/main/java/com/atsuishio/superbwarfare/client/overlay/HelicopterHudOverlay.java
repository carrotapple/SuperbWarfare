package com.atsuishio.superbwarfare.client.overlay;

import com.atsuishio.superbwarfare.ModUtils;
import com.atsuishio.superbwarfare.entity.IHelicopterEntity;
import com.atsuishio.superbwarfare.entity.MobileVehicleEntity;
import com.atsuishio.superbwarfare.network.ModVariables;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Camera;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.text.DecimalFormat;

import static com.atsuishio.superbwarfare.client.RenderHelper.preciseBlit;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class HelicopterHudOverlay {

    private static float scopeScale = 1;
    private static float lerpVy = 1;
    private static float lerpPower = 1;

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void eventHandler(RenderGuiEvent.Pre event) {
        int w = event.getWindow().getGuiScaledWidth();
        int h = event.getWindow().getGuiScaledHeight();
        Player player = Minecraft.getInstance().player;
        Minecraft mc = Minecraft.getInstance();
        Camera camera = mc.gameRenderer.getMainCamera();
        Vec3 cameraPos = camera.getPosition();
        PoseStack poseStack = event.getGuiGraphics().pose();

        if (player != null) {

            if (player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables()).edit)
                return;

            if (player.getVehicle() instanceof IHelicopterEntity iHelicopterEntity && player.getVehicle() instanceof MobileVehicleEntity mobileVehicle && iHelicopterEntity.isDriver(player) && Minecraft.getInstance().options.getCameraType() == CameraType.FIRST_PERSON) {
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
                preciseBlit(event.getGuiGraphics(), ModUtils.loc("textures/screens/helicopter/heli_base.png"), k, l, 0, 0.0F, i, j, i, j);

                poseStack.pushPose();
                poseStack.rotateAround(Axis.ZP.rotationDegrees(-iHelicopterEntity.getRotZ(event.getPartialTick())),w / 2f, h / 2f, 0);
                float pitch = iHelicopterEntity.getRotX(event.getPartialTick());
//                player.displayClientMessage(Component.literal("Angle:" + new java.text.DecimalFormat("##").format(pitch)), true);
                preciseBlit(event.getGuiGraphics(), ModUtils.loc("textures/screens/helicopter/heli_line.png"), (float) w / 2 - 128, (float) h / 2 - 512 - 5.475f * pitch, 0, 0, 256, 1024, 256, 1024);
                poseStack.popPose();
                poseStack.pushPose();
                poseStack.rotateAround(Axis.ZP.rotationDegrees(-iHelicopterEntity.getRotZ(event.getPartialTick())),w / 2f, h / 2f - 56, 0);
                preciseBlit(event.getGuiGraphics(), ModUtils.loc("textures/screens/helicopter/roll_ind.png"), (float) w / 2 - 8, (float) h / 2 - 88, 0, 0, 16, 16, 16, 16);
                poseStack.popPose();

                event.getGuiGraphics().blit(ModUtils.loc("textures/screens/helicopter/heli_power_ruler.png"), w / 2 + 100, h / 2 - 64, 0, 0, 64, 128, 64, 128);

                float power = iHelicopterEntity.getPower();
                lerpPower = Mth.lerp(0.001f * event.getPartialTick(), lerpPower, power);
                preciseBlit(event.getGuiGraphics(), ModUtils.loc("textures/screens/helicopter/heli_power.png"),  (float) w / 2 + 130f,  ((float) h / 2 - 64 + 124 - power * 900), 0, 0, 4, power * 900, 4, power * 900);
                lerpVy = (float) Mth.lerp(0.021f * event.getPartialTick(), lerpVy, mobileVehicle.getDeltaMovement().y() + 0.06f);
                preciseBlit(event.getGuiGraphics(), ModUtils.loc("textures/screens/helicopter/heli_vy_move.png"), (float) w / 2 + 100, ((float) h / 2 - 64 - Math.max(lerpVy, 0) * 100f), 0, 0, 64, 128, 64, 128);
                event.getGuiGraphics().drawString(Minecraft.getInstance().font, Component.literal(new DecimalFormat("##").format(mobileVehicle.getY())),
                        w / 2 + 104, h / 2, 0x66FF00, false);
                preciseBlit(event.getGuiGraphics(), ModUtils.loc("textures/screens/helicopter/speed_frame.png"), (float) w / 2 - 144, (float) h / 2 - 6, 0, 0, 50, 18, 50, 18);
                event.getGuiGraphics().drawString(Minecraft.getInstance().font, Component.literal(new DecimalFormat("##").format(length(mobileVehicle.getDeltaMovement().x, mobileVehicle.getDeltaMovement().y + 0.06, mobileVehicle.getDeltaMovement().z) * 72) + "KM/H"),
                        w / 2 - 140, h / 2, 0x66FF00, false);
            } else {
                scopeScale = 0.7f;
            }
        }
    }

    public static  double length(double x, double y,double z) {
        return Math.sqrt(x * x + y * y + z * z);
    }
}
