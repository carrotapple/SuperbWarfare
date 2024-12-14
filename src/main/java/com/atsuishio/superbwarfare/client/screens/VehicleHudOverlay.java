package com.atsuishio.superbwarfare.client.screens;

import com.atsuishio.superbwarfare.ModUtils;
import com.atsuishio.superbwarfare.config.server.CannonConfig;
import com.atsuishio.superbwarfare.entity.ICannonEntity;
import com.atsuishio.superbwarfare.entity.IVehicleEntity;
import com.atsuishio.superbwarfare.entity.SpeedboatEntity;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class VehicleHudOverlay {

    public static float health = 0;
    public static float maxHealth = 0;

    public static float energy = 0;
    public static float maxEnergy = 0;

    private static final ResourceLocation ARMOR = ModUtils.loc("textures/screens/armor.png");
    private static final ResourceLocation ENERGY = ModUtils.loc("textures/screens/energy.png");
    private static final ResourceLocation HEALTH = ModUtils.loc("textures/screens/armor_value.png");
    private static final ResourceLocation HEALTH_FRAME = ModUtils.loc("textures/screens/armor_value_frame.png");

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void eventHandler(RenderGuiEvent.Pre event) {
        int w = event.getWindow().getGuiScaledWidth();
        int h = event.getWindow().getGuiScaledHeight();
        Player player = Minecraft.getInstance().player;

        if (!shouldRenderCrossHair(player)) return;

        Entity vehicle = player.getVehicle();
        if (vehicle == null) return;

        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        RenderSystem.setShaderColor(1, 1, 1, 1);
        GuiGraphics guiGraphics = event.getGuiGraphics();

        if (vehicle instanceof SpeedboatEntity) {
            health = vehicle.getEntityData().get(SpeedboatEntity.HEALTH);
            maxHealth = CannonConfig.SPEEDBOAT_HP.get();
            energy = vehicle.getEntityData().get(SpeedboatEntity.ENERGY);
            maxEnergy = CannonConfig.SPEEDBOAT_MAX_ENERGY.get().floatValue();
        }

        guiGraphics.pose().pushPose();
        guiGraphics.blit(ENERGY, w - 96, h - 28, 0, 0, 12, 12, 12, 12);
        guiGraphics.blit(HEALTH_FRAME, w - 83, h - 26, 0, 0, 80, 8, 80, 8);
        guiGraphics.blit(HEALTH, w - 83, h - 26, 0, 0, (int) (80 * energy / maxEnergy), 8, 80, 8);
        guiGraphics.blit(ARMOR, w - 96, h - 14, 0, 0, 12, 12, 12, 12);
        guiGraphics.blit(HEALTH_FRAME, w - 83, h - 12, 0, 0, 80, 8, 80, 8);
        guiGraphics.blit(HEALTH, w - 83, h - 12, 0, 0, (int) (80 * health / maxHealth), 8, 80, 8);
        guiGraphics.pose().popPose();

    }

    private static boolean shouldRenderCrossHair(Player player) {
        if (player == null) return false;
        return !player.isSpectator()
                && (player.getVehicle() != null && player.getVehicle() instanceof IVehicleEntity && !(player.getVehicle() instanceof ICannonEntity));
    }
}
