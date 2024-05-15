package net.mcreator.target.procedures;

import net.mcreator.target.network.TargetModVariables;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class WeaponDrawLightProcedure {
    public static void execute(Entity entity, ItemStack itemStack) {
        if (entity == null) return;
        CompoundTag tag = itemStack.getOrCreateTag();
        Item mainHandItem = (entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem();
        if (tag.getDouble("draw") == 1) {
            tag.putDouble("draw", 0);
            tag.putDouble("drawtime", 0);
            if (entity instanceof Player _player)
                _player.getCooldowns().addCooldown(itemStack.getItem(), 11);
            entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                capability.zooming = false;
                capability.syncPlayerVariables(entity);
            });
        }

        if (mainHandItem == itemStack.getItem()) {
            if (tag.getDouble("drawtime") < 11) {
                tag.putDouble("drawtime", (tag.getDouble("drawtime") + 1));
            }
        }
        if (tag.getDouble("fireanim") > 0) {
            tag.putDouble("fireanim", (tag.getDouble("fireanim") - 1));
        }
    }
}
