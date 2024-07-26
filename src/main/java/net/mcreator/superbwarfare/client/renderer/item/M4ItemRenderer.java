package net.mcreator.superbwarfare.client.renderer.item;

import software.bernie.geckolib.renderer.GeoItemRenderer;

import net.minecraft.resources.ResourceLocation;

import net.mcreator.superbwarfare.item.gun.M4Item;
import net.mcreator.superbwarfare.client.layer.M4Layer;
import net.mcreator.superbwarfare.client.model.item.M4ItemModel;

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
