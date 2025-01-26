package com.atsuishio.superbwarfare.item;

import com.atsuishio.superbwarfare.entity.C4Entity;
import com.atsuishio.superbwarfare.tools.EntityFindUtil;
import com.atsuishio.superbwarfare.tools.TooltipTool;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class C4Bomb extends Item {

    public C4Bomb() {
        super(new Item.Properties());
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (player.serializeNBT().contains("C4UUID") && player.serializeNBT().hasUUID("C4UUID")) {
            if (EntityFindUtil.findEntity(player.level(), player.serializeNBT().getUUID("C4UUID").toString()) != null) {
                return InteractionResultHolder.pass(stack);
            }
        }
        if (!level.isClientSide) {
            C4Entity entity = new C4Entity(player, level);
            entity.moveTo(player.getX(), player.getY() + 1.1, player.getZ(), player.getYRot(), 0);
            entity.setYBodyRot(player.getYRot());
            entity.setYHeadRot(player.getYRot());
            entity.setDeltaMovement(0.5 * player.getLookAngle().x, 0.5 * player.getLookAngle().y, 0.5 * player.getLookAngle().z);

            level.addFreshEntity(entity);
        }

        player.getCooldowns().addCooldown(this, 20);

        if (!player.getAbilities().instabuild) {
            stack.shrink(1);
        }

        return InteractionResultHolder.consume(stack);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        TooltipTool.addDevelopingText(pTooltipComponents);
    }
}
