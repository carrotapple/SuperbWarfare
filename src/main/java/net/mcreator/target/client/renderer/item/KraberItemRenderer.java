package net.mcreator.target.client.renderer.item;

import net.mcreator.target.item.gun.Kraber;
import net.mcreator.target.client.layer.KraberLayer;
import net.mcreator.target.client.model.item.KraberItemModel;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class KraberItemRenderer extends GeoItemRenderer<Kraber> {
    public KraberItemRenderer() {
        super(new KraberItemModel());
        this.addRenderLayer(new KraberLayer(this));
    }

    @Override
    public ResourceLocation getTextureLocation(Kraber instance) {
        return super.getTextureLocation(instance);
    }
}
