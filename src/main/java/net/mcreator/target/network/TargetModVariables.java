package net.mcreator.target.network;

import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.Capability;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.Direction;
import net.minecraft.client.Minecraft;

import net.mcreator.target.TargetMod;

import java.util.function.Supplier;
import java.util.ArrayList;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class TargetModVariables {
	@SubscribeEvent
	public static void init(FMLCommonSetupEvent event) {
		TargetMod.addNetworkMessage(PlayerVariablesSyncMessage.class, PlayerVariablesSyncMessage::buffer, PlayerVariablesSyncMessage::new, PlayerVariablesSyncMessage::handler);
	}

	@SubscribeEvent
	public static void init(RegisterCapabilitiesEvent event) {
		event.register(PlayerVariables.class);
	}

	@Mod.EventBusSubscriber
	public static class EventBusVariableHandlers {
		@SubscribeEvent
		public static void onPlayerLoggedInSyncPlayerVariables(PlayerEvent.PlayerLoggedInEvent event) {
			if (!event.getEntity().level().isClientSide()) {
				for (Entity entityiterator : new ArrayList<>(event.getEntity().level().players())) {
					((PlayerVariables) entityiterator.getCapability(PLAYER_VARIABLES_CAPABILITY, null).orElse(new PlayerVariables())).syncPlayerVariables(entityiterator);
				}
			}
		}

		@SubscribeEvent
		public static void onPlayerRespawnedSyncPlayerVariables(PlayerEvent.PlayerRespawnEvent event) {
			if (!event.getEntity().level().isClientSide()) {
				for (Entity entityiterator : new ArrayList<>(event.getEntity().level().players())) {
					((PlayerVariables) entityiterator.getCapability(PLAYER_VARIABLES_CAPABILITY, null).orElse(new PlayerVariables())).syncPlayerVariables(entityiterator);
				}
			}
		}

		@SubscribeEvent
		public static void onPlayerChangedDimensionSyncPlayerVariables(PlayerEvent.PlayerChangedDimensionEvent event) {
			if (!event.getEntity().level().isClientSide()) {
				for (Entity entityiterator : new ArrayList<>(event.getEntity().level().players())) {
					((PlayerVariables) entityiterator.getCapability(PLAYER_VARIABLES_CAPABILITY, null).orElse(new PlayerVariables())).syncPlayerVariables(entityiterator);
				}
			}
		}

		@SubscribeEvent
		public static void clonePlayer(PlayerEvent.Clone event) {
			event.getOriginal().revive();
			PlayerVariables original = ((PlayerVariables) event.getOriginal().getCapability(PLAYER_VARIABLES_CAPABILITY, null).orElse(new PlayerVariables()));
			PlayerVariables clone = ((PlayerVariables) event.getEntity().getCapability(PLAYER_VARIABLES_CAPABILITY, null).orElse(new PlayerVariables()));
			clone.zoom = original.zoom;
			clone.zooming = original.zooming;
			clone.recoil = original.recoil;
			clone.recoilhorizon = original.recoilhorizon;
			clone.firing = original.firing;
			clone.targetangle = original.targetangle;
			clone.rifleammo = original.rifleammo;
			clone.refresh = original.refresh;
			clone.handgunammo = original.handgunammo;
			clone.shotgunammo = original.shotgunammo;
			clone.sniperammo = original.sniperammo;
			clone.bowpullhold = original.bowpullhold;
			clone.bowpull = original.bowpull;
			clone.playerdoublejump = original.playerdoublejump;
			clone.hitind = original.hitind;
			clone.headind = original.headind;
			clone.killind = original.killind;
			if (!event.isWasDeath()) {
			}
			if (!event.getEntity().level().isClientSide()) {
				for (Entity entityiterator : new ArrayList<>(event.getEntity().level().players())) {
					((PlayerVariables) entityiterator.getCapability(PLAYER_VARIABLES_CAPABILITY, null).orElse(new PlayerVariables())).syncPlayerVariables(entityiterator);
				}
			}
		}
	}

	public static final Capability<PlayerVariables> PLAYER_VARIABLES_CAPABILITY = CapabilityManager.get(new CapabilityToken<PlayerVariables>() {
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
		public double recoilhorizon = 0;
		public double firing = 0;
		public double targetangle = 0;
		public double rifleammo = 0;
		public boolean refresh = false;
		public double handgunammo = 0;
		public double shotgunammo = 0;
		public double sniperammo = 0;
		public boolean bowpullhold = false;
		public boolean bowpull = false;
		public boolean playerdoublejump = false;
		public double hitind = 0;
		public double headind = 0;
		public double killind = 0;

		public void syncPlayerVariables(Entity entity) {
			if (entity instanceof ServerPlayer serverPlayer)
				TargetMod.PACKET_HANDLER.send(PacketDistributor.DIMENSION.with(entity.level()::dimension), new PlayerVariablesSyncMessage(this, entity.getId()));
		}

		public Tag writeNBT() {
			CompoundTag nbt = new CompoundTag();
			nbt.putBoolean("zoom", zoom);
			nbt.putBoolean("zooming", zooming);
			nbt.putDouble("recoil", recoil);
			nbt.putDouble("recoilhorizon", recoilhorizon);
			nbt.putDouble("firing", firing);
			nbt.putDouble("targetangle", targetangle);
			nbt.putDouble("rifleammo", rifleammo);
			nbt.putBoolean("refresh", refresh);
			nbt.putDouble("handgunammo", handgunammo);
			nbt.putDouble("shotgunammo", shotgunammo);
			nbt.putDouble("sniperammo", sniperammo);
			nbt.putBoolean("bowpullhold", bowpullhold);
			nbt.putBoolean("bowpull", bowpull);
			nbt.putBoolean("playerdoublejump", playerdoublejump);
			nbt.putDouble("hitind", hitind);
			nbt.putDouble("headind", headind);
			nbt.putDouble("killind", killind);
			return nbt;
		}

		public void readNBT(Tag Tag) {
			CompoundTag nbt = (CompoundTag) Tag;
			zoom = nbt.getBoolean("zoom");
			zooming = nbt.getBoolean("zooming");
			recoil = nbt.getDouble("recoil");
			recoilhorizon = nbt.getDouble("recoilhorizon");
			firing = nbt.getDouble("firing");
			targetangle = nbt.getDouble("targetangle");
			rifleammo = nbt.getDouble("rifleammo");
			refresh = nbt.getBoolean("refresh");
			handgunammo = nbt.getDouble("handgunammo");
			shotgunammo = nbt.getDouble("shotgunammo");
			sniperammo = nbt.getDouble("sniperammo");
			bowpullhold = nbt.getBoolean("bowpullhold");
			bowpull = nbt.getBoolean("bowpull");
			playerdoublejump = nbt.getBoolean("playerdoublejump");
			hitind = nbt.getDouble("hitind");
			headind = nbt.getDouble("headind");
			killind = nbt.getDouble("killind");
		}
	}

	@SubscribeEvent
	public static void registerMessage(FMLCommonSetupEvent event) {
		TargetMod.addNetworkMessage(PlayerVariablesSyncMessage.class, PlayerVariablesSyncMessage::buffer, PlayerVariablesSyncMessage::new, PlayerVariablesSyncMessage::handler);
	}

	public static class PlayerVariablesSyncMessage {
		private final int target;
		private final PlayerVariables data;

		public PlayerVariablesSyncMessage(FriendlyByteBuf buffer) {
			this.data = new PlayerVariables();
			this.data.readNBT(buffer.readNbt());
			this.target = buffer.readInt();
		}

		public PlayerVariablesSyncMessage(PlayerVariables data, int entityid) {
			this.data = data;
			this.target = entityid;
		}

		public static void buffer(PlayerVariablesSyncMessage message, FriendlyByteBuf buffer) {
			buffer.writeNbt((CompoundTag) message.data.writeNBT());
			buffer.writeInt(message.target);
		}

		public static void handler(PlayerVariablesSyncMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
			NetworkEvent.Context context = contextSupplier.get();
			context.enqueueWork(() -> {
				if (!context.getDirection().getReceptionSide().isServer()) {
					PlayerVariables variables = ((PlayerVariables) Minecraft.getInstance().player.level().getEntity(message.target).getCapability(PLAYER_VARIABLES_CAPABILITY, null).orElse(new PlayerVariables()));
					variables.zoom = message.data.zoom;
					variables.zooming = message.data.zooming;
					variables.recoil = message.data.recoil;
					variables.recoilhorizon = message.data.recoilhorizon;
					variables.firing = message.data.firing;
					variables.targetangle = message.data.targetangle;
					variables.rifleammo = message.data.rifleammo;
					variables.refresh = message.data.refresh;
					variables.handgunammo = message.data.handgunammo;
					variables.shotgunammo = message.data.shotgunammo;
					variables.sniperammo = message.data.sniperammo;
					variables.bowpullhold = message.data.bowpullhold;
					variables.bowpull = message.data.bowpull;
					variables.playerdoublejump = message.data.playerdoublejump;
					variables.hitind = message.data.hitind;
					variables.headind = message.data.headind;
					variables.killind = message.data.killind;
				}
			});
			context.setPacketHandled(true);
		}
	}
}
