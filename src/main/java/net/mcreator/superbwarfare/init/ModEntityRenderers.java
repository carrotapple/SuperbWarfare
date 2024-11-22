package net.mcreator.superbwarfare.init;

import net.mcreator.superbwarfare.client.renderer.entity.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModEntityRenderers {

    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.MORTAR.get(), MortarRenderer::new);
        event.registerEntityRenderer(ModEntities.SENPAI.get(), SenpaiRenderer::new);
        event.registerEntityRenderer(ModEntities.CLAYMORE.get(), ClaymoreRenderer::new);
        event.registerEntityRenderer(ModEntities.TASER_BULLET_PROJECTILE.get(), TaserBulletProjectileRenderer::new);
        event.registerEntityRenderer(ModEntities.GUN_GRENADE.get(), GunGrenadeRenderer::new);
        event.registerEntityRenderer(ModEntities.TARGET.get(), TargetRenderer::new);
        event.registerEntityRenderer(ModEntities.RPG_ROCKET.get(), RpgRocketRenderer::new);
        event.registerEntityRenderer(ModEntities.MORTAR_SHELL.get(), MortarShellRenderer::new);
        event.registerEntityRenderer(ModEntities.CANNON_SHELL.get(), CannonShellRenderer::new);
        event.registerEntityRenderer(ModEntities.PROJECTILE.get(), ProjectileEntityRenderer::new);
        event.registerEntityRenderer(ModEntities.MK_42.get(), Mk42Renderer::new);
        event.registerEntityRenderer(ModEntities.DRONE.get(), DroneRenderer::new);
        event.registerEntityRenderer(ModEntities.HAND_GRENADE_ENTITY.get(), HandGrenadeEntityRenderer::new);
        event.registerEntityRenderer(ModEntities.RGO_GRENADE.get(), RgoGrenadeRenderer::new);
        event.registerEntityRenderer(ModEntities.MLE_1934.get(), Mle1934Renderer::new);
        event.registerEntityRenderer(ModEntities.JAVELIN_MISSILE.get(), JavelinMissileRenderer::new);
    }
}
