package com.atsuishio.superbwarfare.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FiringParameters extends Item {

    public FiringParameters() {
        super(new Properties().stacksTo(1));
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        ItemStack stack = pContext.getItemInHand();
        BlockPos pos = pContext.getClickedPos();
        pos = pos.relative(pContext.getClickedFace());
        Player player = pContext.getPlayer();
        if (player == null) return InteractionResult.PASS;

        if (player.isShiftKeyDown()) {
            stack.getOrCreateTag().putDouble("TargetX", pos.getX());
            stack.getOrCreateTag().putDouble("TargetY", pos.getY());
            stack.getOrCreateTag().putDouble("TargetZ", pos.getZ());
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.translatable("tips.superbwarfare.mortar.target_pos").withStyle(ChatFormatting.GRAY)
                .append(Component.literal("[" + pStack.getOrCreateTag().getInt("TargetX")
                        + "," + pStack.getOrCreateTag().getInt("TargetY")
                        + "," + pStack.getOrCreateTag().getInt("TargetZ") + "]")));
    }
}
