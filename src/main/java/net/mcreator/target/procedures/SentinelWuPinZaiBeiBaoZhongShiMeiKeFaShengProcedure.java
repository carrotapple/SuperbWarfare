package net.mcreator.target.procedures;

import net.mcreator.target.tools.GunInfo;
import net.mcreator.target.tools.GunReload;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class SentinelWuPinZaiBeiBaoZhongShiMeiKeFaShengProcedure {
    public static void execute(Entity entity, ItemStack itemstack) {
        if (entity == null)
            return;
        double id = 0;
        double cid = 0;
        id = itemstack.getOrCreateTag().getDouble("id");
        if ((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getOrCreateTag().getDouble("id") != itemstack.getOrCreateTag().getDouble("id")) {
            itemstack.getOrCreateTag().putBoolean("empty_reload", false);
            itemstack.getOrCreateTag().putBoolean("reloading", false);
            itemstack.getOrCreateTag().putDouble("reload_time", 0);
        }
        if (itemstack.getOrCreateTag().getBoolean("reloading") && itemstack.getOrCreateTag().getInt("ammo") == 0) {
            if (itemstack.getOrCreateTag().getDouble("reload_time") == 73) {
                entity.getPersistentData().putDouble("id", id);
                {
                    Entity _ent = entity;
                    if (!_ent.level().isClientSide() && _ent.getServer() != null) {
                        _ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4,
                                _ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), "playsound target:sentinel_reload_empty player @s ~ ~ ~ 100 1");
                    }
                }
            }
            if ((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() == itemstack.getItem()
                    && (entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getOrCreateTag().getDouble("id") == id) {
                if (itemstack.getOrCreateTag().getDouble("reload_time") > 0) {
                    itemstack.getOrCreateTag().putDouble("reload_time", (itemstack.getOrCreateTag().getDouble("reload_time") - 1));
                }
            } else {
                itemstack.getOrCreateTag().putBoolean("reloading", false);
                itemstack.getOrCreateTag().putBoolean("empty_reload", false);
                itemstack.getOrCreateTag().putDouble("reload_time", 0);
            }
            if (itemstack.getOrCreateTag().getDouble("reload_time") == 1 && (entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getOrCreateTag().getDouble("id") == id) {
                GunReload.reload(entity, GunInfo.Type.SNIPER);
            }
        } else if (itemstack.getOrCreateTag().getBoolean("reloading") && itemstack.getOrCreateTag().getInt("ammo") > 0) {
            if (itemstack.getOrCreateTag().getDouble("reload_time") == 53) {
                entity.getPersistentData().putDouble("id", id);
                {
                    Entity _ent = entity;
                    if (!_ent.level().isClientSide() && _ent.getServer() != null) {
                        _ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4,
                                _ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), "playsound target:sentinel_reload_normal player @s ~ ~ ~ 100 1");
                    }
                }
            }
            if ((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() == itemstack.getItem()
                    && (entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getOrCreateTag().getDouble("id") == id) {
                if (itemstack.getOrCreateTag().getDouble("reload_time") > 0) {
                    itemstack.getOrCreateTag().putDouble("reload_time", (itemstack.getOrCreateTag().getDouble("reload_time") - 1));
                }
            } else {
                itemstack.getOrCreateTag().putBoolean("reloading", false);
                itemstack.getOrCreateTag().putBoolean("empty_reload", false);
                itemstack.getOrCreateTag().putDouble("reload_time", 0);
            }
            if (itemstack.getOrCreateTag().getDouble("reload_time") == 1 && (entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getOrCreateTag().getDouble("id") == id) {
                GunReload.reload(entity, GunInfo.Type.SNIPER, true);
            }
        }
        if (itemstack.getOrCreateTag().getDouble("firing") > 0) {
            itemstack.getOrCreateTag().putDouble("firing", (itemstack.getOrCreateTag().getDouble("firing") - 1));
        }
        if (itemstack.getOrCreateTag().getDouble("zoom_firing") > 0) {
            itemstack.getOrCreateTag().putDouble("zoom_firing", (itemstack.getOrCreateTag().getDouble("zoom_firing") - 1));
        }

        cid = itemstack.getOrCreateTag().getDouble("cid");
        if ((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getOrCreateTag().getDouble("cid") != itemstack.getOrCreateTag().getDouble("cid")) {
            itemstack.getOrCreateTag().putDouble("charging", 0);
            itemstack.getOrCreateTag().putDouble("charging_time", 0);
        }
        if (itemstack.getOrCreateTag().getDouble("charging") == 1) {
            if (itemstack.getOrCreateTag().getDouble("charging_time") == 127) {
                entity.getPersistentData().putDouble("cid", cid);
                {
                    Entity _ent = entity;
                    if (!_ent.level().isClientSide() && _ent.getServer() != null) {
                        _ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4,
                                _ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), "playsound target:sentinel_charge player @s ~ ~ ~ 100 1");
                    }
                }
            }
            if ((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() == itemstack.getItem()
                    && (entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getOrCreateTag().getDouble("cid") == cid) {
                if (itemstack.getOrCreateTag().getDouble("charging_time") > 0) {
                    itemstack.getOrCreateTag().putDouble("charging_time", (itemstack.getOrCreateTag().getDouble("charging_time") - 1));
                }
            } else {
                itemstack.getOrCreateTag().putDouble("charging", 0);
                itemstack.getOrCreateTag().putDouble("charging_time", 0);
            }
            if (itemstack.getOrCreateTag().getDouble("charging_time") == 16 && (entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getOrCreateTag().getDouble("cid") == cid) {
                itemstack.getOrCreateTag().putDouble("power", 100);
            }
            if (itemstack.getOrCreateTag().getDouble("charging_time") == 1 && (entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getOrCreateTag().getDouble("cid") == cid) {
                itemstack.getOrCreateTag().putDouble("charging", 0);
            }
        }
        if (itemstack.getOrCreateTag().getDouble("power") > 0) {
            itemstack.getOrCreateTag().putDouble("adddamage", 10);
            itemstack.getOrCreateTag().putDouble("power", (itemstack.getOrCreateTag().getDouble("power") - 0.025));
        } else {
            itemstack.getOrCreateTag().putDouble("adddamage", 0);
        }
        if (itemstack.getOrCreateTag().getDouble("crot") > 0) {
            itemstack.getOrCreateTag().putDouble("crot", (itemstack.getOrCreateTag().getDouble("crot") - 1));
        }
    }
}
