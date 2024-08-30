package net.mcreator.superbwarfare.client.renderer.item;

import net.mcreator.superbwarfare.client.layer.JavelinLayer;
import net.mcreator.superbwarfare.client.model.item.JavelinItemModel;
import net.mcreator.superbwarfare.item.gun.JavelinItem;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class JavelinItemRenderer extends GeoItemRenderer<JavelinItem> {
    public JavelinItemRenderer() {
        super(new JavelinItemModel());
        this.addRenderLayer(new JavelinLayer(this));
    }

    @Override
    public RenderType getRenderType(JavelinItem animatable, ResourceLocation texture, MultiBufferSource bufferSource, float partialTick) {
        return RenderType.entityTranslucent(getTextureLocation(animatable));
    }

    @Override
    public ResourceLocation getTextureLocation(JavelinItem instance) {
        return super.getTextureLocation(instance);
    }
}
