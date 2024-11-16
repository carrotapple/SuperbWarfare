package net.mcreator.superbwarfare.entity;

import net.mcreator.superbwarfare.ModUtils;
import net.mcreator.superbwarfare.client.gui.RangeHelper;
import net.mcreator.superbwarfare.entity.projectile.MortarShellEntity;
import net.mcreator.superbwarfare.init.ModEntities;
import net.mcreator.superbwarfare.init.ModItems;
import net.mcreator.superbwarfare.init.ModParticleTypes;
import net.mcreator.superbwarfare.init.ModSounds;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import static net.mcreator.superbwarfare.tools.ParticleTool.sendParticle;

public class MortarEntity extends Entity implements GeoEntity, AnimatedEntity {
    public static final EntityDataAccessor<Integer> FIRE_TIME = SynchedEntityData.defineId(MortarEntity.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Float> PITCH = SynchedEntityData.defineId(MortarEntity.class, EntityDataSerializers.FLOAT);
    public static final EntityDataAccessor<Float> Y_ROT = SynchedEntityData.defineId(MortarEntity.class, EntityDataSerializers.FLOAT);
    public static final EntityDataAccessor<Float> HEALTH = SynchedEntityData.defineId(MortarEntity.class, EntityDataSerializers.FLOAT);

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    protected int interpolationSteps;
    protected double serverYRot;
    protected double serverXRot;

    public MortarEntity(PlayMessages.SpawnEntity packet, Level world) {
        this(ModEntities.MORTAR.get(), world);
    }

    public MortarEntity(EntityType<MortarEntity> type, Level world) {
        super(type, world);
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(FIRE_TIME, 0);
        this.entityData.define(PITCH, 70f);
        this.entityData.define(Y_ROT, 0f);
        this.entityData.define(HEALTH, 100f);
    }

    @Override
    public boolean isPickable() {
        return !this.isRemoved();
    }

    @Override
    protected float getEyeHeight(Pose pPose, EntityDimensions pSize) {
        return 0.2F;
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (source.getDirectEntity() instanceof ThrownPotion || source.getDirectEntity() instanceof AreaEffectCloud)
            return false;
        if (source.is(DamageTypes.FALL))
            return false;
        if (source.is(DamageTypes.CACTUS))
            return false;
        if (source.is(DamageTypes.DROWN))
            return false;
        if (source.is(DamageTypes.LIGHTNING_BOLT))
            return false;
        if (source.is(DamageTypes.FALLING_ANVIL))
            return false;
        if (source.is(DamageTypes.DRAGON_BREATH))
            return false;
        if (source.is(DamageTypes.WITHER))
            return false;
        if (source.is(DamageTypes.WITHER_SKULL))
            return false;

        if (this.level() instanceof ServerLevel serverLevel) {
            sendParticle(serverLevel, ModParticleTypes.FIRE_STAR.get(), this.getX(), this.getY() + 1, this.getZ(), 2, 0.05, 0.05, 0.05, 0.1, false);
        }
        this.level().playSound(null, this.getOnPos(), ModSounds.HIT.get(), SoundSource.PLAYERS, 1, 1);
        this.entityData.set(HEALTH, this.entityData.get(HEALTH) - amount);

        return true;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        compound.putInt("FireTime", this.entityData.get(FIRE_TIME));
        compound.putFloat("Pitch", this.entityData.get(PITCH));
        compound.putFloat("YRot", this.entityData.get(Y_ROT));
        compound.putFloat("Health", this.entityData.get(HEALTH));
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        if (compound.contains("FireTime")) {
            this.entityData.set(FIRE_TIME, compound.getInt("FireTime"));
        }
        if (compound.contains("Pitch")) {
            this.entityData.set(PITCH, compound.getFloat("Pitch"));
        }
        if (compound.contains("YRot")) {
            this.entityData.set(Y_ROT, compound.getFloat("YRot"));
        }
        if (compound.contains("Health")) {
            this.entityData.set(HEALTH, compound.getFloat("Health"));
        }
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {
        ItemStack mainHandItem = player.getMainHandItem();

        if (mainHandItem.getItem() == ModItems.MORTAR_SHELLS.get() && !player.isShiftKeyDown() && this.entityData.get(FIRE_TIME) == 0) {
            this.entityData.set(FIRE_TIME, 25);

            if (!player.isCreative()) {
                player.getInventory().clearOrCountMatchingItems(p -> ModItems.MORTAR_SHELLS.get() == p.getItem(), 1, player.inventoryMenu.getCraftSlots());
            }
            if (!this.level().isClientSide()) {
                this.level().playSound(null, this.getX(), this.getY(), this.getZ(), ModSounds.MORTAR_LOAD.get(), SoundSource.PLAYERS, 1f, 1f);
                this.level().playSound(null, this.getX(), this.getY(), this.getZ(), ModSounds.MORTAR_FIRE.get(), SoundSource.PLAYERS, 8f, 1f);
                this.level().playSound(null, this.getX(), this.getY(), this.getZ(), ModSounds.MORTAR_DISTANT.get(), SoundSource.PLAYERS, 32f, 1f);
            }
            ModUtils.queueServerWork(20, () -> {
                Level level = this.level();
                if (level instanceof ServerLevel server) {
                    MortarShellEntity entityToSpawn = new MortarShellEntity(ModEntities.MORTAR_SHELL.get(), player, level);
                    entityToSpawn.setPos(this.getX(), this.getEyeY(), this.getZ());
                    entityToSpawn.shoot(this.getLookAngle().x, this.getLookAngle().y, this.getLookAngle().z, 8, (float) 0.9);
                    level.addFreshEntity(entityToSpawn);
                    server.sendParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE, (this.getX() + 3 * this.getLookAngle().x), (this.getY() + 0.1 + 3 * this.getLookAngle().y), (this.getZ() + 3 * this.getLookAngle().z), 8, 0.4, 0.4, 0.4,
                            0.007);
                    server.sendParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE, this.getX(), this.getY(), this.getZ(), 50, 2, 0.02, 2, 0.0005);
                }
            });
        }

        if (player.getMainHandItem().getItem() == ModItems.FIRING_PARAMETERS.get()) {
            if (setTarget(player.getMainHandItem())) {
                player.swing(InteractionHand.MAIN_HAND);
                return InteractionResult.SUCCESS;
            } else {
                player.displayClientMessage(Component.translatable("des.superbwarfare.target.warn").withStyle(ChatFormatting.RED), true);
                return InteractionResult.FAIL;
            }
        }
        if (player.getOffhandItem().getItem() == ModItems.FIRING_PARAMETERS.get()) {
            if (setTarget(player.getOffhandItem())) {
                player.swing(InteractionHand.OFF_HAND);
                return InteractionResult.SUCCESS;
            } else {
                player.displayClientMessage(Component.translatable("des.superbwarfare.target.warn").withStyle(ChatFormatting.RED), true);
                return InteractionResult.FAIL;
            }
        }

        if (player.isShiftKeyDown()) {
            if (mainHandItem.getItem() == ModItems.CROWBAR.get()) {
                this.discard();
                ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(ModItems.MORTAR_DEPLOYER.get()));
                return InteractionResult.SUCCESS;
            }
            this.entityData.set(Y_ROT, Mth.clamp(player.getYRot(), -180, 180));
        }

        return InteractionResult.SUCCESS;
    }

    public boolean setTarget(ItemStack stack) {
        int targetX = stack.getOrCreateTag().getInt("TargetX");
        int targetY = stack.getOrCreateTag().getInt("TargetY");
        int targetZ = stack.getOrCreateTag().getInt("TargetZ");

        this.look(EntityAnchorArgument.Anchor.EYES, new Vec3(targetX, targetY, targetZ));

        double[] angles = new double[2];
        boolean flag = RangeHelper.canReachTarget(8.0, 0.05, 0.99,
                new BlockPos((int) this.getX(), (int) this.getEyeY(), (int) this.getZ()),
                new BlockPos(targetX, targetY, targetZ),
                angles);

        if (flag) {
            this.entityData.set(PITCH, (float) angles[1]);
        }

        return flag;
    }

    private void look(EntityAnchorArgument.Anchor pAnchor, Vec3 pTarget) {
        Vec3 vec3 = pAnchor.apply(this);
        double d0 = (pTarget.x - vec3.x) * 0.2;
        double d2 = (pTarget.z - vec3.z) * 0.2;
        this.entityData.set(Y_ROT, Mth.wrapDegrees((float) (Mth.atan2(d2, d0) * 57.2957763671875) - 90.0F));
    }

    @Override
    public void lerpTo(double x, double y, double z, float yaw, float pitch, int interpolationSteps, boolean interpolate) {
        serverXRot = pitch;
        this.interpolationSteps = 10;
    }

    @Override
    public void baseTick() {
        super.baseTick();
        if (this.entityData.get(FIRE_TIME) > 0) {
            this.entityData.set(FIRE_TIME, this.entityData.get(FIRE_TIME) - 1);
        }
        this.setXRot(-Mth.clamp(entityData.get(PITCH), 20, 89));
        this.xRotO = this.getXRot();
        this.setYRot(entityData.get(Y_ROT));
        this.setYBodyRot(this.getYRot());
        this.setYHeadRot(this.getYRot());
        this.yRotO = this.getYRot();
        this.setRot(this.getYRot(), this.getXRot());

        this.setDeltaMovement(this.getDeltaMovement().add(0.0, -0.04, 0.0));

        if (!this.level().noCollision(this.getBoundingBox())) {
            this.moveTowardsClosestSpace(this.getX(), (this.getBoundingBox().minY + this.getBoundingBox().maxY) / 2.0, this.getZ());
        }

        this.move(MoverType.SELF, this.getDeltaMovement());
        float f = 0.98F;
        if (this.onGround()) {
            BlockPos pos = this.getBlockPosBelowThatAffectsMyMovement();
            f = this.level().getBlockState(pos).getFriction(this.level(), pos, this) * 0.98F;
        }

        this.setDeltaMovement(this.getDeltaMovement().multiply(f, 0.98, f));
        if (this.onGround()) {
            this.setDeltaMovement(this.getDeltaMovement().multiply(1.0, -0.9, 1.0));
        }

        if (this.entityData.get(HEALTH) <= 0) {
            destroy();
        }
        this.refreshDimensions();
    }

    private PlayState movementPredicate(AnimationState<MortarEntity> event) {
        if (this.entityData.get(FIRE_TIME) > 0) {
            return event.setAndContinue(RawAnimation.begin().thenLoop("animation.mortar.fire"));
        }
        return event.setAndContinue(RawAnimation.begin().thenLoop("animation.mortar.idle"));
    }

    protected void destroy() {
        if (this.level() instanceof ServerLevel level) {
            var x = this.getX();
            var y = this.getY();
            var z = this.getZ();
            level.explode(null, x, y, z, 0, Level.ExplosionInteraction.NONE);
            ItemEntity mortar = new ItemEntity(level, x, (y + 1), z, new ItemStack(ModItems.MORTAR_DEPLOYER.get()));
            mortar.setPickUpDelay(10);
            level.addFreshEntity(mortar);
            this.discard();
        }
    }

    public String getSyncedAnimation() {
        return null;
    }

    public void setAnimation(String animation) {
    }

    @Override
    public void setAnimationProcedure(String procedure) {
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar data) {
        data.add(new AnimationController<>(this, "movement", 0, this::movementPredicate));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}
