package net.mcreator.superbwarfare.item;

import net.mcreator.superbwarfare.init.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;

public class TargetDeployer extends Item {
    public TargetDeployer() {
        super(new Item.Properties());
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        super.useOn(context);

        var clickedPos = context.getClickedPos();
        var player = context.getPlayer();
        if (player == null) return InteractionResult.FAIL;

        if (context.getLevel() instanceof ServerLevel level) {
            Entity entityToSpawn = ModEntities.TARGET_1.get().spawn(level, BlockPos.containing(clickedPos.getX() + 0.5, clickedPos.getY() + 1, clickedPos.getZ() + 0.5), MobSpawnType.MOB_SUMMONED);
            if (entityToSpawn != null) {
                entityToSpawn.setDeltaMovement(0, 0, 0);
            }
        }

        context.getItemInHand().shrink(1);
        player.swing(InteractionHand.MAIN_HAND, true);

        return InteractionResult.SUCCESS;
    }
}
