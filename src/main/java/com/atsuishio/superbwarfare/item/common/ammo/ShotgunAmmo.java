package com.atsuishio.superbwarfare.item.common.ammo;

import com.atsuishio.superbwarfare.tools.GunInfo;
import net.minecraft.world.item.Item;

public class ShotgunAmmo extends AmmoSupplierItem {
    public ShotgunAmmo() {
        super(GunInfo.Type.SHOTGUN, 2, new Item.Properties());
    }

}
