package net.mcreator.target.procedures;

import net.mcreator.target.init.TargetModItems;
import net.mcreator.target.network.TargetModVariables;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class WeaponDrawProcedure {
    public static void execute(Entity entity, ItemStack itemStack) {
        if (entity == null) return;
        CompoundTag tag = itemStack.getOrCreateTag();
        Item mainHandItem = (entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem();
        if (tag.getDouble("draw") == 1) {
            tag.putDouble("draw", 0);
            tag.putDouble("drawtime", 0);
            entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                capability.zooming = false;
                capability.syncPlayerVariables(entity);
            });
            if (entity instanceof Player _player)
                _player.getCooldowns().addCooldown(itemStack.getItem(), 16);
            if (itemStack.getItem() == TargetModItems.RPG.get() && tag.getDouble("ammo") == 0) {
                tag.putDouble("empty", 1);
            }
            if (itemStack.getItem() == TargetModItems.SKS.get() && tag.getDouble("ammo") == 0) {
                tag.putDouble("gj", 1);
            }
        }

        if (mainHandItem == itemStack.getItem()) {
            if (tag.getDouble("drawtime") < 16) {
                tag.putDouble("drawtime", (tag.getDouble("drawtime") + 1));
            }
        }
        if (tag.getDouble("fireanim") > 0) {
            tag.putDouble("fireanim", (tag.getDouble("fireanim") - 1));
        }
    }
}
