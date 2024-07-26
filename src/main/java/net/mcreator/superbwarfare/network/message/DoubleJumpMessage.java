package net.mcreator.superbwarfare.network.message;

import net.mcreator.superbwarfare.init.ModSounds;
import net.mcreator.superbwarfare.network.TargetModVariables;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class DoubleJumpMessage {
    private final int type;

    public DoubleJumpMessage(int type) {
        this.type = type;
    }

    public DoubleJumpMessage(FriendlyByteBuf buffer) {
        this.type = buffer.readInt();
    }

    public static void buffer(DoubleJumpMessage message, FriendlyByteBuf buffer) {
        buffer.writeInt(message.type);
    }

    public static void handler(DoubleJumpMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            if (context.getSender() != null) {
                pressAction(context.getSender(), message.type);
            }
        });
        context.setPacketHandled(true);
    }

    public static void pressAction(Player entity, int type) {
        Level level = entity.level();
        double x = entity.getX();
        double y = entity.getY();
        double z = entity.getZ();

        if (!level.hasChunkAt(entity.blockPosition())) {
            return;
        }

        if (type == 0) {
            if ((entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).playerDoubleJump) {
                entity.setDeltaMovement(new Vec3((1 * entity.getLookAngle().x), 0.8, (1 * entity.getLookAngle().z)));
                if (!level.isClientSide()) {
                    level.playSound(null, BlockPos.containing(x, y, z), ModSounds.DOUBLE_JUMP.get(), SoundSource.BLOCKS, 1, 1);
                } else {
                    level.playLocalSound(x, y, z, ModSounds.DOUBLE_JUMP.get(), SoundSource.BLOCKS, 1, 1, false);
                }
                entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                    capability.playerDoubleJump = false;
                    capability.syncPlayerVariables(entity);
                });
            }
        }
    }
}
