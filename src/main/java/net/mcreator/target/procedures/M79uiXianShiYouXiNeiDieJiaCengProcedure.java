package net.mcreator.target.procedures;

import net.mcreator.target.init.TargetModItems;
import net.mcreator.target.network.TargetModVariables;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class M79uiXianShiYouXiNeiDieJiaCengProcedure {
    public static boolean execute(Entity entity) {
        if (entity == null) return false;
        if (entity instanceof Player player) {
            return !player.isSpectator()
                    && player.getMainHandItem().getItem() == TargetModItems.M79.get()
                    && Minecraft.getInstance().options.getCameraType() == CameraType.FIRST_PERSON
                    && !entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables()).zooming;
        }
        return false;
    }
}
