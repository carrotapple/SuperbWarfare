package net.mcreator.superbwarfare.client.renderer.item;

import net.mcreator.superbwarfare.item.gun.Aa12Item;
import net.mcreator.superbwarfare.client.layer.Aa12Layer;
import net.mcreator.superbwarfare.client.model.item.Aa12ItemModel;
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
