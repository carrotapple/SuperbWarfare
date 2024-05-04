package net.mcreator.target.client.gui;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.GuiGraphics;

import net.mcreator.target.world.inventory.GunRecycleGuiMenu;
import net.mcreator.target.network.GunRecycleGuiButtonMessage;
import net.mcreator.target.TargetMod;

import java.util.HashMap;

import com.mojang.blaze3d.systems.RenderSystem;

public class GunRecycleGuiScreen extends AbstractContainerScreen<GunRecycleGuiMenu> {
	private final static HashMap<String, Object> guistate = GunRecycleGuiMenu.guistate;
	private final Level world;
	private final int x, y, z;
	private final Player entity;
	Button button_dismantle;

	public GunRecycleGuiScreen(GunRecycleGuiMenu container, Inventory inventory, Component text) {
		super(container, inventory, text);
		this.world = container.world;
		this.x = container.x;
		this.y = container.y;
		this.z = container.z;
		this.entity = container.entity;
		this.imageWidth = 176;
		this.imageHeight = 166;
	}

	private static final ResourceLocation texture = new ResourceLocation("target:textures/screens/gun_recycle_gui.png");

	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(guiGraphics);
		super.render(guiGraphics, mouseX, mouseY, partialTicks);
		this.renderTooltip(guiGraphics, mouseX, mouseY);
		if (mouseX > leftPos + 58 && mouseX < leftPos + 116 && mouseY > topPos + 54 && mouseY < topPos + 78)
			guiGraphics.renderTooltip(font, Component.translatable("gui.target.gun_recycle_gui.tooltip_if_guns_level_10you_will_get"), mouseX, mouseY);
	}

	@Override
	protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int gx, int gy) {
		RenderSystem.setShaderColor(1, 1, 1, 1);
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		guiGraphics.blit(texture, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, this.imageWidth, this.imageHeight);
		RenderSystem.disableBlend();
	}

	@Override
	public boolean keyPressed(int key, int b, int c) {
		if (key == 256) {
			this.minecraft.player.closeContainer();
			return true;
		}
		return super.keyPressed(key, b, c);
	}

	@Override
	public void containerTick() {
		super.containerTick();
	}

	@Override
	protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
		guiGraphics.drawString(this.font, Component.translatable("gui.target.gun_recycle_gui.label_gun_recycle"), 6, 6, -12829636, false);
	}

	@Override
	public void onClose() {
		super.onClose();
	}

	@Override
	public void init() {
		super.init();
		button_dismantle = Button.builder(Component.translatable("gui.target.gun_recycle_gui.button_dismantle"), e -> {
			if (true) {
				TargetMod.PACKET_HANDLER.sendToServer(new GunRecycleGuiButtonMessage(0, x, y, z));
				GunRecycleGuiButtonMessage.handleButtonAction(entity, 0, x, y, z);
			}
		}).bounds(this.leftPos + 62, this.topPos + 56, 52, 20).build();
		guistate.put("button:button_dismantle", button_dismantle);
		this.addRenderableWidget(button_dismantle);
	}
}
