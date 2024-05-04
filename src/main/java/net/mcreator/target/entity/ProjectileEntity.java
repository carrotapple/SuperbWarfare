package net.mcreator.target.entity;


import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.BlockHitResult;

import net.minecraft.world.level.Level;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.mcreator.target.init.TargetCustomModEntities;
import net.mcreator.target.init.TargetModItems;

import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.*;
import net.minecraft.world.phys.*;

import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.PlayMessages;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.BlockHitResult;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.Packet;

import net.mcreator.target.headshot.BoundingBoxManager;
import net.mcreator.target.headshot.IHeadshotBox;
import net.mcreator.target.init.TargetModEntities;

import net.mcreator.target.procedures.BullettestDanSheWuJiZhongFangKuaiShiProcedure;
import net.mcreator.target.network.TargetModVariables;

import java.util.Iterator;
import java.util.Optional;

public class ProjectileEntity extends ThrowableItemProjectile {

    private float damage;

    public ProjectileEntity(EntityType<? extends ProjectileEntity> p_i50159_1_, Level p_i50159_2_) {
        super(p_i50159_1_, p_i50159_2_);
    }

    public ProjectileEntity(Level world, LivingEntity entity) {
        super(TargetCustomModEntities.PROJECTILE.get(), entity, world);
        this.damage = 0f;
    }

    public ProjectileEntity(Level world, LivingEntity entity, float damage) {
        super(TargetCustomModEntities.PROJECTILE.get(), entity, world);
        this.damage = damage;
    }

    public ProjectileEntity(Level p_i1775_1_, double p_i1775_2_, double p_i1775_4_, double p_i1775_6_) {
        super(TargetCustomModEntities.PROJECTILE.get(), p_i1775_2_, p_i1775_4_, p_i1775_6_, p_i1775_1_);
    }

    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        super.onHitEntity(pResult);
        Entity entity = pResult.getEntity();
        if (entity instanceof LivingEntity livingEntity) {
            livingEntity.hurt(this.damageSources().thrown(this, this.getOwner() instanceof LivingEntity living ? living : null), this.damage);
        }

        if (!this.level().isClientSide) {
            this.discard();
        }
    }

    @Override
    public void tick() {
        super.tick();
        if(this.tickCount >= 20){
            this.discard();
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult pResult) {
        super.onHitBlock(pResult);
        if (!this.level().isClientSide) {
            this.discard();
        }
    }

    @Override
    protected Item getDefaultItem() {
        return TargetModItems.RIFLE_AMMO.get().asItem();
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    public float getDamage() {
        return this.damage;
    }
}
