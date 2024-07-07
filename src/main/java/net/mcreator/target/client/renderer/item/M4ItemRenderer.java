package net.mcreator.target.client.renderer.item;

import software.bernie.geckolib.util.RenderUtils;
import software.bernie.geckolib.renderer.GeoItemRenderer;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.cache.object.BakedGeoModel;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.Minecraft;

import net.mcreator.target.tools.AnimUtils;
import net.mcreator.target.item.gun.M4Item;
import net.mcreator.target.client.layer.M4Layer;
import net.mcreator.target.client.model.item.M4ItemModel;

import java.util.Set;
import java.util.HashSet;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack;

public class M4ItemRenderer extends GeoItemRenderer<M4Item> {
    public M4ItemRenderer() {
        super(new M4ItemModel());
        this.addRenderLayer(new M4Layer(this));
    }

    @Override
    public ResourceLocation getTextureLocation(M4Item instance) {
        return super.getTextureLocation(instance);
    }
}
