package net.mcreator.superbwarfare.client.renderer.item;

import net.mcreator.superbwarfare.item.gun.SentinelItem;
import net.mcreator.superbwarfare.client.layer.SentinelLayer;
import net.mcreator.superbwarfare.client.model.item.SentinelItemModel;
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
