package com.atsuishio.superbwarfare.item.common.ammo;

import com.atsuishio.superbwarfare.tools.GunInfo;
import net.minecraft.world.item.Item;

public class HandgunAmmo extends AmmoSupplierItem {
    public HandgunAmmo() {
        super(GunInfo.Type.HANDGUN, 5, new Item.Properties());
    }

}
