package com.atsuishio.superbwarfare.item.common.ammo;

import com.atsuishio.superbwarfare.tools.GunInfo;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;

import java.util.List;

public class ShotgunAmmoBox extends AmmoSupplierItem {
    public ShotgunAmmoBox() {
        super(GunInfo.Type.SHOTGUN, 12, new Item.Properties().stacksTo(8));
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
        list.add(Component.translatable("des.superbwarfare.shotgun_ammo_box").withStyle(ChatFormatting.GRAY));
    }
}
