package net.mcreator.superbwarfare.client.model.armor;

import net.mcreator.superbwarfare.ModUtils;
import net.mcreator.superbwarfare.item.armor.UsChestIotv;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class UsChestIotvModel extends GeoModel<UsChestIotv> {
	@Override
	public ResourceLocation getAnimationResource(UsChestIotv object) {
		return null;
	}

	@Override
	public ResourceLocation getModelResource(UsChestIotv object) {
		return new ResourceLocation(ModUtils.MODID, "geo/us_chest_iotv.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(UsChestIotv object) {
		return new ResourceLocation(ModUtils.MODID, "textures/armor/us_chest_iotv.png");
	}
}
