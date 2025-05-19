package com.atsuishio.superbwarfare.client.screens;

import com.atsuishio.superbwarfare.Mod;
import com.atsuishio.superbwarfare.menu.DogTagEditorMenu;
import net.minecraft.ChatFormatting;
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

import java.util.Arrays;

@OnlyIn(Dist.CLIENT)
public class DogTagEditorScreen extends AbstractContainerScreen<DogTagEditorMenu> {

    private static final ResourceLocation TEXTURE = Mod.loc("textures/gui/dog_tag_editor.png");

    private ItemStack stack;
    // TODO 改名怎么写？
    private EditBox name;
    private short currentColor = 0;
    private final short[][] icon = new short[16][16];

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

        ItemStack stack = DogTagEditorScreen.this.menu.stack;
        pGuiGraphics.renderItem(stack, i + 18, j + 36);

        var pose = pGuiGraphics.pose();

        pose.pushPose();

        // TODO 为什么渲染不了色块
        for (int x = 0; x < this.icon.length; x++) {
            for (int y = 0; y < this.icon.length; y++) {
                int num = this.icon[x][y];
                if (num != -1) {
                    var color = ChatFormatting.getById(num);
                    if (color != null && color.getColor() != null) {
                        pGuiGraphics.fill(i + 58 + x * 9, j + 36 + y * 9, i + 66 + x * 9, j + 44 + y * 9,
                                -90, color.getColor());
                    }
                }
            }
        }

        pose.popPose();
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        this.renderBackground(pGuiGraphics);
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        this.renderTooltip(pGuiGraphics, pMouseX, pMouseY);
    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;

        // TODO 替换成更精细的单格子判断
        if (pMouseX >= i + 57 && pMouseX <= i + 201 && pMouseY >= j + 36 && pMouseY <= j + 179) {
            this.icon[0][0] = this.currentColor;
        }

        return super.mouseClicked(pMouseX, pMouseY, pButton);
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

        for (var el : this.icon) {
            Arrays.fill(el, (short) -1);
        }

        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;

        for (short k = 0; k < 16; k++) {
            var button = new ColorButton(k, i + 6 + (k % 2) * 22, j + 62 + (k / 2) * 10, 18, 8);
            this.addRenderableWidget(button);
        }
        var eraserButton = new ColorButton((short) -1, i + 17, j + 143, 18, 8);
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

        short color;

        public ColorButton(short color, int pX, int pY, int pWidth, int pHeight) {
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
