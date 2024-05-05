
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.target.init;

import net.mcreator.target.client.model.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = {Dist.CLIENT})
public class TargetModModels {
    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(Modelrpg7_rocket_Converted.LAYER_LOCATION, Modelrpg7_rocket_Converted::createBodyLayer);
        event.registerLayerDefinition(Modelbullet.LAYER_LOCATION, Modelbullet::createBodyLayer);
        event.registerLayerDefinition(Modelmortar_shell_Converted.LAYER_LOCATION, Modelmortar_shell_Converted::createBodyLayer);
        event.registerLayerDefinition(Modeltaser_rod.LAYER_LOCATION, Modeltaser_rod::createBodyLayer);
        event.registerLayerDefinition(ModelGrenade.LAYER_LOCATION, ModelGrenade::createBodyLayer);
        event.registerLayerDefinition(Modelbocekarrow.LAYER_LOCATION, Modelbocekarrow::createBodyLayer);
        event.registerLayerDefinition(Modelclaymore.LAYER_LOCATION, Modelclaymore::createBodyLayer);
    }
}
