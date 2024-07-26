package net.mcreator.superbwarfare.client.renderer.item;

import net.mcreator.superbwarfare.item.LightSaber;
import net.mcreator.superbwarfare.client.layer.LightSaberLayer;
import net.mcreator.superbwarfare.client.model.item.LightSaberItemModel;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class LightSaberItemRenderer extends GeoItemRenderer<LightSaber> {
    public LightSaberItemRenderer() {
        super(new LightSaberItemModel());
        this.addRenderLayer(new LightSaberLayer(this));
    }

    @Override
    public ResourceLocation getTextureLocation(LightSaber instance) {
        return super.getTextureLocation(instance);
    }
}
