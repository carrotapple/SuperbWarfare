package com.atsuishio.superbwarfare.client.model.entity;

import com.atsuishio.superbwarfare.ModUtils;
import com.atsuishio.superbwarfare.entity.ExplosiveEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

import java.util.UUID;

public class ExplosiveModel extends GeoModel<ExplosiveEntity> {

    @Override
    public ResourceLocation getAnimationResource(ExplosiveEntity entity) {
        return ModUtils.loc("animations/c4.animation.json");
    }

    @Override
    public ResourceLocation getModelResource(ExplosiveEntity entity) {
        return ModUtils.loc("geo/c4.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(ExplosiveEntity entity) {
        UUID uuid = entity.getUUID();
        if (uuid.getLeastSignificantBits() % 114 == 0) {
            return ModUtils.loc("textures/entity/c4_alter.png");
        }
        return ModUtils.loc("textures/entity/c4.png");
    }
}
