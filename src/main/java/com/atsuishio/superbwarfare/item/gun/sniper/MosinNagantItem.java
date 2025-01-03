package com.atsuishio.superbwarfare.item.gun.sniper;

import com.atsuishio.superbwarfare.ModUtils;
import com.atsuishio.superbwarfare.client.PoseTool;
import com.atsuishio.superbwarfare.client.renderer.item.MosinNagantItemRenderer;
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
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Set;
import java.util.function.Consumer;

public class MosinNagantItem extends GunItem implements GeoItem, AnimatedItem {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    public static ItemDisplayContext transformType;

    public MosinNagantItem() {
        super(new Properties().stacksTo(1).rarity(Rarity.RARE));
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        super.initializeClient(consumer);
        consumer.accept(new IClientItemExtensions() {
            private final BlockEntityWithoutLevelRenderer renderer = new MosinNagantItemRenderer();

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

    private PlayState fireAnimPredicate(AnimationState<MosinNagantItem> event) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return PlayState.STOP;
        ItemStack stack = player.getMainHandItem();
        if (!stack.is(ModTags.Items.GUN)) return PlayState.STOP;

        if (GunsTool.getGunIntTag(stack, "BoltActionTick") > 0) {
            return event.setAndContinue(RawAnimation.begin().thenPlay("animation.mosin.shift"));
        }

        if (stack.getOrCreateTag().getInt("reload_stage") == 1 && GunsTool.getGunIntTag(stack, "Ammo", 0) == 0) {
            return event.setAndContinue(RawAnimation.begin().thenPlay("animation.mosin.prepare_empty"));
        }

        if (stack.getOrCreateTag().getInt("reload_stage") == 1 && GunsTool.getGunIntTag(stack, "Ammo", 0) > 0) {
            return event.setAndContinue(RawAnimation.begin().thenPlay("animation.mosin.prepare"));
        }

        if (stack.getOrCreateTag().getDouble("load_index") == 0 && stack.getOrCreateTag().getInt("reload_stage") == 2) {
            return event.setAndContinue(RawAnimation.begin().thenPlay("animation.mosin.iterativeload"));
        }

        if (stack.getOrCreateTag().getDouble("load_index") == 1 && stack.getOrCreateTag().getInt("reload_stage") == 2) {
            return event.setAndContinue(RawAnimation.begin().thenPlay("animation.mosin.iterativeload2"));
        }

        if (stack.getOrCreateTag().getInt("reload_stage") == 3) {
            return event.setAndContinue(RawAnimation.begin().thenPlay("animation.mosin.finish"));
        }

        return event.setAndContinue(RawAnimation.begin().thenLoop("animation.mosin.idle"));
    }

    private PlayState idlePredicate(AnimationState<MosinNagantItem> event) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return PlayState.STOP;
        ItemStack stack = player.getMainHandItem();
        if (!stack.is(ModTags.Items.GUN)) return PlayState.STOP;

        if (player.isSprinting() && player.onGround()
                && player.getPersistentData().getDouble("noRun") == 0
                && !(stack.getOrCreateTag().getBoolean("is_empty_reloading"))
                && stack.getOrCreateTag().getInt("reload_stage") != 1
                && stack.getOrCreateTag().getInt("reload_stage") != 2
                && stack.getOrCreateTag().getInt("reload_stage") != 3
                && ClientEventHandler.drawTime < 0.01
                && !stack.getOrCreateTag().getBoolean("reloading")) {
            if (player.hasEffect(MobEffects.MOVEMENT_SPEED) && GunsTool.getGunIntTag(stack, "BoltActionTick") == 0) {
                return event.setAndContinue(RawAnimation.begin().thenLoop("animation.mosin.run_fast"));
            } else {
                return event.setAndContinue(RawAnimation.begin().thenLoop("animation.mosin.run"));
            }
        }

        return event.setAndContinue(RawAnimation.begin().thenLoop("animation.mosin.idle"));
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
        return Set.of(
                ModSounds.MOSIN_NAGANT_BOLT.get(),
                ModSounds.MOSIN_NAGANT_PREPARE.get(),
                ModSounds.MOSIN_NAGANT_PREPARE_EMPTY.get(),
                ModSounds.MOSIN_NAGANT_LOOP.get(),
                ModSounds.MOSIN_NAGANT_END.get()
        );
    }

    public static ItemStack getGunInstance() {
        ItemStack stack = new ItemStack(ModItems.MOSIN_NAGANT.get());
        GunsTool.initCreativeGun(stack, ModItems.MOSIN_NAGANT.getId().getPath());
        return stack;
    }

    @Override
    public void setAnimationProcedure(String procedure) {
    }

    @Override
    public ResourceLocation getGunIcon() {
        return new ResourceLocation(ModUtils.MODID, "textures/gun_icon/mosin_nagant_icon.png");
    }

    @Override
    public String getGunDisplayName() {
        return "MOSIN NAGANT";
    }

    @Override
    public boolean canApplyPerk(Perk perk) {
        return PerkHelper.SNIPER_RIFLE_PERKS.test(perk);
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