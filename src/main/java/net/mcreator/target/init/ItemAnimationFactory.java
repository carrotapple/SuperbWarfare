package net.mcreator.target.init;

import software.bernie.geckolib.animatable.GeoItem;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.TickEvent;

import net.minecraft.world.item.ArmorItem;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.Minecraft;

import net.mcreator.target.item.VectorItem;
import net.mcreator.target.item.TracheliumItem;
import net.mcreator.target.item.TaserItem;
import net.mcreator.target.item.SvdItem;
import net.mcreator.target.item.SksItem;
import net.mcreator.target.item.SentinelItem;
import net.mcreator.target.item.RpkItem;
import net.mcreator.target.item.RpgItem;
import net.mcreator.target.item.RocketItem;
import net.mcreator.target.item.Mk14Item;
import net.mcreator.target.item.MinigunItem;
import net.mcreator.target.item.MarlinItem;
import net.mcreator.target.item.M98bItem;
import net.mcreator.target.item.M870Item;
import net.mcreator.target.item.M79Item;
import net.mcreator.target.item.M60Item;
import net.mcreator.target.item.M4Item;
import net.mcreator.target.item.LightSaberItem;
import net.mcreator.target.item.KraberItem;
import net.mcreator.target.item.HuntingRifleItem;
import net.mcreator.target.item.Hk416Item;
import net.mcreator.target.item.DevotionItem;
import net.mcreator.target.item.BocekItem;
import net.mcreator.target.item.AbekiriItem;
import net.mcreator.target.item.Aa12Item;
import net.mcreator.target.item.AK47Item;

@Mod.EventBusSubscriber
public class ItemAnimationFactory {
	public static void disableUseAnim() {
		try {
			ItemInHandRenderer renderer = Minecraft.getInstance().gameRenderer.itemInHandRenderer;
			if (renderer != null) {
				renderer.mainHandHeight = 1F;
				renderer.oMainHandHeight = 1F;
				renderer.offHandHeight = 1F;
				renderer.oOffHandHeight = 1F;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SubscribeEvent
	public static void animatedItems(TickEvent.PlayerTickEvent event) {
		String animation = "";
		if (event.phase == TickEvent.Phase.START && (event.player.getMainHandItem().getItem() instanceof GeoItem || event.player.getOffhandItem().getItem() instanceof GeoItem)) {
			if (!event.player.getMainHandItem().getOrCreateTag().getString("geckoAnim").equals("") && !(event.player.getMainHandItem().getItem() instanceof ArmorItem)) {
				animation = event.player.getMainHandItem().getOrCreateTag().getString("geckoAnim");
				event.player.getMainHandItem().getOrCreateTag().putString("geckoAnim", "");
				if (event.player.getMainHandItem().getItem() instanceof TaserItem animatable)
					if (event.player.level().isClientSide()) {
						animatable.animationprocedure = animation;
						disableUseAnim();
					}
				if (event.player.getMainHandItem().getItem() instanceof AbekiriItem animatable)
					if (event.player.level().isClientSide()) {
						animatable.animationprocedure = animation;
						disableUseAnim();
					}
				if (event.player.getMainHandItem().getItem() instanceof TracheliumItem animatable)
					if (event.player.level().isClientSide()) {
						animatable.animationprocedure = animation;
						disableUseAnim();
					}
				if (event.player.getMainHandItem().getItem() instanceof VectorItem animatable)
					if (event.player.level().isClientSide()) {
						animatable.animationprocedure = animation;
						disableUseAnim();
					}
				if (event.player.getMainHandItem().getItem() instanceof AK47Item animatable)
					if (event.player.level().isClientSide()) {
						animatable.animationprocedure = animation;
						disableUseAnim();
					}
				if (event.player.getMainHandItem().getItem() instanceof SksItem animatable)
					if (event.player.level().isClientSide()) {
						animatable.animationprocedure = animation;
						disableUseAnim();
					}
				if (event.player.getMainHandItem().getItem() instanceof M4Item animatable)
					if (event.player.level().isClientSide()) {
						animatable.animationprocedure = animation;
						disableUseAnim();
					}
				if (event.player.getMainHandItem().getItem() instanceof Hk416Item animatable)
					if (event.player.level().isClientSide()) {
						animatable.animationprocedure = animation;
						disableUseAnim();
					}
				if (event.player.getMainHandItem().getItem() instanceof Mk14Item animatable)
					if (event.player.level().isClientSide()) {
						animatable.animationprocedure = animation;
						disableUseAnim();
					}
				if (event.player.getMainHandItem().getItem() instanceof MarlinItem animatable)
					if (event.player.level().isClientSide()) {
						animatable.animationprocedure = animation;
						disableUseAnim();
					}
				if (event.player.getMainHandItem().getItem() instanceof SvdItem animatable)
					if (event.player.level().isClientSide()) {
						animatable.animationprocedure = animation;
						disableUseAnim();
					}
				if (event.player.getMainHandItem().getItem() instanceof M98bItem animatable)
					if (event.player.level().isClientSide()) {
						animatable.animationprocedure = animation;
						disableUseAnim();
					}
				if (event.player.getMainHandItem().getItem() instanceof SentinelItem animatable)
					if (event.player.level().isClientSide()) {
						animatable.animationprocedure = animation;
						disableUseAnim();
					}
				if (event.player.getMainHandItem().getItem() instanceof HuntingRifleItem animatable)
					if (event.player.level().isClientSide()) {
						animatable.animationprocedure = animation;
						disableUseAnim();
					}
				if (event.player.getMainHandItem().getItem() instanceof KraberItem animatable)
					if (event.player.level().isClientSide()) {
						animatable.animationprocedure = animation;
						disableUseAnim();
					}
				if (event.player.getMainHandItem().getItem() instanceof M870Item animatable)
					if (event.player.level().isClientSide()) {
						animatable.animationprocedure = animation;
						disableUseAnim();
					}
				if (event.player.getMainHandItem().getItem() instanceof Aa12Item animatable)
					if (event.player.level().isClientSide()) {
						animatable.animationprocedure = animation;
						disableUseAnim();
					}
				if (event.player.getMainHandItem().getItem() instanceof DevotionItem animatable)
					if (event.player.level().isClientSide()) {
						animatable.animationprocedure = animation;
						disableUseAnim();
					}
				if (event.player.getMainHandItem().getItem() instanceof RpkItem animatable)
					if (event.player.level().isClientSide()) {
						animatable.animationprocedure = animation;
						disableUseAnim();
					}
				if (event.player.getMainHandItem().getItem() instanceof M60Item animatable)
					if (event.player.level().isClientSide()) {
						animatable.animationprocedure = animation;
						disableUseAnim();
					}
				if (event.player.getMainHandItem().getItem() instanceof MinigunItem animatable)
					if (event.player.level().isClientSide()) {
						animatable.animationprocedure = animation;
						disableUseAnim();
					}
				if (event.player.getMainHandItem().getItem() instanceof M79Item animatable)
					if (event.player.level().isClientSide()) {
						animatable.animationprocedure = animation;
						disableUseAnim();
					}
				if (event.player.getMainHandItem().getItem() instanceof RpgItem animatable)
					if (event.player.level().isClientSide()) {
						animatable.animationprocedure = animation;
						disableUseAnim();
					}
				if (event.player.getMainHandItem().getItem() instanceof BocekItem animatable)
					if (event.player.level().isClientSide()) {
						animatable.animationprocedure = animation;
						disableUseAnim();
					}
				if (event.player.getMainHandItem().getItem() instanceof LightSaberItem animatable)
					if (event.player.level().isClientSide()) {
						animatable.animationprocedure = animation;
						disableUseAnim();
					}
				if (event.player.getMainHandItem().getItem() instanceof RocketItem animatable)
					if (event.player.level().isClientSide()) {
						animatable.animationprocedure = animation;
						disableUseAnim();
					}
			}
			if (!event.player.getOffhandItem().getOrCreateTag().getString("geckoAnim").equals("") && !(event.player.getOffhandItem().getItem() instanceof ArmorItem)) {
				animation = event.player.getOffhandItem().getOrCreateTag().getString("geckoAnim");
				event.player.getOffhandItem().getOrCreateTag().putString("geckoAnim", "");
				if (event.player.getOffhandItem().getItem() instanceof TaserItem animatable)
					if (event.player.level().isClientSide()) {
						animatable.animationprocedure = animation;
						disableUseAnim();
					}
				if (event.player.getOffhandItem().getItem() instanceof AbekiriItem animatable)
					if (event.player.level().isClientSide()) {
						animatable.animationprocedure = animation;
						disableUseAnim();
					}
				if (event.player.getOffhandItem().getItem() instanceof TracheliumItem animatable)
					if (event.player.level().isClientSide()) {
						animatable.animationprocedure = animation;
						disableUseAnim();
					}
				if (event.player.getOffhandItem().getItem() instanceof VectorItem animatable)
					if (event.player.level().isClientSide()) {
						animatable.animationprocedure = animation;
						disableUseAnim();
					}
				if (event.player.getOffhandItem().getItem() instanceof AK47Item animatable)
					if (event.player.level().isClientSide()) {
						animatable.animationprocedure = animation;
						disableUseAnim();
					}
				if (event.player.getOffhandItem().getItem() instanceof SksItem animatable)
					if (event.player.level().isClientSide()) {
						animatable.animationprocedure = animation;
						disableUseAnim();
					}
				if (event.player.getOffhandItem().getItem() instanceof M4Item animatable)
					if (event.player.level().isClientSide()) {
						animatable.animationprocedure = animation;
						disableUseAnim();
					}
				if (event.player.getOffhandItem().getItem() instanceof Hk416Item animatable)
					if (event.player.level().isClientSide()) {
						animatable.animationprocedure = animation;
						disableUseAnim();
					}
				if (event.player.getOffhandItem().getItem() instanceof Mk14Item animatable)
					if (event.player.level().isClientSide()) {
						animatable.animationprocedure = animation;
						disableUseAnim();
					}
				if (event.player.getOffhandItem().getItem() instanceof MarlinItem animatable)
					if (event.player.level().isClientSide()) {
						animatable.animationprocedure = animation;
						disableUseAnim();
					}
				if (event.player.getOffhandItem().getItem() instanceof SvdItem animatable)
					if (event.player.level().isClientSide()) {
						animatable.animationprocedure = animation;
						disableUseAnim();
					}
				if (event.player.getOffhandItem().getItem() instanceof M98bItem animatable)
					if (event.player.level().isClientSide()) {
						animatable.animationprocedure = animation;
						disableUseAnim();
					}
				if (event.player.getOffhandItem().getItem() instanceof SentinelItem animatable)
					if (event.player.level().isClientSide()) {
						animatable.animationprocedure = animation;
						disableUseAnim();
					}
				if (event.player.getOffhandItem().getItem() instanceof HuntingRifleItem animatable)
					if (event.player.level().isClientSide()) {
						animatable.animationprocedure = animation;
						disableUseAnim();
					}
				if (event.player.getOffhandItem().getItem() instanceof KraberItem animatable)
					if (event.player.level().isClientSide()) {
						animatable.animationprocedure = animation;
						disableUseAnim();
					}
				if (event.player.getOffhandItem().getItem() instanceof M870Item animatable)
					if (event.player.level().isClientSide()) {
						animatable.animationprocedure = animation;
						disableUseAnim();
					}
				if (event.player.getOffhandItem().getItem() instanceof Aa12Item animatable)
					if (event.player.level().isClientSide()) {
						animatable.animationprocedure = animation;
						disableUseAnim();
					}
				if (event.player.getOffhandItem().getItem() instanceof DevotionItem animatable)
					if (event.player.level().isClientSide()) {
						animatable.animationprocedure = animation;
						disableUseAnim();
					}
				if (event.player.getOffhandItem().getItem() instanceof RpkItem animatable)
					if (event.player.level().isClientSide()) {
						animatable.animationprocedure = animation;
						disableUseAnim();
					}
				if (event.player.getOffhandItem().getItem() instanceof M60Item animatable)
					if (event.player.level().isClientSide()) {
						animatable.animationprocedure = animation;
						disableUseAnim();
					}
				if (event.player.getOffhandItem().getItem() instanceof MinigunItem animatable)
					if (event.player.level().isClientSide()) {
						animatable.animationprocedure = animation;
						disableUseAnim();
					}
				if (event.player.getOffhandItem().getItem() instanceof M79Item animatable)
					if (event.player.level().isClientSide()) {
						animatable.animationprocedure = animation;
						disableUseAnim();
					}
				if (event.player.getOffhandItem().getItem() instanceof RpgItem animatable)
					if (event.player.level().isClientSide()) {
						animatable.animationprocedure = animation;
						disableUseAnim();
					}
				if (event.player.getOffhandItem().getItem() instanceof BocekItem animatable)
					if (event.player.level().isClientSide()) {
						animatable.animationprocedure = animation;
						disableUseAnim();
					}
				if (event.player.getOffhandItem().getItem() instanceof LightSaberItem animatable)
					if (event.player.level().isClientSide()) {
						animatable.animationprocedure = animation;
						disableUseAnim();
					}
				if (event.player.getOffhandItem().getItem() instanceof RocketItem animatable)
					if (event.player.level().isClientSide()) {
						animatable.animationprocedure = animation;
						disableUseAnim();
					}
			}
		}
	}
}
