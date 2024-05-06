
/*
 *	MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.target.init;

import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.BasicItemListing;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class TargetModTrades {
    @SubscribeEvent
    public static void registerTrades(VillagerTradesEvent event) {
        if (event.getType() == VillagerProfession.WEAPONSMITH) {
            event.getTrades().get(1).add(new BasicItemListing(new ItemStack(ItemRegistry.TASER_BLUEPRINT.get()),

                    new ItemStack(Items.EMERALD), 16, 5, 0.05f));
            event.getTrades().get(2).add(new BasicItemListing(new ItemStack(Items.EMERALD, 10),

                    new ItemStack(ItemRegistry.STEEL_ACTION.get()), 12, 5, 0.05f));
            event.getTrades().get(4).add(new BasicItemListing(new ItemStack(Items.EMERALD, 32),

                    new ItemStack(ItemRegistry.M_4_BLUEPRINT.get()), 10, 50, 0.05f));
            event.getTrades().get(4).add(new BasicItemListing(new ItemStack(Items.EMERALD, 32),

                    new ItemStack(ItemRegistry.M_79_BLUEPRINT.get()), 10, 50, 0.05f));
            event.getTrades().get(4).add(new BasicItemListing(new ItemStack(Items.EMERALD, 32),

                    new ItemStack(ItemRegistry.MARLIN_BLUEPRINT.get()), 10, 50, 0.05f));
            event.getTrades().get(4).add(new BasicItemListing(new ItemStack(Items.EMERALD, 32),

                    new ItemStack(ItemRegistry.AK_47_BLUEPRINT.get()), 10, 50, 0.05f));
            event.getTrades().get(5).add(new BasicItemListing(new ItemStack(Items.EMERALD, 64),

                    new ItemStack(ItemRegistry.HUNTING_RIFLE_BLUEPRINT.get()), 10, 80, 0.05f));
            event.getTrades().get(5).add(new BasicItemListing(new ItemStack(Items.EMERALD, 64),

                    new ItemStack(ItemRegistry.RPG_BLUEPRINT.get()), 10, 80, 0.05f));
            event.getTrades().get(5).add(new BasicItemListing(new ItemStack(Items.EMERALD, 64),

                    new ItemStack(ItemRegistry.HK_416_BLUEPRINT.get()), 10, 80, 0.05f));
            event.getTrades().get(5).add(new BasicItemListing(new ItemStack(Items.EMERALD, 64),

                    new ItemStack(ItemRegistry.RPK_BLUEPRINT.get()), 10, 80, 0.05f));
            event.getTrades().get(5).add(new BasicItemListing(new ItemStack(Items.EMERALD, 64),

                    new ItemStack(ItemRegistry.VECTOR_BLUEPRINT.get()), 10, 80, 0.05f));
            event.getTrades().get(5).add(new BasicItemListing(new ItemStack(Items.EMERALD, 64),

                    new ItemStack(ItemRegistry.MK_14_BLUEPRINT.get()), 10, 80, 0.05f));
            event.getTrades().get(5).add(new BasicItemListing(new ItemStack(Items.EMERALD, 64),

                    new ItemStack(ItemRegistry.M_60_BLUEPRINT.get()), 10, 80, 0.05f));
            event.getTrades().get(5).add(new BasicItemListing(new ItemStack(Items.EMERALD, 64),

                    new ItemStack(ItemRegistry.SVD_BLUEPRINT.get()), 10, 80, 0.05f));
            event.getTrades().get(5).add(new BasicItemListing(new ItemStack(Items.EMERALD, 64),

                    new ItemStack(ItemRegistry.M_870_BLUEPRINT.get()), 10, 80, 0.05f));
            event.getTrades().get(5).add(new BasicItemListing(new ItemStack(Items.EMERALD, 64),

                    new ItemStack(ItemRegistry.M_98B_BLUEPRINT.get()), 10, 80, 0.05f));
            event.getTrades().get(5).add(new BasicItemListing(new ItemStack(Items.EMERALD),

                    new ItemStack(ItemRegistry.DEVOTION_BLUEPRINT.get()), 10, 80, 0.05f));
            event.getTrades().get(2).add(new BasicItemListing(new ItemStack(Items.EMERALD, 8),

                    new ItemStack(ItemRegistry.STEEL_BARREL.get()), 12, 5, 0.05f));
            event.getTrades().get(2).add(new BasicItemListing(new ItemStack(Items.EMERALD, 6),

                    new ItemStack(ItemRegistry.STEEL_TRIGGER.get()), 12, 5, 0.05f));
            event.getTrades().get(2).add(new BasicItemListing(new ItemStack(Items.EMERALD, 8),

                    new ItemStack(ItemRegistry.STEEL_SPRING.get()), 12, 5, 0.05f));
            event.getTrades().get(3).add(new BasicItemListing(new ItemStack(Items.EMERALD, 16),

                    new ItemStack(ItemRegistry.CEMENTED_CARBIDE_BARREL.get()), 12, 10, 0.05f));
            event.getTrades().get(3).add(new BasicItemListing(new ItemStack(Items.EMERALD, 20),

                    new ItemStack(ItemRegistry.CEMENTED_CARBIDE_ACTION.get()), 10, 10, 0.05f));
            event.getTrades().get(3).add(new BasicItemListing(new ItemStack(Items.EMERALD, 16),

                    new ItemStack(ItemRegistry.CEMENTED_CARBIDE_SPRING.get()), 10, 10, 0.05f));
            event.getTrades().get(3).add(new BasicItemListing(new ItemStack(Items.EMERALD, 12),

                    new ItemStack(ItemRegistry.CEMENTED_CARBIDE_TRIGGER.get()), 10, 10, 0.05f));
        }
    }
}
