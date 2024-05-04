package net.mcreator.target.client.gui;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.GuiGraphics;

import net.mcreator.target.world.inventory.MortarGUIMenu;
import net.mcreator.target.procedures.MortarAngleProcedure;
import net.mcreator.target.procedures.MortarPitchProcedure;
import net.mcreator.target.network.MortarGUIButtonMessage;

import net.mcreator.target.TargetMod;

import java.util.HashMap;

import com.mojang.blaze3d.systems.RenderSystem;

public class MortarGUIScreen extends AbstractContainerScreen<MortarGUIMenu> {
	private final static HashMap<String, Object> guistate = MortarGUIMenu.guistate;
	private final Level world;
	private final int x, y, z;
	private final Player entity;
	Button button_zeng_da_yang_jiao;
	Button button_empty;
	Button button_10;
	Button button_101;
	Button button_05;
	Button button_051;

	public MortarGUIScreen(MortarGUIMenu container, Inventory inventory, Component text) {
		super(container, inventory, text);
		this.world = container.world;
		this.x = container.x;
		this.y = container.y;
		this.z = container.z;
		this.entity = container.entity;
		this.imageWidth = 0;
		this.imageHeight = 166;
	}

	private static final ResourceLocation texture = new ResourceLocation("target:textures/screens/mortar_gui.png");

	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(guiGraphics);
		super.render(guiGraphics, mouseX, mouseY, partialTicks);
		this.renderTooltip(guiGraphics, mouseX, mouseY);
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
		guiGraphics.drawString(this.font,

				MortarAngleProcedure.execute(entity), -18, 98, -1, false);
				guiGraphics.drawString(this.font, Component.literal("Range:" + (int) RangeHelper.getRange(MortarPitchProcedure.execute(entity))), -18, 108, -16711885, false);
	}

	@Override
	public void onClose() {
		super.onClose();
	}

	@Override
	public void init() {
		super.init();
		button_zeng_da_yang_jiao = Button.builder(Component.translatable("gui.target.mortar_gui.button_zeng_da_yang_jiao"), e -> {
			if (true) {
				TargetMod.PACKET_HANDLER.sendToServer(new MortarGUIButtonMessage(0, x, y, z));
				MortarGUIButtonMessage.handleButtonAction(entity, 0, x, y, z);
			}
		}).bounds(this.leftPos + 42, this.topPos + 124, 29, 20).build();
		guistate.put("button:button_zeng_da_yang_jiao", button_zeng_da_yang_jiao);
		this.addRenderableWidget(button_zeng_da_yang_jiao);
		button_empty = Button.builder(Component.translatable("gui.target.mortar_gui.button_empty"), e -> {
			if (true) {
				TargetMod.PACKET_HANDLER.sendToServer(new MortarGUIButtonMessage(1, x, y, z));
				MortarGUIButtonMessage.handleButtonAction(entity, 1, x, y, z);
			}
		}).bounds(this.leftPos + -73, this.topPos + 124, 30, 20).build();
		guistate.put("button:button_empty", button_empty);
		this.addRenderableWidget(button_empty);
		button_10 = Button.builder(Component.translatable("gui.target.mortar_gui.button_10"), e -> {
			if (true) {
				TargetMod.PACKET_HANDLER.sendToServer(new MortarGUIButtonMessage(2, x, y, z));
				MortarGUIButtonMessage.handleButtonAction(entity, 2, x, y, z);
			}
		}).bounds(this.leftPos + 43, this.topPos + 151, 28, 20).build();
		guistate.put("button:button_10", button_10);
		this.addRenderableWidget(button_10);
		button_101 = Button.builder(Component.translatable("gui.target.mortar_gui.button_101"), e -> {
			if (true) {
				TargetMod.PACKET_HANDLER.sendToServer(new MortarGUIButtonMessage(3, x, y, z));
				MortarGUIButtonMessage.handleButtonAction(entity, 3, x, y, z);
			}
		}).bounds(this.leftPos + -73, this.topPos + 151, 30, 20).build();
		guistate.put("button:button_101", button_101);
		this.addRenderableWidget(button_101);
		button_05 = Button.builder(Component.translatable("gui.target.mortar_gui.button_05"), e -> {
			if (true) {
				TargetMod.PACKET_HANDLER.sendToServer(new MortarGUIButtonMessage(4, x, y, z));
				MortarGUIButtonMessage.handleButtonAction(entity, 4, x, y, z);
			}
		}).bounds(this.leftPos + -73, this.topPos + 97, 30, 20).build();
		guistate.put("button:button_05", button_05);
		this.addRenderableWidget(button_05);
		button_051 = Button.builder(Component.translatable("gui.target.mortar_gui.button_051"), e -> {
			if (true) {
				TargetMod.PACKET_HANDLER.sendToServer(new MortarGUIButtonMessage(5, x, y, z));
				MortarGUIButtonMessage.handleButtonAction(entity, 5, x, y, z);
			}
		}).bounds(this.leftPos + 42, this.topPos + 97, 29, 20).build();
		guistate.put("button:button_051", button_051);
		this.addRenderableWidget(button_051);
	}
}
