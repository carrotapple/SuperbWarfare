package net.mcreator.superbwarfare.network;

import net.mcreator.superbwarfare.ModUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModVariables {
    @SubscribeEvent
    public static void init(FMLCommonSetupEvent event) {
        ModUtils.addNetworkMessage(SavedDataSyncMessage.class, SavedDataSyncMessage::buffer, SavedDataSyncMessage::new, SavedDataSyncMessage::handler);
        ModUtils.addNetworkMessage(PlayerVariablesSyncMessage.class, PlayerVariablesSyncMessage::buffer, PlayerVariablesSyncMessage::new, PlayerVariablesSyncMessage::handler);
    }

    @SubscribeEvent
    public static void init(RegisterCapabilitiesEvent event) {
        event.register(PlayerVariables.class);
    }

    @Mod.EventBusSubscriber
    public static class EventBusVariableHandlers {
        @SubscribeEvent
        public static void onPlayerLoggedInSyncPlayerVariables(PlayerEvent.PlayerLoggedInEvent event) {
            if (event.getEntity().level().isClientSide()) return;

            for (Entity entity : new ArrayList<>(event.getEntity().level().players())) {
                entity.getCapability(PLAYER_VARIABLES_CAPABILITY, null).orElse(new PlayerVariables()).syncPlayerVariables(entity);
            }
        }

        @SubscribeEvent
        public static void onPlayerRespawnedSyncPlayerVariables(PlayerEvent.PlayerRespawnEvent event) {
            if (event.getEntity().level().isClientSide()) return;

            for (Entity entity : new ArrayList<>(event.getEntity().level().players())) {
                entity.getCapability(PLAYER_VARIABLES_CAPABILITY, null).orElse(new PlayerVariables()).syncPlayerVariables(entity);
            }
        }

        @SubscribeEvent
        public static void onPlayerChangedDimensionSyncPlayerVariables(PlayerEvent.PlayerChangedDimensionEvent event) {
            if (event.getEntity().level().isClientSide()) return;

            for (Entity entity : new ArrayList<>(event.getEntity().level().players())) {
                entity.getCapability(PLAYER_VARIABLES_CAPABILITY, null).orElse(new PlayerVariables()).syncPlayerVariables(entity);
            }
        }

        @SubscribeEvent
        public static void clonePlayer(PlayerEvent.Clone event) {
            event.getOriginal().revive();
            PlayerVariables original = event.getOriginal().getCapability(PLAYER_VARIABLES_CAPABILITY, null).orElse(new PlayerVariables());
            PlayerVariables clone = event.getEntity().getCapability(PLAYER_VARIABLES_CAPABILITY, null).orElse(new PlayerVariables());
            clone.zoom = original.zoom;
            clone.zooming = original.zooming;
            clone.recoil = original.recoil;
            clone.recoilHorizon = original.recoilHorizon;
            clone.firing = original.firing;
            clone.cannonFiring = original.cannonFiring;
            clone.targetAngle = original.targetAngle;
            clone.rifleAmmo = original.rifleAmmo;
            clone.refresh = original.refresh;
            clone.handgunAmmo = original.handgunAmmo;
            clone.shotgunAmmo = original.shotgunAmmo;
            clone.sniperAmmo = original.sniperAmmo;
            clone.bowPullHold = original.bowPullHold;
            clone.bowPull = original.bowPull;
            clone.playerDoubleJump = original.playerDoubleJump;
            clone.tacticalSprint = original.tacticalSprint;
            clone.tacticalSprintTime = original.tacticalSprintTime;
            clone.tacticalSprintExhaustion = original.tacticalSprintExhaustion;

            if (event.getEntity().level().isClientSide()) return;

            for (Entity entity : new ArrayList<>(event.getEntity().level().players())) {
                entity.getCapability(PLAYER_VARIABLES_CAPABILITY, null).orElse(new PlayerVariables()).syncPlayerVariables(entity);
            }
        }

        @SubscribeEvent
        public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
            if (event.getEntity().level().isClientSide()) return;

            SavedData mapData = MapVariables.get(event.getEntity().level());
            SavedData worldData = WorldVariables.get(event.getEntity().level());
            if (mapData != null)
                ModUtils.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) event.getEntity()), new SavedDataSyncMessage(0, mapData));
            if (worldData != null)
                ModUtils.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) event.getEntity()), new SavedDataSyncMessage(1, worldData));
        }

        @SubscribeEvent
        public static void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
            if (event.getEntity().level().isClientSide()) return;

            SavedData worldData = WorldVariables.get(event.getEntity().level());
            if (worldData != null)
                ModUtils.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) event.getEntity()), new SavedDataSyncMessage(1, worldData));
        }
    }

    public static class WorldVariables extends SavedData {
        public static final String DATA_NAME = "target_world_variables";

        public static WorldVariables load(CompoundTag tag) {
            WorldVariables data = new WorldVariables();
            data.read(tag);
            return data;
        }

        public void read(CompoundTag nbt) {
        }

        @Override
        public CompoundTag save(CompoundTag nbt) {
            return nbt;
        }

        public void syncData(LevelAccessor world) {
            this.setDirty();
            if (world instanceof Level level && !level.isClientSide())
                ModUtils.PACKET_HANDLER.send(PacketDistributor.DIMENSION.with(level::dimension), new SavedDataSyncMessage(1, this));
        }

        static WorldVariables clientSide = new WorldVariables();

        public static WorldVariables get(LevelAccessor world) {
            if (world instanceof ServerLevel level)
                return level.getDataStorage().computeIfAbsent(WorldVariables::load, WorldVariables::new, DATA_NAME);
            return clientSide;
        }
    }

    public static class MapVariables extends SavedData {
        public static final String DATA_NAME = "target_map_variables";
        public boolean pvpMode = false;

        public static MapVariables load(CompoundTag tag) {
            MapVariables data = new MapVariables();
            data.read(tag);
            return data;
        }

        public static MapVariables get(LevelAccessor world) {
            if (world instanceof ServerLevelAccessor serverLevelAcc) {
                var level = serverLevelAcc.getLevel().getServer().getLevel(Level.OVERWORLD);
                if (level != null) {
                    return level.getDataStorage().computeIfAbsent(MapVariables::load, MapVariables::new, DATA_NAME);
                }
            }
            return clientSide;
        }

        public void read(CompoundTag nbt) {
            pvpMode = nbt.getBoolean("pvp_mode");
        }

        public void syncData(LevelAccessor world) {
            this.setDirty();
            if (world instanceof Level && !world.isClientSide())
                ModUtils.PACKET_HANDLER.send(PacketDistributor.ALL.noArg(), new SavedDataSyncMessage(0, this));
        }

        static MapVariables clientSide = new MapVariables();

        @Override
        public CompoundTag save(CompoundTag nbt) {
            nbt.putBoolean("pvp_mode", pvpMode);
            return nbt;
        }
    }

    public static class SavedDataSyncMessage {
        private final int type;
        private SavedData data;

        public SavedDataSyncMessage(FriendlyByteBuf buffer) {
            this.type = buffer.readInt();
            CompoundTag nbt = buffer.readNbt();
            if (nbt == null) return;

            this.data = this.type == 0 ? new MapVariables() : new WorldVariables();
            if (this.data instanceof MapVariables mapVariables)
                mapVariables.read(nbt);
            else if (this.data instanceof WorldVariables worldVariables)
                worldVariables.read(nbt);
        }

        public SavedDataSyncMessage(int type, SavedData data) {
            this.type = type;
            this.data = data;
        }

        public static void buffer(SavedDataSyncMessage message, FriendlyByteBuf buffer) {
            buffer.writeInt(message.type);
            if (message.data != null)
                buffer.writeNbt(message.data.save(new CompoundTag()));
        }

        public static void handler(SavedDataSyncMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
            NetworkEvent.Context context = contextSupplier.get();
            context.enqueueWork(() -> {
                if (!context.getDirection().getReceptionSide().isServer() && message.data != null) {
                    if (message.type == 0)
                        MapVariables.clientSide = (MapVariables) message.data;
                    else
                        WorldVariables.clientSide = (WorldVariables) message.data;
                }
            });
            context.setPacketHandled(true);
        }
    }

    public static final Capability<PlayerVariables> PLAYER_VARIABLES_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {
    });

    @Mod.EventBusSubscriber
    private static class PlayerVariablesProvider implements ICapabilitySerializable<Tag> {
        @SubscribeEvent
        public static void onAttachCapabilities(AttachCapabilitiesEvent<Entity> event) {
            if (event.getObject() instanceof Player && !(event.getObject() instanceof FakePlayer))
                event.addCapability(new ResourceLocation("target", "player_variables"), new PlayerVariablesProvider());
        }

        private final PlayerVariables playerVariables = new PlayerVariables();
        private final LazyOptional<PlayerVariables> instance = LazyOptional.of(() -> playerVariables);

        @Override
        public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
            return cap == PLAYER_VARIABLES_CAPABILITY ? instance.cast() : LazyOptional.empty();
        }

        @Override
        public Tag serializeNBT() {
            return playerVariables.writeNBT();
        }

        @Override
        public void deserializeNBT(Tag nbt) {
            playerVariables.readNBT(nbt);
        }
    }

    public static class PlayerVariables {
        public boolean zoom = false;
        public boolean zooming = false;
        public double recoil = 0;
        public double recoilHorizon = 0;
        public double firing = 0;
        public double cannonFiring = 0;
        public int cannonRecoil = 0;
        public double targetAngle = 0;
        public boolean refresh = false;
        public int rifleAmmo = 0;
        public int handgunAmmo = 0;
        public int shotgunAmmo = 0;
        public int sniperAmmo = 0;
        public boolean bowPullHold = false;
        public boolean bowPull = false;
        public boolean playerDoubleJump = false;
        public boolean tacticalSprint = false;
        public int tacticalSprintTime = 600;
        public boolean tacticalSprintExhaustion = false;

        public void syncPlayerVariables(Entity entity) {
            if (entity instanceof ServerPlayer)
                ModUtils.PACKET_HANDLER.send(PacketDistributor.DIMENSION.with(entity.level()::dimension), new PlayerVariablesSyncMessage(this, entity.getId()));
        }

        public Tag writeNBT() {
            CompoundTag nbt = new CompoundTag();
            nbt.putBoolean("zoom", zoom);
            nbt.putBoolean("zooming", zooming);
            nbt.putDouble("recoil", recoil);
            nbt.putDouble("recoil_horizon", recoilHorizon);
            nbt.putDouble("firing", firing);
            nbt.putDouble("cannonFiring", cannonFiring);
            nbt.putInt("cannonRecoil", cannonRecoil);
            nbt.putDouble("target_angle", targetAngle);
            nbt.putInt("rifle_ammo", rifleAmmo);
            nbt.putBoolean("refresh", refresh);
            nbt.putInt("handgun_ammo", handgunAmmo);
            nbt.putInt("shotgun_ammo", shotgunAmmo);
            nbt.putInt("sniper_ammo", sniperAmmo);
            nbt.putBoolean("bow_pull_hold", bowPullHold);
            nbt.putBoolean("bow_pull", bowPull);
            nbt.putBoolean("player_double_jump", playerDoubleJump);
            nbt.putBoolean("tacticalSprint", tacticalSprint);
            nbt.putInt("tacticalSprintTime", tacticalSprintTime);
            nbt.putBoolean("tacticalSprintExhaustion", tacticalSprintExhaustion);

            return nbt;
        }

        public void readNBT(Tag Tag) {
            CompoundTag nbt = (CompoundTag) Tag;
            zoom = nbt.getBoolean("zoom");
            zooming = nbt.getBoolean("zooming");
            recoil = nbt.getDouble("recoil");
            recoilHorizon = nbt.getDouble("recoil_horizon");
            firing = nbt.getDouble("firing");
            cannonFiring = nbt.getDouble("cannonFiring");
            cannonRecoil = nbt.getInt("cannonRecoil");
            targetAngle = nbt.getDouble("target_angle");
            rifleAmmo = nbt.getInt("rifle_ammo");
            refresh = nbt.getBoolean("refresh");
            handgunAmmo = nbt.getInt("handgun_ammo");
            shotgunAmmo = nbt.getInt("shotgun_ammo");
            sniperAmmo = nbt.getInt("sniper_ammo");
            bowPullHold = nbt.getBoolean("bow_pull_hold");
            bowPull = nbt.getBoolean("bow_pull");
            playerDoubleJump = nbt.getBoolean("player_double_jump");
            tacticalSprint = nbt.getBoolean("tacticalSprint");
            tacticalSprintTime = nbt.getInt("tacticalSprintTime");
            tacticalSprintExhaustion = nbt.getBoolean("tacticalSprintExhaustion");
        }
    }

    @SubscribeEvent
    public static void registerMessage(FMLCommonSetupEvent event) {
        ModUtils.addNetworkMessage(PlayerVariablesSyncMessage.class, PlayerVariablesSyncMessage::buffer, PlayerVariablesSyncMessage::new, PlayerVariablesSyncMessage::handler);
    }

    public static class PlayerVariablesSyncMessage {
        private final int target;
        private final PlayerVariables data;

        public PlayerVariablesSyncMessage(FriendlyByteBuf buffer) {
            this.data = new PlayerVariables();
            this.data.readNBT(buffer.readNbt());
            this.target = buffer.readInt();
        }

        public PlayerVariablesSyncMessage(PlayerVariables data, int entityId) {
            this.data = data;
            this.target = entityId;
        }

        public static void buffer(PlayerVariablesSyncMessage message, FriendlyByteBuf buffer) {
            buffer.writeNbt((CompoundTag) message.data.writeNBT());
            buffer.writeInt(message.target);
        }

        public static void handler(PlayerVariablesSyncMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
            NetworkEvent.Context context = contextSupplier.get();
            context.enqueueWork(() -> {
                context.setPacketHandled(true);
                if (context.getDirection().getReceptionSide().isServer() || Minecraft.getInstance().player == null) {
                    return;
                }

                var entity = Minecraft.getInstance().player.level().getEntity(message.target);
                if (entity == null) {
                    return;
                }

                PlayerVariables variables = entity.getCapability(PLAYER_VARIABLES_CAPABILITY, null).orElse(new PlayerVariables());
                variables.zoom = message.data.zoom;
                variables.zooming = message.data.zooming;
                variables.recoil = message.data.recoil;
                variables.recoilHorizon = message.data.recoilHorizon;
                variables.firing = message.data.firing;
                variables.cannonFiring = message.data.cannonFiring;
                variables.cannonRecoil = message.data.cannonRecoil;
                variables.targetAngle = message.data.targetAngle;
                variables.rifleAmmo = message.data.rifleAmmo;
                variables.refresh = message.data.refresh;
                variables.handgunAmmo = message.data.handgunAmmo;
                variables.shotgunAmmo = message.data.shotgunAmmo;
                variables.sniperAmmo = message.data.sniperAmmo;
                variables.bowPullHold = message.data.bowPullHold;
                variables.bowPull = message.data.bowPull;
                variables.playerDoubleJump = message.data.playerDoubleJump;
                variables.tacticalSprint = message.data.tacticalSprint;
                variables.tacticalSprintTime = message.data.tacticalSprintTime;
                variables.tacticalSprintExhaustion = message.data.tacticalSprintExhaustion;
            });
        }
    }
}
