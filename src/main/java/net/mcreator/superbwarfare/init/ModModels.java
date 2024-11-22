package net.mcreator.superbwarfare.init;

import net.mcreator.superbwarfare.client.model.entity.ModelGrenade;
import net.mcreator.superbwarfare.client.model.entity.ModelHandGrenade;
import net.mcreator.superbwarfare.client.model.entity.ModelMortarShell;
import net.mcreator.superbwarfare.client.model.entity.ModelTaserRod;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = {Dist.CLIENT})
public class ModModels {
    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ModelMortarShell.LAYER_LOCATION, ModelMortarShell::createBodyLayer);
        event.registerLayerDefinition(ModelTaserRod.LAYER_LOCATION, ModelTaserRod::createBodyLayer);
        event.registerLayerDefinition(ModelGrenade.LAYER_LOCATION, ModelGrenade::createBodyLayer);
        event.registerLayerDefinition(ModelHandGrenade.LAYER_LOCATION, ModelHandGrenade::createBodyLayer);
    }
}
