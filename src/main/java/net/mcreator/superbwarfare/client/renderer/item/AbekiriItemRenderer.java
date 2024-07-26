package net.mcreator.superbwarfare.client.renderer.item;

import net.mcreator.superbwarfare.item.gun.Abekiri;
import net.mcreator.superbwarfare.client.layer.AbekiriLayer;
import net.mcreator.superbwarfare.client.model.item.AbekiriItemModel;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class AbekiriItemRenderer extends GeoItemRenderer<Abekiri> {
    public AbekiriItemRenderer() {
        super(new AbekiriItemModel());
        this.addRenderLayer(new AbekiriLayer(this));
    }

    @Override
    public ResourceLocation getTextureLocation(Abekiri instance) {
        return super.getTextureLocation(instance);
    }
}

