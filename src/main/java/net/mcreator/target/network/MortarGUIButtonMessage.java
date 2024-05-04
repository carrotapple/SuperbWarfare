
package net.mcreator.target.network;

import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.core.BlockPos;

import net.mcreator.target.world.inventory.MortarGUIMenu;
import net.mcreator.target.procedures.AngleReduceProcedure;
import net.mcreator.target.procedures.AngleReducePlusProcedure;
import net.mcreator.target.procedures.AngleReduceMiniProcedure;
import net.mcreator.target.procedures.AngleAddProcedure;
import net.mcreator.target.procedures.AngleAddPlusProcedure;
import net.mcreator.target.procedures.AngleAddMiniProcedure;
import net.mcreator.target.TargetMod;

import java.util.function.Supplier;
import java.util.HashMap;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class MortarGUIButtonMessage {
	private final int buttonID, x, y, z;

	public MortarGUIButtonMessage(FriendlyByteBuf buffer) {
		this.buttonID = buffer.readInt();
		this.x = buffer.readInt();
		this.y = buffer.readInt();
		this.z = buffer.readInt();
	}

	public MortarGUIButtonMessage(int buttonID, int x, int y, int z) {
		this.buttonID = buttonID;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public static void buffer(MortarGUIButtonMessage message, FriendlyByteBuf buffer) {
		buffer.writeInt(message.buttonID);
		buffer.writeInt(message.x);
		buffer.writeInt(message.y);
		buffer.writeInt(message.z);
	}

	public static void handler(MortarGUIButtonMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
		NetworkEvent.Context context = contextSupplier.get();
		context.enqueueWork(() -> {
			Player entity = context.getSender();
			int buttonID = message.buttonID;
			int x = message.x;
			int y = message.y;
			int z = message.z;
			handleButtonAction(entity, buttonID, x, y, z);
		});
		context.setPacketHandled(true);
	}

	public static void handleButtonAction(Player entity, int buttonID, int x, int y, int z) {
		Level world = entity.level();
		HashMap guistate = MortarGUIMenu.guistate;
		// security measure to prevent arbitrary chunk generation
		if (!world.hasChunkAt(new BlockPos(x, y, z)))
			return;
		if (buttonID == 0) {

			AngleAddProcedure.execute(entity);
		}
		if (buttonID == 1) {

			AngleReduceProcedure.execute(entity);
		}
		if (buttonID == 2) {

			AngleAddPlusProcedure.execute(entity);
		}
		if (buttonID == 3) {

			AngleReducePlusProcedure.execute(entity);
		}
		if (buttonID == 4) {

			AngleReduceMiniProcedure.execute(entity);
		}
		if (buttonID == 5) {

			AngleAddMiniProcedure.execute(entity);
		}
	}

	@SubscribeEvent
	public static void registerMessage(FMLCommonSetupEvent event) {
		TargetMod.addNetworkMessage(MortarGUIButtonMessage.class, MortarGUIButtonMessage::buffer, MortarGUIButtonMessage::new, MortarGUIButtonMessage::handler);
	}
}
