package com.atsuishio.superbwarfare.item;

import com.atsuishio.superbwarfare.entity.BeamEntity;
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
            if (!level.isClientSide) {
                BeamEntity entity = getBeamEntity(level, player);
                level.addFreshEntity(entity);
            }
            stack.getOrCreateTag().putBoolean("Using", true);
            player.getCooldowns().addCooldown(this, 10);
        } else {
            stack.getOrCreateTag().putBoolean("Using", false);
        }

        return InteractionResultHolder.consume(stack);
    }

    private static BeamEntity getBeamEntity(Level level, Player player) {
        BeamEntity entity = new BeamEntity(player, level);
        entity.moveTo(player.getX() + 0.5 * player.getLookAngle().x,
                player.getEyeY() - 0.3 + 0.5 * player.getLookAngle().y,
                player.getZ() + 0.5 * player.getLookAngle().z, player.getYRot(), 0);
        entity.setYBodyRot(player.getYRot());
        entity.setYHeadRot(player.getYRot());
        return entity;
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
