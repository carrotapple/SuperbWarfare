package com.atsuishio.superbwarfare.item.common.ammo;

import com.atsuishio.superbwarfare.tools.GunInfo;
import net.minecraft.world.item.Item;

public class RifleAmmo extends AmmoSupplierItem {
    public RifleAmmo() {
        super(GunInfo.Type.RIFLE, 5, new Item.Properties());
    }

}
