package net.mcreator.superbwarfare.item;

import net.mcreator.superbwarfare.entity.HandGrenadeEntity;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class HandGrenade extends Item {
    public HandGrenade() {
        super(new Properties().rarity(Rarity.UNCOMMON));
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
        ItemStack stack = playerIn.getItemInHand(handIn);
        playerIn.startUsingItem(handIn);
        return InteractionResultHolder.consume(stack);
    }

    @Override
    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack stack) {
        return UseAnim.SPEAR;
    }

    @Override
    public void releaseUsing(ItemStack stack, Level worldIn, LivingEntity entityLiving, int timeLeft) {
        if (!worldIn.isClientSide) {
            if (entityLiving instanceof Player player) {


                int usingTime = this.getUseDuration(stack) - timeLeft;

                float power = Math.min(usingTime / 10.0f, 1.5f);

                HandGrenadeEntity handGrenade = new HandGrenadeEntity(player, worldIn, 100 - usingTime);
                handGrenade.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0f, power, 0.0f);
                worldIn.addFreshEntity(handGrenade);

                if (!player.isCreative()) {
                    stack.shrink(1);
                }

                player.getCooldowns().addCooldown(stack.getItem(), 25);
            }
        }
    }



    @Override
    public int getUseDuration(ItemStack stack) {
        return 100;
    }
}

