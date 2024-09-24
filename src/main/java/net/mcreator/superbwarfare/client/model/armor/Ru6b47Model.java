package net.mcreator.superbwarfare.client.model.armor;

import net.mcreator.superbwarfare.ModUtils;
import net.mcreator.superbwarfare.item.armor.Ru6b47;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class Ru6b47Model extends GeoModel<Ru6b47> {
	@Override
	public ResourceLocation getAnimationResource(Ru6b47 object) {
		return null;
	}

	@Override
	public ResourceLocation getModelResource(Ru6b47 object) {
		return new ResourceLocation(ModUtils.MODID, "geo/ru_6b47.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(Ru6b47 object) {
		return new ResourceLocation(ModUtils.MODID, "textures/armor/ru_6b47.png");
	}
}
