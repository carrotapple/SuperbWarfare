package net.mcreator.superbwarfare.client.renderer.item;

import net.mcreator.superbwarfare.client.layer.TaserLayer;
import net.mcreator.superbwarfare.client.layer.TaserLayer2;
import net.mcreator.superbwarfare.item.gun.Taser;
import net.mcreator.superbwarfare.client.model.item.TaserItemModel;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class TaserItemRenderer extends GeoItemRenderer<Taser> {
    public TaserItemRenderer() {
        super(new TaserItemModel());
        this.addRenderLayer(new TaserLayer(this));
        this.addRenderLayer(new TaserLayer2(this));
    }

    @Override
    public ResourceLocation getTextureLocation(Taser instance) {
        return super.getTextureLocation(instance);
    }
}

