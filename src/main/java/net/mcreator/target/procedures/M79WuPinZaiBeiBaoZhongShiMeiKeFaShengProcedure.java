package net.mcreator.target.procedures;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

import net.mcreator.target.init.TargetModItems;

public class M79WuPinZaiBeiBaoZhongShiMeiKeFaShengProcedure {
	public static void execute(Entity entity, ItemStack itemstack) {
		if (entity == null)
			return;
		double id = 0;
		double ammo1 = 0;
		id = itemstack.getOrCreateTag().getDouble("id");
		ammo1 = 1 - itemstack.getOrCreateTag().getDouble("ammo");
		if ((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getOrCreateTag().getDouble("id") != itemstack.getOrCreateTag().getDouble("id")) {
			itemstack.getOrCreateTag().putDouble("emptyreload", 0);
			itemstack.getOrCreateTag().putDouble("reloading", 0);
			itemstack.getOrCreateTag().putDouble("reloadtime", 0);
		}
		if (itemstack.getOrCreateTag().getDouble("reloading") == 1) {
			if (itemstack.getOrCreateTag().getDouble("reloadtime") == 86) {
				entity.getPersistentData().putDouble("id", id);
				{
					Entity _ent = entity;
					if (!_ent.level().isClientSide() && _ent.getServer() != null) {
						_ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4,
								_ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), "playsound target:m79reload player @s ~ ~ ~ 100 1");
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
				if (itemstack.getOrCreateTag().getDouble("maxammo") >= ammo1) {
					itemstack.getOrCreateTag().putDouble("ammo", (itemstack.getOrCreateTag().getDouble("ammo") + ammo1));
					if (entity instanceof Player _player) {
						ItemStack _stktoremove = new ItemStack(TargetModItems.GRENADE_40MM.get());
						_player.getInventory().clearOrCountMatchingItems(p -> _stktoremove.getItem() == p.getItem(), 1, _player.inventoryMenu.getCraftSlots());
					}
					itemstack.getOrCreateTag().putDouble("reloading", 0);
					itemstack.getOrCreateTag().putDouble("emptyreload", 0);
				} else {
					itemstack.getOrCreateTag().putDouble("ammo", (itemstack.getOrCreateTag().getDouble("ammo") + itemstack.getOrCreateTag().getDouble("maxammo")));
					if (entity instanceof Player _player) {
						ItemStack _stktoremove = new ItemStack(TargetModItems.GRENADE_40MM.get());
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
