package net.mcreator.target.procedures;

import net.mcreator.target.init.TargetModItems;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class RpgWuPinZaiBeiBaoZhongShiMeiKeFaShengProcedure {
    public static void execute(Entity entity, ItemStack itemstack) {
        if (entity == null) return;
        CompoundTag tag = itemstack.getOrCreateTag();
        double id = tag.getDouble("id");
        if ((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getOrCreateTag().getDouble("id") != tag.getDouble("id")) {
            tag.putDouble("emptyreload", 0);
            tag.putDouble("reloading", 0);
            tag.putDouble("reloadtime", 0);
        }
        if (tag.getDouble("reloading") == 1) {
            if (tag.getDouble("reloadtime") == 91) {
                entity.getPersistentData().putDouble("id", id);
                if (entity.getServer() != null) {
                    entity.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, entity.position(), entity.getRotationVector(), entity.level() instanceof ServerLevel ? (ServerLevel) entity.level() : null, 4,
                            entity.getName().getString(), entity.getDisplayName(), entity.level().getServer(), entity), "playsound target:rpg_reload player @s ~ ~ ~ 100 1");
                }
            }
            if ((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() == itemstack.getItem()
                    && (entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getOrCreateTag().getDouble("id") == id) {
                if (tag.getDouble("reloadtime") > 0) {
                    tag.putDouble("reloadtime", (tag.getDouble("reloadtime") - 1));
                }
            } else {
                tag.putDouble("reloading", 0);
                tag.putDouble("reloadtime", 0);
                tag.putDouble("emptyreload", 0);
            }
            if (tag.getDouble("reloadtime") == 84) {
                tag.putDouble("empty", 0);
            }
            if (tag.getDouble("reloadtime") == 1 && (entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getOrCreateTag().getDouble("id") == id) {
                if (tag.getDouble("maxammo") >= 0) {
                    tag.putDouble("ammo", 1);
                    if (entity instanceof Player _player) {
                        _player.getInventory().clearOrCountMatchingItems(p -> TargetModItems.ROCKET.get() == p.getItem(), 1, _player.inventoryMenu.getCraftSlots());
                    }
                    tag.putDouble("reloading", 0);
                    tag.putDouble("emptyreload", 0);
                }
            }
        }
        WeaponDrawProcedure.execute(entity, itemstack);
    }
}
