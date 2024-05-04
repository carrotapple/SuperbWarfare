package net.mcreator.target.procedures;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.world.item.ItemStack;
import net.minecraft.tags.ItemTags;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;

import net.mcreator.target.init.TargetModItems;

import javax.annotation.Nullable;

import java.util.List;

@Mod.EventBusSubscriber
public class GunsTipsProcedure {
	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public static void onItemTooltip(ItemTooltipEvent event) {
		execute(event, event.getItemStack(), event.getToolTip());
	}

	public static void execute(ItemStack itemstack, List<Component> tooltip) {
		execute(null, itemstack, tooltip);
	}

	private static void execute(@Nullable Event event, ItemStack itemstack, List<Component> tooltip) {
		if (tooltip == null)
			return;
		String firemode = "";
		ItemStack usehand = ItemStack.EMPTY;
		if (itemstack.is(ItemTags.create(new ResourceLocation("target:gun")))) {
			if (itemstack.getItem() == TargetModItems.BOCEK.get()) {
				tooltip.add(Component.literal(("\u00A7l\u00A77Damage:(" + new java.text.DecimalFormat("##.##").format(2.4 * itemstack.getOrCreateTag().getDouble("damageadd")) + ")*10/"
						+ new java.text.DecimalFormat("##.##").format(24 * itemstack.getOrCreateTag().getDouble("damageadd")))));
			} else {
				tooltip.add(Component.literal(("\u00A7l\u00A77Damage:"
						+ new java.text.DecimalFormat("##.##").format((itemstack.getOrCreateTag().getDouble("damage") + itemstack.getOrCreateTag().getDouble("adddamage")) * itemstack.getOrCreateTag().getDouble("damageadd")))));
			}
			if (itemstack.getOrCreateTag().getDouble("level") < 4) {
				tooltip.add(Component.literal(("\u00A7f\u00A7lLevel:" + new java.text.DecimalFormat("##").format(itemstack.getOrCreateTag().getDouble("level")) + " "
						+ new java.text.DecimalFormat("##.##").format(100 * (itemstack.getOrCreateTag().getDouble("damagenow") / itemstack.getOrCreateTag().getDouble("damageneed"))) + "%")));
			} else if (4 <= itemstack.getOrCreateTag().getDouble("level") && itemstack.getOrCreateTag().getDouble("level") < 6) {
				tooltip.add(Component.literal(("\u00A7b\u00A7lLevel:" + new java.text.DecimalFormat("##").format(itemstack.getOrCreateTag().getDouble("level")) + " "
						+ new java.text.DecimalFormat("##.##").format(100 * (itemstack.getOrCreateTag().getDouble("damagenow") / itemstack.getOrCreateTag().getDouble("damageneed"))) + "%")));
			} else if (6 <= itemstack.getOrCreateTag().getDouble("level") && itemstack.getOrCreateTag().getDouble("level") < 8) {
				tooltip.add(Component.literal(("\u00A7d\u00A7lLevel:" + new java.text.DecimalFormat("##").format(itemstack.getOrCreateTag().getDouble("level")) + " "
						+ new java.text.DecimalFormat("##.##").format(100 * (itemstack.getOrCreateTag().getDouble("damagenow") / itemstack.getOrCreateTag().getDouble("damageneed"))) + "%")));
			} else if (8 <= itemstack.getOrCreateTag().getDouble("level") && itemstack.getOrCreateTag().getDouble("level") < 10) {
				tooltip.add(Component.literal(("\u00A76\u00A7lLevel:" + new java.text.DecimalFormat("##").format(itemstack.getOrCreateTag().getDouble("level")) + " "
						+ new java.text.DecimalFormat("##.##").format(100 * (itemstack.getOrCreateTag().getDouble("damagenow") / itemstack.getOrCreateTag().getDouble("damageneed"))) + "%")));
			} else if (10 <= itemstack.getOrCreateTag().getDouble("level")) {
				tooltip.add(Component.literal(("\u00A7c\u00A7lLevel:" + new java.text.DecimalFormat("##").format(itemstack.getOrCreateTag().getDouble("level")) + " "
						+ new java.text.DecimalFormat("##.##").format(100 * (itemstack.getOrCreateTag().getDouble("damagenow") / itemstack.getOrCreateTag().getDouble("damageneed"))) + "%")));
			}
		}
		if (itemstack.getItem() == TargetModItems.CREATIVE_AMMO_BOX.get()) {
			tooltip.add(Component.literal("Creative Ammo * 2147483647"));
		}
		if (itemstack.getItem() == TargetModItems.RIFLE_AMMO_BOX.get()) {
			tooltip.add(Component.literal("Rifle Ammo * 30"));
		}
		if (itemstack.getItem() == TargetModItems.HANDGUN_AMMO_BOX.get()) {
			tooltip.add(Component.literal("Handgun Ammo * 30"));
		}
		if (itemstack.getItem() == TargetModItems.SHOTGUN_AMMO_BOX.get()) {
			tooltip.add(Component.literal("Shotgun Ammo * 12"));
		}
		if (itemstack.getItem() == TargetModItems.SNIPER_AMMO_BOX.get()) {
			tooltip.add(Component.literal("Sniper Ammo * 12"));
		}
	}
}
