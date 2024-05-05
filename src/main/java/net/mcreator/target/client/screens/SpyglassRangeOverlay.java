package net.mcreator.target.client.screens;

import net.mcreator.target.procedures.EntityRangeProcedure;
import net.mcreator.target.procedures.SpyglassRangeXianShiYouXiNeiDieJiaCengProcedure;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber({Dist.CLIENT})
public class SpyglassRangeOverlay {
    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void eventHandler(RenderGuiEvent.Pre event) {
        int w = event.getWindow().getGuiScaledWidth();
        int h = event.getWindow().getGuiScaledHeight();
        Player entity = Minecraft.getInstance().player;
        if (SpyglassRangeXianShiYouXiNeiDieJiaCengProcedure.execute(entity)) {
            event.getGuiGraphics().drawString(
                    Minecraft.getInstance().font,
                    EntityRangeProcedure.execute(entity),
                    w / 2 + 19,
                    h / 2 + -23,
                    -1,
                    false
            );
        }
    }
}
