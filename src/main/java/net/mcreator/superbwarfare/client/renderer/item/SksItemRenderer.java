package net.mcreator.superbwarfare.client.renderer.item;

import net.mcreator.superbwarfare.item.gun.SksItem;
import net.mcreator.superbwarfare.client.layer.SksLayer;
import net.mcreator.superbwarfare.client.model.item.SksItemModel;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class SksItemRenderer extends GeoItemRenderer<SksItem> {
    public SksItemRenderer() {
        super(new SksItemModel());
        this.addRenderLayer(new SksLayer(this));
    }

    @Override
    public ResourceLocation getTextureLocation(SksItem instance) {
        return super.getTextureLocation(instance);
    }
}
