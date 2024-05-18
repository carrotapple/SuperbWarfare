package net.mcreator.target.network.message;

import net.mcreator.target.init.TargetModAttributes;
import net.mcreator.target.tools.TraceTool;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

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
        // security measure to prevent arbitrary chunk generation
        if (!world.hasChunkAt(new BlockPos(x, y, z)))
            return;

        handleButtonAction(entity, buttonID);
    }

    private static void handleButtonAction(Player player, int buttonID) {
        Entity looking = TraceTool.findLookingEntity(player, 6);
        if (looking == null) return;
        if (looking instanceof LivingEntity living) {
            switch (buttonID) {
                case 0 -> {
                    if (living.getAttribute(TargetModAttributes.MORTAR_PITCH.get()).getBaseValue() < 89) {
                        living.getAttribute(TargetModAttributes.MORTAR_PITCH.get()).setBaseValue((living.getAttribute(TargetModAttributes.MORTAR_PITCH.get()).getBaseValue() + 1));
                    }
                }
                case 1 -> {
                    if (living.getAttribute(TargetModAttributes.MORTAR_PITCH.get()).getBaseValue() > 21) {
                        living.getAttribute(TargetModAttributes.MORTAR_PITCH.get()).setBaseValue((living.getAttribute(TargetModAttributes.MORTAR_PITCH.get()).getBaseValue() - 1));
                    }
                }
                case 2 -> {
                    if (living.getAttribute(TargetModAttributes.MORTAR_PITCH.get()).getBaseValue() < 78) {
                        living.getAttribute(TargetModAttributes.MORTAR_PITCH.get()).setBaseValue((living.getAttribute(TargetModAttributes.MORTAR_PITCH.get()).getBaseValue() + 10));
                    }
                }
                case 3 -> {
                    if (living.getAttribute(TargetModAttributes.MORTAR_PITCH.get()).getBaseValue() > 31) {
                        living.getAttribute(TargetModAttributes.MORTAR_PITCH.get()).setBaseValue((living.getAttribute(TargetModAttributes.MORTAR_PITCH.get()).getBaseValue() - 10));
                    }
                }
                case 4 -> {
                    if (living.getAttribute(TargetModAttributes.MORTAR_PITCH.get()).getBaseValue() > 20.5) {
                        living.getAttribute(TargetModAttributes.MORTAR_PITCH.get()).setBaseValue((living.getAttribute(TargetModAttributes.MORTAR_PITCH.get()).getBaseValue() - 0.5));
                    }
                }
                case 5 -> {
                    if (living.getAttribute(TargetModAttributes.MORTAR_PITCH.get()).getBaseValue() < 88.5) {
                        living.getAttribute(TargetModAttributes.MORTAR_PITCH.get()).setBaseValue((living.getAttribute(TargetModAttributes.MORTAR_PITCH.get()).getBaseValue() + 0.5));
                    }
                }
            }
        }
    }
}
