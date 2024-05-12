package net.mcreator.target.client.renderer.item;

import net.mcreator.target.item.gun.M98bItem;
import net.mcreator.target.client.layer.M98bLayer;
import net.mcreator.target.client.model.item.M98bItemModel;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class M98bItemRenderer extends GeoItemRenderer<M98bItem> {
    public M98bItemRenderer() {
        super(new M98bItemModel());
        this.addRenderLayer(new M98bLayer(this));
    }

    @Override
    public ResourceLocation getTextureLocation(M98bItem instance) {
        return super.getTextureLocation(instance);
    }
}
