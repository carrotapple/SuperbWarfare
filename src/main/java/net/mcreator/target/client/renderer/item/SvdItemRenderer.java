package net.mcreator.target.client.renderer.item;

import net.mcreator.target.item.gun.SvdItem;
import net.mcreator.target.client.layer.SvdLayer;
import net.mcreator.target.client.model.item.SvdItemModel;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class SvdItemRenderer extends GeoItemRenderer<SvdItem> {
    public SvdItemRenderer() {
        super(new SvdItemModel());
        this.addRenderLayer(new SvdLayer(this));
    }

    @Override
    public ResourceLocation getTextureLocation(SvdItem instance) {
        return super.getTextureLocation(instance);
    }
}
