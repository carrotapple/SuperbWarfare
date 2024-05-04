
package net.mcreator.target.block;

import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.Entity;
import net.minecraft.network.chat.Component;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;

import net.mcreator.target.procedures.JumppadBlockShiTiZaiFangKuaiZhongPengZhuangShiProcedure;

import java.util.List;
import java.util.Collections;

public class JumppadBlockBlock extends Block {
	public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

	public JumppadBlockBlock() {
		super(BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.BASEDRUM).sound(SoundType.STONE).strength(-1, 3600000).noCollission().noOcclusion().isRedstoneConductor((bs, br, bp) -> false));
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
	}

	@Override
	public void appendHoverText(ItemStack itemstack, BlockGetter world, List<Component> list, TooltipFlag flag) {
		super.appendHoverText(itemstack, world, list, flag);
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
		return switch (state.getValue(FACING)) {
			default -> Shapes.or(box(0, 0, 0, 16, 3, 16), box(-0.25, -0.1, -0.25, 2, 3.25, 2), box(14, -0.1, -0.25, 16.25, 3.25, 2), box(14, -0.1, 14, 16.25, 3.25, 16.25), box(-0.25, -0.1, 14, 2, 3.25, 16.25), box(1, 3, 1, 15, 4, 15));
			case NORTH -> Shapes.or(box(0, 0, 0, 16, 3, 16), box(14, -0.1, 14, 16.25, 3.25, 16.25), box(-0.25, -0.1, 14, 2, 3.25, 16.25), box(-0.25, -0.1, -0.25, 2, 3.25, 2), box(14, -0.1, -0.25, 16.25, 3.25, 2), box(1, 3, 1, 15, 4, 15));
			case EAST -> Shapes.or(box(0, 0, 0, 16, 3, 16), box(-0.25, -0.1, 14, 2, 3.25, 16.25), box(-0.25, -0.1, -0.25, 2, 3.25, 2), box(14, -0.1, -0.25, 16.25, 3.25, 2), box(14, -0.1, 14, 16.25, 3.25, 16.25), box(1, 3, 1, 15, 4, 15));
			case WEST -> Shapes.or(box(0, 0, 0, 16, 3, 16), box(14, -0.1, -0.25, 16.25, 3.25, 2), box(14, -0.1, 14, 16.25, 3.25, 16.25), box(-0.25, -0.1, 14, 2, 3.25, 16.25), box(-0.25, -0.1, -0.25, 2, 3.25, 2), box(1, 3, 1, 15, 4, 15));
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
	public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
		List<ItemStack> dropsOriginal = super.getDrops(state, builder);
		if (!dropsOriginal.isEmpty())
			return dropsOriginal;
		return Collections.singletonList(new ItemStack(this, 1));
	}

	@Override
	public void entityInside(BlockState blockstate, Level world, BlockPos pos, Entity entity) {
		super.entityInside(blockstate, world, pos, entity);
		JumppadBlockShiTiZaiFangKuaiZhongPengZhuangShiProcedure.execute(world, pos.getX(), pos.getY(), pos.getZ(), entity);
	}
}
