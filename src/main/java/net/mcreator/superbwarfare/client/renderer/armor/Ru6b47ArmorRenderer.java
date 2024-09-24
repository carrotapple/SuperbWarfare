package net.mcreator.superbwarfare.client.renderer.armor;

import net.mcreator.superbwarfare.client.model.armor.Ru6b47Model;
import net.mcreator.superbwarfare.item.armor.Ru6b47;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class Ru6b47ArmorRenderer extends GeoArmorRenderer<Ru6b47> {
	public Ru6b47ArmorRenderer() {
		super(new Ru6b47Model());
		this.head = new GeoBone(null, "", false, (double) 0, false, false);
	}

	@Override
	public RenderType getRenderType(Ru6b47 animatable, ResourceLocation texture, MultiBufferSource bufferSource, float partialTick) {
		return RenderType.entityTranslucent(getTextureLocation(animatable));
	}
}
