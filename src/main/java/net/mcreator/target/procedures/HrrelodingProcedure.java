package net.mcreator.target.procedures;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

public class HrrelodingProcedure {
	public static void execute(Entity entity, ItemStack itemstack) {
		if (entity == null)
			return;
		double id = 0;
		double ammo1 = 0;
		double ammo2 = 0;
		id = itemstack.getOrCreateTag().getDouble("id");
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
								_ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), "playsound target:hr_reload player @s ~ ~ ~ 100 1");
					}
				}
			}
			if ((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() == itemstack.getItem()
					&& (entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getOrCreateTag().getDouble("id") == id) {
				if (itemstack.getOrCreateTag().getDouble("reloadtime") > 0) {
					itemstack.getOrCreateTag().putDouble("reloadtime", (itemstack.getOrCreateTag().getDouble("reloadtime") - 1));
				}
			} else {
				itemstack.getOrCreateTag().putDouble("emptyreload", 0);
				itemstack.getOrCreateTag().putDouble("reloading", 0);
				itemstack.getOrCreateTag().putDouble("reloadtime", 0);
			}
			if (itemstack.getOrCreateTag().getDouble("reloadtime") == 1 && (entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getOrCreateTag().getDouble("id") == id) {
				SniperReload1Procedure.execute(entity);
			}
		}
		WeaponDrawProcedure.execute(entity, itemstack);
	}
}
