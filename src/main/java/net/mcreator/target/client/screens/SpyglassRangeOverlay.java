package net.mcreator.target.client.screens;

import net.mcreator.target.tools.TraceTool;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.text.DecimalFormat;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class SpyglassRangeOverlay {

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void eventHandler(RenderGuiEvent.Pre event) {
        int w = event.getWindow().getGuiScaledWidth();
        int h = event.getWindow().getGuiScaledHeight();
        Player player = Minecraft.getInstance().player;
        if (player != null && (player.getMainHandItem().getItem() == Items.SPYGLASS || player.getOffhandItem().getItem() == Items.SPYGLASS ) && player.isUsingItem()) {
            event.getGuiGraphics().drawString(
                    Minecraft.getInstance().font,
                    getDistanceString(player),
                    w / 2 + 19,
                    h / 2 - 23,
                    -1,
                    false
            );
        }
    }

    private static String getDistanceString(Player entity) {
        Entity looking = TraceTool.findLookingEntity(entity, 1024);
        if (looking instanceof LivingEntity) {
            return looking.getDisplayName().getString() + ":" + new DecimalFormat("##.#").format(entity.position().distanceTo(looking.position())) + "M";
        }
        return "";
    }
}
