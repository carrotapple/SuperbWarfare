package net.mcreator.target;

import net.mcreator.target.init.*;
import net.mcreator.target.network.message.*;
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

@Mod("target")
public class TargetMod {
    public static final String MODID = "target";
    public static final String ATTRIBUTE_MODIFIER = "target_attribute_modifier";

    public static final Logger LOGGER = LogManager.getLogger(TargetMod.class);

    public TargetMod() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        TargetModSounds.REGISTRY.register(bus);
        TargetModBlocks.REGISTRY.register(bus);
        TargetModItems.register(bus);
        TargetModEntities.REGISTRY.register(bus);
        TargetModTabs.TABS.register(bus);
        TargetModMobEffects.REGISTRY.register(bus);
        TargetModParticleTypes.REGISTRY.register(bus);
        TargetModPotion.POTIONS.register(bus);
        TargetModMenus.REGISTRY.register(bus);
        TargetModEnchantments.REGISTRY.register(bus);

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
        addNetworkMessage(ZoomMessage.class, ZoomMessage::buffer, ZoomMessage::new, ZoomMessage::handler);
        addNetworkMessage(DoubleJumpMessage.class, DoubleJumpMessage::buffer, DoubleJumpMessage::new, DoubleJumpMessage::handler);
        addNetworkMessage(GunsDataMessage.class, GunsDataMessage::encode, GunsDataMessage::decode, GunsDataMessage::handler, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        addNetworkMessage(FireMessage.class, FireMessage::buffer, FireMessage::new, FireMessage::handler);
        addNetworkMessage(FireModeMessage.class, FireModeMessage::buffer, FireModeMessage::new, FireModeMessage::handler);
        addNetworkMessage(GunRecycleGuiButtonMessage.class, GunRecycleGuiButtonMessage::buffer, GunRecycleGuiButtonMessage::new, GunRecycleGuiButtonMessage::handler);
        addNetworkMessage(ReloadMessage.class, ReloadMessage::encode, ReloadMessage::decode, ReloadMessage::handler);
        addNetworkMessage(PlayerGunKillMessage.class, PlayerGunKillMessage::encode, PlayerGunKillMessage::decode, PlayerGunKillMessage::handler, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        addNetworkMessage(ClientIndicatorMessage.class, ClientIndicatorMessage::encode, ClientIndicatorMessage::decode, ClientIndicatorMessage::handler, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        addNetworkMessage(SensitivityMessage.class, SensitivityMessage::encode, SensitivityMessage::decode, SensitivityMessage::handler);
        addNetworkMessage(AdjustZoomFovMessage.class, AdjustZoomFovMessage::encode, AdjustZoomFovMessage::decode, AdjustZoomFovMessage::handler);
        addNetworkMessage(AdjustMortarAngleMessage.class, AdjustMortarAngleMessage::encode, AdjustMortarAngleMessage::decode, AdjustMortarAngleMessage::handler);

        event.enqueueWork(() -> BrewingRecipeRegistry.addRecipe(Ingredient.of(PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.WATER)),
                Ingredient.of(Items.LIGHTNING_ROD), PotionUtils.setPotion(new ItemStack(Items.POTION), TargetModPotion.SHOCK.get())));
        event.enqueueWork(() -> BrewingRecipeRegistry.addRecipe(Ingredient.of(PotionUtils.setPotion(new ItemStack(Items.POTION), TargetModPotion.SHOCK.get())),
                Ingredient.of(Items.REDSTONE), PotionUtils.setPotion(new ItemStack(Items.POTION), TargetModPotion.LONG_SHOCK.get())));
        event.enqueueWork(() -> BrewingRecipeRegistry.addRecipe(Ingredient.of(PotionUtils.setPotion(new ItemStack(Items.POTION), TargetModPotion.SHOCK.get())),
                Ingredient.of(Items.GLOWSTONE_DUST), PotionUtils.setPotion(new ItemStack(Items.POTION), TargetModPotion.STRONG_SHOCK.get())));
    }
}
