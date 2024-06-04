package net.mcreator.target.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.mcreator.target.TargetMod;
import net.mcreator.target.network.message.MortarGUIButtonMessage;
import net.mcreator.target.tools.TraceTool;
import net.mcreator.target.world.inventory.MortarGUIMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;

import java.text.DecimalFormat;
import java.util.HashMap;

public class MortarGUIScreen extends AbstractContainerScreen<MortarGUIMenu> {
    private final static HashMap<String, Object> GUI_STATE = MortarGUIMenu.GUI_STATE;
    private final int x, y, z;
    private final Player entity;
    Button button_angle_add_1;
    Button button_angle_reduce_1;
    Button button_angle_add_10;
    Button button_angle_reduce_10;
    Button button_angle_reduce_05;
    Button button_angle_add_05;

    public MortarGUIScreen(MortarGUIMenu container, Inventory inventory, Component text) {
        super(container, inventory, text);
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
            if (this.minecraft != null && this.minecraft.player != null) {
                this.minecraft.player.closeContainer();
            }
            return true;
        }
        return super.keyPressed(key, b, c);
    }

    @Override
    public void containerTick() {
        super.containerTick();
    }

    private String getAngleString(Entity entity) {
        if (entity == null) return "";
        Entity mortar = TraceTool.findLookingEntity(entity, 6);
        if (mortar == null) return "";
        return "Angle: " + new DecimalFormat("##.#").format(-mortar.getXRot());
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, getAngleString(entity), -18, 98, -1, false);

        Entity looking = TraceTool.findLookingEntity(entity, 6);
        var range = looking == null ? 0 : -looking.getXRot();

        guiGraphics.drawString(this.font, Component.literal("Range:" + (int) RangeHelper.getRange(range)), -18, 108, -16711885, false);
    }

    @Override
    public void onClose() {
        super.onClose();
    }

    @Override
    public void init() {
        super.init();
        button_angle_add_1 = Button.builder(Component.translatable("gui.target.mortar_gui.button_angle_add_1"), e -> {
            TargetMod.PACKET_HANDLER.sendToServer(new MortarGUIButtonMessage(0, x, y, z));
            MortarGUIButtonMessage.handleButtonAction(entity, 0);
        }).bounds(this.leftPos + 42, this.topPos + 124, 29, 20).build();
        GUI_STATE.put("button:button_angle_add_1", button_angle_add_1);
        this.addRenderableWidget(button_angle_add_1);
        button_angle_reduce_1 = Button.builder(Component.translatable("gui.target.mortar_gui.button_angle_reduce_1"), e -> {
            TargetMod.PACKET_HANDLER.sendToServer(new MortarGUIButtonMessage(1, x, y, z));
            MortarGUIButtonMessage.handleButtonAction(entity, 1);
        }).bounds(this.leftPos - 73, this.topPos + 124, 30, 20).build();
        GUI_STATE.put("button:button_angle_reduce_1", button_angle_reduce_1);
        this.addRenderableWidget(button_angle_reduce_1);
        button_angle_add_10 = Button.builder(Component.translatable("gui.target.mortar_gui.button_angle_add_10"), e -> {
            TargetMod.PACKET_HANDLER.sendToServer(new MortarGUIButtonMessage(2, x, y, z));
            MortarGUIButtonMessage.handleButtonAction(entity, 2);
        }).bounds(this.leftPos + 43, this.topPos + 151, 28, 20).build();
        GUI_STATE.put("button:button_angle_add_10", button_angle_add_10);
        this.addRenderableWidget(button_angle_add_10);
        button_angle_reduce_10 = Button.builder(Component.translatable("gui.target.mortar_gui.button_angle_reduce_10"), e -> {
            TargetMod.PACKET_HANDLER.sendToServer(new MortarGUIButtonMessage(3, x, y, z));
            MortarGUIButtonMessage.handleButtonAction(entity, 3);
        }).bounds(this.leftPos - 73, this.topPos + 151, 30, 20).build();
        GUI_STATE.put("button:button_angle_reduce_10", button_angle_reduce_10);
        this.addRenderableWidget(button_angle_reduce_10);
        button_angle_reduce_05 = Button.builder(Component.translatable("gui.target.mortar_gui.button_angle_reduce_05"), e -> {
            TargetMod.PACKET_HANDLER.sendToServer(new MortarGUIButtonMessage(4, x, y, z));
            MortarGUIButtonMessage.handleButtonAction(entity, 4);
        }).bounds(this.leftPos - 73, this.topPos + 97, 30, 20).build();
        GUI_STATE.put("button:button_angle_reduce_05", button_angle_reduce_05);
        this.addRenderableWidget(button_angle_reduce_05);
        button_angle_add_05 = Button.builder(Component.translatable("gui.target.mortar_gui.button_angle_add_05"), e -> {
            TargetMod.PACKET_HANDLER.sendToServer(new MortarGUIButtonMessage(5, x, y, z));
            MortarGUIButtonMessage.handleButtonAction(entity, 5);
        }).bounds(this.leftPos + 42, this.topPos + 97, 29, 20).build();
        GUI_STATE.put("button:button_angle_add_05", button_angle_add_05);
        this.addRenderableWidget(button_angle_add_05);
    }
}
