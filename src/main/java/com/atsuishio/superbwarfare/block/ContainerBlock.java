package com.atsuishio.superbwarfare.block;

import com.atsuishio.superbwarfare.block.entity.ContainerBlockEntity;
import com.atsuishio.superbwarfare.init.ModBlockEntities;
import com.atsuishio.superbwarfare.init.ModItems;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.List;

@SuppressWarnings("deprecation")
public class ContainerBlock extends BaseEntityBlock {

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final BooleanProperty OPENED = BooleanProperty.create("opened");

    public ContainerBlock() {
        super(BlockBehaviour.Properties.of().sound(SoundType.METAL).strength(3.0f).noOcclusion().requiresCorrectToolForDrops());
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(OPENED, false));
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (!pLevel.isClientSide) {
            ItemStack stack = pPlayer.getItemInHand(pHand);
            if (stack.is(ModItems.CROWBAR.get())) {
                if (!hasEntity(pLevel, pPos)) {
                    pPlayer.displayClientMessage(Component.translatable("des.superbwarfare.container.fail.empty"), true);
                    return InteractionResult.PASS;
                }

                if (canOpen(pLevel, pPos)) {
                    pLevel.setBlockAndUpdate(pPos, pState.setValue(OPENED, true));
                    return InteractionResult.SUCCESS;
                } else {
                    pPlayer.displayClientMessage(Component.translatable("des.superbwarfare.container.fail.open"), true);
                }
            } else {
                pPlayer.displayClientMessage(Component.translatable("des.superbwarfare.container.fail.crowbar"), true);
            }
        }
        return InteractionResult.PASS;
    }

    public boolean hasEntity(Level pLevel, BlockPos pPos) {
        BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
        if (!(blockEntity instanceof ContainerBlockEntity containerBlockEntity)) return false;
        return containerBlockEntity.entity != null || containerBlockEntity.entityType != null;
    }

    public boolean canOpen(Level pLevel, BlockPos pPos) {
        boolean flag = true;

        for (int i = -4; i < 5; i++) {
            for (int j = 0; j < 7; j++) {
                for (int k = -4; k < 5; k++) {
                    if (i == 0 && j == 0 && k == 0) {
                        continue;
                    }
                    if (!pLevel.getBlockState(pPos.offset(i, j, k)).isAir()) {
                        flag = false;
                    }
                }
            }
        }

        return flag;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        if (!pLevel.isClientSide) {
            return createTickerHelper(pBlockEntityType, ModBlockEntities.CONTAINER.get(), ContainerBlockEntity::serverTick);
        }
        return null;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable BlockGetter pLevel, List<Component> pTooltip, TooltipFlag pFlag) {
        super.appendHoverText(pStack, pLevel, pTooltip, pFlag);
        CompoundTag compoundtag = BlockItem.getBlockEntityData(pStack);
        if (compoundtag != null) {
            if (compoundtag.contains("EntityType")) {
                String s = getTranslationKey(compoundtag.getString("EntityType"));
                pTooltip.add(Component.translatable(s == null ? "des.superbwarfare.container.empty" : s).withStyle(ChatFormatting.GRAY));
            }
        }
    }

    @Nullable
    public String getTranslationKey(String path) {
        String[] parts = path.split(":");
        if (parts.length > 1) {
            return "entity." + parts[0] + "." + parts[1];
        } else {
            return null;
        }
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return state.getValue(OPENED) ? box(1, 0, 1, 15, 14, 15) : box(0, 0, 0, 16, 15, 16);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new ContainerBlockEntity(blockPos, blockState);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING).add(OPENED);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite()).setValue(OPENED, false);
    }

}

