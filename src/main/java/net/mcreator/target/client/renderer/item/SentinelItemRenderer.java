package net.mcreator.target.client.renderer.item;

import net.mcreator.target.item.gun.SentinelItem;
import net.mcreator.target.client.layer.SentinelLayer;
import net.mcreator.target.client.model.item.SentinelItemModel;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class SentinelItemRenderer extends GeoItemRenderer<SentinelItem> {
    public SentinelItemRenderer() {
        super(new SentinelItemModel());
        this.addRenderLayer(new SentinelLayer(this));
    }

    @Override
    public ResourceLocation getTextureLocation(SentinelItem instance) {
        return super.getTextureLocation(instance);
    }
}
