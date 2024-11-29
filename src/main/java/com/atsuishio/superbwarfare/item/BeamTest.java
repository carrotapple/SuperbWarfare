package com.atsuishio.superbwarfare.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class BeamTest extends Item {
    public BeamTest() {
        super(new Properties());
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!stack.getOrCreateTag().getBoolean("Using")) {
            stack.getOrCreateTag().putBoolean("Using", true);
            player.getCooldowns().addCooldown(this, 10);
        } else {
            stack.getOrCreateTag().putBoolean("Using", false);
        }

        return InteractionResultHolder.consume(stack);
    }

    @Override
    public void inventoryTick(ItemStack itemstack, Level world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(itemstack, world, entity, slot, selected);
        if (!selected) {
            if (itemstack.getOrCreateTag().getBoolean("Using")) {
                itemstack.getOrCreateTag().putBoolean("Using", false);
            }
        }
    }
}
