package net.mcreator.target.client.renderer.item;

import net.mcreator.target.item.gun.RpkItem;
import net.mcreator.target.client.layer.RpkLayer;
import net.mcreator.target.client.model.item.RpkItemModel;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class RpkItemRenderer extends GeoItemRenderer<RpkItem> {
    public RpkItemRenderer() {
        super(new RpkItemModel());
        this.addRenderLayer(new RpkLayer(this));
    }

    @Override
    public ResourceLocation getTextureLocation(RpkItem instance) {
        return super.getTextureLocation(instance);
    }
}
