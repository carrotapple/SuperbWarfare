package net.mcreator.target.client.renderer.item;

import net.mcreator.target.item.gun.AK47Item;
import net.mcreator.target.client.layer.AK47Layer;
import net.mcreator.target.client.model.item.AK47ItemModel;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class AK47ItemRenderer extends GeoItemRenderer<AK47Item> {
    public AK47ItemRenderer() {
        super(new AK47ItemModel());
        this.addRenderLayer(new AK47Layer(this));
    }

    @Override
    public ResourceLocation getTextureLocation(AK47Item instance) {
        return super.getTextureLocation(instance);
    }
}
