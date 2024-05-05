package net.mcreator.target.client.screens;

import net.mcreator.target.procedures.AmmobarXianShiYouXiNeiDieJiaCengProcedure;
import net.mcreator.target.procedures.AmmocountProcedure;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber({Dist.CLIENT})
public class AmmobarOverlay {
    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void eventHandler(RenderGuiEvent.Pre event) {
        int w = event.getWindow().getGuiScaledWidth();
        int h = event.getWindow().getGuiScaledHeight();
        Player entity = Minecraft.getInstance().player;
        if (AmmobarXianShiYouXiNeiDieJiaCengProcedure.execute(entity)) {
            event.getGuiGraphics().drawString(
                    Minecraft.getInstance().font,
                    AmmocountProcedure.execute(entity),
                    w / 2 + 92,
                    h - 11,
                    -16711936,
                    false
            );
        }
    }
}
