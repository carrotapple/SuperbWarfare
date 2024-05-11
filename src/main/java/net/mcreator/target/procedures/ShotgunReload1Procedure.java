package net.mcreator.target.procedures;

import net.mcreator.target.network.TargetModVariables;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class ShotgunReload1Procedure {
    public static void execute(LivingEntity entity) {
        ItemStack stack = entity.getMainHandItem();
        double mag = stack.getOrCreateTag().getDouble("mag");
        double ammo = stack.getOrCreateTag().getDouble("ammo");

        double empty = mag - ammo;
        double shotgunAmmo = entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables()).shotgunammo;

        stack.getOrCreateTag().putDouble("ammo", ammo + Math.min(empty, shotgunAmmo));

        entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
            capability.shotgunammo = shotgunAmmo >= empty ? shotgunAmmo - empty : 0;
            capability.syncPlayerVariables(entity);
        });

        stack.getOrCreateTag().putDouble("reloading", 0);
        stack.getOrCreateTag().putDouble("emptyreload", 0);
    }
}
