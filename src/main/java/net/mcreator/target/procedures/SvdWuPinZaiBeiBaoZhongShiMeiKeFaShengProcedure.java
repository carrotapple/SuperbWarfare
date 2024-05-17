package net.mcreator.target.procedures;

import net.mcreator.target.tools.GunInfo;
import net.mcreator.target.tools.GunReload;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class SvdWuPinZaiBeiBaoZhongShiMeiKeFaShengProcedure {
    public static void execute(Entity entity, ItemStack itemstack) {
        if (entity == null)
            return;
        double id = 0;
        id = itemstack.getOrCreateTag().getDouble("id");
        if ((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getOrCreateTag().getDouble("id") != itemstack.getOrCreateTag().getDouble("id")) {
            itemstack.getOrCreateTag().putDouble("emptyreload", 0);
            itemstack.getOrCreateTag().putDouble("reloading", 0);
            itemstack.getOrCreateTag().putDouble("reloadtime", 0);
        }
        if (itemstack.getOrCreateTag().getDouble("reloading") == 1 && itemstack.getOrCreateTag().getInt("ammo") == 0) {
            if (itemstack.getOrCreateTag().getDouble("reloadtime") == 66) {
                entity.getPersistentData().putDouble("id", id);
                {
                    Entity _ent = entity;
                    if (!_ent.level().isClientSide() && _ent.getServer() != null) {
                        _ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4,
                                _ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), "playsound target:svd_reload_empty player @s ~ ~ ~ 100 1");
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
                itemstack.getOrCreateTag().putDouble("reloadtime", 0);
                itemstack.getOrCreateTag().putDouble("emptyreload", 0);
            }
            if (itemstack.getOrCreateTag().getDouble("reloadtime") == 14 && (entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getOrCreateTag().getDouble("id") == id) {
                itemstack.getOrCreateTag().putDouble("gj", 0);
            }
            if (itemstack.getOrCreateTag().getDouble("reloadtime") == 1 && (entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getOrCreateTag().getDouble("id") == id) {
                GunReload.reload(entity, GunInfo.Type.SNIPER);
            }
        } else if (itemstack.getOrCreateTag().getDouble("reloading") == 1 && itemstack.getOrCreateTag().getInt("ammo") > 0) {
            if (itemstack.getOrCreateTag().getDouble("reloadtime") == 55) {
                entity.getPersistentData().putDouble("id", id);
                {
                    Entity _ent = entity;
                    if (!_ent.level().isClientSide() && _ent.getServer() != null) {
                        _ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4,
                                _ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), "playsound target:svd_reload_normal player @s ~ ~ ~ 100 1");
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
    }
}
