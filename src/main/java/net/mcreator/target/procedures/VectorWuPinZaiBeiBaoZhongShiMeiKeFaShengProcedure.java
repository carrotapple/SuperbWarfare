package net.mcreator.target.procedures;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

public class VectorWuPinZaiBeiBaoZhongShiMeiKeFaShengProcedure {
	public static void execute(Entity entity, ItemStack itemstack) {
		if (entity == null)
			return;
		double ammo1 = 0;
		double id = 0;
		double ammo2 = 0;
		id = itemstack.getOrCreateTag().getDouble("id");
		ammo1 = 33 - itemstack.getOrCreateTag().getDouble("ammo");
		ammo2 = 34 - itemstack.getOrCreateTag().getDouble("ammo");
		if ((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getOrCreateTag().getDouble("id") != itemstack.getOrCreateTag().getDouble("id")) {
			itemstack.getOrCreateTag().putDouble("emptyreload", 0);
			itemstack.getOrCreateTag().putDouble("reloading", 0);
			itemstack.getOrCreateTag().putDouble("reloadtime", 0);
		}
		if (itemstack.getOrCreateTag().getDouble("reloading") == 1 && itemstack.getOrCreateTag().getDouble("ammo") == 0) {
			if (itemstack.getOrCreateTag().getDouble("reloadtime") == 61) {
				entity.getPersistentData().putDouble("id", id);
				{
					Entity _ent = entity;
					if (!_ent.level().isClientSide() && _ent.getServer() != null) {
						_ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4,
								_ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), "playsound target:vecreload player @s ~ ~ ~ 100 1");
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
				HandgunReload1Procedure.execute(entity);
			}
		} else if (itemstack.getOrCreateTag().getDouble("reloading") == 1 && itemstack.getOrCreateTag().getDouble("ammo") > 0) {
			if (itemstack.getOrCreateTag().getDouble("reloadtime") == 47) {
				entity.getPersistentData().putDouble("id", id);
				{
					Entity _ent = entity;
					if (!_ent.level().isClientSide() && _ent.getServer() != null) {
						_ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4,
								_ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), "playsound target:vecreload2 player @s ~ ~ ~ 100 1");
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
				HandgunReload2Procedure.execute(entity);
			}
		}
		WeaponDrawLightProcedure.execute(entity, itemstack);
	}
}
