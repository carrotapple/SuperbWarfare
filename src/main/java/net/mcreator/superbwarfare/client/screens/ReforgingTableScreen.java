package net.mcreator.superbwarfare.client.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import net.mcreator.superbwarfare.ModUtils;
import net.mcreator.superbwarfare.block.menu.ReforgingTableMenu;
import net.mcreator.superbwarfare.network.message.GunReforgeMessage;
import net.mcreator.superbwarfare.network.message.SetPerkLevelMessage;
import net.mcreator.superbwarfare.perk.Perk;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ReforgingTableScreen extends AbstractContainerScreen<ReforgingTableMenu> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(ModUtils.MODID, "textures/gui/reforging_table.png");

    public ReforgingTableScreen(ReforgingTableMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        imageWidth = 176;
        imageHeight = 177;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int gx, int gy) {
        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        guiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, this.imageWidth, this.imageHeight);
        RenderSystem.disableBlend();
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
        this.titleLabelY = 2;
        this.inventoryLabelX = 8;
        this.inventoryLabelY = 85;

        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;

        ReforgeButton button = new ReforgeButton(i + 124, j + 70);
        UpgradeButton ammoUpgrade = new UpgradeButton(i + 100, j + 32, Perk.Type.AMMO);
        DowngradeButton ammoDowngrade = new DowngradeButton(i + 86, j + 32, Perk.Type.AMMO);
        UpgradeButton funcUpgrade = new UpgradeButton(i + 100, j + 52, Perk.Type.FUNCTIONAL);
        DowngradeButton funcDowngrade = new DowngradeButton(i + 86, j + 52, Perk.Type.FUNCTIONAL);
        UpgradeButton damageUpgrade = new UpgradeButton(i + 100, j + 72, Perk.Type.DAMAGE);
        DowngradeButton damageDowngrade = new DowngradeButton(i + 86, j + 72, Perk.Type.DAMAGE);

        this.addRenderableWidget(button);
        this.addRenderableWidget(ammoUpgrade);
        this.addRenderableWidget(ammoDowngrade);
        this.addRenderableWidget(funcUpgrade);
        this.addRenderableWidget(funcDowngrade);
        this.addRenderableWidget(damageUpgrade);
        this.addRenderableWidget(damageDowngrade);
    }

    @OnlyIn(Dist.CLIENT)
    static class ReforgeButton extends AbstractButton {

        public ReforgeButton(int pX, int pY) {
            super(pX, pY, 40, 16, Component.translatable("button.superbwarfare.reforge"));
        }

        @Override
        public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
            super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        }

//        @Override
//        public void renderWidget(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
//
//        }

        @Override
        public void onPress() {
            ModUtils.PACKET_HANDLER.sendToServer(new GunReforgeMessage(0));
        }

        @Override
        protected void updateWidgetNarration(NarrationElementOutput pNarrationElementOutput) {

        }
    }

    @OnlyIn(Dist.CLIENT)
    static class UpgradeButton extends AbstractButton {
        public Perk.Type type;

        public UpgradeButton(int pX, int pY, Perk.Type type) {
            super(pX, pY, 12, 12, Component.translatable("button.superbwarfare.upgrade"));
            this.type = type;
        }

        @Override
        public void onPress() {
            ModUtils.PACKET_HANDLER.sendToServer(new SetPerkLevelMessage(type.ordinal(), true));
        }

        @Override
        protected void updateWidgetNarration(NarrationElementOutput pNarrationElementOutput) {

        }
    }

    @OnlyIn(Dist.CLIENT)
    static class DowngradeButton extends AbstractButton {
        public Perk.Type type;

        public DowngradeButton(int pX, int pY, Perk.Type type) {
            super(pX, pY, 12, 12, Component.translatable("button.superbwarfare.downgrade"));
            this.type = type;
        }

        @Override
        public void onPress() {
            ModUtils.PACKET_HANDLER.sendToServer(new SetPerkLevelMessage(type.ordinal(), false));
        }

        @Override
        protected void updateWidgetNarration(NarrationElementOutput pNarrationElementOutput) {

        }
    }
}