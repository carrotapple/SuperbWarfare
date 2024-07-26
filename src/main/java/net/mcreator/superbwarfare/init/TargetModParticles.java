package net.mcreator.superbwarfare.init;

import net.mcreator.superbwarfare.client.particle.BulletHoleParticle;
import net.mcreator.superbwarfare.client.particle.FireStarParticle;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class TargetModParticles {
    @SubscribeEvent
    public static void registerParticles(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(TargetModParticleTypes.FIRE_STAR.get(), FireStarParticle::provider);
        event.registerSpriteSet(TargetModParticleTypes.BULLET_HOLE.get(), BulletHoleParticle::provider);
    }
}

