package net.mcreator.target.procedures;

import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;

public class ClaymoreDangShiTiSiWangShiProcedure {
    public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
        if (entity == null)
            return;
        if (world instanceof Level _level && !_level.isClientSide())
            _level.explode(entity, x, y, z, 6.5f, Level.ExplosionInteraction.NONE);
        if (world instanceof Level _level && !_level.isClientSide())
            _level.explode(null, x, y, z, 6.5f, Level.ExplosionInteraction.NONE);
        {
            if (!entity.level().isClientSide() && entity.getServer() != null) {
                entity.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, entity.position(), entity.getRotationVector(), entity.level() instanceof ServerLevel ? (ServerLevel) entity.level() : null, 4,
                        entity.getName().getString(), entity.getDisplayName(), entity.level().getServer(), entity), "target:mediumexp");
            }
        }
        if (!entity.level().isClientSide())
            entity.discard();
    }
}
