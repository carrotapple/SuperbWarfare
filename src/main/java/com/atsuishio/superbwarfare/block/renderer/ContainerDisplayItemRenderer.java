package com.atsuishio.superbwarfare.block.renderer;

import com.atsuishio.superbwarfare.block.display.ContainerDisplayItem;
import com.atsuishio.superbwarfare.block.model.ContainerDisplayModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class ContainerDisplayItemRenderer extends GeoItemRenderer<ContainerDisplayItem> {
	public ContainerDisplayItemRenderer() {
		super(new ContainerDisplayModel());
	}

	@Override
	public RenderType getRenderType(ContainerDisplayItem animatable, ResourceLocation texture, MultiBufferSource bufferSource, float partialTick) {
		return RenderType.entityTranslucent(getTextureLocation(animatable));
	}
}
