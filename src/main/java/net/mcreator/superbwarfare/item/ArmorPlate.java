package net.mcreator.superbwarfare.item;


import net.mcreator.superbwarfare.init.ModTags;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class ArmorPlate extends Item {
    public ArmorPlate() {
        super(new Item.Properties());
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
        ItemStack stack = playerIn.getItemInHand(handIn);
        ItemStack armor = playerIn.getItemBySlot(EquipmentSlot.CHEST);

        if (armor.getItem() == ItemStack.EMPTY.getItem()) return InteractionResultHolder.fail(stack);

        int armorLevel = 1;
        if (armor.is(ModTags.Items.MILITARY_ARMOR)) {
            armorLevel = 2;
        }

        if (armor.getOrCreateTag().getDouble("ArmorPlate") < armorLevel * 30) {
            playerIn.startUsingItem(handIn);
        }

        return InteractionResultHolder.fail(stack);
    }

    @Override
    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack stack) {
        return UseAnim.BOW;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity) {
        if (!pLevel.isClientSide) {

            ItemStack armor = pLivingEntity.getItemBySlot(EquipmentSlot.CHEST);

            int armorLevel = 1;
            if (armor.is(ModTags.Items.MILITARY_ARMOR)) {
                armorLevel = 2;
            }

            armor.getOrCreateTag().putDouble("ArmorPlate", Mth.clamp(armor.getOrCreateTag().getDouble("ArmorPlate") + 30, 0, armorLevel * 30));

            if (pLivingEntity instanceof ServerPlayer serverPlayer) {
                serverPlayer.level().playSound(null, serverPlayer.getOnPos(), SoundEvents.ARMOR_EQUIP_IRON, SoundSource.PLAYERS, 0.5f, 1);
            }

            if (pLivingEntity instanceof Player player && !player.isCreative()) {
                pStack.shrink(1);
            }
        }

        return super.finishUsingItem(pStack, pLevel, pLivingEntity);
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 30;
    }

}
