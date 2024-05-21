package net.mcreator.target.item;

import net.minecraft.world.item.Item;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

public class DogTag extends Item implements ICurioItem {
    public DogTag() {
        super(new Properties().stacksTo(1));
    }
}
