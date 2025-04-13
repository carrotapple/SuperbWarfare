package com.atsuishio.superbwarfare.item.gun.machinegun;

import com.atsuishio.superbwarfare.Mod;
import com.atsuishio.superbwarfare.client.renderer.item.MinigunItemRenderer;
import com.atsuishio.superbwarfare.event.ClientEventHandler;
import com.atsuishio.superbwarfare.init.ModParticleTypes;
import com.atsuishio.superbwarfare.init.ModPerks;
import com.atsuishio.superbwarfare.init.ModTags;
import com.atsuishio.superbwarfare.item.gun.GunItem;
import com.atsuishio.superbwarfare.item.gun.data.GunData;
import com.atsuishio.superbwarfare.perk.Perk;
import com.atsuishio.superbwarfare.tools.ItemNBTTool;
import com.atsuishio.superbwarfare.tools.ParticleTool;
import com.atsuishio.superbwarfare.tools.RarityTool;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.joml.Vector3d;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Consumer;

public class MinigunItem extends GunItem implements GeoItem {
    @Override
    public int getCustomRPM(ItemStack stack) {
        return GunData.from(stack).data().getInt("CustomRPM");
    }

    private static final String TAG_HEAT = "heat";
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    public static ItemDisplayContext transformType;

    public MinigunItem() {
        super(new Item.Properties().stacksTo(1).rarity(RarityTool.LEGENDARY));
    }

    @Override
    public boolean isBarVisible(ItemStack pStack) {
        return ItemNBTTool.getDouble(pStack, TAG_HEAT, 0) != 0;
    }

    @Override
    public int getBarWidth(ItemStack pStack) {
        return Math.round((float) ItemNBTTool.getDouble(pStack, TAG_HEAT, 0) * 13.0F / 51F);
    }

    @Override
    public int getBarColor(ItemStack pStack) {
        double f = 1 - ItemNBTTool.getDouble(pStack, TAG_HEAT, 0) / 55.0F;
        return Mth.hsvToRgb((float) f / 3.0F, 1.0F, 1.0F);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        super.initializeClient(consumer);
        consumer.accept(new IClientItemExtensions() {
            private final BlockEntityWithoutLevelRenderer renderer = new MinigunItemRenderer();

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return renderer;
            }

            private static final HumanoidModel.ArmPose MinigunPose = HumanoidModel.ArmPose.create("Minigun", false, (model, entity, arm) -> {
                if (arm != HumanoidArm.LEFT) {
                    model.rightArm.xRot = -22.5f * Mth.DEG_TO_RAD + model.head.xRot;
                    model.rightArm.yRot = -10f * Mth.DEG_TO_RAD;
                    model.leftArm.xRot = -45f * Mth.DEG_TO_RAD + model.head.xRot;
                    model.leftArm.yRot = 40f * Mth.DEG_TO_RAD;
                }
            });

            @Override
            public HumanoidModel.ArmPose getArmPose(LivingEntity entityLiving, InteractionHand hand, ItemStack itemStack) {
                if (!itemStack.isEmpty()) {
                    if (entityLiving.getUsedItemHand() == hand) {
                        return MinigunPose;
                    }
                }
                return HumanoidModel.ArmPose.EMPTY;
            }
        });
    }

    public void getTransformType(ItemDisplayContext type) {
        transformType = type;
    }

    private PlayState idlePredicate(AnimationState<MinigunItem> event) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return PlayState.STOP;
        ItemStack stack = player.getMainHandItem();
        if (!stack.is(ModTags.Items.GUN)) return PlayState.STOP;

        if (player.isSprinting() && player.onGround() && ClientEventHandler.cantSprint == 0 && ClientEventHandler.drawTime < 0.01) {
            if (ClientEventHandler.tacticalSprint) {
                return event.setAndContinue(RawAnimation.begin().thenLoop("animation.minigun.run_fast"));
            } else {
                return event.setAndContinue(RawAnimation.begin().thenLoop("animation.minigun.run"));
            }
        }
        return event.setAndContinue(RawAnimation.begin().thenLoop("animation.minigun.idle"));
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar data) {
        var idleController = new AnimationController<>(this, "idleController", 6, this::idlePredicate);
        data.add(idleController);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    public void inventoryTick(ItemStack stack, Level world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);

        float yRot = entity.getYRot();
        if (yRot < 0) {
            yRot += 360;
        }
        yRot = yRot + 90 % 360;

        var leftPos = new Vector3d(1.2, -0.3, 0.3);

        if (entity.isSprinting()) {
            leftPos = new Vector3d(1., -0.4, -0.4);
        }


        leftPos.rotateZ(-entity.getXRot() * Mth.DEG_TO_RAD);
        leftPos.rotateY(-yRot * Mth.DEG_TO_RAD);

        double cooldown = 0;
        if (entity.wasInPowderSnow) {
            cooldown = 0.15;
        } else if (entity.isInWaterOrRain()) {
            cooldown = 0.04;
        } else if (entity.isOnFire() || entity.isInLava()) {
            cooldown = -0.1;
        }

        var tag = GunData.from(stack).data();

        if (entity instanceof ServerPlayer serverPlayer && entity.level() instanceof ServerLevel serverLevel && tag.getDouble("heat") > 4 && entity.isInWaterOrRain()) {
            if (entity.isInWater()) {
                ParticleTool.sendParticle(serverLevel, ParticleTypes.BUBBLE_COLUMN_UP,
                        entity.getX() + leftPos.x,
                        entity.getEyeY() + leftPos.y,
                        entity.getZ() + leftPos.z,
                        1, 0.1, 0.1, 0.1, 0.002, true, serverPlayer);
            }
            ParticleTool.sendParticle(serverLevel, ModParticleTypes.CUSTOM_CLOUD.get(),
                    entity.getX() + leftPos.x,
                    entity.getEyeY() + leftPos.y,
                    entity.getZ() + leftPos.z,
                    1, 0.1, 0.1, 0.1, 0.002, true, serverPlayer);
        }

        tag.putDouble("heat", Mth.clamp(tag.getDouble("heat") - 0.05 - cooldown, 0, 55));

        if (tag.getDouble("overheat") > 0) {
            tag.putDouble("overheat", (tag.getDouble("overheat") - 1));
        }
    }

    @Override
    public ResourceLocation getGunIcon() {
        return Mod.loc("textures/gun_icon/minigun_icon.png");
    }

    @Override
    public String getGunDisplayName() {
        return "M134 MINIGUN";
    }

    @Override
    public boolean canApplyPerk(Perk perk) {
        return switch (perk.type) {
            case AMMO -> perk != ModPerks.MICRO_MISSILE.get() && perk != ModPerks.LONGER_WIRE.get();
            case FUNCTIONAL -> perk == ModPerks.FIELD_DOCTOR.get() || perk == ModPerks.INTELLIGENT_CHIP.get();
            case DAMAGE ->
                    perk == ModPerks.MONSTER_HUNTER.get() || perk == ModPerks.KILLING_TALLY.get() || perk == ModPerks.VORPAL_WEAPON.get();
        };
    }

    @Override
    public boolean isAutoWeapon(ItemStack stack) {
        return true;
    }

    @Override
    public boolean useBackpackAmmo(ItemStack stack) {
        return true;
    }
}