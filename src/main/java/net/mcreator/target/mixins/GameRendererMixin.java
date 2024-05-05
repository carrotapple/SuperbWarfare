package net.mcreator.target.mixins;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    private boolean flag;

    @Inject(method = "renderItemInHand(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/Camera;F)V", at = @At("HEAD"))
    public void renderItemInHand(PoseStack p_109121_, Camera p_109122_, float p_109123_, CallbackInfo ci) {
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;
        if (player != null) {
            flag = minecraft.options.bobView().get();
            ItemStack heldItem = player.getMainHandItem();
            if (heldItem.is(ItemTags.create(new ResourceLocation("target", "gun"))) && heldItem.getOrCreateTag().getBoolean("aiming")) {
                minecraft.options.bobView().set(false);
            }
        }
    }

    @Inject(method = "renderItemInHand(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/Camera;F)V", at = @At("RETURN"))
    public void renderItemInHandEnd(PoseStack p_109121_, Camera p_109122_, float p_109123_, CallbackInfo ci) {
        Minecraft.getInstance().options.bobView().set(flag);
    }
}
