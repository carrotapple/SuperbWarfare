package net.mcreator.superbwarfare.client;

import net.mcreator.superbwarfare.client.tooltip.*;
import net.mcreator.superbwarfare.client.tooltip.component.BocekImageComponent;
import net.mcreator.superbwarfare.client.tooltip.component.GunImageComponent;
import net.mcreator.superbwarfare.client.tooltip.component.ShotgunImageComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientRenderHandler {

    @SubscribeEvent
    public static void registerTooltip(RegisterClientTooltipComponentFactoriesEvent event) {
        event.register(GunImageComponent.class, ClientGunImageTooltip::new);
        event.register(ShotgunImageComponent.class, ClientShotgunImageTooltip::new);
        event.register(BocekImageComponent.class, ClientBocekImageTooltip::new);
    }

}
