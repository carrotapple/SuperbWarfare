package net.mcreator.superbwarfare.client.renderer.item;

import net.mcreator.superbwarfare.item.gun.Trachelium;
import net.mcreator.superbwarfare.client.layer.TracheliumLayer;
import net.mcreator.superbwarfare.client.model.item.TracheliumItemModel;
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