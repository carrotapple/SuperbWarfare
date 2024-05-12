package net.mcreator.target.procedures;

import net.mcreator.target.network.TargetModVariables;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class SniperReload1Procedure {
    public static void execute(Entity entity) {
        if (entity == null)
            return;
        double ammo1 = 0;
        double id = 0;
        double ammo2 = 0;
        ItemStack stack = ItemStack.EMPTY;
        stack = (entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY);
        id = stack.getOrCreateTag().getDouble("id");
        ammo1 = stack.getOrCreateTag().getDouble("mag") - stack.getOrCreateTag().getDouble("ammo");
        ammo2 = (stack.getOrCreateTag().getDouble("mag") + 1) - stack.getOrCreateTag().getDouble("ammo");
        if ((entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).sniperAmmo >= ammo1) {
            {
                double _setval = (entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).sniperAmmo - ammo1;
                entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                    capability.sniperAmmo = _setval;
                    capability.syncPlayerVariables(entity);
                });
            }
            stack.getOrCreateTag().putDouble("ammo", (stack.getOrCreateTag().getDouble("ammo") + ammo1));
            stack.getOrCreateTag().putDouble("reloading", 0);
            stack.getOrCreateTag().putDouble("emptyreload", 0);
        } else {
            stack.getOrCreateTag().putDouble("ammo", (stack.getOrCreateTag().getDouble("ammo") + (entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).sniperAmmo));
            {
                double _setval = 0;
                entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                    capability.sniperAmmo = _setval;
                    capability.syncPlayerVariables(entity);
                });
            }
            stack.getOrCreateTag().putDouble("reloading", 0);
            stack.getOrCreateTag().putDouble("emptyreload", 0);
        }
    }
}
