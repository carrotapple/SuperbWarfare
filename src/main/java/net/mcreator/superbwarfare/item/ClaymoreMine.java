package net.mcreator.superbwarfare.item;

import net.mcreator.superbwarfare.entity.ClaymoreEntity;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ClaymoreMine extends Item {
    public ClaymoreMine() {
        super(new Item.Properties());
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide) {
            ClaymoreEntity entity = new ClaymoreEntity(player, level);
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
}
