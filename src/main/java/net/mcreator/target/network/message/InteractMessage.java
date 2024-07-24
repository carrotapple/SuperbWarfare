package net.mcreator.target.network.message;

import net.mcreator.target.init.TargetModTags;
import net.mcreator.target.tools.TraceTool;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class InteractMessage {
    private final int type;

    public InteractMessage(int type) {
        this.type = type;
    }

    public static InteractMessage decode(FriendlyByteBuf buffer) {
        return new InteractMessage(buffer.readInt());
    }

    public static void encode(InteractMessage message, FriendlyByteBuf buffer) {
        buffer.writeInt(message.type);
    }

    public static void handler(InteractMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            if (context.getSender() != null) {
                pressAction(context.getSender(), message.type);
            }
        });
        context.setPacketHandled(true);
    }

    public static void pressAction(Player player, int type) {
        Level level = player.level();

        if (!level.isLoaded(player.blockPosition()))
            return;
        if (type == 0) {
            if (player.getMainHandItem().is(TargetModTags.Items.GUN)) {
                double block_range = player.getBlockReach();
                double entity_range = player.getBlockReach();

                Vec3 looking = Vec3.atLowerCornerOf(player.level().clip(new ClipContext(player.getEyePosition(), player.getEyePosition().add(player.getLookAngle().scale(block_range)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, player)).getBlockPos());
                BlockPos blockPos = BlockPos.containing(looking.x(), looking.y(), looking.z());
                player.level().getBlockState(blockPos).use(player.level(), player, InteractionHand.MAIN_HAND, BlockHitResult.miss(new Vec3(blockPos.getX(), blockPos.getY(), blockPos.getZ()), Direction.UP, blockPos));

                Entity lookingEntity = TraceTool.findLookingEntity(player, entity_range);
                if (lookingEntity == null)
                    return;
                player.interactOn(lookingEntity, InteractionHand.MAIN_HAND);
            }
        }
    }
}
