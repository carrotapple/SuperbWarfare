package net.mcreator.superbwarfare.client.model.armor;

import net.mcreator.superbwarfare.ModUtils;
import net.mcreator.superbwarfare.item.armor.RuChest6b43;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class RuChest6b43Model extends GeoModel<RuChest6b43> {
	@Override
	public ResourceLocation getAnimationResource(RuChest6b43 object) {
		return null;
	}

	@Override
	public ResourceLocation getModelResource(RuChest6b43 object) {
		return new ResourceLocation(ModUtils.MODID, "geo/ru_chest_6b43.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(RuChest6b43 object) {
		return new ResourceLocation(ModUtils.MODID, "textures/armor/ru_chest_6b43.png");
	}
}
