package net.mcreator.superbwarfare.entity.projectile;

import net.mcreator.superbwarfare.ModUtils;
import net.mcreator.superbwarfare.entity.AnimatedEntity;
import net.mcreator.superbwarfare.init.ModDamageTypes;
import net.mcreator.superbwarfare.init.ModEntities;
import net.mcreator.superbwarfare.init.ModItems;
import net.mcreator.superbwarfare.init.ModSounds;
import net.mcreator.superbwarfare.network.message.ClientIndicatorMessage;
import net.mcreator.superbwarfare.tools.ParticleTool;
import net.mcreator.superbwarfare.tools.ProjectileTool;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BellBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.PlayMessages;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class JavelinMissileEntity extends ThrowableItemProjectile implements GeoEntity, AnimatedEntity {
    public static final EntityDataAccessor<String> ANIMATION = SynchedEntityData.defineId(JavelinMissileEntity.class, EntityDataSerializers.STRING);
    public static final EntityDataAccessor<String> TARGET_UUID = SynchedEntityData.defineId(JavelinMissileEntity.class, EntityDataSerializers.STRING);
    public static final EntityDataAccessor<Boolean> TOP = SynchedEntityData.defineId(JavelinMissileEntity.class, EntityDataSerializers.BOOLEAN);
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public String animationprocedure = "empty";
    private float monsterMultiplier = 0.0f;
    private float damage = 300.0f;

    public JavelinMissileEntity(EntityType<? extends JavelinMissileEntity> type, Level world) {
        super(type, world);
    }

    public JavelinMissileEntity(LivingEntity entity, Level level, float damage) {
        super(ModEntities.JAVELIN_MISSILE.get(), entity, level);
        this.damage = damage;
    }

    public JavelinMissileEntity(PlayMessages.SpawnEntity spawnEntity, Level level) {
        this(ModEntities.JAVELIN_MISSILE.get(), level);
    }

    public void setMonsterMultiplier(float monsterMultiplier) {
        this.monsterMultiplier = monsterMultiplier;
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.JAVELIN_MISSILE.get();
    }

    public void setTargetUuid(String uuid) {
        this.entityData.set(TARGET_UUID, uuid);
    }

    public void setAttackMode(boolean mode) {
        this.entityData.set(TOP, mode);
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(TARGET_UUID, "none");
        this.entityData.define(TOP, false);
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double pDistance) {
        return true;
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        float damageMultiplier = 1 + this.monsterMultiplier;
        Entity entity = result.getEntity();
        if (this.getOwner() instanceof LivingEntity living) {
            if (!living.level().isClientSide() && living instanceof ServerPlayer player) {
                living.level().playSound(null, living.blockPosition(), ModSounds.INDICATION.get(), SoundSource.VOICE, 1, 1);

                ModUtils.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> player), new ClientIndicatorMessage(0, 5));
            }
        }

        if (entity instanceof LivingEntity) {
            entity.invulnerableTime = 0;
        }

        if (entity instanceof Monster monster) {
            monster.hurt(ModDamageTypes.causeCannonFireDamage(this.level().registryAccess(), this, this.getOwner()), this.damage * damageMultiplier);
        } else {
            entity.hurt(ModDamageTypes.causeCannonFireDamage(this.level().registryAccess(), this, this.getOwner()), this.damage);
        }

        if (this.tickCount > 1) {
            if (this.level() instanceof ServerLevel) {
                ProjectileTool.causeCustomExplode(this,
                        ModDamageTypes.causeProjectileBoomDamage(this.level().registryAccess(), this, this.getOwner()),
                        entity, this.damage, 8.0f, this.monsterMultiplier);
            }
        }

        this.discard();
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    @Override
    public void onHitBlock(BlockHitResult blockHitResult) {
        super.onHitBlock(blockHitResult);
        BlockPos resultPos = blockHitResult.getBlockPos();
        BlockState state = this.level().getBlockState(resultPos);

        if (state.getBlock() instanceof BellBlock bell) {
            bell.attemptToRing(this.level(), resultPos, blockHitResult.getDirection());
        }

        if (this.tickCount > 1) {
            if (this.level() instanceof ServerLevel) {
                ProjectileTool.causeCustomExplode(this,
                        ModDamageTypes.causeProjectileBoomDamage(this.level().registryAccess(), this, this.getOwner()),
                        this, this.damage, 8.0f, this.monsterMultiplier);
            }
        }

        this.discard();
    }

    @Override
    public void tick() {
        super.tick();
        Entity entity = this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(512))
                .stream().filter(e -> e.getStringUUID().equals(entityData.get(TARGET_UUID))).findFirst().orElse(null);


        if (this.tickCount == 4) {
            if (!this.level().isClientSide() && this.level() instanceof ServerLevel serverLevel) {
                ParticleTool.sendParticle(serverLevel, ParticleTypes.CLOUD, this.xo, this.yo, this.zo, 15, 0.8, 0.8, 0.8, 0.01, true);
                ParticleTool.sendParticle(serverLevel, ParticleTypes.CAMPFIRE_COSY_SMOKE, this.xo, this.yo, this.zo, 10, 0.8, 0.8, 0.8, 0.01, true);
            }
        }

        if (this.tickCount > 3) {
            if (entity != null) {
                if (entityData.get(TOP)) {
                    double px = this.getX();
                    double ex = entity.getX();
                    double pz = this.getZ();
                    double ez = entity.getZ();

                    if (Math.sqrt(Math.pow(px - ex, 2) + Math.pow(pz - ez, 2)) > 10) {
                        this.look(EntityAnchorArgument.Anchor.EYES, new Vec3(entity.getX(), entity.getY() + Mth.clamp(4 * this.tickCount, 0, 90), entity.getZ()));
                    } else {
                        this.look(EntityAnchorArgument.Anchor.EYES, new Vec3(entity.getX(), entity.getEyeY() + (entity instanceof EnderDragon ? -3 : 1), entity.getZ()));
                    }
                } else {
                    this.look(EntityAnchorArgument.Anchor.EYES, new Vec3(entity.getX(), entity.getEyeY() + (entity instanceof EnderDragon ? -3 : 1), entity.getZ()));
                }
            }
        }

        if (this.tickCount > 4) {
            this.setDeltaMovement(new Vec3(
                    0.7f * this.getDeltaMovement().x + 1.3f * this.getLookAngle().x,
                    0.7f * this.getDeltaMovement().y + 1.3f * this.getLookAngle().y,
                    0.7f * this.getDeltaMovement().z + 1.3f * this.getLookAngle().z
            ));
            if (!this.level().isClientSide() && this.level() instanceof ServerLevel serverLevel) {
                ParticleTool.sendParticle(serverLevel, ParticleTypes.CAMPFIRE_COSY_SMOKE, this.xo, this.yo, this.zo, 1, 0, 0, 0, 0, true);
            }
        }

        if (this.tickCount > 200 || this.isInWater()) {
            if (this.level() instanceof ServerLevel) {
                ProjectileTool.causeCustomExplode(this,
                        ModDamageTypes.causeProjectileBoomDamage(this.level().registryAccess(), this, this.getOwner()),
                        this, this.damage, 8.0f, this.monsterMultiplier);
            }
            this.discard();
        }
    }

    private void look(EntityAnchorArgument.Anchor pAnchor, Vec3 pTarget) {
        Vec3 vec3 = pAnchor.apply(this);
        double d0 = (pTarget.x - vec3.x) * 0.2;
        double d1 = (pTarget.y - vec3.y) * 0.2;
        double d2 = (pTarget.z - vec3.z) * 0.2;
        double d3 = Math.sqrt(d0 * d0 + d2 * d2);
        this.setXRot(Mth.wrapDegrees((float) (-(Mth.atan2(d1, d3) * 57.2957763671875))));
        this.setYRot(Mth.wrapDegrees((float) (Mth.atan2(d2, d0) * 57.2957763671875) - 90.0F));
        this.setYHeadRot(this.getYRot());
        this.xRotO = this.getXRot();
        this.yRotO = this.getYRot();
    }

    private PlayState movementPredicate(AnimationState<JavelinMissileEntity> event) {
        if (this.animationprocedure.equals("empty")) {
            return event.setAndContinue(RawAnimation.begin().thenLoop("animation.jvm.idle"));
        }
        return PlayState.STOP;
    }

    private PlayState procedurePredicate(AnimationState<JavelinMissileEntity> event) {
        if (!animationprocedure.equals("empty") && event.getController().getAnimationState() == AnimationController.State.STOPPED) {
            event.getController().setAnimation(RawAnimation.begin().thenPlay(this.animationprocedure));
            if (event.getController().getAnimationState() == AnimationController.State.STOPPED) {
                this.animationprocedure = "empty";
                event.getController().forceAnimationReset();
            }
        } else if (animationprocedure.equals("empty")) {
            return PlayState.STOP;
        }
        return PlayState.CONTINUE;
    }

    @Override
    protected float getGravity() {
        return 0F;
    }

    public String getSyncedAnimation() {
        return this.entityData.get(ANIMATION);
    }

    public void setAnimation(String animation) {
        this.entityData.set(ANIMATION, animation);
    }

    @Override
    public void setAnimationProcedure(String procedure) {
        this.animationprocedure = procedure;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar data) {
        data.add(new AnimationController<>(this, "movement", 0, this::movementPredicate));
        data.add(new AnimationController<>(this, "procedure", 0, this::procedurePredicate));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}
