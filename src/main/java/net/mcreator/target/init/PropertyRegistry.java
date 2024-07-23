package net.mcreator.target.init;

import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.mcreator.target.tools.ItemNBTTool;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class PropertyRegistry {
    @SubscribeEvent
    public static void propertyOverrideRegistry(FMLClientSetupEvent event) {
        event.enqueueWork(() -> ItemProperties.register(TargetModItems.MONITOR.get(), new ResourceLocation("target", "monitor_linked"),
                (itemStack, clientWorld, livingEntity, seed) -> ItemNBTTool.getBoolean(itemStack, "linked", false) ? 1.0F : 0.0F));
    }
}