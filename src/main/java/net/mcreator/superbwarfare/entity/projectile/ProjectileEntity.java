package net.mcreator.superbwarfare.entity.projectile;

import net.mcreator.superbwarfare.ModUtils;
import net.mcreator.superbwarfare.block.BarbedWireBlock;
import net.mcreator.superbwarfare.entity.*;
import net.mcreator.superbwarfare.init.*;
import net.mcreator.superbwarfare.item.Transcript;
import net.mcreator.superbwarfare.network.message.ClientIndicatorMessage;
import net.mcreator.superbwarfare.network.message.PlayerGunKillMessage;
import net.mcreator.superbwarfare.tools.CustomExplosion;
import net.mcreator.superbwarfare.tools.ExtendedEntityRayTraceResult;
import net.mcreator.superbwarfare.tools.HitboxHelper;
import net.mcreator.superbwarfare.tools.ParticleTool;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Explosion;
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
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.annotation.Nullable;
import java.text.DecimalFormat;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static net.mcreator.superbwarfare.entity.TargetEntity.DOWN_TIME;

@SuppressWarnings({"unused", "UnusedReturnValue", "SuspiciousNameCombination"})
public class ProjectileEntity extends Entity implements IEntityAdditionalSpawnData, GeoEntity, AnimatedEntity {
    public static final EntityDataAccessor<Float> COLOR_R = SynchedEntityData.defineId(ProjectileEntity.class, EntityDataSerializers.FLOAT);
    public static final EntityDataAccessor<Float> COLOR_G = SynchedEntityData.defineId(ProjectileEntity.class, EntityDataSerializers.FLOAT);
    public static final EntityDataAccessor<Float> COLOR_B = SynchedEntityData.defineId(ProjectileEntity.class, EntityDataSerializers.FLOAT);
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

    @Nullable
    protected LivingEntity shooter;
    protected int shooterId;
    private float damage = 1f;
    private float headShot = 1f;
    private float monsterMultiple = 0.0f;
    private float legShot = 0.5f;
    private boolean beast = false;
    private boolean zoom = false;
    private float bypassArmorRate = 0.0f;
    private float undeadMultiple = 1.0f;
    private boolean jhpBullet = false;
    private int jhpLevel = 0;
    private boolean heBullet = false;
    private int heLevel = 0;
    private Supplier<MobEffectInstance> mobEffect = () -> null;

    public ProjectileEntity(EntityType<? extends ProjectileEntity> p_i50159_1_, Level p_i50159_2_) {
        super(p_i50159_1_, p_i50159_2_);
    }

    public ProjectileEntity(Level level) {
        super(ModEntities.PROJECTILE.get(), level);
    }

    public ProjectileEntity(PlayMessages.SpawnEntity packet, Level world) {
        super(ModEntities.PROJECTILE.get(), world);
    }

    @Nullable
    protected EntityResult findEntityOnPath(Vec3 startVec, Vec3 endVec) {
        if (this.shooter == null) return null;

        Vec3 hitVec = null;
        Entity hitEntity = null;
        boolean headshot = false;
        boolean legShot = false;
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

            if (entity.equals(this.shooter.getVehicle())) continue;

            if (entity instanceof TargetEntity && entity.getEntityData().get(DOWN_TIME) > 0) continue;

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
                legShot = result.isLegShot();
            }
        }
        return hitEntity != null ? new EntityResult(hitEntity, hitVec, headshot, legShot) : null;
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

        if (this.beast) {
            boundingBox = boundingBox.inflate(3);
        }

        Vec3 hitPos = boundingBox.clip(startVec, endVec).orElse(null);

        if (hitPos == null) {
            return null;
        }
        Vec3 hitBoxPos = hitPos.subtract(entity.position());
        boolean headshot = false;
        boolean legShot = false;
        float eyeHeight = entity.getEyeHeight();
        float BodyHeight = entity.getBbHeight();
        if ((eyeHeight - 0.35) < hitBoxPos.y && hitBoxPos.y < (eyeHeight + 0.4) &&
                !(entity instanceof ClaymoreEntity || entity instanceof MortarEntity || entity instanceof ICannonEntity || entity instanceof DroneEntity)) {
            headshot = true;
        }
        if (hitBoxPos.y < (0.33 * BodyHeight) && !(entity instanceof ClaymoreEntity || entity instanceof MortarEntity ||
                entity instanceof ICannonEntity || entity instanceof DroneEntity)) {
            legShot = true;
        }

        return new EntityResult(entity, hitPos, headshot, legShot);
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(COLOR_R, 1.0f);
        this.entityData.define(COLOR_G, 222 / 255f);
        this.entityData.define(COLOR_B, 39 / 255f);
    }

    @Override
    public void tick() {
        super.tick();
        this.updateHeading();
        this.onProjectileTick();

        Vec3 vec = this.getDeltaMovement();

        if (!this.level().isClientSide() && this.shooter != null) {
            Vec3 startVec = this.position();
            Vec3 endVec = startVec.add(this.getDeltaMovement());
            HitResult result = rayTraceBlocks(this.level(), new ClipContext(startVec, endVec, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this), IGNORE_LEAVES);
            if (result.getType() != HitResult.Type.MISS) {
                endVec = result.getLocation();
            }

            List<EntityResult> entityResults = new ArrayList<>();
            var temp = findEntitiesOnPath(startVec, endVec);
            if (temp != null) entityResults.addAll(temp);
            entityResults.sort(Comparator.comparingDouble(e -> e.getHitPos().distanceTo(this.shooter.position())));

            for (EntityResult entityResult : entityResults) {
                result = new ExtendedEntityRayTraceResult(entityResult);
                if (((EntityHitResult) result).getEntity() instanceof Player player) {
                    if (this.shooter instanceof Player p && !p.canHarmPlayer(player)) {
                        result = null;
                    }
                }
                if (result != null) {
                    this.onHit(result);
                }

                if (!this.beast) {
                    this.bypassArmorRate -= 0.2F;
                    if (this.bypassArmorRate < 0.8F) {
                        break;
                    }
                }
            }
            if (entityResults.isEmpty()) {
                this.onHit(result);
            }

            this.setPos(this.getX() + vec.x, this.getY() + vec.y, this.getZ() + vec.z);
        } else {
            this.setPosRaw(this.getX() + vec.x, this.getY() + vec.y, this.getZ() + vec.z);
        }

        this.setDeltaMovement(vec.x, vec.y - 0.02, vec.z);

        this.tickCount++;
        if (this.tickCount > 40) {
            this.discard();
        }
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag) {

    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag) {

    }

    protected void onProjectileTick() {
    }

    private void onHit(HitResult result) {
        if (result instanceof BlockHitResult blockHitResult) {
            if (blockHitResult.getType() == HitResult.Type.MISS) {
                return;
            }
            BlockPos resultPos = blockHitResult.getBlockPos();
            BlockState state = this.level().getBlockState(resultPos);
            SoundEvent event = state.getBlock().getSoundType(state, this.level(), resultPos, this).getBreakSound();
            this.level().playSound(null, result.getLocation().x, result.getLocation().y, result.getLocation().z, event, SoundSource.AMBIENT, 1.0F, 1.0F);
            Vec3 hitVec = result.getLocation();

            if (state.getBlock() instanceof BellBlock bell) {
                bell.attemptToRing(this.level(), resultPos, blockHitResult.getDirection());
            }

            if (state.getBlock() instanceof TargetBlock) {
                if (this.shooter == null) return;

                int rings = getRings(blockHitResult, hitVec);
                double dis = this.shooter.position().distanceTo(hitVec);
                recordHitScore(rings, dis);
            }

            this.onHitBlock(hitVec);
            if (heBullet) {
                explosionBulletBlock(this, damage, heLevel, monsterMultiple + 1, hitVec);
            }
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

    protected void explosionBulletBlock(Entity projectile , float damage, int heLevel, float monsterMultiple, Vec3 hitVec) {
        CustomExplosion explosion = new CustomExplosion(projectile.level(), projectile,
                ModDamageTypes.causeProjectileBoomDamage(projectile.level().registryAccess(), projectile, this.getShooter()), (float) ((2 + 0.1 * damage) * (1 + 0.1 * heLevel)),
                hitVec.x, hitVec.y, hitVec.z, (float)((2.5 + 0.01 * damage) * (1 + 0.05 * heLevel)) , Explosion.BlockInteraction.KEEP).setDamageMultiplier(monsterMultiple);
        explosion.explode();
        net.minecraftforge.event.ForgeEventFactory.onExplosionStart(projectile.level(), explosion);
        explosion.finalizeExplosion(false);
        ParticleTool.spawnSmallExplosionParticles(this.level(), hitVec);
    }

    private static int getRings(@NotNull BlockHitResult blockHitResult, @NotNull Vec3 hitVec) {
        Direction direction = blockHitResult.getDirection();
        double x = Math.abs(Mth.frac(hitVec.x) - 0.5);
        double y = Math.abs(Mth.frac(hitVec.y) - 0.5);
        double z = Math.abs(Mth.frac(hitVec.z) - 0.5);
        Direction.Axis axis = direction.getAxis();
        double v;
        if (axis == Direction.Axis.Y) {
            v = Math.max(x, z);
        } else if (axis == Direction.Axis.Z) {
            v = Math.max(x, y);
        } else {
            v = Math.max(y, z);
        }

        return Math.max(1, Mth.ceil(10.0 * Mth.clamp((0.5 - v) / 0.5, 0.0, 1.0)));
    }

    private void recordHitScore(int score, double distance) {
        if (!(shooter instanceof Player player)) {
            return;
        }

        player.displayClientMessage(Component.literal(String.valueOf(score))
                .append(Component.translatable("des.superbwarfare.shoot.rings"))
                .append(Component.literal(new DecimalFormat("##.#").format(distance) + "m")), false);

        if (!this.shooter.level().isClientSide() && this.shooter instanceof ServerPlayer serverPlayer) {
            var holder = score == 10 ? Holder.direct(ModSounds.HEADSHOT.get()) : Holder.direct(ModSounds.INDICATION.get());
            serverPlayer.connection.send(new ClientboundSoundPacket(holder, SoundSource.PLAYERS, player.getX(), player.getY(), player.getZ(), 1f, 1f, player.level().random.nextLong()));
            ModUtils.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) player), new ClientIndicatorMessage(score == 10 ? 1 : 0, 5));
        }

        ItemStack stack = player.getOffhandItem();
        if (stack.is(ModItems.TRANSCRIPT.get())) {
            final int size = 10;

            ListTag tags = stack.getOrCreateTag().getList(Transcript.TAG_SCORES, Tag.TAG_COMPOUND);

            Queue<CompoundTag> queue = new ArrayDeque<>();
            for (int i = 0; i < tags.size(); i++) {
                queue.add(tags.getCompound(i));
            }

            CompoundTag tag = new CompoundTag();
            tag.putInt("Score", score);
            tag.putDouble("Distance", distance);
            queue.offer(tag);

            while (queue.size() > size) {
                queue.poll();
            }

            ListTag newTags = new ListTag();
            newTags.addAll(queue);

            stack.getOrCreateTag().put(Transcript.TAG_SCORES, newTags);
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
        if (this.shooter == null) return;

        float mMultiple = 1 + this.monsterMultiple;

        if (entity == null) return;

        if (entity instanceof PartEntity<?> part) {
            entity = part.getParent();
        }

        if (beast && entity instanceof LivingEntity living) {
            if (living.isDeadOrDying()) return;
            if (living instanceof TargetEntity) return;

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
                                        shooter == null ? "" : shooter.getDisplayName()
                                )
                        )
                );
            } else {
                living.setHealth(0);
                living.level().broadcastEntityEvent(living, (byte) 60);
                living.remove(RemovalReason.KILLED);
                living.gameEvent(GameEvent.ENTITY_DIE);
            }

            level().playSound(living, new BlockPos((int) living.getX(), (int) living.getY(), (int) living.getZ()), ModSounds.OUCH.get(), SoundSource.PLAYERS, 2.0F, 1.0F);
            return;
        }

        if (entity instanceof Monster) {
            this.damage *= mMultiple;
        }

        if (entity instanceof LivingEntity living && living.getMobType() == MobType.UNDEAD) {
            this.damage *= this.undeadMultiple;
        }

        if (entity instanceof LivingEntity living && jhpBullet) {
            this.damage *= (1.0f + 0.12f * jhpLevel) * ((float)(10 / (living.getAttributeValue(Attributes.ARMOR) + 10)) + 0.25f);
        }

        if (heBullet) {
            explosionBulletEntity(this, entity, damage, heLevel, mMultiple);
        }

        if (headshot) {
            if (!this.shooter.level().isClientSide() && this.shooter instanceof ServerPlayer player) {
                var holder = Holder.direct(ModSounds.HEADSHOT.get());
                player.connection.send(new ClientboundSoundPacket(holder, SoundSource.PLAYERS, player.getX(), player.getY(), player.getZ(), 1f, 1f, player.level().random.nextLong()));

                ModUtils.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> player), new ClientIndicatorMessage(1, 5));
            }

            performDamage(entity, this.damage, true);
        } else {
            if (!this.shooter.level().isClientSide() && this.shooter instanceof ServerPlayer player) {
                var holder = Holder.direct(ModSounds.INDICATION.get());
                player.connection.send(new ClientboundSoundPacket(holder, SoundSource.PLAYERS, player.getX(), player.getY(), player.getZ(), 1f, 1f, player.level().random.nextLong()));

                ModUtils.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> player), new ClientIndicatorMessage(0, 5));
            }

            if (legShot) {
                if (entity instanceof LivingEntity living) {
                    if (living instanceof Player player && player.isCreative()) {
                        return;
                    }
                    if (!living.level().isClientSide()) {
                        living.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20, 2, false, false));
                    }
                }
                this.damage *= this.legShot;
            }

            performDamage(entity, this.damage, false);
        }

        if (this.mobEffect.get() != null && entity instanceof LivingEntity living) {
            living.addEffect(this.mobEffect.get(), this.shooter);
        }

        this.discard();
    }

    protected void explosionBulletEntity(Entity projectile, Entity target, float damage, int heLevel, float monsterMultiple) {
        CustomExplosion explosion = new CustomExplosion(projectile.level(), projectile,
                ModDamageTypes.causeProjectileBoomDamage(projectile.level().registryAccess(), projectile, this.getShooter()), (float) ((2 + 0.1 * damage) * (1 + 0.1 * heLevel)),
                target.getX(), target.getY(), target.getZ(), (float)((2.5 + 0.01 * damage) * (1 + 0.05 * heLevel)) , Explosion.BlockInteraction.KEEP).setDamageMultiplier(monsterMultiple);
        explosion.explode();
        net.minecraftforge.event.ForgeEventFactory.onExplosionStart(projectile.level(), explosion);
        explosion.finalizeExplosion(false);
        ParticleTool.spawnSmallExplosionParticles(target.level(), target.position());
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
        if (!startVec.equals(endVec)) {
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
        }
        return p_217300_2_.apply(context);
    }

    public LivingEntity getShooter() {
        return this.shooter;
    }

    public int getShooterId() {
        return this.shooterId;
    }

    public float getBypassArmorRate() {
        return this.bypassArmorRate;
    }

    public void updateHeading() {
        double horizontalDistance = this.getDeltaMovement().horizontalDistance();
        this.setYRot((float) (Mth.atan2(this.getDeltaMovement().x(), this.getDeltaMovement().z()) * (180D / Math.PI)));
        this.setXRot((float) (Mth.atan2(this.getDeltaMovement().y(), horizontalDistance) * (180D / Math.PI)));
        this.yRotO = this.getYRot();
        this.xRotO = this.getXRot();
    }

    private void performDamage(Entity entity, float damage, boolean headshot) {
        float rate = Mth.clamp(this.bypassArmorRate, 0, 1);

        float normalDamage = damage * Mth.clamp(1 - rate, 0, 1);
        float absoluteDamage = damage * Mth.clamp(rate, 0, 1);

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

    /**
     * Builders
     */
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

    public ProjectileEntity monsterMultiple(float monsterMultiple) {
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

    public ProjectileEntity jhpBullet(boolean jhpBullet, int jhpLevel) {
        this.jhpBullet = true;
        this.jhpLevel = jhpLevel;
        return this;
    }

    public ProjectileEntity heBullet(boolean heBullet, int heLevel) {
        this.heBullet = true;
        this.heLevel = heLevel;
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

    public ProjectileEntity undeadMultiple(float undeadMultiple) {
        this.undeadMultiple = undeadMultiple;
        return this;
    }

    public ProjectileEntity effect(Supplier<MobEffectInstance> supplier) {
        this.mobEffect = supplier;
        return this;
    }

    public void setRGB(float[] rgb) {
        this.entityData.set(COLOR_R, rgb[0]);
        this.entityData.set(COLOR_G, rgb[1]);
        this.entityData.set(COLOR_B, rgb[2]);
    }
}
