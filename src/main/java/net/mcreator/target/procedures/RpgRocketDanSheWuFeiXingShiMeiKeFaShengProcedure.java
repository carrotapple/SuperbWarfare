package net.mcreator.target.procedures;

import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class RpgRocketDanSheWuFeiXingShiMeiKeFaShengProcedure {
    public static void execute(Entity immediatesourceentity) {
        if (immediatesourceentity == null)
            return;
        double life = 0;
        immediatesourceentity.getPersistentData().putDouble("time", (1 + immediatesourceentity.getPersistentData().getDouble("time")));
        life = immediatesourceentity.getPersistentData().getDouble("time");
        if (life == 4) {
            {
                Entity _ent = immediatesourceentity;
                if (!_ent.level().isClientSide() && _ent.getServer() != null) {
                    _ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4,
                            _ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), "particle minecraft:campfire_cosy_smoke ~ ~ ~ 0.8 0.8 0.8 0.01 50 force");
                }
            }
        }
        if (life >= 4) {
            immediatesourceentity.setDeltaMovement(new Vec3((1.04 * immediatesourceentity.getDeltaMovement().x()), (1.04 * immediatesourceentity.getDeltaMovement().y() - 0.02), (1.04 * immediatesourceentity.getDeltaMovement().z())));
            {
                Entity _ent = immediatesourceentity;
                if (!_ent.level().isClientSide() && _ent.getServer() != null) {
                    _ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4,
                            _ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), "particle minecraft:smoke ~ ~ ~ 0 0 0 0 2 force");
                }
            }
            {
                Entity _ent = immediatesourceentity;
                if (!_ent.level().isClientSide() && _ent.getServer() != null) {
                    _ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4,
                            _ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), "particle minecraft:campfire_cosy_smoke ~ ~ ~ 0 0 0 0 2 force");
                }
            }
        }
        if (life >= 90) {
            {
                Entity _ent = immediatesourceentity;
                if (!_ent.level().isClientSide() && _ent.getServer() != null) {
                    _ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4,
                            _ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), "target:mediumexp");
                }
            }
            if (!immediatesourceentity.level().isClientSide())
                immediatesourceentity.discard();
        }
    }
}
