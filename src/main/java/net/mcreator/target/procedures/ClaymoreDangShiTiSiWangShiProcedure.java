package net.mcreator.target.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

public class ClaymoreDangShiTiSiWangShiProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		if (world instanceof Level _level && !_level.isClientSide())
			_level.explode(entity, x, y, z, 6.5f, Level.ExplosionInteraction.NONE);
		if (world instanceof Level _level && !_level.isClientSide())
			_level.explode(null, x, y, z, 6.5f, Level.ExplosionInteraction.NONE);
		{
			Entity _ent = entity;
			if (!_ent.level().isClientSide() && _ent.getServer() != null) {
				_ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4,
						_ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), "target:mediumexp");
			}
		}
		if (!entity.level().isClientSide())
			entity.discard();
	}
}
