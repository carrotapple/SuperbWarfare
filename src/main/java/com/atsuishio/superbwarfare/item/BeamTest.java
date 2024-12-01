package com.atsuishio.superbwarfare.item;

import com.atsuishio.superbwarfare.capability.LaserCapability;
import com.atsuishio.superbwarfare.capability.LaserHandler;
import com.atsuishio.superbwarfare.capability.ModCapabilities;
import com.atsuishio.superbwarfare.entity.projectile.LaserEntity;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
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

        player.getCapability(ModCapabilities.LASER_CAPABILITY).ifPresent(capability -> {
            player.startUsingItem(hand);

            if (!level.isClientSide) {
                double px = player.getX();
                double py = player.getY() + player.getBbHeight() * 0.6F;
                double pz = player.getZ();
                float yHeadRotAngle = (float) Math.toRadians(player.yHeadRot + 90);
                float xHeadRotAngle = (float) (float) -Math.toRadians(player.getXRot());
                LaserEntity laserEntity = new LaserEntity(player.level(), player, px, py, pz, yHeadRotAngle, xHeadRotAngle, 6000);
                capability.init(new LaserHandler(player, laserEntity, 0));
                capability.start();

                stack.getOrCreateTag().putBoolean("LaserFiring", true);
            }
        });

        return InteractionResultHolder.consume(player.getItemInHand(hand));
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity livingEntity, int timeCharged) {
        if (livingEntity instanceof Player player) {
            player.getCapability(ModCapabilities.LASER_CAPABILITY).ifPresent(LaserCapability.ILaserCapability::stop);
            stack.getOrCreateTag().putBoolean("LaserFiring", false);
        }
        super.releaseUsing(stack, level, livingEntity, timeCharged);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity) {
        if (pLivingEntity instanceof Player player) {
            player.getCapability(ModCapabilities.LASER_CAPABILITY).ifPresent(LaserCapability.ILaserCapability::stop);
            pStack.getOrCreateTag().putBoolean("LaserFiring", false);
        }
        return super.finishUsingItem(pStack, pLevel, pLivingEntity);
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 6000;
    }
}
