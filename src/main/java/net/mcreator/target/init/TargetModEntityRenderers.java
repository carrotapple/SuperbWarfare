package net.mcreator.target.init;

import net.mcreator.target.client.renderer.entity.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class TargetModEntityRenderers {
    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(TargetModEntities.TARGET_1.get(), Target1Renderer::new);
        event.registerEntityRenderer(TargetModEntities.MORTAR.get(), MortarRenderer::new);
        event.registerEntityRenderer(TargetModEntities.SENPAI.get(), SenpaiRenderer::new);
        event.registerEntityRenderer(TargetModEntities.CLAYMORE.get(), ClaymoreRenderer::new);
        event.registerEntityRenderer(TargetModEntities.TASER_BULLET_PROJECTILE.get(), TaserBulletProjectileRenderer::new);
        event.registerEntityRenderer(TargetModEntities.GUN_GRENADE.get(), GunGrenadeRenderer::new);
        event.registerEntityRenderer(TargetModEntities.TARGET.get(), TargetRenderer::new);
        event.registerEntityRenderer(TargetModEntities.RPG_ROCKET.get(), RpgRocketRenderer::new);
        event.registerEntityRenderer(TargetModEntities.MORTAR_SHELL.get(), MortarShellRenderer::new);
        event.registerEntityRenderer(TargetModEntities.BOCEK_ARROW.get(), BocekArrowRenderer::new);
        event.registerEntityRenderer(TargetModEntities.PROJECTILE.get(), ProjectileRenderer::new);
        event.registerEntityRenderer(TargetModEntities.FRAG.get(), FragRenderer::new);
    }
}
