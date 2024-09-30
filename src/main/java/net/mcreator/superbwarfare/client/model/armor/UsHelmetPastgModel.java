package net.mcreator.superbwarfare.client.model.armor;

import net.mcreator.superbwarfare.ModUtils;
import net.mcreator.superbwarfare.item.armor.UsHelmetPastg;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class UsHelmetPastgModel extends GeoModel<UsHelmetPastg> {
	@Override
	public ResourceLocation getAnimationResource(UsHelmetPastg object) {
		return null;
	}

	@Override
	public ResourceLocation getModelResource(UsHelmetPastg object) {
		return new ResourceLocation(ModUtils.MODID, "geo/us_helmet_pastg.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(UsHelmetPastg object) {
		return new ResourceLocation(ModUtils.MODID, "textures/armor/us_helmet_pastg.png");
	}
}
