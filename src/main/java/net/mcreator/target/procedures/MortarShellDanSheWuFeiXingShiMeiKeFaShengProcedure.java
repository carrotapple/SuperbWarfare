package net.mcreator.target.procedures;

import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

public class MortarShellDanSheWuFeiXingShiMeiKeFaShengProcedure {
	public static void execute(Entity immediatesourceentity) {
		if (immediatesourceentity == null)
			return;
		{
			Entity _ent = immediatesourceentity;
			if (!_ent.level().isClientSide() && _ent.getServer() != null) {
				_ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4,
						_ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), "particle minecraft:campfire_cosy_smoke ~ ~ ~ 0 0 0 0 2 force");
			}
		}
	}
}
