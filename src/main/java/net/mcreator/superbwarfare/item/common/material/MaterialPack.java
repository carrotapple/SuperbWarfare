package net.mcreator.superbwarfare.item.common.material;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

public class MaterialPack extends Item {

    public MaterialPack(Rarity rarity) {
        super(new Properties().rarity(rarity));
    }
}
