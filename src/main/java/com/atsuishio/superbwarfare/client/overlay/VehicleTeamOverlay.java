package com.atsuishio.superbwarfare.client.overlay;

import com.atsuishio.superbwarfare.ModUtils;
import com.atsuishio.superbwarfare.entity.vehicle.base.VehicleEntity;
import com.atsuishio.superbwarfare.tools.FormatTool;
import com.atsuishio.superbwarfare.tools.TraceTool;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

@OnlyIn(Dist.CLIENT)
public class VehicleTeamOverlay implements IGuiOverlay {

    public static final String ID = ModUtils.MODID + "_vehicle_team";

    @Override
    public void render(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) {
        Player player = gui.getMinecraft().player;
        PoseStack poseStack = guiGraphics.pose();
        if (player == null) return;

        boolean lookAtEntity = false;

        double entityRange = 0;
        Entity lookingEntity = TraceTool.findLookingEntity(player, 520);

        if (lookingEntity instanceof VehicleEntity) {
            lookAtEntity = true;
            entityRange = player.distanceTo(lookingEntity);
        }

        if (lookAtEntity) {
            poseStack.pushPose();
            poseStack.scale(0.8f, 0.8f, 1);
            var font = gui.getMinecraft().font;

            if (lookingEntity.getFirstPassenger() instanceof Player player1) {
                guiGraphics.drawString(font,
                        Component.literal(player1.getDisplayName().getString() + (player1.getTeam() == null ? "" : " <" + (player1.getTeam().getName()) + ">")),
                        screenWidth / 2 + 90, screenHeight / 2 - 4, player1.getTeamColor(), false);
                guiGraphics.drawString(font,
                        Component.literal(lookingEntity.getDisplayName().getString() + " " + FormatTool.format1D(entityRange, "m")),
                        screenWidth / 2 + 90, screenHeight / 2 + 5, player1.getTeamColor(), false);
            } else {
                guiGraphics.drawString(font,
                        Component.literal(lookingEntity.getDisplayName().getString() + " " + FormatTool.format1D(entityRange, "M")),
                        screenWidth / 2 + 90, screenHeight / 2 + 5, -1, false);
            }
            poseStack.popPose();
        }
    }
}
