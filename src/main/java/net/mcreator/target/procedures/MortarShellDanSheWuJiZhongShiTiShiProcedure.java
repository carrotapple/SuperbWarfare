package net.mcreator.target.procedures;

import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;

public class MortarShellDanSheWuJiZhongShiTiShiProcedure {
    public static void execute(LevelAccessor world, Entity immediatesourceentity) {
        if (immediatesourceentity == null)
            return;
        {
            Entity _ent = immediatesourceentity;
            if (!_ent.level().isClientSide() && _ent.getServer() != null) {
                _ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4,
                        _ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), "target:mediumexp");
            }
        }
        if (world instanceof Level _level && !_level.isClientSide())
            _level.explode(immediatesourceentity, (immediatesourceentity.getX()), (immediatesourceentity.getY()), (immediatesourceentity.getZ()), 7.5f, Level.ExplosionInteraction.NONE);
    }
}
