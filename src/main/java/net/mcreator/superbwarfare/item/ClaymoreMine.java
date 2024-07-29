package net.mcreator.superbwarfare.item;

import net.mcreator.superbwarfare.entity.ClaymoreEntity;
import net.mcreator.superbwarfare.init.ModEntities;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ClaymoreMine extends Item {
    public ClaymoreMine() {
        super(new Item.Properties());
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (world instanceof ServerLevel serverLevel) {
            TamableAnimal entityToSpawn = new ClaymoreEntity(ModEntities.CLAYMORE.get(), serverLevel);
            entityToSpawn.moveTo(player.getX(), player.getY() + 1.1, player.getZ(), player.getYRot(), player.getXRot());
            entityToSpawn.setYBodyRot(player.getYRot());
            entityToSpawn.setYHeadRot(player.getYRot());
            entityToSpawn.setDeltaMovement((0.5 * player.getLookAngle().x), (0.5 * player.getLookAngle().y), (0.5 * player.getLookAngle().z));
            entityToSpawn.finalizeSpawn(serverLevel, serverLevel.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
            entityToSpawn.tame(player);
            serverLevel.addFreshEntity(entityToSpawn);
        }

        player.getCooldowns().addCooldown(this, 20);
        stack.shrink(1);

        return InteractionResultHolder.consume(stack);
    }
}
