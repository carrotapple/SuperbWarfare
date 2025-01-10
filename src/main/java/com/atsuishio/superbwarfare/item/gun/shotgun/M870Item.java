package com.atsuishio.superbwarfare.item.gun.shotgun;

import com.atsuishio.superbwarfare.ModUtils;
import com.atsuishio.superbwarfare.client.PoseTool;
import com.atsuishio.superbwarfare.client.renderer.item.M870ItemRenderer;
import com.atsuishio.superbwarfare.client.tooltip.component.ShotgunImageComponent;
import com.atsuishio.superbwarfare.event.ClientEventHandler;
import com.atsuishio.superbwarfare.init.ModItems;
import com.atsuishio.superbwarfare.init.ModSounds;
import com.atsuishio.superbwarfare.init.ModTags;
import com.atsuishio.superbwarfare.item.AnimatedItem;
import com.atsuishio.superbwarfare.item.gun.GunItem;
import com.atsuishio.superbwarfare.perk.Perk;
import com.atsuishio.superbwarfare.perk.PerkHelper;
import com.atsuishio.superbwarfare.tools.GunsTool;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

public class M870Item extends GunItem implements GeoItem, AnimatedItem {

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    public static ItemDisplayContext transformType;

    public M870Item() {
        super(new Item.Properties().stacksTo(1).rarity(Rarity.RARE));
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        super.initializeClient(consumer);
        consumer.accept(new IClientItemExtensions() {
            private final BlockEntityWithoutLevelRenderer renderer = new M870ItemRenderer();

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return renderer;
            }

            @Override
            public HumanoidModel.ArmPose getArmPose(LivingEntity entityLiving, InteractionHand hand, ItemStack stack) {
                return PoseTool.pose(entityLiving, hand, stack);
            }
        });
    }

    public void getTransformType(ItemDisplayContext type) {
        transformType = type;
    }

    private PlayState fireAnimPredicate(AnimationState<M870Item> event) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return PlayState.STOP;
        ItemStack stack = player.getMainHandItem();
        if (!stack.is(ModTags.Items.GUN)) return PlayState.STOP;

        if (GunsTool.getGunIntTag(stack, "BoltActionTick") > 0) {
            return event.setAndContinue(RawAnimation.begin().thenPlay("animation.m870.shift"));
        }

        if (stack.getOrCreateTag().getInt("reload_stage") == 1 && stack.getOrCreateTag().getDouble("prepare_load") > 0) {
            return event.setAndContinue(RawAnimation.begin().thenPlay("animation.m870.preparealt"));
        }

        if (stack.getOrCreateTag().getInt("reload_stage") == 1 && stack.getOrCreateTag().getDouble("prepare") > 0) {
            return event.setAndContinue(RawAnimation.begin().thenPlay("animation.m870.prepare"));
        }

        if (stack.getOrCreateTag().getDouble("load_index") == 0 && stack.getOrCreateTag().getInt("reload_stage") == 2) {
            return event.setAndContinue(RawAnimation.begin().thenPlay("animation.m870.iterativeload"));
        }

        if (stack.getOrCreateTag().getDouble("load_index") == 1 && stack.getOrCreateTag().getInt("reload_stage") == 2) {
            return event.setAndContinue(RawAnimation.begin().thenPlay("animation.m870.iterativeload2"));
        }

        if (stack.getOrCreateTag().getInt("reload_stage") == 3) {
            return event.setAndContinue(RawAnimation.begin().thenPlay("animation.m870.finish"));
        }

        return event.setAndContinue(RawAnimation.begin().thenLoop("animation.m870.idle"));
    }

    private PlayState idlePredicate(AnimationState<M870Item> event) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return PlayState.STOP;
        ItemStack stack = player.getMainHandItem();
        if (!stack.is(ModTags.Items.GUN)) return PlayState.STOP;

        if (player.isSprinting()
                && player.onGround()
                && player.getPersistentData().getDouble("noRun") == 0
                && ClientEventHandler.drawTime < 0.01
                && !GunsTool.getGunBooleanTag(stack, "Reloading")) {
            if (player.hasEffect(MobEffects.MOVEMENT_SPEED)) {
                return event.setAndContinue(RawAnimation.begin().thenLoop("animation.m870.run_fast"));
            } else {
                return event.setAndContinue(RawAnimation.begin().thenLoop("animation.m870.run"));
            }
        }

        event.getController().setAnimation(RawAnimation.begin().thenLoop("animation.m870.idle"));
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar data) {
        var fireAnimController = new AnimationController<>(this, "fireAnimController", 1, this::fireAnimPredicate);
        data.add(fireAnimController);
        var idleController = new AnimationController<>(this, "idleController", 3, this::idlePredicate);
        data.add(idleController);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    public Set<SoundEvent> getReloadSound() {
        return Set.of(ModSounds.M_870_PREPARE_LOAD.get(),
                ModSounds.M_870_LOOP.get(),
                ModSounds.M_870_BOLT.get());
    }

    public static ItemStack getGunInstance() {
        ItemStack stack = new ItemStack(ModItems.M_870.get());
        GunsTool.initCreativeGun(stack, ModItems.M_870.getId().getPath());
        return stack;
    }

    @Override
    public void setAnimationProcedure(String procedure) {
    }

    @Override
    public ResourceLocation getGunIcon() {
        return ModUtils.loc("textures/gun_icon/m870_icon.png");
    }

    @Override
    public String getGunDisplayName() {
        return "M870 MCS";
    }

    @Override
    public boolean canApplyPerk(Perk perk) {
        return PerkHelper.SHOTGUN_PERKS.test(perk) || PerkHelper.MAGAZINE_PERKS.test(perk);
    }

    @Override
    public @NotNull Optional<TooltipComponent> getTooltipImage(@NotNull ItemStack pStack) {
        return Optional.of(new ShotgunImageComponent(pStack));
    }

    @Override
    public boolean isIterativeReload(ItemStack stack) {
        return true;
    }

    @Override
    public int getFireMode() {
        return FireMode.SEMI.flag;
    }
}