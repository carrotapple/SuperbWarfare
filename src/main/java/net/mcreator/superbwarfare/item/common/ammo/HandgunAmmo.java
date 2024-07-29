package net.mcreator.superbwarfare.item.common.ammo;

import net.mcreator.superbwarfare.tools.GunInfo;
import net.minecraft.world.item.Item;

public class HandgunAmmo extends AmmoSupplierItem {
    public HandgunAmmo() {
        super(GunInfo.Type.HANDGUN, 5, new Item.Properties());
    }

}
