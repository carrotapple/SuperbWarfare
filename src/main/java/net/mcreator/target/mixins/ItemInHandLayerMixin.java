package net.mcreator.target.mixins;

import com.mojang.blaze3d.vertex.PoseStack;
import net.mcreator.target.item.gun.GunItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.entity.layers.PlayerItemInHandLayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Author: MrCrayfish
 */
@Mixin(ItemInHandLayer.class)
public class ItemInHandLayerMixin
{
    @SuppressWarnings({"ConstantConditions"})
    @Inject(method = "renderArmWithItem", at = @At(value = "HEAD"), cancellable = true)
    private void renderArmWithItemHead(LivingEntity entity, ItemStack stack, ItemDisplayContext display, HumanoidArm arm, PoseStack poseStack, MultiBufferSource source, int light, CallbackInfo ci)
    {
        if(entity.getType() == EntityType.PLAYER)
        {
            InteractionHand hand = Minecraft.getInstance().options.mainHand().get() == arm ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
            if(hand == InteractionHand.OFF_HAND)
            {
                if(stack.getItem() instanceof GunItem)
                {
                    ci.cancel();
                    return;
                }

                if(entity.getMainHandItem().getItem() instanceof GunItem) {
                    ci.cancel();
                    return;
                }
            }
            if(stack.getItem() instanceof GunItem)
            {
                ci.cancel();
            }
        }
    }

}
