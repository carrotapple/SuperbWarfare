package com.atsuishio.superbwarfare.block.entity;

import com.atsuishio.superbwarfare.init.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class ContainerBlockEntity extends BlockEntity implements GeoBlockEntity {

    public EntityType<?> entityType;
    public Entity entity = null;

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public ContainerBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.CONTAINER.get(), pos, state);
    }

    private PlayState predicate(AnimationState<ContainerBlockEntity> event) {
        return event.setAndContinue(RawAnimation.begin().thenPlay("animation.container.open"));
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
        if (compound.contains("Entity")) {
            entity.deserializeNBT(compound.getCompound("Entity"));
        }
        if (compound.contains("EntityType")) {
            this.entityType = EntityType.byString(compound.getString("EntityType")).orElse(null);
        }
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        if (this.entity != null) {
            compound.put("Entity", this.entity.serializeNBT());
        }
        if (this.entityType != null) {
            compound.putString("EntityType", EntityType.getKey(this.entityType).toString());
        }
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return this.saveWithFullMetadata();
    }

}
