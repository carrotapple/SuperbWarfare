package net.mcreator.target.init;

import net.mcreator.target.item.*;
import net.mcreator.target.item.common.ammo.Rocket;
import net.mcreator.target.item.gun.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import software.bernie.geckolib.animatable.GeoItem;

@Mod.EventBusSubscriber
public class ItemAnimationFactory {
    public static void disableUseAnim() {
        try {
            ItemInHandRenderer renderer = Minecraft.getInstance().gameRenderer.itemInHandRenderer;
            renderer.mainHandHeight = 1F;
            renderer.oMainHandHeight = 1F;
            renderer.offHandHeight = 1F;
            renderer.oOffHandHeight = 1F;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SubscribeEvent
    public static void animatedItems(TickEvent.PlayerTickEvent event) {
        String animation;
        ItemStack mainHandItemStack = event.player.getMainHandItem();
        Item mainHandItem = mainHandItemStack.getItem();
        if (event.phase == TickEvent.Phase.START && (mainHandItem instanceof GeoItem || event.player.getOffhandItem().getItem() instanceof GeoItem)) {
            if (!mainHandItemStack.getOrCreateTag().getString("geckoAnim").isEmpty() && !(mainHandItem instanceof ArmorItem)) {
                animation = mainHandItemStack.getOrCreateTag().getString("geckoAnim");
                mainHandItemStack.getOrCreateTag().putString("geckoAnim", "");

                if (mainHandItem instanceof Taser animatable)
                    if (event.player.level().isClientSide()) {
                        animatable.animationProcedure = animation;
                        disableUseAnim();
                    }
                if (mainHandItem instanceof Abekiri animatable)
                    if (event.player.level().isClientSide()) {
                        animatable.animationProcedure = animation;
                        disableUseAnim();
                    }
                if (mainHandItem instanceof Trachelium animatable)
                    if (event.player.level().isClientSide()) {
                        animatable.animationProcedure = animation;
                        disableUseAnim();
                    }
                if (mainHandItem instanceof VectorItem animatable)
                    if (event.player.level().isClientSide()) {
                        animatable.animationProcedure = animation;
                        disableUseAnim();
                    }
                if (mainHandItem instanceof AK47Item animatable)
                    if (event.player.level().isClientSide()) {
                        animatable.animationProcedure = animation;
                        disableUseAnim();
                    }
                if (mainHandItem instanceof SksItem animatable)
                    if (event.player.level().isClientSide()) {
                        animatable.animationProcedure = animation;
                        disableUseAnim();
                    }
                if (mainHandItem instanceof M4Item animatable)
                    if (event.player.level().isClientSide()) {
                        animatable.animationProcedure = animation;
                        disableUseAnim();
                    }
                if (mainHandItem instanceof Hk416Item animatable)
                    if (event.player.level().isClientSide()) {
                        animatable.animationProcedure = animation;
                        disableUseAnim();
                    }
                if (mainHandItem instanceof Mk14Item animatable)
                    if (event.player.level().isClientSide()) {
                        animatable.animationProcedure = animation;
                        disableUseAnim();
                    }
                if (mainHandItem instanceof MarlinItem animatable)
                    if (event.player.level().isClientSide()) {
                        animatable.animationProcedure = animation;
                        disableUseAnim();
                    }
                if (mainHandItem instanceof SvdItem animatable)
                    if (event.player.level().isClientSide()) {
                        animatable.animationProcedure = animation;
                        disableUseAnim();
                    }
                if (mainHandItem instanceof M98bItem animatable)
                    if (event.player.level().isClientSide()) {
                        animatable.animationProcedure = animation;
                        disableUseAnim();
                    }
                if (mainHandItem instanceof SentinelItem animatable)
                    if (event.player.level().isClientSide()) {
                        animatable.animationProcedure = animation;
                        disableUseAnim();
                    }
                if (mainHandItem instanceof HuntingRifle animatable)
                    if (event.player.level().isClientSide()) {
                        animatable.animationProcedure = animation;
                        disableUseAnim();
                    }
                if (mainHandItem instanceof Kraber animatable)
                    if (event.player.level().isClientSide()) {
                        animatable.animationProcedure = animation;
                        disableUseAnim();
                    }
                if (mainHandItem instanceof M870Item animatable)
                    if (event.player.level().isClientSide()) {
                        animatable.animationProcedure = animation;
                        disableUseAnim();
                    }
                if (mainHandItem instanceof Aa12Item animatable)
                    if (event.player.level().isClientSide()) {
                        animatable.animationProcedure = animation;
                        disableUseAnim();
                    }
                if (mainHandItem instanceof Devotion animatable)
                    if (event.player.level().isClientSide()) {
                        animatable.animationProcedure = animation;
                        disableUseAnim();
                    }
                if (mainHandItem instanceof RpkItem animatable)
                    if (event.player.level().isClientSide()) {
                        animatable.animationProcedure = animation;
                        disableUseAnim();
                    }
                if (mainHandItem instanceof M60Item animatable)
                    if (event.player.level().isClientSide()) {
                        animatable.animationProcedure = animation;
                        disableUseAnim();
                    }
                if (mainHandItem instanceof Minigun animatable)
                    if (event.player.level().isClientSide()) {
                        animatable.animationProcedure = animation;
                        disableUseAnim();
                    }
                if (mainHandItem instanceof M79Item animatable)
                    if (event.player.level().isClientSide()) {
                        animatable.animationProcedure = animation;
                        disableUseAnim();
                    }
                if (mainHandItem instanceof RpgItem animatable)
                    if (event.player.level().isClientSide()) {
                        animatable.animationProcedure = animation;
                        disableUseAnim();
                    }
                if (mainHandItem instanceof BocekItem animatable)
                    if (event.player.level().isClientSide()) {
                        animatable.animationProcedure = animation;
                        disableUseAnim();
                    }
                if (mainHandItem instanceof LightSaberItem animatable)
                    if (event.player.level().isClientSide()) {
                        animatable.animationProcedure = animation;
                        disableUseAnim();
                    }
                if (mainHandItem instanceof Rocket animatable)
                    if (event.player.level().isClientSide()) {
                        animatable.animationProcedure = animation;
                        disableUseAnim();
                    }
            }
            if (!event.player.getOffhandItem().getOrCreateTag().getString("geckoAnim").equals("") && !(event.player.getOffhandItem().getItem() instanceof ArmorItem)) {
                animation = event.player.getOffhandItem().getOrCreateTag().getString("geckoAnim");
                event.player.getOffhandItem().getOrCreateTag().putString("geckoAnim", "");
                if (event.player.getOffhandItem().getItem() instanceof Taser animatable)
                    if (event.player.level().isClientSide()) {
                        animatable.animationProcedure = animation;
                        disableUseAnim();
                    }
                if (event.player.getOffhandItem().getItem() instanceof Abekiri animatable)
                    if (event.player.level().isClientSide()) {
                        animatable.animationProcedure = animation;
                        disableUseAnim();
                    }
                if (event.player.getOffhandItem().getItem() instanceof Trachelium animatable)
                    if (event.player.level().isClientSide()) {
                        animatable.animationProcedure = animation;
                        disableUseAnim();
                    }
                if (event.player.getOffhandItem().getItem() instanceof VectorItem animatable)
                    if (event.player.level().isClientSide()) {
                        animatable.animationProcedure = animation;
                        disableUseAnim();
                    }
                if (event.player.getOffhandItem().getItem() instanceof AK47Item animatable)
                    if (event.player.level().isClientSide()) {
                        animatable.animationProcedure = animation;
                        disableUseAnim();
                    }
                if (event.player.getOffhandItem().getItem() instanceof SksItem animatable)
                    if (event.player.level().isClientSide()) {
                        animatable.animationProcedure = animation;
                        disableUseAnim();
                    }
                if (event.player.getOffhandItem().getItem() instanceof M4Item animatable)
                    if (event.player.level().isClientSide()) {
                        animatable.animationProcedure = animation;
                        disableUseAnim();
                    }
                if (event.player.getOffhandItem().getItem() instanceof Hk416Item animatable)
                    if (event.player.level().isClientSide()) {
                        animatable.animationProcedure = animation;
                        disableUseAnim();
                    }
                if (event.player.getOffhandItem().getItem() instanceof Mk14Item animatable)
                    if (event.player.level().isClientSide()) {
                        animatable.animationProcedure = animation;
                        disableUseAnim();
                    }
                if (event.player.getOffhandItem().getItem() instanceof MarlinItem animatable)
                    if (event.player.level().isClientSide()) {
                        animatable.animationProcedure = animation;
                        disableUseAnim();
                    }
                if (event.player.getOffhandItem().getItem() instanceof SvdItem animatable)
                    if (event.player.level().isClientSide()) {
                        animatable.animationProcedure = animation;
                        disableUseAnim();
                    }
                if (event.player.getOffhandItem().getItem() instanceof M98bItem animatable)
                    if (event.player.level().isClientSide()) {
                        animatable.animationProcedure = animation;
                        disableUseAnim();
                    }
                if (event.player.getOffhandItem().getItem() instanceof SentinelItem animatable)
                    if (event.player.level().isClientSide()) {
                        animatable.animationProcedure = animation;
                        disableUseAnim();
                    }
                if (event.player.getOffhandItem().getItem() instanceof HuntingRifle animatable)
                    if (event.player.level().isClientSide()) {
                        animatable.animationProcedure = animation;
                        disableUseAnim();
                    }
                if (event.player.getOffhandItem().getItem() instanceof Kraber animatable)
                    if (event.player.level().isClientSide()) {
                        animatable.animationProcedure = animation;
                        disableUseAnim();
                    }
                if (event.player.getOffhandItem().getItem() instanceof M870Item animatable)
                    if (event.player.level().isClientSide()) {
                        animatable.animationProcedure = animation;
                        disableUseAnim();
                    }
                if (event.player.getOffhandItem().getItem() instanceof Aa12Item animatable)
                    if (event.player.level().isClientSide()) {
                        animatable.animationProcedure = animation;
                        disableUseAnim();
                    }
                if (event.player.getOffhandItem().getItem() instanceof Devotion animatable)
                    if (event.player.level().isClientSide()) {
                        animatable.animationProcedure = animation;
                        disableUseAnim();
                    }
                if (event.player.getOffhandItem().getItem() instanceof RpkItem animatable)
                    if (event.player.level().isClientSide()) {
                        animatable.animationProcedure = animation;
                        disableUseAnim();
                    }
                if (event.player.getOffhandItem().getItem() instanceof M60Item animatable)
                    if (event.player.level().isClientSide()) {
                        animatable.animationProcedure = animation;
                        disableUseAnim();
                    }
                if (event.player.getOffhandItem().getItem() instanceof Minigun animatable)
                    if (event.player.level().isClientSide()) {
                        animatable.animationProcedure = animation;
                        disableUseAnim();
                    }
                if (event.player.getOffhandItem().getItem() instanceof M79Item animatable)
                    if (event.player.level().isClientSide()) {
                        animatable.animationProcedure = animation;
                        disableUseAnim();
                    }
                if (event.player.getOffhandItem().getItem() instanceof RpgItem animatable)
                    if (event.player.level().isClientSide()) {
                        animatable.animationProcedure = animation;
                        disableUseAnim();
                    }
                if (event.player.getOffhandItem().getItem() instanceof BocekItem animatable)
                    if (event.player.level().isClientSide()) {
                        animatable.animationProcedure = animation;
                        disableUseAnim();
                    }
                if (event.player.getOffhandItem().getItem() instanceof LightSaberItem animatable)
                    if (event.player.level().isClientSide()) {
                        animatable.animationProcedure = animation;
                        disableUseAnim();
                    }
                if (event.player.getOffhandItem().getItem() instanceof Rocket animatable)
                    if (event.player.level().isClientSide()) {
                        animatable.animationProcedure = animation;
                        disableUseAnim();
                    }
            }
        }
    }
}
