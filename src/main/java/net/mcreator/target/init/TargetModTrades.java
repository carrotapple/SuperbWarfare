
/*
*	MCreator note: This file will be REGENERATED on each build.
*/
package net.mcreator.target.init;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.common.BasicItemListing;

import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.npc.VillagerProfession;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class TargetModTrades {
	@SubscribeEvent
	public static void registerTrades(VillagerTradesEvent event) {
		if (event.getType() == VillagerProfession.WEAPONSMITH) {
			event.getTrades().get(1).add(new BasicItemListing(new ItemStack(TargetModItems.TASER_BLUEPRINT.get()),

					new ItemStack(Items.EMERALD), 16, 5, 0.05f));
			event.getTrades().get(2).add(new BasicItemListing(new ItemStack(Items.EMERALD, 10),

					new ItemStack(TargetModItems.STEEL_ACTION.get()), 12, 5, 0.05f));
			event.getTrades().get(4).add(new BasicItemListing(new ItemStack(Items.EMERALD, 32),

					new ItemStack(TargetModItems.M_4_BLUEPRINT.get()), 10, 50, 0.05f));
			event.getTrades().get(4).add(new BasicItemListing(new ItemStack(Items.EMERALD, 32),

					new ItemStack(TargetModItems.M_79_BLUEPRINT.get()), 10, 50, 0.05f));
			event.getTrades().get(4).add(new BasicItemListing(new ItemStack(Items.EMERALD, 32),

					new ItemStack(TargetModItems.MARLIN_BLUEPRINT.get()), 10, 50, 0.05f));
			event.getTrades().get(4).add(new BasicItemListing(new ItemStack(Items.EMERALD, 32),

					new ItemStack(TargetModItems.AK_47_BLUEPRINT.get()), 10, 50, 0.05f));
			event.getTrades().get(5).add(new BasicItemListing(new ItemStack(Items.EMERALD, 64),

					new ItemStack(TargetModItems.HUNTING_RIFLE_BLUEPRINT.get()), 10, 80, 0.05f));
			event.getTrades().get(5).add(new BasicItemListing(new ItemStack(Items.EMERALD, 64),

					new ItemStack(TargetModItems.RPG_BLUEPRINT.get()), 10, 80, 0.05f));
			event.getTrades().get(5).add(new BasicItemListing(new ItemStack(Items.EMERALD, 64),

					new ItemStack(TargetModItems.HK_416_BLUEPRINT.get()), 10, 80, 0.05f));
			event.getTrades().get(5).add(new BasicItemListing(new ItemStack(Items.EMERALD, 64),

					new ItemStack(TargetModItems.RPK_BLUEPRINT.get()), 10, 80, 0.05f));
			event.getTrades().get(5).add(new BasicItemListing(new ItemStack(Items.EMERALD, 64),

					new ItemStack(TargetModItems.VECTOR_BLUEPRINT.get()), 10, 80, 0.05f));
			event.getTrades().get(5).add(new BasicItemListing(new ItemStack(Items.EMERALD, 64),

					new ItemStack(TargetModItems.MK_14_BLUEPRINT.get()), 10, 80, 0.05f));
			event.getTrades().get(5).add(new BasicItemListing(new ItemStack(Items.EMERALD, 64),

					new ItemStack(TargetModItems.M_60_BLUEPRINT.get()), 10, 80, 0.05f));
			event.getTrades().get(5).add(new BasicItemListing(new ItemStack(Items.EMERALD, 64),

					new ItemStack(TargetModItems.SVD_BLUEPRINT.get()), 10, 80, 0.05f));
			event.getTrades().get(5).add(new BasicItemListing(new ItemStack(Items.EMERALD, 64),

					new ItemStack(TargetModItems.M_870_BLUEPRINT.get()), 10, 80, 0.05f));
			event.getTrades().get(5).add(new BasicItemListing(new ItemStack(Items.EMERALD, 64),

					new ItemStack(TargetModItems.M_98B_BLUEPRINT.get()), 10, 80, 0.05f));
			event.getTrades().get(5).add(new BasicItemListing(new ItemStack(Items.EMERALD),

					new ItemStack(TargetModItems.DEVOTION_BLUPRINT.get()), 10, 80, 0.05f));
			event.getTrades().get(2).add(new BasicItemListing(new ItemStack(Items.EMERALD, 8),

					new ItemStack(TargetModItems.STEEL_BARREL.get()), 12, 5, 0.05f));
			event.getTrades().get(2).add(new BasicItemListing(new ItemStack(Items.EMERALD, 6),

					new ItemStack(TargetModItems.STEEL_TRIGGER.get()), 12, 5, 0.05f));
			event.getTrades().get(2).add(new BasicItemListing(new ItemStack(Items.EMERALD, 8),

					new ItemStack(TargetModItems.STEEL_SPRING.get()), 12, 5, 0.05f));
			event.getTrades().get(3).add(new BasicItemListing(new ItemStack(Items.EMERALD, 16),

					new ItemStack(TargetModItems.CEMENTED_CARBIDE_BARREL.get()), 12, 10, 0.05f));
			event.getTrades().get(3).add(new BasicItemListing(new ItemStack(Items.EMERALD, 20),

					new ItemStack(TargetModItems.CEMENTED_CARBIDE_ACTION.get()), 10, 10, 0.05f));
			event.getTrades().get(3).add(new BasicItemListing(new ItemStack(Items.EMERALD, 16),

					new ItemStack(TargetModItems.CEMENTEDCARBIDESPRING.get()), 10, 10, 0.05f));
			event.getTrades().get(3).add(new BasicItemListing(new ItemStack(Items.EMERALD, 12),

					new ItemStack(TargetModItems.CEMENTEDCARBIDE_TRIGGER.get()), 10, 10, 0.05f));
		}
	}
}
