package net.mcreator.target.item.common.ammo;

import net.mcreator.target.procedures.CreativeAmmoBoxWanJiaWanChengShiYongWuPinShiProcedure;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

public class CreativeAmmoBox extends Item {
    public CreativeAmmoBox() {
        super(new Item.Properties().stacksTo(64).rarity(Rarity.EPIC));
    }

    @Override
    public UseAnim getUseAnimation(ItemStack itemstack) {
        return UseAnim.EAT;
    }

    @Override
    public int getUseDuration(ItemStack itemstack) {
        return 6;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean isFoil(ItemStack itemstack) {
        return true;
    }

    @Override
    public void appendHoverText(ItemStack itemstack, Level world, List<Component> list, TooltipFlag flag) {
        list.add(Component.translatable("des.target.creative_ammo_box").withStyle(ChatFormatting.GRAY));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player entity, InteractionHand hand) {
        InteractionResultHolder<ItemStack> ar = super.use(world, entity, hand);
        CreativeAmmoBoxWanJiaWanChengShiYongWuPinShiProcedure.execute(entity, ar.getObject());
        return ar;
    }
}
