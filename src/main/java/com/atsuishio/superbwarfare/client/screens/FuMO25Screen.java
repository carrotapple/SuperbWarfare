package com.atsuishio.superbwarfare.client.screens;

import com.atsuishio.superbwarfare.ModUtils;
import com.atsuishio.superbwarfare.block.entity.FuMO25BlockEntity;
import com.atsuishio.superbwarfare.client.RenderHelper;
import com.atsuishio.superbwarfare.menu.FuMO25Menu;
import com.atsuishio.superbwarfare.network.message.RadarChangeModeMessage;
import com.atsuishio.superbwarfare.network.message.RadarSetParametersMessage;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.math.Axis;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@OnlyIn(Dist.CLIENT)
public class FuMO25Screen extends AbstractContainerScreen<FuMO25Menu> {

    private static final ResourceLocation TEXTURE = ModUtils.loc("textures/gui/radar.png");
    private static final ResourceLocation SCAN = ModUtils.loc("textures/gui/radar_scan.png");

    public FuMO25Screen(FuMO25Menu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        imageWidth = 340;
        imageHeight = 166;
    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        pGuiGraphics.blit(TEXTURE, i, j, 0, 0, this.imageWidth, this.imageHeight, 358, 328);

        // 目标位置
        renderTargets(pGuiGraphics);

        // 扫描盘
        renderScan(pGuiGraphics);

        // 网格线
        renderXLine(pGuiGraphics, pPartialTick, i, j);

        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);

        // FE
        long energy = FuMO25Screen.this.menu.getEnergy();
        float energyRate = (float) energy / (float) FuMO25BlockEntity.MAX_ENERGY;
        pGuiGraphics.blit(TEXTURE, i + 278, j + 39, 178, 167, (int) (54 * energyRate), 16, 358, 328);
    }

    private void renderXLine(GuiGraphics guiGraphics, float partialTick, int i, int j) {
        var poseStack = guiGraphics.pose();
        poseStack.pushPose();

        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);

        guiGraphics.blit(TEXTURE, i + 8, j + 11, 0, 167, 147, 147, 358, 328);

        poseStack.popPose();
    }

    private void renderTargets(GuiGraphics guiGraphics) {
        var entities = FuMO25ScreenHelper.entities;
        if (entities == null || entities.isEmpty()) return;
        var pos = FuMO25ScreenHelper.pos;
        if (pos == null) return;

        int type = (int) FuMO25Screen.this.menu.getFuncType();
        int range = type == 1 ? FuMO25BlockEntity.MAX_RANGE : FuMO25BlockEntity.DEFAULT_RANGE;

        var poseStack = guiGraphics.pose();
        poseStack.pushPose();

        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;

        int centerX = i + 81;
        int centerY = j + 84;

        for (var entity : entities) {
            double moveX = (entity.getX() - pos.getX()) / range * 74;
            double moveZ = (entity.getZ() - pos.getZ()) / range * 74;

            RenderHelper.preciseBlit(guiGraphics, TEXTURE, (float) (centerX + moveX), (float) (centerY + moveZ),
                    233, 167, 4, 4, 358, 328);
        }

        poseStack.popPose();
    }

    private void renderScan(GuiGraphics guiGraphics) {
        if (FuMO25Screen.this.menu.getEnergy() <= 0) return;

        var poseStack = guiGraphics.pose();
        poseStack.pushPose();

        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;

        poseStack.rotateAround(Axis.ZP.rotationDegrees(System.currentTimeMillis() % 36000000 / 30f), i + 9 + 145 / 2f, j + 12 + 145 / 2f, 0);

        guiGraphics.blit(SCAN, i + 9, j + 12, 0, 0, 145, 145, 145, 145);

        poseStack.popPose();
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        this.renderBackground(pGuiGraphics);
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        this.renderTooltip(pGuiGraphics, pMouseX, pMouseY);
    }

    @Override
    protected void renderTooltip(GuiGraphics pGuiGraphics, int pX, int pY) {
        super.renderTooltip(pGuiGraphics, pX, pY);

        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;

        List<Component> tooltip = new ArrayList<>();
        tooltip.add(Component.translatable("des.superbwarfare.charging_station.energy", FuMO25Screen.this.menu.getEnergy(),
                FuMO25BlockEntity.MAX_ENERGY));

        if ((pX - i) >= 278 && (pX - i) <= 332 && (pY - j) >= 39 && (pY - j) <= 55) {
            pGuiGraphics.renderTooltip(this.font, tooltip, Optional.empty(), pX, pY);
        }
    }

    // 本方法留空
    @Override
    protected void renderLabels(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY) {
    }

    @Override
    protected void init() {
        super.init();
        this.titleLabelX = 33;
        this.titleLabelY = 5;
        this.inventoryLabelX = 105;
        this.inventoryLabelY = 128;

        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;

        LockButton lockButton = new LockButton(i + 304, j + 61);
        this.addRenderableWidget(lockButton);

        ModeButton widerButton = new ModeButton(i + 171, j + 61, 1);
        this.addRenderableWidget(widerButton);

        ModeButton glowButton = new ModeButton(i + 201, j + 61, 2);
        this.addRenderableWidget(glowButton);

        ModeButton guideButton = new ModeButton(i + 231, j + 61, 3);
        this.addRenderableWidget(guideButton);
    }

    @OnlyIn(Dist.CLIENT)
    static class LockButton extends AbstractButton {

        public LockButton(int pX, int pY) {
            super(pX, pY, 29, 15, Component.literal(""));
        }

        @Override
        public void onPress() {
            ModUtils.PACKET_HANDLER.sendToServer(new RadarSetParametersMessage((byte) 0));
        }

        @Override
        protected void renderWidget(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
            pGuiGraphics.blit(TEXTURE, this.getX(), this.getY(), 148, this.isHovered() ? 183 : 167, 29, 15, 358, 328);
        }

        @Override
        protected void updateWidgetNarration(NarrationElementOutput pNarrationElementOutput) {
        }
    }

    @OnlyIn(Dist.CLIENT)
    static class ModeButton extends AbstractButton {

        private final int mode;

        public ModeButton(int pX, int pY, int mode) {
            super(pX, pY, 29, 15, Component.literal(""));
            this.mode = mode;
        }

        @Override
        public void onPress() {
            ModUtils.PACKET_HANDLER.sendToServer(new RadarChangeModeMessage((byte) this.mode));
        }

        @Override
        protected void renderWidget(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
            pGuiGraphics.blit(TEXTURE, this.getX(), this.getY(), 148, this.isHovered() ? 183 + this.mode * 32 : 167 + this.mode * 32,
                    29, 15, 358, 328);
        }

        @Override
        protected void updateWidgetNarration(NarrationElementOutput pNarrationElementOutput) {
        }
    }
}
