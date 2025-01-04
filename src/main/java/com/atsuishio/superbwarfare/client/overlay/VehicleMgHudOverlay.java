package com.atsuishio.superbwarfare.client.overlay;

import com.atsuishio.superbwarfare.ModUtils;
import com.atsuishio.superbwarfare.client.RenderHelper;
import com.atsuishio.superbwarfare.entity.vehicle.SpeedboatEntity;
import com.atsuishio.superbwarfare.event.ClientEventHandler;
import com.atsuishio.superbwarfare.init.ModTags;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.text.DecimalFormat;

import static com.atsuishio.superbwarfare.client.RenderHelper.preciseBlit;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class VehicleMgHudOverlay {

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

        float fovAdjust = (float) 70 / Minecraft.getInstance().options.fov().get();

        float f = (float) Math.min(w, h);
        float f1 = Math.min((float) w / f, (float) h / f) * fovAdjust;
        int i = Mth.floor(f * f1);
        int j = Mth.floor(f * f1);
        int k = (w - i) / 2;
        int l = (h - j) / 2;
        RenderHelper.preciseBlit(event.getGuiGraphics(), ModUtils.loc("textures/screens/cannon/cannon_crosshair_notzoom.png"), k, l, 0, 0.0F, i, j, i, j);

        if (ClientEventHandler.vehicleFovLerp > 1.01) {
            event.getGuiGraphics().blit(ModUtils.loc("textures/screens/drone_fov.png"), w / 2 + 100, h / 2 - 64, 0, 0, 64, 129, 64, 129);
            GuiGraphics guiGraphics = event.getGuiGraphics();
            preciseBlit(guiGraphics, ModUtils.loc("textures/screens/drone_fov_move.png"), (float) w / 2 + 100, (float) (h / 2 - 64 - ((ClientEventHandler.vehicleFovLerp - 1) * 23.8)), 0, 0, 64, 129, 64, 129);
            event.getGuiGraphics().drawString(Minecraft.getInstance().font, Component.literal(new DecimalFormat("##.#").format(ClientEventHandler.vehicleFovLerp) + "x"),
                    w / 2 + 144, h / 2 + 56 - (int) ((ClientEventHandler.vehicleFovLerp - 1) * 23.8), -1, false);
        }
    }

    private static boolean shouldRenderCrossHair(Player player) {
        if (player == null) return false;
        return !player.isSpectator()
                && player.getVehicle() instanceof SpeedboatEntity && ClientEventHandler.zoom && !player.getMainHandItem().is(ModTags.Items.GUN);
    }
}
