package net.mcreator.superbwarfare.client;

import net.mcreator.superbwarfare.client.tooltip.ClientImageTooltip;
import net.mcreator.superbwarfare.client.tooltip.ImageTooltip;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientRenderHandler {

    @SubscribeEvent
    public static void registerTooltip(RegisterClientTooltipComponentFactoriesEvent event) {
        event.register(ImageTooltip.class, ClientImageTooltip::new);
    }

}
