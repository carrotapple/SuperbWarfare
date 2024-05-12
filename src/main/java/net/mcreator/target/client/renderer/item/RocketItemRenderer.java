package net.mcreator.target.client.renderer.item;

import net.mcreator.target.item.common.ammo.Rocket;
import net.mcreator.target.client.model.item.RocketItemModel;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class RocketItemRenderer extends GeoItemRenderer<Rocket> {
    public RocketItemRenderer() {
        super(new RocketItemModel());
    }

    @Override
    public ResourceLocation getTextureLocation(Rocket instance) {
        return super.getTextureLocation(instance);
    }
}
