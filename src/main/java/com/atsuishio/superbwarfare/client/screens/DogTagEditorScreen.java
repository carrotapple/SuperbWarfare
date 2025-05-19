package com.atsuishio.superbwarfare.client.screens;

import com.atsuishio.superbwarfare.Mod;
import com.atsuishio.superbwarfare.menu.DogTagEditorMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class DogTagEditorScreen extends AbstractContainerScreen<DogTagEditorMenu> {

    private static final ResourceLocation TEXTURE = Mod.loc("textures/gui/dog_tag_editor.png");

    public DogTagEditorScreen(DogTagEditorMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        imageWidth = 207;
        imageHeight = 185;
    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        pGuiGraphics.blit(TEXTURE, i, j, 0, 0, this.imageWidth, this.imageHeight, 256, 256);


    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        this.renderBackground(pGuiGraphics);
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);

        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;

        ItemStack stack = DogTagEditorScreen.this.menu.stack;
        pGuiGraphics.renderItem(stack, i + 18, j + 36);

        this.renderTooltip(pGuiGraphics, pMouseX, pMouseY);
    }

    @Override
    protected void init() {
        super.init();
    }

    // 留空
    @Override
    protected void renderLabels(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY) {
    }

    @OnlyIn(Dist.CLIENT)
    static class ColorButton extends AbstractButton {

        int color;

        public ColorButton(int color, int pX, int pY, int pWidth, int pHeight) {
            super(pX, pY, pWidth, pHeight, Component.literal(""));
            this.color = color;
        }

        @Override
        public void onPress() {

        }

        @Override
        protected void updateWidgetNarration(NarrationElementOutput pNarrationElementOutput) {
        }
    }
}
