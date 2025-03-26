package com.atsuishio.superbwarfare.block.entity;

import com.atsuishio.superbwarfare.block.ContainerBlock;
import com.atsuishio.superbwarfare.init.ModBlockEntities;
import com.atsuishio.superbwarfare.tools.ParticleTool;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.annotation.Nullable;

public class SmallContainerBlockEntity extends BlockEntity implements GeoBlockEntity {

    @Nullable
    public ResourceLocation lootTable;
    protected long lootTableSeed;
    public int tick = 0;

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public SmallContainerBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.SMALL_CONTAINER.get(), pos, state);
    }

    public static void serverTick(Level pLevel, BlockPos pPos, BlockState pState, SmallContainerBlockEntity blockEntity) {
        if (!pState.getValue(ContainerBlock.OPENED)) {
            return;
        }

        if (blockEntity.tick < 20) {
            blockEntity.tick++;
            blockEntity.setChanged();

            if (blockEntity.tick == 18) {
                ParticleTool.sendParticle((ServerLevel) pLevel, ParticleTypes.EXPLOSION, pPos.getX(), pPos.getY() + 1, pPos.getZ(), 40, 1.5, 1.5, 1.5, 1, false);
                pLevel.playSound(null, pPos, SoundEvents.GENERIC_EXPLODE, SoundSource.BLOCKS, 4.0F, (1.0F + (pLevel.random.nextFloat() - pLevel.random.nextFloat()) * 0.2F) * 0.7F);
            }
        } else {

            pLevel.setBlockAndUpdate(pPos, Blocks.AIR.defaultBlockState());
        }
    }

    private PlayState predicate(AnimationState<SmallContainerBlockEntity> event) {
//        if (this.getBlockState().getValue(ContainerBlock.OPENED)) {
//            return event.setAndContinue(RawAnimation.begin().thenPlay("animation.container.open"));
//        }
        return PlayState.STOP;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar data) {
        data.add(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        if (compound.contains("LootTable", 8)) {
            this.lootTable = new ResourceLocation(compound.getString("LootTable"));
            this.lootTableSeed = compound.getLong("LootTableSeed");
        }
        this.tick = compound.getInt("Tick");
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        if (this.lootTable != null) {
            compound.putString("LootTable", this.lootTable.toString());
            if (this.lootTableSeed != 0L) {
                compound.putLong("LootTableSeed", this.lootTableSeed);
            }
        }
        compound.putInt("Tick", this.tick);
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return this.saveWithFullMetadata();
    }

    @Override
    public void saveToItem(ItemStack pStack) {
        CompoundTag tag = new CompoundTag();
        if (this.lootTable != null) {
            tag.putString("LootTable", this.lootTable.toString());
            if (this.lootTableSeed != 0L) {
                tag.putLong("LootTableSeed", this.lootTableSeed);
            }
        }
        BlockItem.setBlockEntityData(pStack, this.getType(), tag);
    }
}
