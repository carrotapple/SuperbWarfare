package net.mcreator.target.procedures;

import net.mcreator.target.tools.GunInfo;
import net.mcreator.target.tools.GunReload;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class Mk14WuPinZaiBeiBaoZhongShiMeiKeFaShengProcedure {
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
            if (itemstack.getOrCreateTag().getDouble("reloading_time") == 55) {
                entity.getPersistentData().putDouble("id", id);
                {
                    if (!entity.level().isClientSide() && entity.getServer() != null) {
                        entity.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, entity.position(), entity.getRotationVector(), entity.level() instanceof ServerLevel ? (ServerLevel) entity.level() : null, 4,
                                entity.getName().getString(), entity.getDisplayName(), entity.level().getServer(), entity), "playsound target:mk_14_reload_empty player @s ~ ~ ~ 100 1");
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
            if (itemstack.getOrCreateTag().getDouble("reloading_time") == 5 && (entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getOrCreateTag().getDouble("id") == id) {
                itemstack.getOrCreateTag().putDouble("gj", 0);
            }
            if (itemstack.getOrCreateTag().getDouble("reloading_time") == 1 && (entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getOrCreateTag().getDouble("id") == id) {
                GunReload.reload(entity, GunInfo.Type.RIFLE);
            }
        } else if (itemstack.getOrCreateTag().getBoolean("reloading") && itemstack.getOrCreateTag().getInt("ammo") > 0) {
            if (itemstack.getOrCreateTag().getDouble("reloading_time") == 45) {
                entity.getPersistentData().putDouble("id", id);
                {
                    if (!entity.level().isClientSide() && entity.getServer() != null) {
                        entity.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, entity.position(), entity.getRotationVector(), entity.level() instanceof ServerLevel ? (ServerLevel) entity.level() : null, 4,
                                entity.getName().getString(), entity.getDisplayName(), entity.level().getServer(), entity), "playsound target:mk_14_reload_normal player @s ~ ~ ~ 100 1");
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
