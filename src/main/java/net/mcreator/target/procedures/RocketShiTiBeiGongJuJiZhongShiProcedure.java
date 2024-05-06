package net.mcreator.target.procedures;

import net.mcreator.target.init.TargetModItems;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;

public class RocketShiTiBeiGongJuJiZhongShiProcedure {
    public static void execute(LevelAccessor world, Entity sourceentity) {
        if (sourceentity == null)
            return;
        if (Math.random() < 0.25) {
            if (world instanceof Level _level && !_level.isClientSide())
                _level.explode(sourceentity, (sourceentity.getX()), (sourceentity.getY() + 1), (sourceentity.getZ()), 6, Level.ExplosionInteraction.NONE);
            if (world instanceof Level _level && !_level.isClientSide())
                _level.explode(null, (sourceentity.getX()), (sourceentity.getY() + 1), (sourceentity.getZ()), 6, Level.ExplosionInteraction.NONE);
            if (sourceentity instanceof Player _player) {
                ItemStack _stktoremove = new ItemStack(TargetModItems.ROCKET.get());
                _player.getInventory().clearOrCountMatchingItems(p -> _stktoremove.getItem() == p.getItem(), 1, _player.inventoryMenu.getCraftSlots());
            }
            {
                Entity _ent = sourceentity;
                if (!_ent.level().isClientSide() && _ent.getServer() != null) {
                    _ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4,
                            _ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), "target:mediumexp");
                }
            }
        }
    }
}
