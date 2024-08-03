package net.mcreator.superbwarfare.entity;

import net.mcreator.superbwarfare.ModUtils;
import net.mcreator.superbwarfare.block.BarbedWireBlock;
import net.mcreator.superbwarfare.init.ModDamageTypes;
import net.mcreator.superbwarfare.init.ModEntities;
import net.mcreator.superbwarfare.init.ModParticleTypes;
import net.mcreator.superbwarfare.init.ModSounds;
import net.mcreator.superbwarfare.network.message.ClientIndicatorMessage;
import net.mcreator.superbwarfare.network.message.PlayerGunKillMessage;
import net.mcreator.superbwarfare.tools.ExtendedEntityRayTraceResult;
import net.mcreator.superbwarfare.tools.HitboxHelper;
import net.mcreator.superbwarfare.tools.ParticleTool;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.*;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.entity.PartEntity;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.PlayMessages;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

public class ProjectileEntity extends Entity implements IEntityAdditionalSpawnData, GeoEntity, AnimatedEntity {

    public static final EntityDataAccessor<String> ANIMATION = SynchedEntityData.defineId(ProjectileEntity.class, EntityDataSerializers.STRING);

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    public String animationProcedure = "empty";
    private static final Predicate<Entity> PROJECTILE_TARGETS = input -> input != null && input.isPickable() && !input.isSpectator() && input.isAlive();

    private static final Predicate<BlockState> IGNORE_LEAVES = input -> input != null && (input.getBlock() instanceof LeavesBlock
            || input.getBlock() instanceof FenceBlock
            || input.getBlock() instanceof IronBarsBlock
            || input.getBlock() instanceof StainedGlassPaneBlock
            || input.getBlock() instanceof DoorBlock
            || input.getBlock() instanceof TrapDoorBlock
            || input.getBlock() instanceof BarbedWireBlock);
    protected LivingEntity shooter;
    protected int shooterId;
    private float damage = 1f;
    private float headShot = 1f;
    private int monsterMultiple = 0;
    private float legShot = 0.5f;
    private boolean beast = false;
    private boolean zoom = false;
    private float bypassArmorRate = 0.0f;

    public ProjectileEntity(EntityType<? extends ProjectileEntity> p_i50159_1_, Level p_i50159_2_) {
        super(p_i50159_1_, p_i50159_2_);
    }

    public ProjectileEntity(Level level) {
        super(ModEntities.PROJECTILE.get(), level);
    }

    public ProjectileEntity(PlayMessages.SpawnEntity packet, Level world) {
        super(ModEntities.PROJECTILE.get(), world);
    }

    public ProjectileEntity shooter(LivingEntity shooter) {
        this.shooter = shooter;
        return this;
    }

    public ProjectileEntity damage(float damage) {
        this.damage = damage;
        return this;
    }

    public ProjectileEntity headShot(float headShot) {
        this.headShot = headShot;
        return this;
    }

    public ProjectileEntity monsterMultiple(int monsterMultiple) {
        this.monsterMultiple = monsterMultiple;
        return this;
    }

    public ProjectileEntity legShot(float legShot) {
        this.legShot = legShot;
        return this;
    }

    public ProjectileEntity beast() {
        this.beast = true;
        return this;
    }

    public ProjectileEntity zoom(boolean zoom) {
        this.zoom = zoom;
        return this;
    }

    public ProjectileEntity bypassArmorRate(float bypassArmorRate) {
        this.bypassArmorRate = bypassArmorRate;
        return this;
    }

    @Nullable
    protected EntityResult findEntityOnPath(Vec3 startVec, Vec3 endVec) {
        Vec3 hitVec = null;
        Entity hitEntity = null;
        boolean headshot = false;
        boolean legshot = false;
        List<Entity> entities = this.level()
                .getEntities(this,
                        this.getBoundingBox()
                                .expandTowards(this.getDeltaMovement())
                                .inflate(this.beast ? 3 : 1),
                        PROJECTILE_TARGETS
                );
        double closestDistance = Double.MAX_VALUE;

        for (Entity entity : entities) {
            if (entity.equals(this.shooter)) continue;

            EntityResult result = this.getHitResult(entity, startVec, endVec);
            if (result == null) continue;

            Vec3 hitPos = result.getHitPos();

            if (hitPos == null) continue;

            double distanceToHit = startVec.distanceTo(hitPos);
            if (distanceToHit < closestDistance) {
                hitVec = hitPos;
                hitEntity = entity;
                closestDistance = distanceToHit;
                headshot = result.isHeadshot();
                legshot = result.isLegShot();
            }
        }
        return hitEntity != null ? new EntityResult(hitEntity, hitVec, headshot, legshot) : null;
    }

    @Nullable
    protected List<EntityResult> findEntitiesOnPath(Vec3 startVec, Vec3 endVec) {
        List<EntityResult> hitEntities = new ArrayList<>();
        List<Entity> entities = this.level().getEntities(
                this,
                this.getBoundingBox()
                        .expandTowards(this.getDeltaMovement())
                        .inflate(this.beast ? 3 : 1),
                PROJECTILE_TARGETS
        );
        for (Entity entity : entities) {
            if (!entity.equals(this.shooter)) {
                EntityResult result = this.getHitResult(entity, startVec, endVec);
                if (result == null) continue;
                hitEntities.add(result);
            }
        }
        return hitEntities;
    }

    /**
     * From TaC-Z
     */
    @Nullable
    private EntityResult getHitResult(Entity entity, Vec3 startVec, Vec3 endVec) {
        double expandHeight = entity instanceof Player && !entity.isCrouching() ? 0.0625 : 0.0;
        AABB boundingBox = entity.getBoundingBox();
        Vec3 velocity = new Vec3(entity.getX() - entity.xOld, entity.getY() - entity.yOld, entity.getZ() - entity.zOld);

        if (entity instanceof ServerPlayer player && this.shooter instanceof ServerPlayer serverPlayerOwner) {
            int ping = Mth.floor((serverPlayerOwner.latency / 1000.0) * 20.0 + 0.5);
            boundingBox = HitboxHelper.getBoundingBox(player, ping);
            velocity = HitboxHelper.getVelocity(player, ping);
        }
        boundingBox = boundingBox.expandTowards(0, expandHeight, 0);

        boundingBox = boundingBox.expandTowards(velocity.x, velocity.y, velocity.z);

        double playerHitboxOffset = 3;
        if (entity instanceof ServerPlayer) {
            if (entity.getVehicle() != null) {
                boundingBox = boundingBox.move(velocity.multiply(playerHitboxOffset / 2, playerHitboxOffset / 2, playerHitboxOffset / 2));
            }
            boundingBox = boundingBox.move(velocity.multiply(playerHitboxOffset, playerHitboxOffset, playerHitboxOffset));
        }

        if (entity.getVehicle() != null) {
            boundingBox = boundingBox.move(velocity.multiply(-2.5, -2.5, -2.5));
        }
        boundingBox = boundingBox.move(velocity.multiply(-5, -5, -5));
        Vec3 hitPos = boundingBox.clip(startVec, endVec).orElse(null);

        if (hitPos == null) {
            return null;
        }
        Vec3 hitBoxPos = hitPos.subtract(entity.position());
        boolean headshot = false;
        boolean legshot = false;
        float eyeHeight = entity.getEyeHeight();
        float BodyHeight = entity.getBbHeight();
        if ((eyeHeight - 0.35) < hitBoxPos.y && hitBoxPos.y < (eyeHeight + 0.4) && !(entity instanceof ClaymoreEntity || entity instanceof MortarEntity)) {
            headshot = true;
        }
        if (hitBoxPos.y < (0.33 * BodyHeight) && !(entity instanceof ClaymoreEntity || entity instanceof MortarEntity)) {
            legshot = true;
        }

        return new EntityResult(entity, hitPos, headshot, legshot);
    }

    @Override
    protected void defineSynchedData() {

    }

    @Override
    public void tick() {
        super.tick();
        this.updateHeading();
        this.onProjectileTick();

        Vec3 vec = this.getDeltaMovement();

        if (!this.level().isClientSide()) {
            Vec3 startVec = this.position();
            Vec3 endVec = startVec.add(this.getDeltaMovement());
            HitResult result = rayTraceBlocks(this.level(), new ClipContext(startVec, endVec, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this), IGNORE_LEAVES);
            if (result.getType() != HitResult.Type.MISS) {
                endVec = result.getLocation();
            }

            List<EntityResult> entityResults = new ArrayList<>();

            if (this.beast) {
                var temp = findEntitiesOnPath(startVec, endVec);
                if (temp != null) entityResults.addAll(temp);
            } else {
                var temp = this.findEntityOnPath(startVec, endVec);
                if (temp != null) entityResults.add(temp);
            }

            for (var entityResult : entityResults) {
                result = new ExtendedEntityRayTraceResult(entityResult);
                if (((EntityHitResult) result).getEntity() instanceof Player player) {
                    if (this.shooter instanceof Player && !((Player) this.shooter).canHarmPlayer(player)) {
                        result = null;
                    }
                }
                if (result != null) {
                    this.onHit(result);
                }
            }
            if (entityResults.isEmpty()) {
                this.onHit(result);
            }

            this.setPos(this.getX() + vec.x, this.getY() + vec.y, this.getZ() + vec.z);
        } else {
            this.setPosRaw(this.getX() + vec.x, this.getY() + vec.y, this.getZ() + vec.z);
        }

        double x = vec.x;
        double y = vec.y;
        double z = vec.z;

        this.setDeltaMovement(x - 0.02 * x, y - 0.02 * y - 0.05, z - 0.02 * z);

        this.tickCount++;
        if (this.tickCount > 40) {
            this.discard();
        }
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag p_20052_) {

    }

    @Override
    protected void addAdditionalSaveData(CompoundTag p_20139_) {

    }

    protected void onProjectileTick() {
    }

    private void onHit(HitResult result) {
        if (result instanceof BlockHitResult blockHitResult) {
            if (blockHitResult.getType() == HitResult.Type.MISS) {
                return;
            }
            Vec3 hitVec = result.getLocation();
            this.onHitBlock(hitVec);
        }

        if (result instanceof ExtendedEntityRayTraceResult entityHitResult) {
            Entity entity = entityHitResult.getEntity();
            if (entity.getId() == this.shooterId) {
                return;
            }

            if (this.shooter instanceof Player player) {
                if (entity.hasIndirectPassenger(player)) {
                    return;
                }
            }

            this.onHitEntity(entity, entityHitResult.isHeadshot(), entityHitResult.isLegshot());
            entity.invulnerableTime = 0;
        }
    }

    protected void onHitBlock(Vec3 location) {

        if (this.level() instanceof ServerLevel serverLevel) {
            if (this.beast) {
                ParticleTool.sendParticle(serverLevel, ParticleTypes.END_ROD, location.x, location.y, location.z, 15, 0.1, 0.1, 0.1, 0.05, true);
            } else {
                ParticleTool.sendParticle(serverLevel, ModParticleTypes.BULLET_HOLE.get(), location.x, location.y, location.z, 1, 0, 0, 0, 0, true);
                ParticleTool.sendParticle(serverLevel, ParticleTypes.SMOKE, location.x, location.y, location.z, 3, 0, 0.1, 0, 0.01, true);

                this.discard();
            }
            serverLevel.playSound(null, new BlockPos((int) location.x, (int) location.y, (int) location.z), ModSounds.LAND.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
        }
    }

    protected void onHitEntity(Entity entity, boolean headshot, boolean legShot) {
        float mMultiple = 1 + 0.2f * this.monsterMultiple;

        if (entity == null) return;

        if (entity instanceof PartEntity<?> part) {
            entity = part.getParent();
        }

        if (beast && entity instanceof LivingEntity living) {
            if (living.isDeadOrDying()) return;

            if (this.shooter instanceof ServerPlayer player) {
                ModUtils.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> player), new ClientIndicatorMessage(0, 5));
                var holder = Holder.direct(ModSounds.INDICATION.get());
                player.connection.send(new ClientboundSoundPacket(holder, SoundSource.PLAYERS, player.getX(), player.getY(), player.getZ(), 1f, 1f, player.level().random.nextLong()));
                ((ServerLevel) this.level()).sendParticles(ParticleTypes.DAMAGE_INDICATOR, living.getX(), living.getY() + .5, living.getZ(), 1000, .4, .7, .4, 0);

                ModUtils.PACKET_HANDLER.send(PacketDistributor.ALL.noArg(), new PlayerGunKillMessage(player.getId(), living.getId(), false, ModDamageTypes.BEAST));
            }

            if (living instanceof ServerPlayer victim) {
                living.setHealth(0);
                living.level().players().forEach(
                        p -> p.sendSystemMessage(
                                Component.translatable("death.attack.beast_gun",
                                        victim.getDisplayName(),
                                        shooter.getDisplayName()
                                )
                        )
                );
            } else {
                living.setHealth(0);
                living.level().broadcastEntityEvent(living, (byte) 60);
                living.remove(Entity.RemovalReason.KILLED);
                living.gameEvent(GameEvent.ENTITY_DIE);
            }

            return;
        }

        if (headshot) {
            if (!this.shooter.level().isClientSide() && this.shooter instanceof ServerPlayer player) {
                var holder = Holder.direct(ModSounds.HEADSHOT.get());
                player.connection.send(new ClientboundSoundPacket(holder, SoundSource.PLAYERS, player.getX(), player.getY(), player.getZ(), 1f, 1f, player.level().random.nextLong()));

                ModUtils.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> player), new ClientIndicatorMessage(1, 5));
            }
            if (entity instanceof Monster monster) {
                performDamage(monster, this.damage * mMultiple, true);
            } else {
                performDamage(entity, this.damage, true);
            }
        } else if (legShot) {
            if (!this.shooter.level().isClientSide() && this.shooter instanceof ServerPlayer player) {
                var holder = Holder.direct(ModSounds.INDICATION.get());
                player.connection.send(new ClientboundSoundPacket(holder, SoundSource.PLAYERS, player.getX(), player.getY(), player.getZ(), 1f, 1f, player.level().random.nextLong()));

                ModUtils.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> player), new ClientIndicatorMessage(0, 5));
            }

            if (entity instanceof Monster monster) {
                performDamage(monster, this.damage * mMultiple * this.legShot, false);
            } else {
                performDamage(entity, this.damage * this.legShot, false);
            }

            if (entity instanceof LivingEntity living) {
                if (living instanceof Player player && player.isCreative()) {
                    return;
                }
                if (!living.level().isClientSide()) {
                    living.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20, 2, false, false));
                }
            }
        } else {
            if (!this.shooter.level().isClientSide() && this.shooter instanceof ServerPlayer player) {
                var holder = Holder.direct(ModSounds.INDICATION.get());
                player.connection.send(new ClientboundSoundPacket(holder, SoundSource.PLAYERS, player.getX(), player.getY(), player.getZ(), 1f, 1f, player.level().random.nextLong()));

                ModUtils.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> player), new ClientIndicatorMessage(0, 5));
            }

            if (entity instanceof Monster monster) {
                performDamage(monster, this.damage * mMultiple, false);
            } else {
                performDamage(entity, this.damage, false);
            }
        }
        this.discard();
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    public float getDamage() {
        return this.damage;
    }

    public void shoot(double p_37266_, double p_37267_, double p_37268_, float p_37269_, float p_37270_) {
        Vec3 vec3 = (new Vec3(p_37266_, p_37267_, p_37268_)).normalize().add(this.random.triangle(0.0D, 0.0172275D * (double) p_37270_), this.random.triangle(0.0D, 0.0172275D * (double) p_37270_), this.random.triangle(0.0D, 0.0172275D * (double) p_37270_)).scale(p_37269_);
        this.setDeltaMovement(vec3);
        double d0 = vec3.horizontalDistance();
        this.setYRot((float) (Mth.atan2(vec3.x, vec3.z) * (double) (180F / (float) Math.PI)));
        this.setXRot((float) (Mth.atan2(vec3.y, d0) * (double) (180F / (float) Math.PI)));
        this.yRotO = this.getYRot();
        this.xRotO = this.getXRot();
    }

    @SuppressWarnings("SameParameterValue")
    private static BlockHitResult rayTraceBlocks(Level world, ClipContext context, Predicate<BlockState> ignorePredicate) {
        return performRayTrace(context, (rayTraceContext, blockPos) -> {
            BlockState blockState = world.getBlockState(blockPos);
            if (ignorePredicate.test(blockState)) return null;
            FluidState fluidState = world.getFluidState(blockPos);
            Vec3 startVec = rayTraceContext.getFrom();
            Vec3 endVec = rayTraceContext.getTo();
            VoxelShape blockShape = rayTraceContext.getBlockShape(blockState, world, blockPos);
            BlockHitResult blockResult = world.clipWithInteractionOverride(startVec, endVec, blockPos, blockShape, blockState);
            VoxelShape fluidShape = rayTraceContext.getFluidShape(fluidState, world, blockPos);
            BlockHitResult fluidResult = fluidShape.clip(startVec, endVec, blockPos);
            double blockDistance = blockResult == null ? Double.MAX_VALUE : rayTraceContext.getFrom().distanceToSqr(blockResult.getLocation());
            double fluidDistance = fluidResult == null ? Double.MAX_VALUE : rayTraceContext.getFrom().distanceToSqr(fluidResult.getLocation());
            return blockDistance <= fluidDistance ? blockResult : fluidResult;
        }, (rayTraceContext) -> {
            Vec3 Vector3d = rayTraceContext.getFrom().subtract(rayTraceContext.getTo());
            return BlockHitResult.miss(rayTraceContext.getTo(), Direction.getNearest(Vector3d.x, Vector3d.y, Vector3d.z), BlockPos.containing(rayTraceContext.getTo()));
        });
    }

    private static <T> T performRayTrace(ClipContext context, BiFunction<ClipContext, BlockPos, T> hitFunction, Function<ClipContext, T> p_217300_2_) {
        Vec3 startVec = context.getFrom();
        Vec3 endVec = context.getTo();
        if (startVec.equals(endVec)) {
            return p_217300_2_.apply(context);
        } else {
            double startX = Mth.lerp(-0.0000001, endVec.x, startVec.x);
            double startY = Mth.lerp(-0.0000001, endVec.y, startVec.y);
            double startZ = Mth.lerp(-0.0000001, endVec.z, startVec.z);
            double endX = Mth.lerp(-0.0000001, startVec.x, endVec.x);
            double endY = Mth.lerp(-0.0000001, startVec.y, endVec.y);
            double endZ = Mth.lerp(-0.0000001, startVec.z, endVec.z);
            int blockX = Mth.floor(endX);
            int blockY = Mth.floor(endY);
            int blockZ = Mth.floor(endZ);
            BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos(blockX, blockY, blockZ);
            T t = hitFunction.apply(context, mutablePos);
            if (t != null) {
                return t;
            }

            double deltaX = startX - endX;
            double deltaY = startY - endY;
            double deltaZ = startZ - endZ;
            int signX = Mth.sign(deltaX);
            int signY = Mth.sign(deltaY);
            int signZ = Mth.sign(deltaZ);
            double d9 = signX == 0 ? Double.MAX_VALUE : (double) signX / deltaX;
            double d10 = signY == 0 ? Double.MAX_VALUE : (double) signY / deltaY;
            double d11 = signZ == 0 ? Double.MAX_VALUE : (double) signZ / deltaZ;
            double d12 = d9 * (signX > 0 ? 1.0D - Mth.frac(endX) : Mth.frac(endX));
            double d13 = d10 * (signY > 0 ? 1.0D - Mth.frac(endY) : Mth.frac(endY));
            double d14 = d11 * (signZ > 0 ? 1.0D - Mth.frac(endZ) : Mth.frac(endZ));

            while (d12 <= 1.0D || d13 <= 1.0D || d14 <= 1.0D) {
                if (d12 < d13) {
                    if (d12 < d14) {
                        blockX += signX;
                        d12 += d9;
                    } else {
                        blockZ += signZ;
                        d14 += d11;
                    }
                } else if (d13 < d14) {
                    blockY += signY;
                    d13 += d10;
                } else {
                    blockZ += signZ;
                    d14 += d11;
                }

                T t1 = hitFunction.apply(context, mutablePos.set(blockX, blockY, blockZ));
                if (t1 != null) {
                    return t1;
                }
            }

            return p_217300_2_.apply(context);
        }
    }

    public LivingEntity getShooter() {
        return this.shooter;
    }

    public int getShooterId() {
        return this.shooterId;
    }

    public void updateHeading() {
        double horizontalDistance = this.getDeltaMovement().horizontalDistance();
        this.setYRot((float) (Mth.atan2(this.getDeltaMovement().x(), this.getDeltaMovement().z()) * (180D / Math.PI)));
        this.setXRot((float) (Mth.atan2(this.getDeltaMovement().y(), horizontalDistance) * (180D / Math.PI)));
        this.yRotO = this.getYRot();
        this.xRotO = this.getXRot();
    }

    private void performDamage(Entity entity, float damage, boolean headshot) {
        float normalDamage = damage * Mth.clamp(1 - bypassArmorRate, 0, 1);
        float absoluteDamage = damage * Mth.clamp(bypassArmorRate, 0, 1);

        entity.invulnerableTime = 0;
        if (headshot) {
            entity.hurt(ModDamageTypes.causeGunFireHeadshotDamage(this.level().registryAccess(), this, this.shooter), normalDamage * this.headShot);
            entity.invulnerableTime = 0;
            entity.hurt(ModDamageTypes.causeGunFireHeadshotAbsoluteDamage(this.level().registryAccess(), this, this.shooter), absoluteDamage * this.headShot);
        } else {
            entity.hurt(ModDamageTypes.causeGunFireDamage(this.level().registryAccess(), this, this.shooter), normalDamage);
            entity.invulnerableTime = 0;
            entity.hurt(ModDamageTypes.causeGunFireAbsoluteDamage(this.level().registryAccess(), this, this.shooter), absoluteDamage);
        }
    }

    @Override
    public void writeSpawnData(FriendlyByteBuf buffer) {
    }

    @Override
    public void readSpawnData(FriendlyByteBuf additionalData) {
    }

    public static class EntityResult {
        private final Entity entity;
        private final Vec3 hitVec;
        private final boolean headshot;
        private final boolean legShot;

        public EntityResult(Entity entity, Vec3 hitVec, boolean headshot, boolean legShot) {
            this.entity = entity;
            this.hitVec = hitVec;
            this.headshot = headshot;
            this.legShot = legShot;
        }

        /**
         * Gets the entity that was hit by the projectile
         */
        public Entity getEntity() {
            return this.entity;
        }

        /**
         * Gets the position the projectile hit
         */
        public Vec3 getHitPos() {
            return this.hitVec;
        }

        /**
         * Gets if this was a headshot
         */
        public boolean isHeadshot() {
            return this.headshot;
        }

        public boolean isLegShot() {
            return this.legShot;
        }
    }

    public String getSyncedAnimation() {
        return this.entityData.get(ANIMATION);
    }

    public void setAnimation(String animation) {
        this.entityData.set(ANIMATION, animation);
    }

    @Override
    public void setAnimationProcedure(String procedure) {
        this.animationProcedure = procedure;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar data) {
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    public boolean isZoom() {
        return this.zoom;
    }
}
