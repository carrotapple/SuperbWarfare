package com.atsuishio.superbwarfare.item;

import com.atsuishio.superbwarfare.client.renderer.item.LungeMineRenderer;
import com.atsuishio.superbwarfare.config.server.ExplosionDestroyConfig;
import com.atsuishio.superbwarfare.init.ModDamageTypes;
import com.atsuishio.superbwarfare.init.ModItems;
import com.atsuishio.superbwarfare.tools.CustomExplosion;
import com.atsuishio.superbwarfare.tools.ParticleTool;
import com.atsuishio.superbwarfare.tools.TraceTool;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Consumer;

public class LungeMine extends Item implements GeoItem, AnimatedItem {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    public static ItemDisplayContext transformType;

    public LungeMine() {
        super(new Properties().stacksTo(1));
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        super.initializeClient(consumer);
        consumer.accept(new IClientItemExtensions() {
            private final BlockEntityWithoutLevelRenderer renderer = new LungeMineRenderer();

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return renderer;
            }

            private static final HumanoidModel.ArmPose LungeMinePose = HumanoidModel.ArmPose.create("LungeMine", false, (model, entity, arm) -> {
                if (arm != HumanoidArm.LEFT) {
                    model.rightArm.xRot = -22.5f * Mth.DEG_TO_RAD + model.head.xRot;
                    model.rightArm.yRot = -10f * Mth.DEG_TO_RAD;
                    model.leftArm.xRot = -55f * Mth.DEG_TO_RAD + model.head.xRot;
                    model.leftArm.yRot = 50f * Mth.DEG_TO_RAD;
                }
            });

            @Override
            public HumanoidModel.ArmPose getArmPose(LivingEntity entityLiving, InteractionHand hand, ItemStack itemStack) {
                if (!itemStack.isEmpty()) {
                    if (entityLiving.getUsedItemHand() == hand) {
                        return LungeMinePose;
                    }
                }
                return HumanoidModel.ArmPose.EMPTY;
            }
        });
    }

    public static void attack(ItemStack stack, Player player) {
        if (stack.getOrCreateTag().getInt("AttackTime") == 0) {
            stack.getOrCreateTag().putInt("AttackTime" , 6);
            player.getCooldowns().addCooldown(stack.getItem(), 6);
            player.level().playSound(null, player.getOnPos(), SoundEvents.PLAYER_ATTACK_SWEEP, SoundSource.PLAYERS, 1, 1);
        }

    }

    public void getTransformType(ItemDisplayContext type) {
        transformType = type;
    }

    private PlayState idlePredicate(AnimationState<LungeMine> event) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return PlayState.STOP;
        ItemStack stack = player.getMainHandItem();
        if (stack.is(ModItems.LUNGE_MINE.get())) {
            if (stack.getOrCreateTag().getInt("AttackTime") > 0) {
                return event.setAndContinue(RawAnimation.begin().thenPlay("animation.lunge_mine.fire"));
            }
        }
        return event.setAndContinue(RawAnimation.begin().thenLoop("animation.lunge_mine.idle"));
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar data) {
        var idleController = new AnimationController<>(this, "idleController", 0, this::idlePredicate);
        data.add(idleController);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    public void setAnimationProcedure(String procedure) {
    }

    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entity) {
        return false;
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return false;
    }

    @Override
    public boolean canAttackBlock(BlockState p_41441_, Level p_41442_, BlockPos p_41443_, Player p_41444_) {
        return false;
    }

    @Override
    public void inventoryTick(ItemStack itemstack, Level world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(itemstack, world, entity, slot, selected);

        if (itemstack.getOrCreateTag().getInt("AttackTime") >= 3 && itemstack.getOrCreateTag().getInt("AttackTime") <= 4) {
            if (selected && entity.level() instanceof ServerLevel) {
                boolean lookAtEntity = false;

                Entity lookingEntity = TraceTool.findLookingEntity(entity,4.5);

                HitResult result = entity.level().clip(new ClipContext(entity.getEyePosition(), entity.getEyePosition().add(entity.getLookAngle().scale(4.5)),
                        ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, entity));

                Vec3 looking = Vec3.atLowerCornerOf(entity.level().clip(new ClipContext(entity.getEyePosition(), entity.getEyePosition().add(entity.getLookAngle().scale(4)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, entity)).getBlockPos());
                BlockState blockState = entity.level().getBlockState(BlockPos.containing(looking.x(), looking.y(), looking.z()));

                Vec3 hitPos = result.getLocation();

                if (lookingEntity != null) {
                    lookAtEntity = true;
                }

//                if (entity instanceof Player player) {
//                    player.displayClientMessage(Component.literal("" + lookAtEntity), true);
//                }

                if (lookAtEntity) {
                    if (entity instanceof Player player && !player.isCreative()) {
                        itemstack.shrink(1);
                    }
                    lookingEntity.hurt(ModDamageTypes.causeCannonFireDamage(entity.level().registryAccess(), entity, entity), 150);
                    causeLungeMineExplode(entity.level(), entity, lookingEntity);
                } else if (blockState.canOcclude()) {
                    if (entity instanceof Player player && !player.isCreative()) {
                        itemstack.shrink(1);
                    }
                    CustomExplosion explosion = new CustomExplosion(entity.level(), null,
                            ModDamageTypes.causeProjectileBoomDamage(entity.level().registryAccess(), entity, entity), 60,
                            looking.x, looking.y, looking.z, 4f, ExplosionDestroyConfig.EXPLOSION_DESTROY.get() ? Explosion.BlockInteraction.DESTROY : Explosion.BlockInteraction.KEEP).setDamageMultiplier(1.25f);
                    explosion.explode();
                    net.minecraftforge.event.ForgeEventFactory.onExplosionStart(entity.level(), explosion);
                    explosion.finalizeExplosion(false);
                    ParticleTool.spawnMediumExplosionParticles(entity.level(), hitPos);
                }
            }
        }

        if (itemstack.getOrCreateTag().getInt("AttackTime") > 0) {
            itemstack.getOrCreateTag().putInt("AttackTime", itemstack.getOrCreateTag().getInt("AttackTime") - 1);
        }
    }

    public static void causeLungeMineExplode(Level pLevel, Entity entity, Entity pLivingEntity) {
        CustomExplosion explosion = new CustomExplosion(pLevel, pLivingEntity,
                ModDamageTypes.causeProjectileBoomDamage(pLevel.registryAccess(), pLivingEntity, entity), 60,
                pLivingEntity.getX(), pLivingEntity.getY(), pLivingEntity.getZ(), 4f, ExplosionDestroyConfig.EXPLOSION_DESTROY.get() ? Explosion.BlockInteraction.DESTROY : Explosion.BlockInteraction.KEEP).setDamageMultiplier(1.25f);
        explosion.explode();
        net.minecraftforge.event.ForgeEventFactory.onExplosionStart(pLevel, explosion);
        explosion.finalizeExplosion(false);
        ParticleTool.spawnMediumExplosionParticles(pLevel, pLivingEntity.position());
    }
}