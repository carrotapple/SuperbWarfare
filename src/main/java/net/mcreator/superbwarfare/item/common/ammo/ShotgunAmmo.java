package net.mcreator.superbwarfare.item.common.ammo;

import net.mcreator.superbwarfare.tools.GunInfo;
import net.minecraft.world.item.Item;

public class ShotgunAmmo extends AmmoSupplierItem {
    public ShotgunAmmo() {
        super(GunInfo.Type.SHOTGUN, 2, new Item.Properties());
    }

}
