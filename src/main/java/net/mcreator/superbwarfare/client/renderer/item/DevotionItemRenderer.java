package net.mcreator.superbwarfare.client.renderer.item;

import net.mcreator.superbwarfare.item.gun.Devotion;
import net.mcreator.superbwarfare.client.layer.DevotionLayer;
import net.mcreator.superbwarfare.client.model.item.DevotionItemModel;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class DevotionItemRenderer extends GeoItemRenderer<Devotion> {
    public DevotionItemRenderer() {
        super(new DevotionItemModel());
        this.addRenderLayer(new DevotionLayer(this));
    }

    @Override
    public ResourceLocation getTextureLocation(Devotion instance) {
        return super.getTextureLocation(instance);
    }
}

