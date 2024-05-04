
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.target.init;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.api.distmarker.Dist;

import net.mcreator.target.client.renderer.TaserBulletProjectileRenderer;
import net.mcreator.target.client.renderer.TargetRenderer;
import net.mcreator.target.client.renderer.Target1Renderer;
import net.mcreator.target.client.renderer.SenpaiRenderer;
import net.mcreator.target.client.renderer.RpgRocketRenderer;
import net.mcreator.target.client.renderer.MortarShellRenderer;
import net.mcreator.target.client.renderer.MortarRenderer;
import net.mcreator.target.client.renderer.GunGrenadeRenderer;
import net.mcreator.target.client.renderer.ClaymoreRenderer;
import net.mcreator.target.client.renderer.BulletRenderer;
import net.mcreator.target.client.renderer.BocekarrowRenderer;

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
		event.registerEntityRenderer(TargetModEntities.BULLET.get(), BulletRenderer::new);
		event.registerEntityRenderer(TargetModEntities.RPG_ROCKET.get(), RpgRocketRenderer::new);
		event.registerEntityRenderer(TargetModEntities.MORTAR_SHELL.get(), MortarShellRenderer::new);
		event.registerEntityRenderer(TargetModEntities.BOCEKARROW.get(), BocekarrowRenderer::new);
	}
}
