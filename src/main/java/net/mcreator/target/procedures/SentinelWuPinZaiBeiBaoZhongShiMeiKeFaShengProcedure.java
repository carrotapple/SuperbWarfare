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
            itemstack.getOrCreateTag().putDouble("emptyreload", 0);
            itemstack.getOrCreateTag().putDouble("reloading", 0);
            itemstack.getOrCreateTag().putDouble("reloadtime", 0);
        }
        if (itemstack.getOrCreateTag().getDouble("reloading") == 1 && itemstack.getOrCreateTag().getInt("ammo") == 0) {
            if (itemstack.getOrCreateTag().getDouble("reloadtime") == 73) {
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
                if (itemstack.getOrCreateTag().getDouble("reloadtime") > 0) {
                    itemstack.getOrCreateTag().putDouble("reloadtime", (itemstack.getOrCreateTag().getDouble("reloadtime") - 1));
                }
            } else {
                itemstack.getOrCreateTag().putDouble("reloading", 0);
                itemstack.getOrCreateTag().putDouble("emptyreload", 0);
                itemstack.getOrCreateTag().putDouble("reloadtime", 0);
            }
            if (itemstack.getOrCreateTag().getDouble("reloadtime") == 1 && (entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getOrCreateTag().getDouble("id") == id) {
                GunReload.reload(entity, GunInfo.Type.SNIPER);
            }
        } else if (itemstack.getOrCreateTag().getDouble("reloading") == 1 && itemstack.getOrCreateTag().getInt("ammo") > 0) {
            if (itemstack.getOrCreateTag().getDouble("reloadtime") == 53) {
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
                if (itemstack.getOrCreateTag().getDouble("reloadtime") > 0) {
                    itemstack.getOrCreateTag().putDouble("reloadtime", (itemstack.getOrCreateTag().getDouble("reloadtime") - 1));
                }
            } else {
                itemstack.getOrCreateTag().putDouble("reloading", 0);
                itemstack.getOrCreateTag().putDouble("emptyreload", 0);
                itemstack.getOrCreateTag().putDouble("reloadtime", 0);
            }
            if (itemstack.getOrCreateTag().getDouble("reloadtime") == 1 && (entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getOrCreateTag().getDouble("id") == id) {
                GunReload.reload(entity, GunInfo.Type.SNIPER, true);
            }
        }
        if (itemstack.getOrCreateTag().getDouble("firing") > 0) {
            itemstack.getOrCreateTag().putDouble("firing", (itemstack.getOrCreateTag().getDouble("firing") - 1));
        }
        if (itemstack.getOrCreateTag().getDouble("zoomfiring") > 0) {
            itemstack.getOrCreateTag().putDouble("zoomfiring", (itemstack.getOrCreateTag().getDouble("zoomfiring") - 1));
        }

        cid = itemstack.getOrCreateTag().getDouble("cid");
        if ((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getOrCreateTag().getDouble("cid") != itemstack.getOrCreateTag().getDouble("cid")) {
            itemstack.getOrCreateTag().putDouble("charging", 0);
            itemstack.getOrCreateTag().putDouble("chargingtime", 0);
        }
        if (itemstack.getOrCreateTag().getDouble("charging") == 1) {
            if (itemstack.getOrCreateTag().getDouble("chargingtime") == 127) {
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
                if (itemstack.getOrCreateTag().getDouble("chargingtime") > 0) {
                    itemstack.getOrCreateTag().putDouble("chargingtime", (itemstack.getOrCreateTag().getDouble("chargingtime") - 1));
                }
            } else {
                itemstack.getOrCreateTag().putDouble("charging", 0);
                itemstack.getOrCreateTag().putDouble("chargingtime", 0);
            }
            if (itemstack.getOrCreateTag().getDouble("chargingtime") == 16 && (entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getOrCreateTag().getDouble("cid") == cid) {
                itemstack.getOrCreateTag().putDouble("power", 100);
            }
            if (itemstack.getOrCreateTag().getDouble("chargingtime") == 1 && (entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getOrCreateTag().getDouble("cid") == cid) {
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
