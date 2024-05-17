package net.mcreator.target.procedures;

import net.mcreator.target.tools.GunInfo;
import net.mcreator.target.tools.GunReload;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class SksWuPinZaiBeiBaoZhongShiMeiKeFaShengProcedure {
    public static void execute(Entity entity, ItemStack itemstack) {
        if (entity == null)
            return;
        double id = 0;
        id = itemstack.getOrCreateTag().getDouble("id");
        if ((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getOrCreateTag().getDouble("id") != itemstack.getOrCreateTag().getDouble("id")) {
            itemstack.getOrCreateTag().putBoolean("empty_reload", false);
            itemstack.getOrCreateTag().putBoolean("reloading", false);
            itemstack.getOrCreateTag().putDouble("reloading_time", 0);
        }
        if (itemstack.getOrCreateTag().getBoolean("reloading") && itemstack.getOrCreateTag().getInt("ammo") == 0) {
            if (itemstack.getOrCreateTag().getDouble("reloading_time") == 57) {
                entity.getPersistentData().putDouble("id", id);
                {
                    Entity _ent = entity;
                    if (!_ent.level().isClientSide() && _ent.getServer() != null) {
                        _ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4,
                                _ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), "playsound target:sks_reload_empty player @s ~ ~ ~ 100 1");
                    }
                }
            }
            if ((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() == itemstack.getItem()
                    && (entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getOrCreateTag().getDouble("id") == id) {
                if (itemstack.getOrCreateTag().getDouble("reloading_time") > 0) {
                    itemstack.getOrCreateTag().putDouble("reloading_time", (itemstack.getOrCreateTag().getDouble("reloading_time") - 1));
                }
            } else {
                itemstack.getOrCreateTag().putBoolean("reloading", false);
                itemstack.getOrCreateTag().putDouble("reloading_time", 0);
                itemstack.getOrCreateTag().putBoolean("empty_reload", false);
            }
            if (itemstack.getOrCreateTag().getDouble("reloading_time") == 14 && (entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getOrCreateTag().getDouble("id") == id) {
                itemstack.getOrCreateTag().putDouble("gj", 0);
            }
            if (itemstack.getOrCreateTag().getDouble("reloading_time") == 1 && (entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getOrCreateTag().getDouble("id") == id) {
                GunReload.reload(entity, GunInfo.Type.RIFLE);
            }
        } else if (itemstack.getOrCreateTag().getBoolean("reloading") && itemstack.getOrCreateTag().getInt("ammo") > 0) {
            if (itemstack.getOrCreateTag().getDouble("reloading_time") == 41) {
                entity.getPersistentData().putDouble("id", id);
                {
                    Entity _ent = entity;
                    if (!_ent.level().isClientSide() && _ent.getServer() != null) {
                        _ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4,
                                _ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), "playsound target:sks_reload_normal player @s ~ ~ ~ 100 1");
                    }
                }
            }
            if ((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() == itemstack.getItem()
                    && (entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getOrCreateTag().getDouble("id") == id) {
                if (itemstack.getOrCreateTag().getDouble("reloading_time") > 0) {
                    itemstack.getOrCreateTag().putDouble("reloading_time", (itemstack.getOrCreateTag().getDouble("reloading_time") - 1));
                }
            } else {
                itemstack.getOrCreateTag().putBoolean("reloading", false);
                itemstack.getOrCreateTag().putBoolean("empty_reload", false);
                itemstack.getOrCreateTag().putDouble("reloading_time", 0);
            }
            if (itemstack.getOrCreateTag().getDouble("reloading_time") == 1 && (entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getOrCreateTag().getDouble("id") == id) {
                GunReload.reload(entity, GunInfo.Type.RIFLE, true);
            }
        }
    }
}
