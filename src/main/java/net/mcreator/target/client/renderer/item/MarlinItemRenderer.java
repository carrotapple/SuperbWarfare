package net.mcreator.target.client.renderer.item;

import net.mcreator.target.item.gun.MarlinItem;
import net.mcreator.target.client.layer.MarlinLayer;
import net.mcreator.target.client.model.item.MarlinItemModel;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class MarlinItemRenderer extends GeoItemRenderer<MarlinItem> {
    public MarlinItemRenderer() {
        super(new MarlinItemModel());
        this.addRenderLayer(new MarlinLayer(this));
    }

    @Override
    public ResourceLocation getTextureLocation(MarlinItem instance) {
        return super.getTextureLocation(instance);
    }
}
