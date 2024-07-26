package net.mcreator.superbwarfare.init;

import net.mcreator.superbwarfare.client.model.entity.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = {Dist.CLIENT})
public class TargetModModels {
    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ModelRPGRocket.LAYER_LOCATION, ModelRPGRocket::createBodyLayer);
        event.registerLayerDefinition(ModelBullet.LAYER_LOCATION, ModelBullet::createBodyLayer);
        event.registerLayerDefinition(ModelMortarShell.LAYER_LOCATION, ModelMortarShell::createBodyLayer);
        event.registerLayerDefinition(ModelTaserRod.LAYER_LOCATION, ModelTaserRod::createBodyLayer);
        event.registerLayerDefinition(ModelGrenade.LAYER_LOCATION, ModelGrenade::createBodyLayer);
        event.registerLayerDefinition(ModelBocekArrow.LAYER_LOCATION, ModelBocekArrow::createBodyLayer);
        event.registerLayerDefinition(ModelClaymore.LAYER_LOCATION, ModelClaymore::createBodyLayer);
    }
}
