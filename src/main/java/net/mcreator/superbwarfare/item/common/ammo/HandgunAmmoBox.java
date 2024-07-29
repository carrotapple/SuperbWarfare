package net.mcreator.superbwarfare.item.common.ammo;

import net.mcreator.superbwarfare.tools.GunInfo;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;

import java.util.List;

public class HandgunAmmoBox extends AmmoSupplierItem {
    public HandgunAmmoBox() {
        super(GunInfo.Type.HANDGUN, 30, new Item.Properties());
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
        list.add(Component.translatable("des.superbwarfare.handgun_ammo_box").withStyle(ChatFormatting.GRAY));
    }
}
