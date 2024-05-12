package net.mcreator.target.client.renderer.item;

import net.mcreator.target.item.LightSaberItem;
import net.mcreator.target.client.layer.LightSaberLayer;
import net.mcreator.target.client.model.item.LightSaberItemModel;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class LightSaberItemRenderer extends GeoItemRenderer<LightSaberItem> {
    public LightSaberItemRenderer() {
        super(new LightSaberItemModel());
        this.addRenderLayer(new LightSaberLayer(this));
    }

    @Override
    public ResourceLocation getTextureLocation(LightSaberItem instance) {
        return super.getTextureLocation(instance);
    }
}
