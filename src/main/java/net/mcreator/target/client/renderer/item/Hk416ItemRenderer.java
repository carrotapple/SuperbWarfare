package net.mcreator.target.client.renderer.item;

import net.mcreator.target.item.gun.Hk416Item;
import net.mcreator.target.client.layer.Hk416Layer;
import net.mcreator.target.client.model.item.Hk416ItemModel;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class Hk416ItemRenderer extends GeoItemRenderer<Hk416Item> {
    public Hk416ItemRenderer() {
        super(new Hk416ItemModel());
        this.addRenderLayer(new Hk416Layer(this));
    }

    @Override
    public ResourceLocation getTextureLocation(Hk416Item instance) {
        return super.getTextureLocation(instance);
    }
}
