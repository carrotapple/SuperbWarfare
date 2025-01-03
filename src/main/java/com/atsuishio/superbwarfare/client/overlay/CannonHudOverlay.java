package com.atsuishio.superbwarfare.client.overlay;

import com.atsuishio.superbwarfare.ModUtils;
import com.atsuishio.superbwarfare.client.RenderHelper;
import com.atsuishio.superbwarfare.entity.AnnihilatorEntity;
import com.atsuishio.superbwarfare.entity.ICannonEntity;
import com.atsuishio.superbwarfare.entity.Mk42Entity;
import com.atsuishio.superbwarfare.entity.Mle1934Entity;
import com.atsuishio.superbwarfare.event.ClientEventHandler;
import com.atsuishio.superbwarfare.tools.TraceTool;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
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

import java.text.DecimalFormat;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class CannonHudOverlay {

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void eventHandler(RenderGuiEvent.Pre event) {
        int w = event.getWindow().getGuiScaledWidth();
        int h = event.getWindow().getGuiScaledHeight();
        Player player = Minecraft.getInstance().player;

        if (!shouldRenderCrossHair(player)) return;

        Entity cannon = player.getVehicle();
        if (cannon == null) return;

        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        RenderSystem.setShaderColor(1, 1, 1, 1);

        float indicatorPosH = 0;
        if (cannon instanceof Mk42Entity) {
            indicatorPosH = 1.3f;
        }

        if (cannon instanceof Mle1934Entity) {
            indicatorPosH = 1.2f;
        }

        if (cannon instanceof AnnihilatorEntity) {
            indicatorPosH = cannon.getEntityData().get(AnnihilatorEntity.OFFSET_ANGLE);
        }

        float yRotOffset = Mth.lerp(event.getPartialTick(), player.yRotO, player.getYRot());
        float xRotOffset = Mth.lerp(event.getPartialTick(), player.xRotO, player.getXRot());
        float diffY = cannon.getViewYRot(event.getPartialTick()) - yRotOffset;
        float diffX = cannon.getViewXRot(event.getPartialTick()) - xRotOffset + indicatorPosH;
        float fovAdjust = (float) 70 / Minecraft.getInstance().options.fov().get();
        if (diffY > 180.0f) {
            diffY -= 360.0f;
        } else if (diffY < -180.0f) {
            diffY += 360.0f;
        }
        float f = (float) Math.min(w, h);
        float f1 = Math.min((float) w / f, (float) h / f) * fovAdjust;
        int i = Mth.floor(f * f1);
        int j = Mth.floor(f * f1);
        int k = (w - i) / 2;
        int l = (h - j) / 2;
        if (ClientEventHandler.zoom) {
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
                event.getGuiGraphics().drawString(Minecraft.getInstance().font, Component.translatable("des.superbwarfare.drone.range")
                                .append(Component.literal(new DecimalFormat("##.#").format(entityRange) + "M " + lookingEntity.getDisplayName().getString())),
                        w / 2 + 14, h / 2 - 20, -1, false);
            } else {
                if (blockRange > 511) {
                    event.getGuiGraphics().drawString(Minecraft.getInstance().font, Component.translatable("des.superbwarfare.drone.range")
                            .append(Component.literal("---M")), w / 2 + 14, h / 2 - 20, -1, false);
                } else {
                    event.getGuiGraphics().drawString(Minecraft.getInstance().font, Component.translatable("des.superbwarfare.drone.range")
                                    .append(Component.literal(new DecimalFormat("##.#").format(blockRange) + "M")),
                            w / 2 + 14, h / 2 - 20, -1, false);
                }
            }
            if (cannon instanceof AnnihilatorEntity) {
                RenderHelper.preciseBlit(event.getGuiGraphics(), ModUtils.loc("textures/screens/cannon/laser_cannon_crosshair.png"), k, l, 0, 0.0F, i, j, i, j);
            } else {
                RenderHelper.preciseBlit(event.getGuiGraphics(), ModUtils.loc("textures/screens/cannon/cannon_crosshair.png"), k, l, 0, 0.0F, i, j, i, j);
            }
            RenderHelper.preciseBlit(event.getGuiGraphics(), ModUtils.loc("textures/screens/cannon/indicator.png"), k + (float) Math.tan(Mth.clamp(Mth.DEG_TO_RAD * diffY, -1.5, 1.5)) * 5 * i / 1.4f * (90 - Math.abs(player.getXRot())) / 90, l + (float) Math.tan(Mth.clamp(Mth.DEG_TO_RAD * diffX, -1.5, 1.5)) * 5 * j / 1.4f, 0, 0.0F, i, j, i, j);
        } else {
            RenderHelper.preciseBlit(event.getGuiGraphics(), ModUtils.loc("textures/screens/cannon/cannon_crosshair_notzoom.png"), k, l, 0, 0.0F, i, j, i, j);
        }

        event.getGuiGraphics().drawString(Minecraft.getInstance().font, Component.translatable("des.superbwarfare.mortar.pitch")
                        .append(Component.literal(new DecimalFormat("##.#").format(-cannon.getXRot()) + "°")),
                w / 2 + 14, h / 2 - 29, -1, false);
    }

    private static boolean shouldRenderCrossHair(Player player) {
        if (player == null) return false;
        return !player.isSpectator()
                && (player.getVehicle() != null && (player.getVehicle() instanceof ICannonEntity));
    }
}
