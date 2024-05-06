package net.mcreator.target.procedures;

import net.mcreator.target.init.TargetModItems;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemHandlerHelper;

public class ClaymoreYouJiShiTiShiProcedure {
    public static void execute(Entity entity, Entity sourceentity) {
        if (entity == null || sourceentity == null)
            return;
        if ((entity instanceof TamableAnimal _tamIsTamedBy && sourceentity instanceof LivingEntity _livEnt && _tamIsTamedBy.isOwnedBy(_livEnt)) && sourceentity.isShiftKeyDown()) {
            if (!entity.level().isClientSide())
                entity.discard();
            if (sourceentity instanceof Player _player) {
                ItemStack _setstack = new ItemStack(TargetModItems.CLAYMORE_MINE.get());
                _setstack.setCount(1);
                ItemHandlerHelper.giveItemToPlayer(_player, _setstack);
            }
        }
    }
}
