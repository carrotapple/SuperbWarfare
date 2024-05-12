package net.mcreator.target.client.renderer.item;

import net.mcreator.target.item.gun.M870Item;
import net.mcreator.target.client.layer.M870Layer;
import net.mcreator.target.client.model.item.M870ItemModel;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class M870ItemRenderer extends GeoItemRenderer<M870Item> {
    public M870ItemRenderer() {
        super(new M870ItemModel());
        this.addRenderLayer(new M870Layer(this));
    }

    @Override
    public ResourceLocation getTextureLocation(M870Item instance) {
        return super.getTextureLocation(instance);
    }
}
