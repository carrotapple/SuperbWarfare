package com.atsuishio.superbwarfare.init;

import com.atsuishio.superbwarfare.ModUtils;
import com.atsuishio.superbwarfare.tools.ItemNBTTool;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModProperties {
    @SubscribeEvent
    public static void propertyOverrideRegistry(FMLClientSetupEvent event) {
        event.enqueueWork(() -> ItemProperties.register(ModItems.MONITOR.get(), ModUtils.loc("monitor_linked"),
                (itemStack, clientWorld, livingEntity, seed) -> ItemNBTTool.getBoolean(itemStack, "Linked", false) ? 1.0F : 0.0F));
        event.enqueueWork(() -> ItemProperties.register(ModItems.ARMOR_PLATE.get(), ModUtils.loc("armor_plate_infinite"),
                (itemStack, clientWorld, livingEntity, seed) -> ItemNBTTool.getBoolean(itemStack, "Infinite", false) ? 1.0F : 0.0F));
    }
}