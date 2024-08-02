package net.mcreator.superbwarfare.client.renderer.item;

import net.mcreator.superbwarfare.client.layer.Glock17Layer;
import net.mcreator.superbwarfare.client.model.item.Glock17ItemModel;
import net.mcreator.superbwarfare.item.gun.Glock17Item;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class Glock17ItemRenderer extends GeoItemRenderer<Glock17Item> {
    public Glock17ItemRenderer() {
        super(new Glock17ItemModel());
        this.addRenderLayer(new Glock17Layer(this));
    }

    @Override
    public ResourceLocation getTextureLocation(Glock17Item instance) {
        return super.getTextureLocation(instance);
    }
}
