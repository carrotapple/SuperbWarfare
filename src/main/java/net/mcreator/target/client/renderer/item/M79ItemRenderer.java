package net.mcreator.target.client.renderer.item;

import net.mcreator.target.item.gun.M79Item;
import net.mcreator.target.client.layer.M79Layer;
import net.mcreator.target.client.model.item.M79ItemModel;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class M79ItemRenderer extends GeoItemRenderer<M79Item> {
    public M79ItemRenderer() {
        super(new M79ItemModel());
        this.addRenderLayer(new M79Layer(this));
    }

    @Override
    public ResourceLocation getTextureLocation(M79Item instance) {
        return super.getTextureLocation(instance);
    }
}
