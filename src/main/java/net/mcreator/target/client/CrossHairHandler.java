package net.mcreator.target.client;

import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import net.minecraft.tags.ItemTags;
import net.minecraft.resources.ResourceLocation;

import net.mcreator.target.network.TargetModVariables;

@Mod.EventBusSubscriber(modid = "target", value = Dist.CLIENT)
public class CrossHairHandler {
    @SubscribeEvent
    public static void onRenderOverlay(RenderGuiOverlayEvent.Pre event) {
        if (event.getOverlay() != VanillaGuiOverlay.CROSSHAIR.type()) {
            return;
        }

        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) {
            return;
        }

        if (!mc.options.getCameraType().isFirstPerson()) {
            return;
        }

        ItemStack heldItem = mc.player.getMainHandItem();
        {
        if ((mc.player.getMainHandItem()).is(ItemTags.create(new ResourceLocation("target:gun")))) {
                event.setCanceled(true);            	
        	}
        }
    }
}
