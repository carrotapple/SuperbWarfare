package net.mcreator.superbwarfare.init;

import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.BasicItemListing;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModTrades {
    @SubscribeEvent
    public static void registerTrades(VillagerTradesEvent event) {
        if (event.getType() == VillagerProfession.WEAPONSMITH) {
            event.getTrades().get(1).add(new BasicItemListing(new ItemStack(ModItems.TASER_BLUEPRINT.get()),
                    new ItemStack(Items.EMERALD), 16, 5, 0.05f));

            event.getTrades().get(2).add(new BasicItemListing(new ItemStack(Items.EMERALD, 10),
                    new ItemStack(ModItems.STEEL_ACTION.get()), 12, 5, 0.05f));

            event.getTrades().get(4).add(new BasicItemListing(new ItemStack(Items.EMERALD, 32),
                    new ItemStack(ModItems.M_4_BLUEPRINT.get()), 10, 50, 0.05f));

            event.getTrades().get(4).add(new BasicItemListing(new ItemStack(Items.EMERALD, 32),
                    new ItemStack(ModItems.M_79_BLUEPRINT.get()), 10, 50, 0.05f));

            event.getTrades().get(4).add(new BasicItemListing(new ItemStack(Items.EMERALD, 32),
                    new ItemStack(ModItems.MARLIN_BLUEPRINT.get()), 10, 50, 0.05f));

            event.getTrades().get(4).add(new BasicItemListing(new ItemStack(Items.EMERALD, 32),
                    new ItemStack(ModItems.AK_47_BLUEPRINT.get()), 10, 50, 0.05f));

            event.getTrades().get(5).add(new BasicItemListing(new ItemStack(Items.EMERALD, 64),
                    new ItemStack(ModItems.HUNTING_RIFLE_BLUEPRINT.get()), 10, 80, 0.05f));

            event.getTrades().get(5).add(new BasicItemListing(new ItemStack(Items.EMERALD, 64),
                    new ItemStack(ModItems.RPG_BLUEPRINT.get()), 10, 80, 0.05f));

            event.getTrades().get(5).add(new BasicItemListing(new ItemStack(Items.EMERALD, 64),
                    new ItemStack(ModItems.HK_416_BLUEPRINT.get()), 10, 80, 0.05f));

            event.getTrades().get(5).add(new BasicItemListing(new ItemStack(Items.EMERALD, 64),
                    new ItemStack(ModItems.RPK_BLUEPRINT.get()), 10, 80, 0.05f));

            event.getTrades().get(5).add(new BasicItemListing(new ItemStack(Items.EMERALD, 64),
                    new ItemStack(ModItems.VECTOR_BLUEPRINT.get()), 10, 80, 0.05f));

            event.getTrades().get(5).add(new BasicItemListing(new ItemStack(Items.EMERALD, 64),
                    new ItemStack(ModItems.MK_14_BLUEPRINT.get()), 10, 80, 0.05f));

            event.getTrades().get(5).add(new BasicItemListing(new ItemStack(Items.EMERALD, 64),
                    new ItemStack(ModItems.M_60_BLUEPRINT.get()), 10, 80, 0.05f));

            event.getTrades().get(5).add(new BasicItemListing(new ItemStack(Items.EMERALD, 64),
                    new ItemStack(ModItems.SVD_BLUEPRINT.get()), 10, 80, 0.05f));

            event.getTrades().get(5).add(new BasicItemListing(new ItemStack(Items.EMERALD, 64),
                    new ItemStack(ModItems.M_870_BLUEPRINT.get()), 10, 80, 0.05f));

            event.getTrades().get(5).add(new BasicItemListing(new ItemStack(Items.EMERALD, 64),
                    new ItemStack(ModItems.M_98B_BLUEPRINT.get()), 10, 80, 0.05f));

            event.getTrades().get(5).add(new BasicItemListing(new ItemStack(Items.EMERALD),
                    new ItemStack(ModItems.DEVOTION_BLUEPRINT.get()), 10, 80, 0.05f));

            event.getTrades().get(2).add(new BasicItemListing(new ItemStack(Items.EMERALD, 8),
                    new ItemStack(ModItems.STEEL_BARREL.get()), 12, 5, 0.05f));

            event.getTrades().get(2).add(new BasicItemListing(new ItemStack(Items.EMERALD, 6),
                    new ItemStack(ModItems.STEEL_TRIGGER.get()), 12, 5, 0.05f));

            event.getTrades().get(2).add(new BasicItemListing(new ItemStack(Items.EMERALD, 8),
                    new ItemStack(ModItems.STEEL_SPRING.get()), 12, 5, 0.05f));

            event.getTrades().get(3).add(new BasicItemListing(new ItemStack(Items.EMERALD, 16),
                    new ItemStack(ModItems.CEMENTED_CARBIDE_BARREL.get()), 12, 10, 0.05f));

            event.getTrades().get(3).add(new BasicItemListing(new ItemStack(Items.EMERALD, 20),
                    new ItemStack(ModItems.CEMENTED_CARBIDE_ACTION.get()), 10, 10, 0.05f));

            event.getTrades().get(3).add(new BasicItemListing(new ItemStack(Items.EMERALD, 16),
                    new ItemStack(ModItems.CEMENTED_CARBIDE_SPRING.get()), 10, 10, 0.05f));

            event.getTrades().get(3).add(new BasicItemListing(new ItemStack(Items.EMERALD, 12),
                    new ItemStack(ModItems.CEMENTED_CARBIDE_TRIGGER.get()), 10, 10, 0.05f));
        }
    }
}
