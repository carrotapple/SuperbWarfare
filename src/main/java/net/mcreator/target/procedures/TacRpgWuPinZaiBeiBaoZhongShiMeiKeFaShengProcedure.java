package net.mcreator.target.procedures;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

import net.mcreator.target.init.TargetModItems;

public class TacRpgWuPinZaiBeiBaoZhongShiMeiKeFaShengProcedure {
	public static void execute(Entity entity, ItemStack itemstack) {
		if (entity == null)
			return;
		double ammo1 = 0;
		double id = 0;
		id = itemstack.getOrCreateTag().getDouble("id");
		ammo1 = 1 - itemstack.getOrCreateTag().getDouble("ammo");
		if ((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getOrCreateTag().getDouble("id") != itemstack.getOrCreateTag().getDouble("id")) {
			itemstack.getOrCreateTag().putDouble("emptyreload", 0);
			itemstack.getOrCreateTag().putDouble("reloading", 0);
			itemstack.getOrCreateTag().putDouble("reloadtime", 0);
		}
		if (itemstack.getOrCreateTag().getDouble("reloading") == 1) {
			if (itemstack.getOrCreateTag().getDouble("reloadtime") == 91) {
				entity.getPersistentData().putDouble("id", id);
				{
					Entity _ent = entity;
					if (!_ent.level().isClientSide() && _ent.getServer() != null) {
						_ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4,
								_ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), "playsound target:rpg7_reload player @s ~ ~ ~ 100 1");
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
			if (itemstack.getOrCreateTag().getDouble("reloadtime") == 84) {
				itemstack.getOrCreateTag().putDouble("empty", 0);
			}
			if (itemstack.getOrCreateTag().getDouble("reloadtime") == 1 && (entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getOrCreateTag().getDouble("id") == id) {
				if (itemstack.getOrCreateTag().getDouble("maxammo") >= 0) {
					itemstack.getOrCreateTag().putDouble("ammo", 1);
					if (entity instanceof Player _player) {
						ItemStack _stktoremove = new ItemStack(TargetModItems.ROCKET.get());
						_player.getInventory().clearOrCountMatchingItems(p -> _stktoremove.getItem() == p.getItem(), 1, _player.inventoryMenu.getCraftSlots());
					}
					itemstack.getOrCreateTag().putDouble("reloading", 0);
					itemstack.getOrCreateTag().putDouble("emptyreload", 0);
				}
			}
		}
		WeaponDrawProcedure.execute(entity, itemstack);
	}
}
