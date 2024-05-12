package net.mcreator.target.network;

import net.mcreator.target.TargetMod;
import net.mcreator.target.procedures.BowlooseProcedure;
import net.mcreator.target.procedures.PressFireProcedure;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class FireMessage {
    int type, pressedms;

    public FireMessage(int type, int pressedms) {
        this.type = type;
        this.pressedms = pressedms;
    }

    public FireMessage(FriendlyByteBuf buffer) {
        this.type = buffer.readInt();
        this.pressedms = buffer.readInt();
    }

    public static void buffer(FireMessage message, FriendlyByteBuf buffer) {
        buffer.writeInt(message.type);
        buffer.writeInt(message.pressedms);
    }

    public static void handler(FireMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> pressAction(context.getSender(), message.type));
        context.setPacketHandled(true);
    }

    public static void pressAction(Player player, int type) {
        Level world = player.level();
        // security measure to prevent arbitrary chunk generation
        if (!world.hasChunkAt(player.blockPosition()))
            return;
        if (type == 0) {
            PressFireProcedure.execute(player);
        } else if (type == 1) {
            player.getPersistentData().putDouble("firing", 0);
            player.getPersistentData().putDouble("minifiring", 0);
            player.getPersistentData().putDouble("minigunfiring", 0);
            player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                capability.bowPullHold = false;
                capability.syncPlayerVariables(player);
            });
            BowlooseProcedure.execute(player);
        }
    }

    @SubscribeEvent
    public static void registerMessage(FMLCommonSetupEvent event) {
        TargetMod.addNetworkMessage(FireMessage.class, FireMessage::buffer, FireMessage::new, FireMessage::handler);
    }
}
