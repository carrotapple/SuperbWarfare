package net.mcreator.target.procedures;

import net.mcreator.target.network.TargetModVariables;
import net.minecraft.world.entity.player.Player;

public class ReleaseFireProcedure {
    public static void execute(Player player) {
        player.getPersistentData().putDouble("firing", 0);
        player.getPersistentData().putDouble("minifiring", 0);
        player.getPersistentData().putDouble("minigunfiring", 0);
        player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
            capability.bowpullhold = false;
            capability.syncPlayerVariables(player);
        });
        BowlooseProcedure.execute(player);
    }
}
