package net.mcreator.target.init;

import net.mcreator.target.item.AnimatedItem;
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

    private static void setAnimationState(ItemStack itemStack, boolean isClientSide) {
        if (!itemStack.getOrCreateTag().getString("geckoAnim").isEmpty() && !(itemStack.getItem() instanceof ArmorItem)) {
            String animation = itemStack.getOrCreateTag().getString("geckoAnim");
            itemStack.getOrCreateTag().putString("geckoAnim", "");

            if (itemStack.getItem() instanceof AnimatedItem animatable && isClientSide) {
                animatable.setAnimationProcedure(animation);
                disableUseAnim();
            }
        }
    }

    @SubscribeEvent
    public static void animatedItems(TickEvent.PlayerTickEvent event) {
        ItemStack mainHandItemStack = event.player.getMainHandItem();
        Item mainHandItem = mainHandItemStack.getItem();
        ItemStack offhandItemStack = event.player.getOffhandItem();
        Item offhandItem = offhandItemStack.getItem();

        if (event.phase == TickEvent.Phase.START && (mainHandItem instanceof GeoItem || offhandItem instanceof GeoItem)) {
            boolean isClientSide = event.player.level().isClientSide();
            setAnimationState(mainHandItemStack, isClientSide);
            setAnimationState(offhandItemStack, isClientSide);
        }
    }
}
