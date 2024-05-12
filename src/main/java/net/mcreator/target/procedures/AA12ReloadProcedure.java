package net.mcreator.target.procedures;

import net.mcreator.target.network.TargetModVariables;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;

public class AA12ReloadProcedure {
    public static void execute(LivingEntity entity, boolean plusOne) {
        CompoundTag tag = entity.getMainHandItem().getOrCreateTag();
        double mag = tag.getDouble("mag");
        double ammo = tag.getDouble("ammo");

        double empty = mag - ammo + (plusOne ? 1 : 0);
        double shotgunAmmo = entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).map(c -> c.shotgunAmmo).orElse(0d);

        tag.putDouble("ammo", ammo + Math.min(empty, shotgunAmmo));

        entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
            capability.shotgunAmmo = Math.max(0, shotgunAmmo - empty);
            capability.syncPlayerVariables(entity);
        });

        tag.putDouble("reloading", 0);
        tag.putDouble("emptyreload", 0);
    }
}
