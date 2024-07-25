package net.mcreator.target.item;

import net.mcreator.target.init.TargetModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;

public class Drone extends Item {
    public Drone() {
        super(new Properties().rarity(Rarity.RARE));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        InteractionResultHolder<ItemStack> ar = super.use(world, player, hand);

        if (world instanceof ServerLevel level) {
            Entity entityToSpawn = TargetModEntities.DRONE.get().spawn(level, BlockPos.containing(player.getX() + 1.5 * player.getLookAngle().x, player.getY(), player.getZ() + 1.5 * player.getLookAngle().z), MobSpawnType.MOB_SUMMONED);
            if (entityToSpawn != null) {
                entityToSpawn.setYRot(player.getYRot());
                entityToSpawn.setYBodyRot(player.getYRot());
                entityToSpawn.setYHeadRot(player.getYRot());
                entityToSpawn.setDeltaMovement(0, 0, 0);
            }
        }

        if (!player.isCreative()) {
            player.getItemInHand(hand).shrink(1);
            player.swing(InteractionHand.MAIN_HAND, true);
        }

        return ar;
    }
}
