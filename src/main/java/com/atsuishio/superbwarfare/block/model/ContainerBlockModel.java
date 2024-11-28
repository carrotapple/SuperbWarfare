package com.atsuishio.superbwarfare.block.model;

import com.atsuishio.superbwarfare.ModUtils;
import com.atsuishio.superbwarfare.block.entity.ContainerTileEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class ContainerBlockModel extends GeoModel<ContainerTileEntity> {
	@Override
	public ResourceLocation getAnimationResource(ContainerTileEntity animatable) {
		return ModUtils.loc("animations/container.animation.json");
	}

	@Override
	public ResourceLocation getModelResource(ContainerTileEntity animatable) {
		return ModUtils.loc( "geo/container.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(ContainerTileEntity animatable) {
		return ModUtils.loc( "textures/block/container.png");
	}
}
