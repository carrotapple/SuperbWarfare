package net.mcreator.superbwarfare.client.renderer.item;

import net.mcreator.superbwarfare.client.layer.M1911Layer;
import net.mcreator.superbwarfare.client.model.item.M1911ItemModel;
import net.mcreator.superbwarfare.item.gun.M1911Item;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class M1911ItemRenderer extends GeoItemRenderer<M1911Item> {
    public M1911ItemRenderer() {
        super(new M1911ItemModel());
        this.addRenderLayer(new M1911Layer(this));
    }

    @Override
    public ResourceLocation getTextureLocation(M1911Item instance) {
        return super.getTextureLocation(instance);
    }
}
