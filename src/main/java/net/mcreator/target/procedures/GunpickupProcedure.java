package net.mcreator.target.procedures;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;

import net.minecraft.world.item.ItemStack;
import net.minecraft.tags.ItemTags;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber
public class GunpickupProcedure {
	@SubscribeEvent
	public static void onPickup(EntityItemPickupEvent event) {
		execute(event, event.getItem().getItem());
	}

	public static void execute(ItemStack itemstack) {
		execute(null, itemstack);
	}

	private static void execute(@Nullable Event event, ItemStack itemstack) {
		if (itemstack.is(ItemTags.create(new ResourceLocation("target:gun")))) {
			itemstack.getOrCreateTag().putDouble("drawtime", 0);
		}
	}
}
