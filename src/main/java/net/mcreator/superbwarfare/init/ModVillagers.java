package net.mcreator.superbwarfare.init;

import com.google.common.collect.ImmutableSet;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.mcreator.superbwarfare.ModUtils;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

@Mod.EventBusSubscriber(modid = ModUtils.MODID)
public class ModVillagers {
    public static final DeferredRegister<PoiType> POI_TYPES = DeferredRegister.create(ForgeRegistries.POI_TYPES, ModUtils.MODID);
    public static final DeferredRegister<VillagerProfession> VILLAGER_PROFESSIONS = DeferredRegister.create(ForgeRegistries.VILLAGER_PROFESSIONS, ModUtils.MODID);

    public static final RegistryObject<PoiType> ARMS_DEALER_POI = POI_TYPES.register("arms_dealer",
            () -> new PoiType(ImmutableSet.copyOf(ModBlocks.REFORGING_TABLE.get().getStateDefinition().getPossibleStates()), 1, 1));

    public static final RegistryObject<VillagerProfession> ARMS_DEALER = VILLAGER_PROFESSIONS.register("arms_dealer",
            () -> new VillagerProfession("arms_dealer", holder -> holder.get() == ARMS_DEALER_POI.get(), holder -> holder.get() == ARMS_DEALER_POI.get(),
                    ImmutableSet.of(), ImmutableSet.of(), null));

    public static void register(IEventBus eventBus) {
        POI_TYPES.register(eventBus);
        VILLAGER_PROFESSIONS.register(eventBus);
    }

    @SubscribeEvent
    public static void addCustomTrades(VillagerTradesEvent event) {
        if (event.getType() == ModVillagers.ARMS_DEALER.get()) {
            Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();

//            trades.get(1).add(((pTrader, pRandom) -> new MerchantOffer(new ItemStack(ItemRegistry.RED_AHOGE.get(), 5),
//                    new ItemStack(Items.EMERALD, 1), 10, 2, 0.05f)));
//            trades.get(1).add(((pTrader, pRandom) -> new MerchantOffer(new ItemStack(ItemRegistry.WHITE_AHOGE.get(), 5),
//                    new ItemStack(Items.EMERALD, 1), 10, 2, 0.05f)));
        }
    }

}
