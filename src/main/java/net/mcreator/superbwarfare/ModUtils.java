package net.mcreator.superbwarfare;

import net.mcreator.superbwarfare.init.*;
import net.mcreator.superbwarfare.network.ModVariables;
import net.mcreator.superbwarfare.network.message.*;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

@Mod(ModUtils.MODID)
public class ModUtils {
    public static final String MODID = "superbwarfare";
    public static final String ATTRIBUTE_MODIFIER = "superbwarfare_attribute_modifier";

    public static final Logger LOGGER = LogManager.getLogger(ModUtils.class);

    public ModUtils() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        ModPerks.register(bus);
        ModSounds.REGISTRY.register(bus);
        ModBlocks.REGISTRY.register(bus);
        ModItems.register(bus);
        ModEntities.REGISTRY.register(bus);
        ModTabs.TABS.register(bus);
        ModMobEffects.REGISTRY.register(bus);
        ModParticleTypes.REGISTRY.register(bus);
        ModPotion.POTIONS.register(bus);
        ModMenuTypes.REGISTRY.register(bus);
        ModVillagers.register(bus);

        bus.addListener(this::onCommonSetup);

        MinecraftForge.EVENT_BUS.register(this);
    }

    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel PACKET_HANDLER = NetworkRegistry.newSimpleChannel(new ResourceLocation(MODID, MODID), () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);
    private static int messageID = 0;

    public static <T> void addNetworkMessage(Class<T> messageType, BiConsumer<T, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, T> decoder, BiConsumer<T, Supplier<NetworkEvent.Context>> messageConsumer) {
        PACKET_HANDLER.registerMessage(messageID, messageType, encoder, decoder, messageConsumer);
        messageID++;
    }

    public static <T> void addNetworkMessage(Class<T> messageType, BiConsumer<T, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, T> decoder, BiConsumer<T, Supplier<NetworkEvent.Context>> messageConsumer, Optional<NetworkDirection> direction) {
        PACKET_HANDLER.registerMessage(messageID, messageType, encoder, decoder, messageConsumer, direction);
        messageID++;
    }

    private static final Collection<AbstractMap.SimpleEntry<Runnable, Integer>> workQueue = new ConcurrentLinkedQueue<>();

    public static void queueServerWork(int tick, Runnable action) {
        workQueue.add(new AbstractMap.SimpleEntry<>(action, tick));
    }

    @SubscribeEvent
    public void tick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            List<AbstractMap.SimpleEntry<Runnable, Integer>> actions = new ArrayList<>();
            workQueue.forEach(work -> {
                work.setValue(work.getValue() - 1);
                if (work.getValue() == 0)
                    actions.add(work);
            });
            actions.forEach(e -> e.getKey().run());
            workQueue.removeAll(actions);
        }
    }

    public void onCommonSetup(final FMLCommonSetupEvent event) {
        addNetworkMessage(ZoomMessage.class, ZoomMessage::encode, ZoomMessage::decode, ZoomMessage::handler);
        addNetworkMessage(DoubleJumpMessage.class, DoubleJumpMessage::encode, DoubleJumpMessage::decode, DoubleJumpMessage::handler);
        addNetworkMessage(GunsDataMessage.class, GunsDataMessage::encode, GunsDataMessage::decode, GunsDataMessage::handler, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        addNetworkMessage(FireMessage.class, FireMessage::encode, FireMessage::new, FireMessage::handler);
        addNetworkMessage(VehicleFireMessage.class, VehicleFireMessage::encode, VehicleFireMessage::new, VehicleFireMessage::handler);
        addNetworkMessage(FireModeMessage.class, FireModeMessage::encode, FireModeMessage::new, FireModeMessage::handler);
        addNetworkMessage(ReloadMessage.class, ReloadMessage::encode, ReloadMessage::decode, ReloadMessage::handler);
        addNetworkMessage(PlayerGunKillMessage.class, PlayerGunKillMessage::encode, PlayerGunKillMessage::decode, PlayerGunKillMessage::handler, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        addNetworkMessage(ClientIndicatorMessage.class, ClientIndicatorMessage::encode, ClientIndicatorMessage::decode, ClientIndicatorMessage::handler, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        addNetworkMessage(SensitivityMessage.class, SensitivityMessage::encode, SensitivityMessage::decode, SensitivityMessage::handler);
        addNetworkMessage(AdjustZoomFovMessage.class, AdjustZoomFovMessage::encode, AdjustZoomFovMessage::decode, AdjustZoomFovMessage::handler);
        addNetworkMessage(AdjustMortarAngleMessage.class, AdjustMortarAngleMessage::encode, AdjustMortarAngleMessage::decode, AdjustMortarAngleMessage::handler);
        addNetworkMessage(InteractMessage.class, InteractMessage::encode, InteractMessage::decode, InteractMessage::handler);
        addNetworkMessage(DroneMovementMessage.class, DroneMovementMessage::encode, DroneMovementMessage::decode, DroneMovementMessage::handler);
        addNetworkMessage(DroneFireMessage.class, DroneFireMessage::encode, DroneFireMessage::new, DroneFireMessage::handler);
        addNetworkMessage(SimulationDistanceMessage.class, SimulationDistanceMessage::encode, SimulationDistanceMessage::decode, SimulationDistanceMessage::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        addNetworkMessage(GunReforgeMessage.class, GunReforgeMessage::encode, GunReforgeMessage::decode, GunReforgeMessage::handler);
        addNetworkMessage(SetPerkLevelMessage.class, SetPerkLevelMessage::encode, SetPerkLevelMessage::decode, SetPerkLevelMessage::handler);
        addNetworkMessage(BreathMessage.class, BreathMessage::encode, BreathMessage::decode, BreathMessage::handler);
        addNetworkMessage(ModVariables.SavedDataSyncMessage.class, ModVariables.SavedDataSyncMessage::buffer, ModVariables.SavedDataSyncMessage::new, ModVariables.SavedDataSyncMessage::handler);
        addNetworkMessage(ModVariables.PlayerVariablesSyncMessage.class, ModVariables.PlayerVariablesSyncMessage::buffer, ModVariables.PlayerVariablesSyncMessage::new, ModVariables.PlayerVariablesSyncMessage::handler);
        addNetworkMessage(ModVariables.PlayerVariablesSyncMessage.class, ModVariables.PlayerVariablesSyncMessage::buffer, ModVariables.PlayerVariablesSyncMessage::new, ModVariables.PlayerVariablesSyncMessage::handler);

        event.enqueueWork(() -> BrewingRecipeRegistry.addRecipe(Ingredient.of(PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.WATER)),
                Ingredient.of(Items.LIGHTNING_ROD), PotionUtils.setPotion(new ItemStack(Items.POTION), ModPotion.SHOCK.get())));
        event.enqueueWork(() -> BrewingRecipeRegistry.addRecipe(Ingredient.of(PotionUtils.setPotion(new ItemStack(Items.POTION), ModPotion.SHOCK.get())),
                Ingredient.of(Items.REDSTONE), PotionUtils.setPotion(new ItemStack(Items.POTION), ModPotion.LONG_SHOCK.get())));
        event.enqueueWork(() -> BrewingRecipeRegistry.addRecipe(Ingredient.of(PotionUtils.setPotion(new ItemStack(Items.POTION), ModPotion.SHOCK.get())),
                Ingredient.of(Items.GLOWSTONE_DUST), PotionUtils.setPotion(new ItemStack(Items.POTION), ModPotion.STRONG_SHOCK.get())));
    }
}
