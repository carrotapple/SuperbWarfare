package net.mcreator.target.client.renderer.item;

import net.mcreator.target.item.gun.M60Item;
import net.mcreator.target.client.layer.M60Layer;
import net.mcreator.target.client.model.item.M60ItemModel;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class M60ItemRenderer extends GeoItemRenderer<M60Item> {
    public M60ItemRenderer() {
        super(new M60ItemModel());
        this.addRenderLayer(new M60Layer(this));
    }

    @Override
    public ResourceLocation getTextureLocation(M60Item instance) {
        return super.getTextureLocation(instance);
    }
}
