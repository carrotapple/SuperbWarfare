package net.mcreator.target.procedures;

import net.mcreator.target.network.TargetModVariables;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class AbekiriWuPinZaiBeiBaoZhongShiMeiKeFaShengProcedure {
    public static void execute(Entity entity, ItemStack itemstack) {
        if (entity == null)
            return;
        int ammo1 = 0;
        double id = 0;
        id = itemstack.getOrCreateTag().getDouble("id");
        ammo1 = 2 - itemstack.getOrCreateTag().getInt("ammo");
        if ((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getOrCreateTag().getDouble("id") != itemstack.getOrCreateTag().getDouble("id")) {
            itemstack.getOrCreateTag().putDouble("emptyreload", 0);
            itemstack.getOrCreateTag().putDouble("reloading", 0);
            itemstack.getOrCreateTag().putDouble("reloadtime", 0);
        }
        if (itemstack.getOrCreateTag().getDouble("reloading") == 1 && itemstack.getOrCreateTag().getInt("ammo") == 0) {
            if (itemstack.getOrCreateTag().getDouble("reloadtime") == 99) {
                entity.getPersistentData().putDouble("id", id);
                {
                    if (!entity.level().isClientSide() && entity.getServer() != null) {
                        entity.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, entity.position(), entity.getRotationVector(), entity.level() instanceof ServerLevel ? (ServerLevel) entity.level() : null, 4,
                                entity.getName().getString(), entity.getDisplayName(), entity.level().getServer(), entity), "playsound target:abekiri_reload_empty player @s ~ ~ ~ 100 1");
                    }
                }
            }
            if ((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() == itemstack.getItem()
                    && (entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getOrCreateTag().getDouble("id") == id) {
                if (itemstack.getOrCreateTag().getDouble("reloadtime") > 0) {
                    itemstack.getOrCreateTag().putDouble("reloadtime", (itemstack.getOrCreateTag().getDouble("reloadtime") - 1));
                }
            } else {
                itemstack.getOrCreateTag().putDouble("reloading", 0);
                itemstack.getOrCreateTag().putDouble("emptyreload", 0);
                itemstack.getOrCreateTag().putDouble("reloadtime", 0);
            }
            if (itemstack.getOrCreateTag().getDouble("reloadtime") == 1 && (entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getOrCreateTag().getDouble("id") == id) {
                if ((entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).shotgunAmmo >= ammo1) {
                    {
                        int _setval = (entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).shotgunAmmo - ammo1;
                        entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                            capability.shotgunAmmo = _setval;
                            capability.syncPlayerVariables(entity);
                        });
                    }
                    itemstack.getOrCreateTag().putInt("ammo", (itemstack.getOrCreateTag().getInt("ammo") + ammo1));
                    itemstack.getOrCreateTag().putDouble("reloading", 0);
                    itemstack.getOrCreateTag().putDouble("emptyreload", 0);
                } else {
                    {
                        int _setval = itemstack.getOrCreateTag().getInt("ammo") + (entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).shotgunAmmo;
                        entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                            capability.shotgunAmmo = _setval;
                            capability.syncPlayerVariables(entity);
                        });
                    }
                    itemstack.getOrCreateTag().putInt("maxammo", 0);
                    itemstack.getOrCreateTag().putDouble("reloading", 0);
                }
            }
        } else if (itemstack.getOrCreateTag().getDouble("reloading") == 1 && itemstack.getOrCreateTag().getInt("ammo") == 1) {
            if (itemstack.getOrCreateTag().getDouble("reloadtime") == 83) {
                entity.getPersistentData().putDouble("id", id);
                {
                    if (!entity.level().isClientSide() && entity.getServer() != null) {
                        entity.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, entity.position(), entity.getRotationVector(), entity.level() instanceof ServerLevel ? (ServerLevel) entity.level() : null, 4,
                                entity.getName().getString(), entity.getDisplayName(), entity.level().getServer(), entity), "playsound target:abekiri_reload_normal player @s ~ ~ ~ 100 1");
                    }
                }
            }
            if ((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() == itemstack.getItem()
                    && (entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getOrCreateTag().getDouble("id") == id) {
                if (itemstack.getOrCreateTag().getDouble("reloadtime") > 0) {
                    itemstack.getOrCreateTag().putDouble("reloadtime", (itemstack.getOrCreateTag().getDouble("reloadtime") - 1));
                }
            } else {
                itemstack.getOrCreateTag().putDouble("reloading", 0);
                itemstack.getOrCreateTag().putDouble("emptyreload", 0);
                itemstack.getOrCreateTag().putDouble("reloadtime", 0);
            }
            if (itemstack.getOrCreateTag().getDouble("reloadtime") == 1 && (entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getOrCreateTag().getDouble("id") == id) {
                if ((entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).shotgunAmmo >= ammo1) {
                    {
                        int _setval = (entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).shotgunAmmo - ammo1;
                        entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                            capability.shotgunAmmo = _setval;
                            capability.syncPlayerVariables(entity);
                        });
                    }
                    itemstack.getOrCreateTag().putInt("ammo", (itemstack.getOrCreateTag().getInt("ammo") + ammo1));
                    itemstack.getOrCreateTag().putDouble("reloading", 0);
                    itemstack.getOrCreateTag().putDouble("emptyreload", 0);
                } else {
                    {
                        int _setval = itemstack.getOrCreateTag().getInt("ammo") + (entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).shotgunAmmo;
                        entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                            capability.shotgunAmmo = _setval;
                            capability.syncPlayerVariables(entity);
                        });
                    }
                    itemstack.getOrCreateTag().putInt("maxammo", 0);
                    itemstack.getOrCreateTag().putDouble("reloading", 0);
                }
            }
        }
        WeaponDrawLightProcedure.execute(entity, itemstack);
    }
}
