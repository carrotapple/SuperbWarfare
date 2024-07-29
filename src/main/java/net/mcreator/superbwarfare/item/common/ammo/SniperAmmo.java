package net.mcreator.superbwarfare.item.common.ammo;

import net.mcreator.superbwarfare.tools.GunInfo;
import net.minecraft.world.item.Item;

public class SniperAmmo extends AmmoSupplierItem {
    public SniperAmmo() {
        super(GunInfo.Type.SNIPER, 2, new Item.Properties());
    }

}
