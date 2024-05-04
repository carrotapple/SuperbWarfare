package net.mcreator.target.entity;



import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.phys.*;

import net.mcreator.target.headshot.BoundingBoxManager;
import net.mcreator.target.headshot.IHeadshotBox;
import net.mcreator.target.init.TargetCustomModEntities;
import net.mcreator.target.init.TargetModItems;
import net.mcreator.target.procedures.ProjectileHitEntity;
import net.mcreator.target.procedures.ProjectileHeadshotEntity;

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

        final Vec3 position = this.position();
        Entity entity = pResult.getEntity();
        if (entity instanceof LivingEntity livingEntity) {
            entity.invulnerableTime = 0;
        }
        AABB boundingBox = entity.getBoundingBox();
        Vec3 startVec = this.position();
        Vec3 endVec = startVec.add(this.getDeltaMovement());
        Vec3 hitPos = boundingBox.clip(startVec, endVec).orElse(null);
        /* Check for headshot */
        boolean headshot = false;
        if(entity instanceof LivingEntity)
        {
            IHeadshotBox<LivingEntity> headshotBox = (IHeadshotBox<LivingEntity>) BoundingBoxManager.getHeadshotBoxes(entity.getType());
            if(headshotBox != null)
            {
                AABB box = headshotBox.getHeadshotBox((LivingEntity) entity);
                if(box != null)
                {
                    box = box.move(boundingBox.getCenter().x, boundingBox.minY, boundingBox.getCenter().z);
                    Optional<Vec3> headshotHitPos = box.clip(startVec, endVec);
                    if(!headshotHitPos.isPresent())
                    {
                        box = box.inflate( 0.2, 0.2, 0.2);
                        headshotHitPos = box.clip(startVec, endVec);
                    }
                    if(headshotHitPos.isPresent() && (hitPos == null || headshotHitPos.get().distanceTo(hitPos) < 0.55))
                    {
                        hitPos = headshotHitPos.get();
                        headshot = true;
                    }
                    if(headshot){
                        ProjectileHeadshotEntity.execute(this.level(), pResult.getEntity(), this, this.getOwner());
                    }
                }
            }
        }
        ProjectileHitEntity.execute(this.level(), pResult.getEntity(), this, this.getOwner());
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
