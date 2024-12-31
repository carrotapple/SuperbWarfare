package com.atsuishio.superbwarfare.client.model.entity;

import com.atsuishio.superbwarfare.ModUtils;
import com.atsuishio.superbwarfare.entity.Ah6Entity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class Ah6Model extends GeoModel<Ah6Entity> {

    @Override
    public ResourceLocation getAnimationResource(Ah6Entity entity) {
        return null;
//        return ModUtils.loc("animations/wheel_chair.animation.json");
    }

    @Override
    public ResourceLocation getModelResource(Ah6Entity entity) {
        return new ResourceLocation(ModUtils.MODID, "geo/wheel_chair.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Ah6Entity entity) {
        return ModUtils.loc("textures/entity/wheel_chair.png");
    }
}
