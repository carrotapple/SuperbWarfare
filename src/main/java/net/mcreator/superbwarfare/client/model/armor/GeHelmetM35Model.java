package net.mcreator.superbwarfare.client.model.armor;

import net.mcreator.superbwarfare.ModUtils;
import net.mcreator.superbwarfare.item.armor.GeHelmetM35;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class GeHelmetM35Model extends GeoModel<GeHelmetM35> {
	@Override
	public ResourceLocation getAnimationResource(GeHelmetM35 object) {
		return null;
	}

	@Override
	public ResourceLocation getModelResource(GeHelmetM35 object) {
		return new ResourceLocation(ModUtils.MODID, "geo/ge_helmet_m_35.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(GeHelmetM35 object) {
		return new ResourceLocation(ModUtils.MODID, "textures/armor/ge_helmet_m_35.png");
	}
}
