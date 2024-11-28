package com.atsuishio.superbwarfare.block.model;

import com.atsuishio.superbwarfare.ModUtils;
import com.atsuishio.superbwarfare.block.display.ContainerDisplayItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class ContainerDisplayModel extends GeoModel<ContainerDisplayItem> {
	@Override
	public ResourceLocation getAnimationResource(ContainerDisplayItem animatable) {
		return ModUtils.loc( "animations/container.animation.json");
	}

	@Override
	public ResourceLocation getModelResource(ContainerDisplayItem animatable) {
		return ModUtils.loc("geo/container.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(ContainerDisplayItem entity) {
		return ModUtils.loc("textures/block/container.png");
	}
}
