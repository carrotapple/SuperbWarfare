package net.mcreator.superbwarfare.item;

import net.mcreator.superbwarfare.client.gui.RangeHelper;
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

public class TestItem extends Item {

    public TestItem() {
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
            stack.getOrCreateTag().putInt("StartX", pos.getX());
            stack.getOrCreateTag().putInt("StartY", pos.getY());
            stack.getOrCreateTag().putInt("StartZ", pos.getZ());
            return InteractionResult.SUCCESS;
        } else {
            stack.getOrCreateTag().putInt("TargetX", pos.getX());
            stack.getOrCreateTag().putInt("TargetY", pos.getY());
            stack.getOrCreateTag().putInt("TargetZ", pos.getZ());
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.literal("Start Pos: [" + pStack.getOrCreateTag().getInt("StartX") + ","
                + pStack.getOrCreateTag().getInt("StartY") + "," + pStack.getOrCreateTag().getInt("StartZ") + "]"));
        pTooltipComponents.add(Component.literal("Target Pos: [" + pStack.getOrCreateTag().getInt("TargetX") + ","
                + pStack.getOrCreateTag().getInt("TargetY") + "," + pStack.getOrCreateTag().getInt("TargetZ") + "]"));

        double[] angles = new double[2];
        boolean flag = RangeHelper.canReachTarget(8.0, 0.05, 0.99,
                new BlockPos(pStack.getOrCreateTag().getInt("StartX"), pStack.getOrCreateTag().getInt("StartY"), pStack.getOrCreateTag().getInt("StartZ")),
                new BlockPos(pStack.getOrCreateTag().getInt("TargetX"), pStack.getOrCreateTag().getInt("TargetY"), pStack.getOrCreateTag().getInt("TargetZ")),
                angles);

        pTooltipComponents.add(Component.literal(flag + " Angles: " + angles[0] + "," + angles[1]));
    }
}
