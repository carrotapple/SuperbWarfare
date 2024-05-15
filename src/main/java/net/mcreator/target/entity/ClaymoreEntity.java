package net.mcreator.target.entity;

import net.mcreator.target.TargetMod;
import net.mcreator.target.init.TargetModEntities;
import net.mcreator.target.init.TargetModItems;
import net.mcreator.target.procedures.MedexpProcedure;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
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

import java.util.Comparator;

// TODO 重写阔剑地雷
@Mod.EventBusSubscriber
public class ClaymoreEntity extends TamableAnimal implements GeoEntity, AnimatedEntity {
    public static final EntityDataAccessor<Boolean> SHOOT = SynchedEntityData.defineId(ClaymoreEntity.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<String> ANIMATION = SynchedEntityData.defineId(ClaymoreEntity.class, EntityDataSerializers.STRING);
    public static final EntityDataAccessor<String> TEXTURE = SynchedEntityData.defineId(ClaymoreEntity.class, EntityDataSerializers.STRING);
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private boolean swinging;
    private boolean lastloop;
    private long lastSwing;
    public String animationProcedure = "empty";

    public ClaymoreEntity(PlayMessages.SpawnEntity packet, Level world) {
        this(TargetModEntities.CLAYMORE.get(), world);
    }

    public ClaymoreEntity(EntityType<ClaymoreEntity> type, Level world) {
        super(type, world);
        xpReward = 0;
        setNoAi(false);
        setPersistenceRequired();
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(SHOOT, false);
        this.entityData.define(ANIMATION, "undefined");
        this.entityData.define(TEXTURE, "claymore");
    }

    public void setTexture(String texture) {
        this.entityData.set(TEXTURE, texture);
    }

    public String getTexture() {
        return this.entityData.get(TEXTURE);
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();

    }

    @Override
    public MobType getMobType() {
        return super.getMobType();
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (source.is(DamageTypes.IN_FIRE))
            return false;
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
        if (source.is(DamageTypes.EXPLOSION))
            return false;
        if (source.is(DamageTypes.DRAGON_BREATH))
            return false;
        if (source.is(DamageTypes.WITHER))
            return false;
        if (source.is(DamageTypes.WITHER_SKULL))
            return false;
        return super.hurt(source, amount);
    }

    @Override
    public void die(DamageSource source) {
        super.die(source);

        if (level() instanceof ServerLevel server) {
            server.explode(null, this.getX(), this.getY(), this.getZ(), 6.5f, Level.ExplosionInteraction.NONE);
            server.explode(this, this.getX(), this.getY(), this.getZ(), 6.5f, Level.ExplosionInteraction.NONE);

            this.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, this.position(), this.getRotationVector(), this.level() instanceof ServerLevel ? (ServerLevel) this.level() : null, 4,
                    this.getName().getString(), this.getDisplayName(), this.level().getServer(), this), "target:mediumexp");
            this.discard();
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putString("Texture", this.getTexture());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains("Texture"))
            this.setTexture(compound.getString("Texture"));
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        InteractionResult retval = InteractionResult.sidedSuccess(this.level().isClientSide());
        Item item = itemstack.getItem();
        if (itemstack.getItem() instanceof SpawnEggItem) {
            retval = super.mobInteract(player, hand);
        } else if (this.level().isClientSide()) {
            retval = (this.isTame() && this.isOwnedBy(player) || this.isFood(itemstack)) ? InteractionResult.sidedSuccess(this.level().isClientSide()) : InteractionResult.PASS;
        } else {
            if (this.isTame()) {
                if (this.isOwnedBy(player)) {
                    if (item.isEdible() && this.isFood(itemstack) && this.getHealth() < this.getMaxHealth()) {
                        this.usePlayerItem(player, hand, itemstack);
                        this.heal((float) item.getFoodProperties().getNutrition());
                        retval = InteractionResult.sidedSuccess(this.level().isClientSide());
                    } else if (this.isFood(itemstack) && this.getHealth() < this.getMaxHealth()) {
                        this.usePlayerItem(player, hand, itemstack);
                        this.heal(4);
                        retval = InteractionResult.sidedSuccess(this.level().isClientSide());
                    } else {
                        retval = super.mobInteract(player, hand);
                    }
                }
            } else if (this.isFood(itemstack)) {
                this.usePlayerItem(player, hand, itemstack);
                if (this.random.nextInt(3) == 0 && !net.minecraftforge.event.ForgeEventFactory.onAnimalTame(this, player)) {
                    this.tame(player);
                    this.level().broadcastEntityEvent(this, (byte) 7);
                } else {
                    this.level().broadcastEntityEvent(this, (byte) 6);
                }
                this.setPersistenceRequired();
                retval = InteractionResult.sidedSuccess(this.level().isClientSide());
            } else {
                retval = super.mobInteract(player, hand);
                if (retval == InteractionResult.SUCCESS || retval == InteractionResult.CONSUME)
                    this.setPersistenceRequired();
            }
        }

        if (this.isOwnedBy(player) && player.isShiftKeyDown()) {
            if (!this.level().isClientSide()) this.discard();
            ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(TargetModItems.CLAYMORE_MINE.get()));
        }

        return retval;
    }

    @Override
    public void baseTick() {
        super.baseTick();
        var data = this.getPersistentData();
        var level = this.level();
        var x = this.getX();
        var y = this.getY();
        var z = this.getZ();

        if (data.getDouble("claymore") > 0) {
            data.putDouble("claymore", data.getDouble("claymore") - 1);
        }

        data.putDouble("life", data.getDouble("life") + 1);
        if (data.getDouble("life") >= 12000) {
            if (!this.level().isClientSide()) this.discard();
        }
        if (data.getDouble("def") >= 100) {
            if (!this.level().isClientSide() && this.getServer() != null) {
                this.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, this.position(), this.getRotationVector(), this.level() instanceof ServerLevel ? (ServerLevel) this.level() : null, 4,
                        this.getName().getString(), this.getDisplayName(), this.level().getServer(), this), "playsound minecraft:item.shield.break player @p ~ ~ ~ 1 1");
            }
            if (!this.level().isClientSide()) this.discard();

            if (!level.isClientSide()) {
                level.playSound(null, BlockPos.containing(x, y, z), SoundEvents.SHIELD_BREAK, SoundSource.PLAYERS, 1, 1);
            } else {
                level.playLocalSound(x, y, z, SoundEvents.SHIELD_BREAK, SoundSource.PLAYERS, 1, 1, false);
            }

            if (level instanceof ServerLevel server) {
                ItemEntity entityToSpawn = new ItemEntity(server, x, y, z, new ItemStack(TargetModItems.CLAYMORE_MINE.get()));
                entityToSpawn.setPickUpDelay(10);
                server.addFreshEntity(entityToSpawn);
            }
        }
        this.removeAllEffects();
        this.clearFire();
        if (data.getDouble("trigger") <= 60) {
            data.putDouble("trigger", data.getDouble("trigger") + 1);
        }
        if (data.getDouble("trigger") >= 40) {
            final Vec3 center = new Vec3(x + 1.5 * this.getLookAngle().x, y + 1.5 * this.getLookAngle().y, z + 1.5 * this.getLookAngle().z);
            for (Entity target : level.getEntitiesOfClass(Entity.class, new AABB(center, center).inflate(2.5 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(e -> e.distanceToSqr(center))).toList()) {
                var condition = this.getOwner() != target
                        && target instanceof LivingEntity
                        && !(target instanceof ClaymoreEntity)
                        && !(target instanceof Target1Entity)
                        && !(target instanceof Player player && (player.isCreative() || player.isSpectator()))
                        && (!this.isAlliedTo(target) || target.getTeam() == null || target.getTeam().getName().equals("TDM"))
                        && !target.isShiftKeyDown();
                if (!condition) continue;

                if (!level.isClientSide()) {
                    MedexpProcedure.execute(this.level(), this.getX(), this.getY(), this.getZ());
                    this.discard();
                }
                target.getPersistentData().putDouble("claymore", 5);
                TargetMod.queueServerWork(1, () -> {
                    if (!level.isClientSide())
                        level.explode(this.getOwner(), target.getX(), target.getY(), target.getZ(), 6.5f, Level.ExplosionInteraction.NONE);
                });
            }
        }

        this.refreshDimensions();
    }

    @Override
    public EntityDimensions getDimensions(Pose p_33597_) {
        return super.getDimensions(p_33597_).scale((float) 0.5);
    }

    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverWorld, AgeableMob ageable) {
        ClaymoreEntity retval = TargetModEntities.CLAYMORE.get().create(serverWorld);
        if (retval != null) {
            retval.finalizeSpawn(serverWorld, serverWorld.getCurrentDifficultyAt(retval.blockPosition()), MobSpawnType.BREEDING, null, null);
        }
        return retval;
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return false;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    protected void doPush(Entity entityIn) {
    }

    @Override
    protected void pushEntities() {
    }

    @Override
    public void aiStep() {
        super.aiStep();
        this.updateSwingTime();
    }

    public static void init() {
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0)
                .add(Attributes.MAX_HEALTH, 20)
                .add(Attributes.ARMOR, 0)
                .add(Attributes.ATTACK_DAMAGE, 0)
                .add(Attributes.FOLLOW_RANGE, 16)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1);
    }

    private PlayState movementPredicate(AnimationState event) {
        if (this.animationProcedure.equals("empty")) {
            return event.setAndContinue(RawAnimation.begin().thenLoop("animation.claymore.idle"));
        }
        return PlayState.STOP;
    }

    private PlayState procedurePredicate(AnimationState event) {
        if (!animationProcedure.equals("empty") && event.getController().getAnimationState() == AnimationController.State.STOPPED) {
            event.getController().setAnimation(RawAnimation.begin().thenPlay(this.animationProcedure));
            if (event.getController().getAnimationState() == AnimationController.State.STOPPED) {
                this.animationProcedure = "empty";
                event.getController().forceAnimationReset();
            }
        } else if (animationProcedure.equals("empty")) {
            return PlayState.STOP;
        }
        return PlayState.CONTINUE;
    }

    @Override
    protected void tickDeath() {
        ++this.deathTime;
        if (this.deathTime == 1) {
            this.remove(ClaymoreEntity.RemovalReason.KILLED);
            this.dropExperience();
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
        data.add(new AnimationController<>(this, "movement", 4, this::movementPredicate));
        data.add(new AnimationController<>(this, "procedure", 4, this::procedurePredicate));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @SubscribeEvent
    public static void onEntityAttacked(LivingAttackEvent event) {
        var damagesource = event.getSource();
        var entity = event.getEntity();
        var sourceentity = event.getSource().getEntity();
        if (damagesource == null || entity == null || sourceentity == null) return;

        if (entity instanceof ClaymoreEntity tamEnt && tamEnt.getOwner() == sourceentity) {
            if (tamEnt.getOwner() instanceof Player player && player.isCreative()) {
                if (entity instanceof ClaymoreEntity claymore && damagesource.is(ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation("target:deleted_mod_element")))) {
                    entity.setYRot(sourceentity.getYRot());
                    entity.setXRot(entity.getXRot());
                    entity.setYBodyRot(entity.getYRot());
                    entity.setYHeadRot(entity.getYRot());
                    entity.yRotO = entity.getYRot();
                    entity.xRotO = entity.getXRot();
                    claymore.yBodyRotO = claymore.getYRot();
                    claymore.yHeadRotO = claymore.getYRot();
                }
                event.setCanceled(true);
            }
        }
    }
}
