package net.mcreator.target.client.renderer.item;

import net.mcreator.target.item.gun.Aa12Item;
import net.mcreator.target.client.layer.Aa12Layer;
import net.mcreator.target.client.model.item.Aa12ItemModel;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class Aa12ItemRenderer extends GeoItemRenderer<Aa12Item> {
    public Aa12ItemRenderer() {
        super(new Aa12ItemModel());
        this.addRenderLayer(new Aa12Layer(this));
    }

    @Override
    public ResourceLocation getTextureLocation(Aa12Item instance) {
        return super.getTextureLocation(instance);
    }
}
