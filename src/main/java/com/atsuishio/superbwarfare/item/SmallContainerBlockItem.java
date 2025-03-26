package com.atsuishio.superbwarfare.item;

import com.atsuishio.superbwarfare.init.ModBlocks;
import net.minecraft.world.item.BlockItem;

public class SmallContainerBlockItem extends BlockItem {

    public SmallContainerBlockItem() {
        super(ModBlocks.SMALL_CONTAINER.get(), new Properties().stacksTo(1));
    }
}
