package net.mcreator.superbwarfare.block;

import net.mcreator.superbwarfare.entity.Mk42Entity;
import net.mcreator.superbwarfare.entity.Target1Entity;
import net.mcreator.superbwarfare.init.TargetModSounds;
import net.mcreator.superbwarfare.network.TargetModVariables;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class JumpPadBlock extends Block {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    public JumpPadBlock() {
        super(BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.BASEDRUM).sound(SoundType.STONE).strength(-1, 3600000).noCollission().noOcclusion().isRedstoneConductor((bs, br, bp) -> false));
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }


    @Override
    public boolean propagatesSkylightDown(BlockState state, BlockGetter reader, BlockPos pos) {
        return true;
    }

    @Override
    public VoxelShape getVisualShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return Shapes.empty();
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return switch (state.getValue(FACING)) {
            default ->
                    Shapes.or(box(0, 0, 0, 16, 3, 16), box(-0.25, -0.1, -0.25, 2, 3.25, 2), box(14, -0.1, -0.25, 16.25, 3.25, 2), box(14, -0.1, 14, 16.25, 3.25, 16.25), box(-0.25, -0.1, 14, 2, 3.25, 16.25), box(1, 3, 1, 15, 4, 15));
            case NORTH ->
                    Shapes.or(box(0, 0, 0, 16, 3, 16), box(14, -0.1, 14, 16.25, 3.25, 16.25), box(-0.25, -0.1, 14, 2, 3.25, 16.25), box(-0.25, -0.1, -0.25, 2, 3.25, 2), box(14, -0.1, -0.25, 16.25, 3.25, 2), box(1, 3, 1, 15, 4, 15));
            case EAST ->
                    Shapes.or(box(0, 0, 0, 16, 3, 16), box(-0.25, -0.1, 14, 2, 3.25, 16.25), box(-0.25, -0.1, -0.25, 2, 3.25, 2), box(14, -0.1, -0.25, 16.25, 3.25, 2), box(14, -0.1, 14, 16.25, 3.25, 16.25), box(1, 3, 1, 15, 4, 15));
            case WEST ->
                    Shapes.or(box(0, 0, 0, 16, 3, 16), box(14, -0.1, -0.25, 16.25, 3.25, 2), box(14, -0.1, 14, 16.25, 3.25, 16.25), box(-0.25, -0.1, 14, 2, 3.25, 16.25), box(-0.25, -0.1, -0.25, 2, 3.25, 2), box(1, 3, 1, 15, 4, 15));
        };
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    public BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.rotate(mirrorIn.getRotation(state.getValue(FACING)));
    }

    @Override
    public void entityInside(BlockState blockstate, Level level, BlockPos pos, Entity entity) {
        super.entityInside(blockstate, level, pos, entity);

        // 禁止套娃
        if (entity instanceof Target1Entity || entity instanceof Mk42Entity) return;

        boolean zooming = entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).map(c -> c.zooming).orElse(false);

        if (entity.isShiftKeyDown()) {
            if (entity.onGround()) {
                entity.setDeltaMovement(new Vec3(5 * entity.getLookAngle().x, 1.5, 5 * entity.getLookAngle().z));
            } else {
                entity.setDeltaMovement(new Vec3(1.8 * entity.getLookAngle().x, 1.5, 1.8 * entity.getLookAngle().z));
            }
        } else {
            entity.setDeltaMovement(new Vec3(0.7 * entity.getDeltaMovement().x(), 1.7, 0.7 * entity.getDeltaMovement().z()));
        }

        if (!zooming) {
            entity.getPersistentData().putDouble("vy", 0.8);
        }

        if (!level.isClientSide()) {
            level.playSound(null, BlockPos.containing(pos.getX(), pos.getY(), pos.getZ()), TargetModSounds.JUMP.get(), SoundSource.BLOCKS, 1, 1);
        } else {
            level.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), TargetModSounds.JUMP.get(), SoundSource.BLOCKS, 1, 1, false);
        }

        entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
            capability.playerDoubleJump = true;
            capability.syncPlayerVariables(entity);
        });
    }
}
