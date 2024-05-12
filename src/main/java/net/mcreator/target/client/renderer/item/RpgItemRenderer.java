package net.mcreator.target.client.renderer.item;

import net.mcreator.target.item.gun.RpgItem;
import net.mcreator.target.client.layer.RpgLayer;
import net.mcreator.target.client.model.item.RpgItemModel;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class RpgItemRenderer extends GeoItemRenderer<RpgItem> {
    public RpgItemRenderer() {
        super(new RpgItemModel());
        this.addRenderLayer(new RpgLayer(this));
    }

    @Override
    public ResourceLocation getTextureLocation(RpgItem instance) {
        return super.getTextureLocation(instance);
    }
}
