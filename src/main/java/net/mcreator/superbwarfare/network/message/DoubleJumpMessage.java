package net.mcreator.superbwarfare.network.message;

import net.mcreator.superbwarfare.init.ModSounds;
import net.mcreator.superbwarfare.network.ModVariables;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class DoubleJumpMessage {
    private final int type;

    public DoubleJumpMessage(int type) {
        this.type = type;
    }

    public static DoubleJumpMessage decode(FriendlyByteBuf buffer) {
        return new DoubleJumpMessage(buffer.readInt());
    }

    public static void encode(DoubleJumpMessage message, FriendlyByteBuf buffer) {
        buffer.writeInt(message.type);
    }

    public static void handler(DoubleJumpMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();

            if (player != null) {
                Level level = player.level();
                double x = player.getX();
                double y = player.getY();
                double z = player.getZ();

                if (!level.isLoaded(player.blockPosition())) {
                    return;
                }

                if (message.type == 0) {
                    if (player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables()).playerDoubleJump) {
                        player.setDeltaMovement(new Vec3(player.getLookAngle().x, 0.8, player.getLookAngle().z));
                        if (!level.isClientSide()) {
                            level.playSound(null, BlockPos.containing(x, y, z), ModSounds.DOUBLE_JUMP.get(), SoundSource.BLOCKS, 1, 1);
                        } else {
                            level.playLocalSound(x, y, z, ModSounds.DOUBLE_JUMP.get(), SoundSource.BLOCKS, 1, 1, false);
                        }

                        player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                            capability.playerDoubleJump = false;
                            capability.syncPlayerVariables(player);
                        });
                    }
                }
            }
        });
        context.setPacketHandled(true);
    }
}
