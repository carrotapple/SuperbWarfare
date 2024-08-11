package net.mcreator.superbwarfare.client.renderer.item;

import net.mcreator.superbwarfare.client.layer.K98Layer;
import net.mcreator.superbwarfare.client.model.item.K98ItemModel;
import net.mcreator.superbwarfare.item.gun.K98Item;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class K98ItemRenderer extends GeoItemRenderer<K98Item> {
    public K98ItemRenderer() {
        super(new K98ItemModel());
        this.addRenderLayer(new K98Layer(this));
    }

    @Override
    public ResourceLocation getTextureLocation(K98Item instance) {
        return super.getTextureLocation(instance);
    }
}
