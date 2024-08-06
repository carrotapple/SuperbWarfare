package net.mcreator.superbwarfare.client.renderer.item;

import net.mcreator.superbwarfare.client.layer.Qbz95Layer;
import net.mcreator.superbwarfare.client.model.item.Qbz95ItemModel;
import net.mcreator.superbwarfare.item.gun.Qbz95Item;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class Qbz95ItemRenderer extends GeoItemRenderer<Qbz95Item> {
    public Qbz95ItemRenderer() {
        super(new Qbz95ItemModel());
        this.addRenderLayer(new Qbz95Layer(this));
    }

    @Override
    public ResourceLocation getTextureLocation(Qbz95Item instance) {
        return super.getTextureLocation(instance);
    }
}
