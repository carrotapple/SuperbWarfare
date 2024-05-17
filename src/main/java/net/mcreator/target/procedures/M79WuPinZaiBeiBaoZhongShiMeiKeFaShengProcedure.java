package net.mcreator.target.procedures;

import net.mcreator.target.init.TargetModItems;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class M79WuPinZaiBeiBaoZhongShiMeiKeFaShengProcedure {
    public static void execute(Entity entity, ItemStack itemstack) {
        if (entity == null)
            return;
        double id = itemstack.getOrCreateTag().getDouble("id");
        int ammo1 = 1 - itemstack.getOrCreateTag().getInt("ammo");
        if ((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getOrCreateTag().getDouble("id") != itemstack.getOrCreateTag().getDouble("id")) {
            itemstack.getOrCreateTag().putDouble("emptyreload", 0);
            itemstack.getOrCreateTag().putDouble("reloading", 0);
            itemstack.getOrCreateTag().putDouble("reloadtime", 0);
        }
        if (itemstack.getOrCreateTag().getDouble("reloading") == 1) {
            if (itemstack.getOrCreateTag().getDouble("reloadtime") == 86) {
                entity.getPersistentData().putDouble("id", id);
                {
                    if (!entity.level().isClientSide() && entity.getServer() != null) {
                        entity.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, entity.position(), entity.getRotationVector(), entity.level() instanceof ServerLevel ? (ServerLevel) entity.level() : null, 4,
                                entity.getName().getString(), entity.getDisplayName(), entity.level().getServer(), entity), "playsound target:m_79_reload player @s ~ ~ ~ 100 1");
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
            if (itemstack.getOrCreateTag().getDouble("reloadtime") == 1 && (entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getOrCreateTag().getDouble("id") == id) {
                if (itemstack.getOrCreateTag().getInt("maxammo") >= ammo1) {
                    itemstack.getOrCreateTag().putInt("ammo", (itemstack.getOrCreateTag().getInt("ammo") + ammo1));
                    if (entity instanceof Player _player) {
                        ItemStack _stktoremove = new ItemStack(TargetModItems.GRENADE_40MM.get());
                        _player.getInventory().clearOrCountMatchingItems(p -> _stktoremove.getItem() == p.getItem(), 1, _player.inventoryMenu.getCraftSlots());
                    }
                    itemstack.getOrCreateTag().putDouble("reloading", 0);
                    itemstack.getOrCreateTag().putDouble("emptyreload", 0);
                } else {
                    itemstack.getOrCreateTag().putInt("ammo", (itemstack.getOrCreateTag().getInt("ammo") + itemstack.getOrCreateTag().getInt("maxammo")));
                    if (entity instanceof Player _player) {
                        ItemStack _stktoremove = new ItemStack(TargetModItems.GRENADE_40MM.get());
                        _player.getInventory().clearOrCountMatchingItems(p -> _stktoremove.getItem() == p.getItem(), 1, _player.inventoryMenu.getCraftSlots());
                    }
                    itemstack.getOrCreateTag().putDouble("reloading", 0);
                    itemstack.getOrCreateTag().putDouble("emptyreload", 0);
                }
            }
        }
    }
}
