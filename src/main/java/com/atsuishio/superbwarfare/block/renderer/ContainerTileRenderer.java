package com.atsuishio.superbwarfare.block.renderer;

import com.atsuishio.superbwarfare.block.entity.ContainerTileEntity;
import com.atsuishio.superbwarfare.block.model.ContainerBlockModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class ContainerTileRenderer extends GeoBlockRenderer<ContainerTileEntity> {
	public ContainerTileRenderer() {
		super(new ContainerBlockModel());
	}

	@Override
	public RenderType getRenderType(ContainerTileEntity animatable, ResourceLocation texture, MultiBufferSource bufferSource, float partialTick) {
		return RenderType.entityTranslucent(getTextureLocation(animatable));
	}
}
