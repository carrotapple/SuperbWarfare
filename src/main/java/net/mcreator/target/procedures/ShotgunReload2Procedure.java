package net.mcreator.target.procedures;

import net.mcreator.target.network.TargetModVariables;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class ShotgunReload2Procedure {
    public static void execute(LivingEntity entity) {
        ItemStack stack = entity.getMainHandItem();
        double mag = stack.getOrCreateTag().getDouble("mag");
        double ammo = stack.getOrCreateTag().getDouble("ammo");
        double ammo2 = mag + 1 - ammo;
        double shotgunAmmo = (entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).shotgunammo;

        stack.getOrCreateTag().putDouble("ammo", ammo + Math.min(ammo2, shotgunAmmo));
        entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
            capability.shotgunammo = shotgunAmmo >= ammo2 ? shotgunAmmo - ammo2 : 0;
            capability.syncPlayerVariables(entity);
        });
        stack.getOrCreateTag().putDouble("reloading", 0);
        stack.getOrCreateTag().putDouble("emptyreload", 0);
    }
}
