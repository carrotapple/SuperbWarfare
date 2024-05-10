package net.mcreator.target.procedures;

import net.mcreator.target.network.TargetModVariables;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class M870WuPinZaiBeiBaoZhongShiMeiKeFaShengProcedure {
    public static void execute(Entity entity, ItemStack itemstack) {
        if (entity == null)
            return;
        double ammo1 = 0;
        double id = 0;
        id = itemstack.getOrCreateTag().getDouble("id");
        ammo1 = 8 - itemstack.getOrCreateTag().getDouble("ammo");
        if ((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getOrCreateTag().getDouble("id") != itemstack.getOrCreateTag().getDouble("id")) {
            itemstack.getOrCreateTag().putDouble("finish", 0);
            itemstack.getOrCreateTag().putDouble("reloading", 0);
            itemstack.getOrCreateTag().putDouble("prepare", 0);
            itemstack.getOrCreateTag().putDouble("loading", 0);
            itemstack.getOrCreateTag().putDouble("forcestop", 0);
            itemstack.getOrCreateTag().putDouble("stop", 0);
            itemstack.getOrCreateTag().putDouble("emptyreload", 0);
        }
        if (itemstack.getOrCreateTag().getDouble("prepare") > 0) {
            itemstack.getOrCreateTag().putDouble("prepare", (itemstack.getOrCreateTag().getDouble("prepare") - 1));
        }
        if (itemstack.getOrCreateTag().getDouble("loading") > 0) {
            itemstack.getOrCreateTag().putDouble("loading", (itemstack.getOrCreateTag().getDouble("loading") - 1));
        }
        if (itemstack.getOrCreateTag().getDouble("finish") > 0 && itemstack.getOrCreateTag().getDouble("loading") == 0) {
            itemstack.getOrCreateTag().putDouble("finish", (itemstack.getOrCreateTag().getDouble("finish") - 1));
        }
        if ((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getOrCreateTag().getDouble("id") != itemstack.getOrCreateTag().getDouble("id")) {
            itemstack.getOrCreateTag().putDouble("reloading", 0);
        }
        if (itemstack.getOrCreateTag().getDouble("reloading") == 1 && (entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getOrCreateTag().getDouble("id") == id) {
            if (itemstack.getOrCreateTag().getDouble("prepare") == 10 && itemstack.getOrCreateTag().getDouble("emptyreload") == 1) {
                itemstack.getOrCreateTag().putDouble("ammo", (itemstack.getOrCreateTag().getDouble("ammo") + 1));
                {
                    double _setval = (entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).shotgunammo - 1;
                    entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                        capability.shotgunammo = _setval;
                        capability.syncPlayerVariables(entity);
                    });
                }
            }
            if (itemstack.getOrCreateTag().getDouble("prepare") == 0 && itemstack.getOrCreateTag().getDouble("loading") == 0
                    && !(itemstack.getOrCreateTag().getDouble("ammo") >= 8 || (entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).shotgunammo == 0)) {
                if (itemstack.getOrCreateTag().getDouble("forcestop") == 1) {
                    itemstack.getOrCreateTag().putDouble("stop", 1);
                } else {
                    itemstack.getOrCreateTag().putDouble("loading", 16);
                    if (entity instanceof Player _player)
                        _player.getCooldowns().addCooldown(itemstack.getItem(), 16);
                    {
                        if (!entity.level().isClientSide() && entity.getServer() != null) {
                            entity.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, entity.position(), entity.getRotationVector(), entity.level() instanceof ServerLevel ? (ServerLevel) entity.level() : null, 4,
                                    entity.getName().getString(), entity.getDisplayName(), entity.level().getServer(), entity), "playsound target:m_870_reloadloop player @s ~ ~ ~ 100 1");
                        }
                    }
                    if (itemstack.getOrCreateTag().getDouble("loadindex") == 0) {
                        itemstack.getOrCreateTag().putDouble("loadindex", 1);
                    } else {
                        itemstack.getOrCreateTag().putDouble("loadindex", 0);
                    }
                }
            }
            if (itemstack.getOrCreateTag().getDouble("loading") == 9) {
                itemstack.getOrCreateTag().putDouble("ammo", (itemstack.getOrCreateTag().getDouble("ammo") + 1));
                {
                    double _setval = (entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).shotgunammo - 1;
                    entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                        capability.shotgunammo = _setval;
                        capability.syncPlayerVariables(entity);
                    });
                }
            }
            if ((itemstack.getOrCreateTag().getDouble("ammo") >= 8 || (entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).shotgunammo == 0)
                    && itemstack.getOrCreateTag().getDouble("loading") == 0 || itemstack.getOrCreateTag().getDouble("stop") == 1) {
                itemstack.getOrCreateTag().putDouble("forcestop", 0);
                itemstack.getOrCreateTag().putDouble("stop", 0);
                itemstack.getOrCreateTag().putDouble("finish", 12);
                if (entity instanceof Player _player)
                    _player.getCooldowns().addCooldown(itemstack.getItem(), 12);
                itemstack.getOrCreateTag().putDouble("reloading", 0);
                itemstack.getOrCreateTag().putDouble("emptyreload", 0);
            }
        }
        if (itemstack.getOrCreateTag().getDouble("firing") > 0) {
            itemstack.getOrCreateTag().putDouble("firing", (itemstack.getOrCreateTag().getDouble("firing") - 1));
        }
        WeaponDrawProcedure.execute(entity, itemstack);
    }
}
