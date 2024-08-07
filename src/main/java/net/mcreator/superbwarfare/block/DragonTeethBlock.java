package net.mcreator.superbwarfare.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class DragonTeethBlock extends Block {
    public DragonTeethBlock() {
        super(BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.BASEDRUM).sound(SoundType.STONE).strength(13f, 28f).requiresCorrectToolForDrops().noOcclusion().isRedstoneConductor((bs, br, bp) -> false));
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, BlockGetter reader, BlockPos pos) {
        return true;
    }

    @Override
    public int getLightBlock(BlockState state, BlockGetter worldIn, BlockPos pos) {
        return 0;
    }

    @Override
    public VoxelShape getVisualShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return Shapes.empty();
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return Shapes.or(box(1, 0, 1, 15, 3, 15),
                box(2, 3, 2, 14, 6, 14),
                box(3, 6, 3, 13, 9, 13),
                box(4, 9, 4, 12, 12, 12),
                box(5, 12, 5, 11, 15, 11),
                box(6, 15, 6, 10, 16, 10));
    }

}

