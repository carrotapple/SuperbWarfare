package net.mcreator.target.client.renderer.item;

import net.mcreator.target.item.gun.Trachelium;
import net.mcreator.target.client.layer.TracheliumLayer;
import net.mcreator.target.client.model.item.TracheliumItemModel;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class TracheliumItemRenderer extends GeoItemRenderer<Trachelium> {
    public TracheliumItemRenderer() {
        super(new TracheliumItemModel());
        this.addRenderLayer(new TracheliumLayer(this));
    }

    @Override
    public ResourceLocation getTextureLocation(Trachelium instance) {
        return super.getTextureLocation(instance);
    }
}