package net.mcreator.target.client.renderer.item;

import net.mcreator.target.item.gun.HuntingRifle;
import net.mcreator.target.client.layer.HuntingRifleLayer;
import net.mcreator.target.client.model.item.HuntingRifleItemModel;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class HuntingRifleItemRenderer extends GeoItemRenderer<HuntingRifle> {
    public HuntingRifleItemRenderer() {
        super(new HuntingRifleItemModel());
        this.addRenderLayer(new HuntingRifleLayer(this));
    }

    @Override
    public ResourceLocation getTextureLocation(HuntingRifle instance) {
        return super.getTextureLocation(instance);
    }
}

