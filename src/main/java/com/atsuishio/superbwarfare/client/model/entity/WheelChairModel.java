package com.atsuishio.superbwarfare.client.model.entity;

import com.atsuishio.superbwarfare.ModUtils;
import com.atsuishio.superbwarfare.entity.WheelChairEntityMobile;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class WheelChairModel extends GeoModel<WheelChairEntityMobile> {

    @Override
    public ResourceLocation getAnimationResource(WheelChairEntityMobile entity) {
        return null;
//        return ModUtils.loc("animations/wheel_chair.animation.json");
    }

    @Override
    public ResourceLocation getModelResource(WheelChairEntityMobile entity) {
        return new ResourceLocation(ModUtils.MODID, "geo/wheel_chair.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(WheelChairEntityMobile entity) {
        return ModUtils.loc("textures/entity/wheel_chair.png");
    }
}
