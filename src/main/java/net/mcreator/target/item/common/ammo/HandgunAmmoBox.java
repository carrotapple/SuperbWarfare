package net.mcreator.target.item.common.ammo;

import net.mcreator.target.tools.GunInfo;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;

import java.util.List;

public class HandgunAmmoBox extends AmmoSupplierItem {
    public HandgunAmmoBox() {
        super(GunInfo.Type.HANDGUN, 60, new Item.Properties().stacksTo(64).rarity(Rarity.COMMON));
    }

    @Override
    public UseAnim getUseAnimation(ItemStack itemstack) {
        return UseAnim.EAT;
    }

    @Override
    public int getUseDuration(ItemStack itemstack) {
        return 16;
    }

    @Override
    public void appendHoverText(ItemStack itemstack, Level world, List<Component> list, TooltipFlag flag) {
        list.add(Component.translatable("des.target.handgun_ammo_box").withStyle(ChatFormatting.GRAY));
    }
}
