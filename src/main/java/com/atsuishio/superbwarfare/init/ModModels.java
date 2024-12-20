package com.atsuishio.superbwarfare.init;

import com.atsuishio.superbwarfare.ModUtils;
import com.atsuishio.superbwarfare.client.model.block.FuMO25Model;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ModUtils.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModModels {

    @SubscribeEvent
    public static void registerLayer(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(FuMO25Model.LAYER_LOCATION, FuMO25Model::createBodyLayer);
    }
}
