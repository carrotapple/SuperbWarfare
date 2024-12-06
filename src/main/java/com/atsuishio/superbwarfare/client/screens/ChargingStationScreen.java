package com.atsuishio.superbwarfare.client.screens;

import com.atsuishio.superbwarfare.ModUtils;
import com.atsuishio.superbwarfare.block.entity.ChargingStationBlockEntity;
import com.atsuishio.superbwarfare.block.menu.ChargingStationMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ChargingStationScreen extends AbstractContainerScreen<ChargingStationMenu> {

    private static final ResourceLocation TEXTURE = ModUtils.loc("textures/gui/charging_station.png");

    public ChargingStationScreen(ChargingStationMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        imageWidth = 176;
        imageHeight = 166;
    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        pGuiGraphics.blit(TEXTURE, i, j, 0, 0, this.imageWidth, this.imageHeight);

        int fuelTick = ChargingStationScreen.this.menu.getFuelTick();
        int maxFuelTick = ChargingStationScreen.this.menu.getMaxFuelTick();
        int energy = ChargingStationScreen.this.menu.getEnergy();

        if (maxFuelTick == 0) {
            maxFuelTick = ChargingStationBlockEntity.DEFAULT_FUEL_TIME;
        }

        // Fuel
        float fuelRate = (float) fuelTick / (float) maxFuelTick;
        pGuiGraphics.blit(TEXTURE, i + 45, j + 51 - (int) (13 * fuelRate), 177, 14 - (int) (13 * fuelRate), 13, (int) (13 * fuelRate));

        // Energy
        float energyRate = (float) energy / (float) ChargingStationBlockEntity.MAX_ENERGY;
        pGuiGraphics.blit(TEXTURE, i + 80, j + 70 - (int) (54 * energyRate),
                177, 17, 16, (int) (54 * energyRate));
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        this.renderBackground(pGuiGraphics);
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        this.renderTooltip(pGuiGraphics, pMouseX, pMouseY);
    }

    @Override
    protected void init() {
        super.init();
        this.titleLabelX = 8;
        this.titleLabelY = 5;
        this.inventoryLabelX = 8;
        this.inventoryLabelY = 74;
    }

}
