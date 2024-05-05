package net.mcreator.target.procedures;

import net.mcreator.target.init.TargetModItems;
import net.mcreator.target.network.TargetModVariables;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.items.ItemHandlerHelper;

public class Target1DangYouJiShiTiShiProcedure {
    public static void execute(double y, Entity entity, Entity sourceentity) {
        if (entity == null || sourceentity == null)
            return;
        if (sourceentity.isShiftKeyDown()) {
            if (!entity.level().isClientSide())
                entity.discard();
            if (sourceentity instanceof Player _player) {
                ItemStack _setstack = new ItemStack(TargetModItems.TARGETDEPLOYER.get());
                _setstack.setCount(1);
                ItemHandlerHelper.giveItemToPlayer(_player, _setstack);
            }
        } else {
            if (!(sourceentity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).zooming) {
                entity.lookAt(EntityAnchorArgument.Anchor.EYES, new Vec3((sourceentity.getX()), y, (sourceentity.getZ())));
                {
                    Entity _ent = entity;
                    _ent.setYRot(entity.getYRot());
                    _ent.setXRot(0);
                    _ent.setYBodyRot(_ent.getYRot());
                    _ent.setYHeadRot(_ent.getYRot());
                    _ent.yRotO = _ent.getYRot();
                    _ent.xRotO = _ent.getXRot();
                    if (_ent instanceof LivingEntity _entity) {
                        _entity.yBodyRotO = _entity.getYRot();
                        _entity.yHeadRotO = _entity.getYRot();
                    }
                }
                entity.getPersistentData().putDouble("targetdown", 0);
            }
        }
    }
}
