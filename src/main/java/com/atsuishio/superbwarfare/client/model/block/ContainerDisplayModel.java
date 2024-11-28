package com.atsuishio.superbwarfare.client.model.block;

import com.atsuishio.superbwarfare.ModUtils;
import com.atsuishio.superbwarfare.item.ContainerBlockItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class ContainerDisplayModel extends GeoModel<ContainerBlockItem> {

    @Override
    public ResourceLocation getAnimationResource(ContainerBlockItem animatable) {
        return ModUtils.loc("animations/container.animation.json");
    }

    @Override
    public ResourceLocation getModelResource(ContainerBlockItem animatable) {
        return ModUtils.loc("geo/container.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(ContainerBlockItem entity) {
        return ModUtils.loc("textures/block/container.png");
    }
}
