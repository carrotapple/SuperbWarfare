package net.mcreator.superbwarfare.client.renderer.item;

import net.mcreator.superbwarfare.item.gun.Minigun;
import net.mcreator.superbwarfare.client.layer.MinigunLayer;
import net.mcreator.superbwarfare.client.model.item.MinigunItemModel;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class MinigunItemRenderer extends GeoItemRenderer<Minigun> {
    public MinigunItemRenderer() {
        super(new MinigunItemModel());
        this.addRenderLayer(new MinigunLayer(this));
    }

    @Override
    public ResourceLocation getTextureLocation(Minigun instance) {
        return super.getTextureLocation(instance);
    }
}
