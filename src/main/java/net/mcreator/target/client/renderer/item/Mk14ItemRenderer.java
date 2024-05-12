package net.mcreator.target.client.renderer.item;

import net.mcreator.target.item.gun.Mk14Item;
import net.mcreator.target.client.layer.Mk14Layer;
import net.mcreator.target.client.model.item.Mk14ItemModel;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class Mk14ItemRenderer extends GeoItemRenderer<Mk14Item> {
    public Mk14ItemRenderer() {
        super(new Mk14ItemModel());
        this.addRenderLayer(new Mk14Layer(this));
    }

    @Override
    public ResourceLocation getTextureLocation(Mk14Item instance) {
        return super.getTextureLocation(instance);
    }
}
