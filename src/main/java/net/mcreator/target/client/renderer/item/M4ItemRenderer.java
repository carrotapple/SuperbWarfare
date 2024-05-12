package net.mcreator.target.client.renderer.item;

import net.mcreator.target.item.gun.M4Item;
import net.mcreator.target.client.layer.M4Layer;
import net.mcreator.target.client.model.item.M4ItemModel;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class M4ItemRenderer extends GeoItemRenderer<M4Item> {
    public M4ItemRenderer() {
        super(new M4ItemModel());
        this.addRenderLayer(new M4Layer(this));
    }

    @Override
    public ResourceLocation getTextureLocation(M4Item instance) {
        return super.getTextureLocation(instance);
    }
}
