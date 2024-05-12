package net.mcreator.target.client.renderer.item;

import net.mcreator.target.item.gun.Taser;
import net.mcreator.target.client.model.item.TaserItemModel;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class TaserItemRenderer extends GeoItemRenderer<Taser> {
    public TaserItemRenderer() {
        super(new TaserItemModel());
    }

    @Override
    public ResourceLocation getTextureLocation(Taser instance) {
        return super.getTextureLocation(instance);
    }
}

