package com.atsuishio.superbwarfare.client.overlay;

import com.atsuishio.superbwarfare.ModUtils;
import com.atsuishio.superbwarfare.entity.vehicle.AnnihilatorEntity;
import com.atsuishio.superbwarfare.entity.vehicle.ICannonEntity;
import com.atsuishio.superbwarfare.event.ClientEventHandler;
import com.atsuishio.superbwarfare.tools.TraceTool;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
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
public class CannonHudOverlay {

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void eventHandler(RenderGuiEvent.Pre event) {
        int w = event.getWindow().getGuiScaledWidth();
        int h = event.getWindow().getGuiScaledHeight();
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        Camera camera = mc.gameRenderer.getMainCamera();
        Vec3 cameraPos = camera.getPosition();
        GuiGraphics guiGraphics = event.getGuiGraphics();
        PoseStack poseStack = guiGraphics.pose();

        if (!shouldRenderCrossHair(player)) return;

        Entity cannon = player.getVehicle();
        if (cannon == null) return;

        poseStack.pushPose();
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        RenderSystem.setShaderColor(1, 1, 1, 1);

        preciseBlit(event.getGuiGraphics(), ModUtils.loc("textures/screens/compass_white.png"), (float) w / 2 - 128, (float) 10, 128 + ((float) 64 / 45 * (Mth.lerp(event.getPartialTick(), cannon.yRotO, cannon.getYRot()))), 0, 256, 16, 512, 16);
        preciseBlit(event.getGuiGraphics(), ModUtils.loc("textures/screens/roll_ind_white.png"), w / 2 - 4, 27, 0, 0.0F, 8, 8, 8, 8);

        String angle = new DecimalFormat("#0.0").format(Mth.lerp(event.getPartialTick(), cannon.yRotO, cannon.getYRot()));
        int width = Minecraft.getInstance().font.width(angle);
        event.getGuiGraphics().drawString(Minecraft.getInstance().font, Component.literal(angle), w / 2 - width / 2, 40, -1, false);

        preciseBlit(event.getGuiGraphics(), ModUtils.loc("textures/screens/cannon/cannon_pitch.png"), w / 2 + 166, h / 2 - 64, 0, 0.0F, 8, 128, 8, 128);

        String pitch = new DecimalFormat("#0.0").format(-Mth.lerp(event.getPartialTick(), cannon.xRotO, cannon.getXRot()));
        int widthP = Minecraft.getInstance().font.width(pitch);

        poseStack.pushPose();

        event.getGuiGraphics().pose().translate(0, Mth.lerp(event.getPartialTick(), cannon.xRotO, cannon.getXRot()) * 0.7, 0);
        preciseBlit(event.getGuiGraphics(), ModUtils.loc("textures/screens/cannon/cannon_pitch_ind.png"), w / 2 + 158, h / 2 - 4, 0, 0.0F, 8, 8, 8, 8);
        event.getGuiGraphics().drawString(Minecraft.getInstance().font, Component.literal(pitch), w / 2 + 157 - widthP, h / 2 - 4, -1, false);
        poseStack.popPose();

        float fovAdjust = (float) 70 / Minecraft.getInstance().options.fov().get();

        float f = (float) Math.min(w, h);
        float f1 = Math.min((float) w / f, (float) h / f) * fovAdjust;
        int i = Mth.floor(f * f1);
        int j = Mth.floor(f * f1);
        int k = (w - i) / 2;
        int l = (h - j) / 2;
        if (ClientEventHandler.zoomVehicle) {
            Entity lookingEntity = TraceTool.findLookingEntity(player, 512);
            boolean lookAtEntity = false;
            double blockRange = player.position().distanceTo((Vec3.atLowerCornerOf(player.level().clip(
                    new ClipContext(new Vec3(player.getX(), player.getEyeY() + 1, player.getZ()), new Vec3(player.getX(), player.getEyeY() + 1, player.getZ()).add(player.getLookAngle().scale(512)),
                            ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, player)).getBlockPos())));

            double entityRange = 0;
            if (lookingEntity instanceof LivingEntity living) {
                lookAtEntity = true;
                entityRange = player.distanceTo(living);
            }
            if (lookAtEntity) {
                event.getGuiGraphics().drawString(Minecraft.getInstance().font, Component.translatable("tips.superbwarfare.drone.range")
                                .append(Component.literal(new DecimalFormat("##.#").format(entityRange) + "M " + lookingEntity.getDisplayName().getString())),
                        w / 2 + 14, h / 2 - 20, -1, false);
            } else {
                if (blockRange > 511) {
                    event.getGuiGraphics().drawString(Minecraft.getInstance().font, Component.translatable("tips.superbwarfare.drone.range")
                            .append(Component.literal("---M")), w / 2 + 14, h / 2 - 20, -1, false);
                } else {
                    event.getGuiGraphics().drawString(Minecraft.getInstance().font, Component.translatable("tips.superbwarfare.drone.range")
                                    .append(Component.literal(new DecimalFormat("##.#").format(blockRange) + "M")),
                            w / 2 + 14, h / 2 - 20, -1, false);
                }
            }
            if (cannon instanceof AnnihilatorEntity) {
                preciseBlit(event.getGuiGraphics(), ModUtils.loc("textures/screens/cannon/laser_cannon_crosshair.png"), k, l, 0, 0.0F, i, j, i, j);
            } else {
                preciseBlit(event.getGuiGraphics(), ModUtils.loc("textures/screens/cannon/cannon_crosshair.png"), k, l, 0, 0.0F, i, j, i, j);
            }
            float diffY = -Mth.wrapDegrees(Mth.lerp(event.getPartialTick(), player.yHeadRotO, player.getYHeadRot()) - Mth.lerp(event.getPartialTick(), cannon.yRotO, cannon.getYRot()));

            preciseBlit(event.getGuiGraphics(), ModUtils.loc("textures/screens/cannon/indicator.png"), w / 2 - 4.3f + 0.45f * diffY, h / 2 - 10, 0, 0.0F, 8, 8, 8, 8);
        } else {
            preciseBlit(event.getGuiGraphics(), ModUtils.loc("textures/screens/cannon/cannon_crosshair_notzoom.png"), k, l, 0, 0.0F, i, j, i, j);
        }

        poseStack.popPose();
    }

    private static boolean shouldRenderCrossHair(Player player) {
        if (player == null) return false;
        return !player.isSpectator()
                && (player.getVehicle() != null && (player.getVehicle() instanceof ICannonEntity));
    }
}
