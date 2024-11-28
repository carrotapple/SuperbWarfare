package com.atsuishio.superbwarfare.block.listener;

import com.atsuishio.superbwarfare.ModUtils;
import com.atsuishio.superbwarfare.block.renderer.ContainerTileRenderer;
import com.atsuishio.superbwarfare.init.ModBlockEntities;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ModUtils.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientListener {
	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
		event.registerBlockEntityRenderer(ModBlockEntities.CONTAINER.get(), context -> new ContainerTileRenderer());
	}
}
