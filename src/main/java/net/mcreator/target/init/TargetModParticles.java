
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.target.init;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.api.distmarker.Dist;

import net.mcreator.target.client.particle.FirestarParticle;
import net.mcreator.target.client.particle.BulltholeParticle;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class TargetModParticles {
	@SubscribeEvent
	public static void registerParticles(RegisterParticleProvidersEvent event) {
		event.registerSpriteSet(TargetModParticleTypes.FIRESTAR.get(), FirestarParticle::provider);
		event.registerSpriteSet(TargetModParticleTypes.BULLTHOLE.get(), BulltholeParticle::provider);
	}
}
