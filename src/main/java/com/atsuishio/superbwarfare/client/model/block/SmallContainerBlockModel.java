package com.atsuishio.superbwarfare.client.model.block;

import com.atsuishio.superbwarfare.ModUtils;
import com.atsuishio.superbwarfare.block.entity.SmallContainerBlockEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class SmallContainerBlockModel extends GeoModel<SmallContainerBlockEntity> {

    @Override
    public ResourceLocation getAnimationResource(SmallContainerBlockEntity animatable) {
//        return ModUtils.loc("animations/small_container.animation.json");
        return null;
    }

    @Override
    public ResourceLocation getModelResource(SmallContainerBlockEntity animatable) {
        return ModUtils.loc("geo/small_container.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(SmallContainerBlockEntity animatable) {
        return ModUtils.loc("textures/block/small_container.png");
    }
}
