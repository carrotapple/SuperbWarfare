package com.atsuishio.superbwarfare.client.screens;

import com.atsuishio.superbwarfare.ModUtils;
import com.atsuishio.superbwarfare.block.entity.FuMO25BlockEntity;
import com.atsuishio.superbwarfare.menu.FuMO25Menu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

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

        // 扫描盘
        renderScan(pGuiGraphics, pPartialTick);

        // 网格线
        pGuiGraphics.blit(TEXTURE, i + 8, j + 11, 0, 167, 147, 147, 358, 328);

        // FE
        long energy = FuMO25Screen.this.menu.getEnergy();
        float energyRate = (float) energy / (float) FuMO25BlockEntity.MAX_ENERGY;
        pGuiGraphics.blit(TEXTURE, i + 278, j + 39, 178, 167, (int) (54 * energyRate), 16, 358, 328);
    }

    private void renderScan(GuiGraphics guiGraphics, float partialTick) {
        if (FuMO25Screen.this.menu.getEnergy() <= 0) return;

    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        this.renderBackground(pGuiGraphics);
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        this.renderTooltip(pGuiGraphics, pMouseX, pMouseY);
    }

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
    }
}
