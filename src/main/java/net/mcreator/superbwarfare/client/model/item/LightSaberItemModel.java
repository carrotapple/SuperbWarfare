package net.mcreator.superbwarfare.client.model.item;

import net.mcreator.superbwarfare.ModUtils;
import net.mcreator.superbwarfare.item.LightSaber;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class LightSaberItemModel extends GeoModel<LightSaber> {

    @Override
    public ResourceLocation getAnimationResource(LightSaber animatable) {
        return null;
    }

    @Override
    public ResourceLocation getModelResource(LightSaber animatable) {
        return ModUtils.loc("geo/light_saber.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(LightSaber animatable) {
        return ModUtils.loc("textures/item/light_saber.png");
    }
}
