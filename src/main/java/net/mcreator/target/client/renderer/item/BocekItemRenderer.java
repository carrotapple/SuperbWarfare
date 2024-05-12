package net.mcreator.target.client.renderer.item;

import net.mcreator.target.item.gun.BocekItem;
import net.mcreator.target.client.layer.BocekLayer;
import net.mcreator.target.client.model.item.BocekItemModel;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class BocekItemRenderer extends GeoItemRenderer<BocekItem> {
    public BocekItemRenderer() {
        super(new BocekItemModel());
        this.addRenderLayer(new BocekLayer(this));
    }

    @Override
    public ResourceLocation getTextureLocation(BocekItem instance) {
        return super.getTextureLocation(instance);
    }
}
