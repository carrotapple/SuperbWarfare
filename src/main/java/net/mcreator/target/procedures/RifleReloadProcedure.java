package net.mcreator.target.procedures;

import net.mcreator.target.network.TargetModVariables;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class RifleReloadProcedure {
    public static void execute(Entity entity) {
        execute(entity, false);
    }

    public static void execute(Entity entity, boolean extraOne) {
        if (entity == null) return;

        ItemStack stack = (entity instanceof LivingEntity living ? living.getMainHandItem() : ItemStack.EMPTY);

        CompoundTag tag = stack.getOrCreateTag();

        double mag = tag.getDouble("mag");
        double ammo = tag.getDouble("ammo");
        double rifleAmmo = entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).map(c -> c.rifleAmmo).orElse(0d);

        double ammo1 = mag - ammo + (extraOne ? 1 : 0);

        tag.putDouble("ammo", ammo + Math.min(ammo1, rifleAmmo));
        entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
            capability.rifleAmmo = Math.max(0, rifleAmmo - ammo1);
            capability.syncPlayerVariables(entity);
        });

        tag.putDouble("reloading", 0);
        tag.putDouble("emptyreload", 0);
    }
}
