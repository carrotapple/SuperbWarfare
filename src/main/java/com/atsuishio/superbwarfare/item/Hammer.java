package com.atsuishio.superbwarfare.item;

import com.atsuishio.superbwarfare.init.ModItems;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class Hammer extends SwordItem {

    public Hammer() {
        super(Tiers.IRON, 9, -3.2f, new Item.Properties().durability(400));
    }

    @Override
    public boolean hasCraftingRemainingItem(ItemStack stack) {
        return true;
    }

    @Override
    public ItemStack getCraftingRemainingItem(ItemStack itemstack) {
        ItemStack stack = new ItemStack(this);
        stack.setDamageValue(itemstack.getDamageValue() + 1);
        if (stack.getDamageValue() >= stack.getMaxDamage()) {
            return ItemStack.EMPTY;
        }
        return stack;
    }

    @Override
    public boolean isRepairable(ItemStack itemstack) {
        return true;
    }

    @SubscribeEvent
    public static void onItemCrafted(PlayerEvent.ItemCraftedEvent event) {
        var item = event.getCrafting();
        var container = event.getInventory();
        var player = event.getEntity();
        if (player == null) return;

        if (player.level().isClientSide) return;

        if (item.is(ModItems.HAMMER.get())) {
            int count = 0;
            for (int i = 0; i < container.getContainerSize(); i++) {
                if (container.getItem(i).is(ModItems.HAMMER.get())) count++;
            }
            if (count == 2) {
                container.clearContent();
            }
        }
    }
}
