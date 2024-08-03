package net.mcreator.superbwarfare.client.renderer.item;

import net.mcreator.superbwarfare.client.layer.Glock18Layer;
import net.mcreator.superbwarfare.client.model.item.Glock18ItemModel;
import net.mcreator.superbwarfare.item.gun.Glock18Item;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class Glock18ItemRenderer extends GeoItemRenderer<Glock18Item> {
    public Glock18ItemRenderer() {
        super(new Glock18ItemModel());
        this.addRenderLayer(new Glock18Layer(this));
    }

    @Override
    public ResourceLocation getTextureLocation(Glock18Item instance) {
        return super.getTextureLocation(instance);
    }
}
