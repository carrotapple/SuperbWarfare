package com.atsuishio.superbwarfare.client.overlay;

import com.atsuishio.superbwarfare.client.gui.RangeHelper;
import com.atsuishio.superbwarfare.entity.MortarEntity;
import com.atsuishio.superbwarfare.tools.FormatTool;
import com.atsuishio.superbwarfare.tools.TraceTool;
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
public class MortarInfoOverlay {

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void eventHandler(RenderGuiEvent.Pre event) {
        int w = event.getWindow().getGuiScaledWidth();
        int h = event.getWindow().getGuiScaledHeight();
        Player player = Minecraft.getInstance().player;
        Entity lookingEntity = null;
        if (player != null) {
            lookingEntity = TraceTool.findLookingEntity(player, 6);
        }
        if (lookingEntity instanceof MortarEntity mortar) {

            player.displayClientMessage(Component.translatable("tips.superbwarfare.mortar.pitch").append(FormatTool.format1D(-mortar.getXRot(), "° "))
                            .append(Component.translatable("tips.superbwarfare.mortar.yaw")).append(FormatTool.format1D(mortar.getYRot(), "° "))
                            .append(Component.translatable("tips.superbwarfare.mortar.range")).append(FormatTool.format1D((int) RangeHelper.getRange(-mortar.getXRot()))).append("m")
                    , true);

//            event.getGuiGraphics().drawString(Minecraft.getInstance().font, Component.translatable("tips.superbwarfare.mortar.yaw")
//                            .append(Component.literal(FormatTool.format1D(mortar.getYRot(), "°"))),
//                    w / 2 + 12, h / 2 - 36, -1, false);
//            event.getGuiGraphics().drawString(Minecraft.getInstance().font, Component.translatable("tips.superbwarfare.mortar.pitch")
//                            .append(Component.literal(FormatTool.format1D(-mortar.getXRot(), "°"))),
//                    w / 2 + 12, h / 2 - 28, -1, false);
        }
    }
}
