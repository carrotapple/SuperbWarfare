package net.mcreator.target.procedures;

import net.mcreator.target.init.TargetModItems;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class TasercooldownProcedure {
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
        if (itemstack.getOrCreateTag().getBoolean("reloading")) {
            if (itemstack.getOrCreateTag().getDouble("reloading_time") == 55) {
                entity.getPersistentData().putDouble("id", id);
                {
                    Entity _ent = entity;
                    if (!_ent.level().isClientSide() && _ent.getServer() != null) {
                        _ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4,
                                _ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), "playsound target:taser_reload player @s ~ ~ ~ 100 1");
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
            if (itemstack.getOrCreateTag().getDouble("reloading_time") == 1 && (entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getOrCreateTag().getDouble("id") == id) {
                if (itemstack.getOrCreateTag().getInt("maxammo") >= 1) {
                    itemstack.getOrCreateTag().putInt("ammo", 1);
                    if (entity instanceof Player _player) {
                        ItemStack _stktoremove = new ItemStack(TargetModItems.TASER_ELECTRODE.get());
                        _player.getInventory().clearOrCountMatchingItems(p -> _stktoremove.getItem() == p.getItem(), 1, _player.inventoryMenu.getCraftSlots());
                    }
                    itemstack.getOrCreateTag().putBoolean("reloading", false);
                    itemstack.getOrCreateTag().putBoolean("empty_reload", false);
                }
            }
        }
    }
}
