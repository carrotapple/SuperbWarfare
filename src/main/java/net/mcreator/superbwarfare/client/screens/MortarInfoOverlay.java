package net.mcreator.superbwarfare.client.screens;

import net.mcreator.superbwarfare.entity.MortarEntity;
import net.mcreator.superbwarfare.tools.TraceTool;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.text.DecimalFormat;

import static net.mcreator.superbwarfare.entity.MortarEntity.PITCH;
import static net.mcreator.superbwarfare.entity.MortarEntity.Y_ROT;

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
            event.getGuiGraphics().drawString(Minecraft.getInstance().font, Component.translatable("des.superbwarfare.mortar.yaw")
                            .append(Component.literal(new DecimalFormat("##.#").format(mortar.getEntityData().get(Y_ROT)) + "°")),
                    w / 2 + 12, h / 2 - 36, -1, false);
            event.getGuiGraphics().drawString(Minecraft.getInstance().font, Component.translatable("des.superbwarfare.mortar.pitch")
                            .append(Component.literal(new DecimalFormat("##.#").format(mortar.getEntityData().get(PITCH)) + "°")),
                    w / 2 + 12, h / 2 - 28, -1, false);
        }
    }
}
