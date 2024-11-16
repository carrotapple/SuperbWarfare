package net.mcreator.superbwarfare.item;

import net.mcreator.superbwarfare.init.ModItems;
import net.mcreator.superbwarfare.init.ModTags;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
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
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ArmorPlate extends Item {
    public ArmorPlate() {
        super(new Item.Properties());
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        if (pStack.getOrCreateTag().getBoolean("Infinite")) {
            pTooltipComponents.add(Component.translatable("des.superbwarfare.armor_plate.infinite").withStyle(ChatFormatting.GRAY));
        }
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
        ItemStack stack = playerIn.getItemInHand(handIn);
        ItemStack armor = playerIn.getItemBySlot(EquipmentSlot.CHEST);

        if (armor == ItemStack.EMPTY) return InteractionResultHolder.fail(stack);

        int armorLevel = 1;
        if (armor.is(ModTags.Items.MILITARY_ARMOR)) {
            armorLevel = 2;
        } else if (armor.is(ModTags.Items.MILITARY_ARMOR_HEAVY)) {
            armorLevel = 3;
        }

        if (armor.getOrCreateTag().getDouble("ArmorPlate") < armorLevel * 15) {
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
            } else if (armor.is(ModTags.Items.MILITARY_ARMOR_HEAVY)) {
                armorLevel = 3;
            }

            armor.getOrCreateTag().putDouble("ArmorPlate", Mth.clamp(armor.getOrCreateTag().getDouble("ArmorPlate") + 15, 0, armorLevel * 15));

            if (pLivingEntity instanceof ServerPlayer serverPlayer) {
                serverPlayer.level().playSound(null, serverPlayer.getOnPos(), SoundEvents.ARMOR_EQUIP_IRON, SoundSource.PLAYERS, 0.5f, 1);
            }

            if (pLivingEntity instanceof Player player && !player.isCreative() && !pStack.getOrCreateTag().getBoolean("Infinite")) {
                pStack.shrink(1);
            }
        }

        return super.finishUsingItem(pStack, pLevel, pLivingEntity);
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 20;
    }

    public static ItemStack getInfiniteInstance() {
        ItemStack stack = new ItemStack(ModItems.ARMOR_PLATE.get());
        stack.getOrCreateTag().putBoolean("Infinite", true);
        return stack;
    }
}
