package net.mcreator.superbwarfare.client.renderer.item;

import net.mcreator.superbwarfare.item.gun.VectorItem;
import net.mcreator.superbwarfare.client.layer.VectorLayer;
import net.mcreator.superbwarfare.client.model.item.VectorItemModel;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class VectorItemRenderer extends GeoItemRenderer<VectorItem> {
    public VectorItemRenderer() {
        super(new VectorItemModel());
        this.addRenderLayer(new VectorLayer(this));
    }

    @Override
    public ResourceLocation getTextureLocation(VectorItem instance) {
        return super.getTextureLocation(instance);
    }
}
