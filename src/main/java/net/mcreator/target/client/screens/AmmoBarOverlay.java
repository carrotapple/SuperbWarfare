package net.mcreator.target.client.screens;

import net.mcreator.target.event.PlayerEventHandler;
import net.mcreator.target.init.TargetModTags;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class AmmoBarOverlay {

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void eventHandler(RenderGuiEvent.Pre event) {
        int w = event.getWindow().getGuiScaledWidth();
        int h = event.getWindow().getGuiScaledHeight();
        Player entity = Minecraft.getInstance().player;
        if (entity != null && entity.getMainHandItem().is(TargetModTags.Items.GUN)) {
            event.getGuiGraphics().drawString(
                    Minecraft.getInstance().font,
                    PlayerEventHandler.handleAmmoCount(entity),
                    w / 2 + 92,
                    h - 11,
                    -16711936,
                    false
            );
        }
    }
}
