package net.mcreator.superbwarfare.item;

import net.mcreator.superbwarfare.entity.HandGrenadeEntity;
import net.mcreator.superbwarfare.init.ModDamageTypes;
import net.mcreator.superbwarfare.init.ModSounds;
import net.mcreator.superbwarfare.tools.CustomExplosion;
import net.mcreator.superbwarfare.tools.ParticleTool;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Explosion;
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
        if (playerIn instanceof ServerPlayer serverPlayer) {
            serverPlayer.level().playSound(null, serverPlayer.getOnPos(), ModSounds.GRENADE_PULL.get(), SoundSource.PLAYERS, 1, 1);
        }
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

                player.getCooldowns().addCooldown(stack.getItem(), 25);

                int usingTime = this.getUseDuration(stack) - timeLeft;
                float power = Math.min(usingTime / 10.0f, 1.5f);

                HandGrenadeEntity handGrenade = new HandGrenadeEntity(player, worldIn, 100 - usingTime);
                handGrenade.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0f, power, 0.0f);
                worldIn.addFreshEntity(handGrenade);

                if (player instanceof ServerPlayer serverPlayer) {
                    serverPlayer.level().playSound(null, serverPlayer.getOnPos(), ModSounds.GRENADE_THROW.get(), SoundSource.PLAYERS, 1, 1);
                }

                if (!player.isCreative()) {
                    stack.shrink(1);
                }
            }
        }
    }

    @Override
    public ItemStack finishUsingItem(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity) {
        if (!pLevel.isClientSide) {
            CustomExplosion explosion = new CustomExplosion(pLevel, null,
                    ModDamageTypes.causeProjectileBoomDamage(pLevel.registryAccess(), pLivingEntity, pLivingEntity), 90,
                    pLivingEntity.getX(), pLivingEntity.getY(), pLivingEntity.getZ(), 6.5f, Explosion.BlockInteraction.KEEP).setDamageMultiplier(1.25f);
            explosion.explode();
            net.minecraftforge.event.ForgeEventFactory.onExplosionStart(pLevel, explosion);
            explosion.finalizeExplosion(false);
            ParticleTool.spawnMediumExplosionParticles(pLevel, pLivingEntity.position());

            if (pLivingEntity instanceof Player player) {
                player.getCooldowns().addCooldown(pStack.getItem(), 25);
            }
        }

        return super.finishUsingItem(pStack, pLevel, pLivingEntity);
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 100;
    }
}

