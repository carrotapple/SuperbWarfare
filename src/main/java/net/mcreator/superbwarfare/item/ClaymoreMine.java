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
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        ClaymoreEntity claymore = new ClaymoreEntity(player, world);
        claymore.setPos(player.getX(), player.getEyeY() - 0.3, player.getZ());
        claymore.shoot(player.getLookAngle().x, player.getLookAngle().y, player.getLookAngle().z, 0.2f,0);
        claymore.setRotY(player.getYRot());
        world.addFreshEntity(claymore);

        player.getCooldowns().addCooldown(this, 20);
        stack.shrink(1);

        return InteractionResultHolder.consume(stack);
    }
}
