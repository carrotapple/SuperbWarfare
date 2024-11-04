package net.mcreator.superbwarfare.item.gun.rifle;

import net.mcreator.superbwarfare.ModUtils;
import net.mcreator.superbwarfare.client.PoseTool;
import net.mcreator.superbwarfare.client.renderer.item.MarlinItemRenderer;
import net.mcreator.superbwarfare.event.ClientEventHandler;
import net.mcreator.superbwarfare.init.ModItems;
import net.mcreator.superbwarfare.init.ModSounds;
import net.mcreator.superbwarfare.init.ModTags;
import net.mcreator.superbwarfare.item.AnimatedItem;
import net.mcreator.superbwarfare.item.gun.GunItem;
import net.mcreator.superbwarfare.perk.Perk;
import net.mcreator.superbwarfare.perk.PerkHelper;
import net.mcreator.superbwarfare.tools.GunsTool;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
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

public class MarlinItem extends GunItem implements GeoItem, AnimatedItem {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    public static ItemDisplayContext transformType;

    public MarlinItem() {
        super(new Item.Properties().stacksTo(1).rarity(Rarity.COMMON));
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        super.initializeClient(consumer);
        consumer.accept(new IClientItemExtensions() {
            private final BlockEntityWithoutLevelRenderer renderer = new MarlinItemRenderer();

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

    private PlayState idlePredicate(AnimationState<MarlinItem> event) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return PlayState.STOP;
        ItemStack stack = player.getMainHandItem();
        if (!stack.is(ModTags.Items.GUN)) return PlayState.STOP;

        if (transformType != null && transformType.firstPerson()) {
            if (stack.getOrCreateTag().getDouble("marlin_animation_time") > 0 && !stack.getOrCreateTag().getBoolean("fastfiring")) {
                return event.setAndContinue(RawAnimation.begin().thenPlay("animation.marlin.shift"));
            }

            if (stack.getOrCreateTag().getDouble("marlin_animation_time") > 0 && stack.getOrCreateTag().getBoolean("fastfiring")) {
                return event.setAndContinue(RawAnimation.begin().thenPlay("animation.marlin.shift2"));
            }

            if (stack.getOrCreateTag().getInt("reload_stage") == 1 && stack.getOrCreateTag().getDouble("prepare") > 0) {
                return event.setAndContinue(RawAnimation.begin().thenPlay("animation.marlin.prepare"));
            }

            if (stack.getOrCreateTag().getDouble("load_index") == 0 && stack.getOrCreateTag().getInt("reload_stage") == 2) {
                return event.setAndContinue(RawAnimation.begin().thenPlay("animation.marlin.iterativeload"));
            }

            if (stack.getOrCreateTag().getDouble("load_index") == 1 && stack.getOrCreateTag().getInt("reload_stage") == 2) {
                return event.setAndContinue(RawAnimation.begin().thenPlay("animation.marlin.iterativeload2"));
            }

            if (stack.getOrCreateTag().getInt("reload_stage") == 3) {
                return event.setAndContinue(RawAnimation.begin().thenPlay("animation.marlin.finish"));
            }

            if (player.isSprinting() && player.onGround() && player.getPersistentData().getDouble("noRun") == 0 && ClientEventHandler.drawTime < 0.01) {
                if (player.hasEffect(MobEffects.MOVEMENT_SPEED)) {
                    return event.setAndContinue(RawAnimation.begin().thenLoop("animation.marlin.run_fast"));
                } else {
                    return event.setAndContinue(RawAnimation.begin().thenLoop("animation.marlin.run"));
                }
            }

            event.getController().setAnimation(RawAnimation.begin().thenLoop("animation.marlin.idle"));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar data) {
        var idleController = new AnimationController<>(this, "idleController", 3, this::idlePredicate);
        data.add(idleController);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    public void inventoryTick(ItemStack itemstack, Level world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(itemstack, world, entity, slot, selected);
        var tag = itemstack.getOrCreateTag();
        if (tag.getDouble("marlin_animation_time") > 0) {
            tag.putDouble("marlin_animation_time", tag.getDouble("marlin_animation_time") - 1);
        }
    }

    @Override
    public Set<SoundEvent> getReloadSound() {
        return Set.of(ModSounds.MARLIN_LOOP.get(), ModSounds.MARLIN_PREPARE.get(), ModSounds.MARLIN_END.get());
    }

    public static ItemStack getGunInstance() {
        ItemStack stack = new ItemStack(ModItems.MARLIN.get());
        GunsTool.initCreativeGun(stack, ModItems.MARLIN.getId().getPath());
        return stack;
    }

    @Override
    public void setAnimationProcedure(String procedure) {
    }

    @Override
    public ResourceLocation getGunIcon() {
        return new ResourceLocation(ModUtils.MODID, "textures/gun_icon/marlin_icon.png");
    }

    @Override
    public String getGunDisplayName() {
        return " MARLIN-1894";
    }

    @Override
    public boolean canApplyPerk(Perk perk) {
        return PerkHelper.RIFLE_PERKS.test(perk) || PerkHelper.MAGAZINE_PERKS.test(perk);
    }
}