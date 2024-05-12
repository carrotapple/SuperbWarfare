package net.mcreator.target.procedures;

import net.mcreator.target.network.TargetModVariables;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class HandgunReload1Procedure {
    public static void execute(Entity entity) {
        if (entity == null)
            return;
        double ammo1;
        ItemStack stack;
        stack = (entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY);
        ammo1 = stack.getOrCreateTag().getDouble("mag") - stack.getOrCreateTag().getDouble("ammo");
        if ((entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).handgunAmmo >= ammo1) {
            {
                double _setval = (entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).handgunAmmo - ammo1;
                entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                    capability.handgunAmmo = _setval;
                    capability.syncPlayerVariables(entity);
                });
            }
            stack.getOrCreateTag().putDouble("ammo", (stack.getOrCreateTag().getDouble("ammo") + ammo1));
            stack.getOrCreateTag().putDouble("reloading", 0);
            stack.getOrCreateTag().putDouble("emptyreload", 0);
        } else {
            stack.getOrCreateTag().putDouble("ammo", (stack.getOrCreateTag().getDouble("ammo") + (entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).handgunAmmo));
            {
                double _setval = 0;
                entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                    capability.handgunAmmo = _setval;
                    capability.syncPlayerVariables(entity);
                });
            }
            stack.getOrCreateTag().putDouble("reloading", 0);
            stack.getOrCreateTag().putDouble("emptyreload", 0);
        }
    }
}
