package net.mcreator.target.procedures;

import net.mcreator.target.init.TargetModItems;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;
import java.util.List;

@Mod.EventBusSubscriber
public class GunsTipsProcedure {
    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        execute(event, event.getItemStack(), event.getToolTip());
    }

    public static void execute(ItemStack itemstack, List<Component> tooltip) {
        execute(null, itemstack, tooltip);
    }

    private static void execute(@Nullable Event event, ItemStack itemstack, List<Component> tooltip) {
        if (tooltip == null)
            return;
        if (itemstack.getItem() == TargetModItems.CREATIVE_AMMO_BOX.get()) {
            tooltip.add(Component.literal("Creative Ammo * 2147483647"));
        }
        if (itemstack.getItem() == TargetModItems.RIFLE_AMMO_BOX.get()) {
            tooltip.add(Component.literal("Rifle Ammo * 30"));
        }
        if (itemstack.getItem() == TargetModItems.HANDGUN_AMMO_BOX.get()) {
            tooltip.add(Component.literal("Handgun Ammo * 30"));
        }
        if (itemstack.getItem() == TargetModItems.SHOTGUN_AMMO_BOX.get()) {
            tooltip.add(Component.literal("Shotgun Ammo * 12"));
        }
        if (itemstack.getItem() == TargetModItems.SNIPER_AMMO_BOX.get()) {
            tooltip.add(Component.literal("Sniper Ammo * 12"));
        }
    }
}
