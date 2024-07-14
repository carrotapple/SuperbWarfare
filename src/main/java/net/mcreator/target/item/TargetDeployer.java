package net.mcreator.target.item;

import net.mcreator.target.init.TargetModEntities;
import net.mcreator.target.init.TargetModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

import java.util.List;

public class TargetDeployer extends Item {
    public TargetDeployer() {
        super(new Item.Properties().stacksTo(1).rarity(Rarity.COMMON));
    }

    @Override
    public void appendHoverText(ItemStack itemstack, Level world, List<Component> list, TooltipFlag flag) {
        super.appendHoverText(itemstack, world, list, flag);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        super.useOn(context);

        var clickedPos = context.getClickedPos();
        var player = context.getPlayer();
        if (player == null) return InteractionResult.FAIL;

        if (context.getLevel() instanceof ServerLevel level) {
            Entity entityToSpawn = TargetModEntities.TARGET_1.get().spawn(level, BlockPos.containing(clickedPos.getX() + 0.5, clickedPos.getY() + 1, clickedPos.getZ() + 0.5), MobSpawnType.MOB_SUMMONED);
            if (entityToSpawn != null) {
                entityToSpawn.setDeltaMovement(0, 0, 0);
            }
        }

        context.getItemInHand().shrink(1);
        player.swing(InteractionHand.MAIN_HAND, true);

        return InteractionResult.SUCCESS;
    }
}
