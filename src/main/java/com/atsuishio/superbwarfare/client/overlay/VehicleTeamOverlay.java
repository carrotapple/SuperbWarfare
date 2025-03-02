package com.atsuishio.superbwarfare.client.overlay;

import com.atsuishio.superbwarfare.entity.vehicle.VehicleEntity;
import com.atsuishio.superbwarfare.tools.FormatTool;
import com.atsuishio.superbwarfare.tools.TraceTool;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class VehicleTeamOverlay {

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void eventHandler(RenderGuiEvent.Pre event) {
        int w = event.getWindow().getGuiScaledWidth();
        int h = event.getWindow().getGuiScaledHeight();
        Player player = Minecraft.getInstance().player;
        PoseStack poseStack = event.getGuiGraphics().pose();
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
            if (lookingEntity.getFirstPassenger() instanceof Player player1) {
                event.getGuiGraphics().drawString(Minecraft.getInstance().font,
                        Component.literal(player1.getDisplayName().getString() + (player1.getTeam() == null ? "" : " <" + (player1.getTeam().getName()) + ">")),
                        w / 2 + 90, h / 2 - 4, player1.getTeamColor(), false);
                event.getGuiGraphics().drawString(Minecraft.getInstance().font,
                        Component.literal(lookingEntity.getDisplayName().getString() + " " + FormatTool.format1D(entityRange, "m")),
                        w / 2 + 90, h / 2 + 5, player1.getTeamColor(), false);
            } else {
                event.getGuiGraphics().drawString(Minecraft.getInstance().font,
                        Component.literal(lookingEntity.getDisplayName().getString() + " " + FormatTool.format1D(entityRange, "M")),
                        w / 2 + 90, h / 2 + 5, -1, false);
            }
            poseStack.popPose();
        }
    }
}
