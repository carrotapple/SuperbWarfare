package com.atsuishio.superbwarfare.client.screens;

import com.atsuishio.superbwarfare.Mod;
import com.atsuishio.superbwarfare.menu.DogTagEditorMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ServerboundRenameItemPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class DogTagEditorScreen extends AbstractContainerScreen<DogTagEditorMenu> {

    private static final ResourceLocation TEXTURE = Mod.loc("textures/gui/dog_tag_editor.png");

    private ItemStack stack;
    // TODO 改名怎么写？
    private EditBox name;
    private int currentColor = 0;

    public DogTagEditorScreen(DogTagEditorMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        this.stack = pMenu.stack;
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
    public void containerTick() {
        super.containerTick();
        this.name.tick();
    }

    @Override
    protected void init() {
        super.init();
        this.subInit();

        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;

        for (int k = 0; k < 16; k++) {
            var button = new ColorButton(k, i + 6 + (k % 2) * 22, j + 62 + (k / 2) * 10, 18, 8);
            this.addRenderableWidget(button);
        }
        var eraserButton = new ColorButton(-1, i + 17, j + 143, 18, 8);
        this.addRenderableWidget(eraserButton);
    }

    protected void subInit() {
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        this.name = new EditBox(this.font, i + 9, j + 7, 180, 12, stack.getHoverName());
        this.name.setCanLoseFocus(false);
        this.name.setTextColor(-1);
        this.name.setTextColorUneditable(-1);
        this.name.setBordered(false);
        this.name.setMaxLength(50);
        this.name.setResponder(this::onNameChanged);
        this.name.setValue("");
        this.addWidget(this.name);
        this.setInitialFocus(this.name);
        this.name.setEditable(true);
    }

    private void onNameChanged(String name) {
        String s = name;
        if (!stack.hasCustomHoverName() && name.equals(stack.getHoverName().getString())) {
            s = "";
        }

        if (this.menu.setItemName(s)) {
            if (this.minecraft != null && this.minecraft.player != null) {
                this.minecraft.player.connection.send(new ServerboundRenameItemPacket(s));
            }
        }
    }

    // 留空
    @Override
    protected void renderLabels(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY) {
    }

    @OnlyIn(Dist.CLIENT)
    class ColorButton extends AbstractButton {

        int color;

        public ColorButton(int color, int pX, int pY, int pWidth, int pHeight) {
            super(pX, pY, pWidth, pHeight, Component.literal(""));
            this.color = color;
        }

        @Override
        public void onPress() {
            DogTagEditorScreen.this.currentColor = this.color;
        }

        @Override
        protected void updateWidgetNarration(NarrationElementOutput pNarrationElementOutput) {
        }

        @Override
        protected void renderWidget(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
            if (this.isHovered || DogTagEditorScreen.this.currentColor == this.color) {
                if (this.color == -1) {
                    pGuiGraphics.blit(TEXTURE, this.getX(), this.getY(), 19, 186,
                            18, 8, 256, 256);
                } else {
                    pGuiGraphics.blit(TEXTURE, this.getX(), this.getY(), 0, 186,
                            18, 8, 256, 256);
                }
            }
        }
    }
}
