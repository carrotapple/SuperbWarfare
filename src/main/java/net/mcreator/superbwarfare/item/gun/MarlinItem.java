package net.mcreator.superbwarfare.item.gun;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.mcreator.superbwarfare.ModUtils;
import net.mcreator.superbwarfare.client.renderer.item.MarlinItemRenderer;
import net.mcreator.superbwarfare.init.TargetModItems;
import net.mcreator.superbwarfare.init.TargetModSounds;
import net.mcreator.superbwarfare.init.TargetModTags;
import net.mcreator.superbwarfare.item.AnimatedItem;
import net.mcreator.superbwarfare.tools.GunsTool;
import net.mcreator.superbwarfare.tools.TooltipTool;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.*;
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

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;

public class MarlinItem extends GunItem implements GeoItem, AnimatedItem {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    public String animationProcedure = "empty";
    public static ItemDisplayContext transformType;

    public MarlinItem() {
        super(new Item.Properties().stacksTo(1).rarity(Rarity.RARE));
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
            public HumanoidModel.ArmPose getArmPose(LivingEntity entityLiving, InteractionHand hand, ItemStack itemStack) {
                return HumanoidModel.ArmPose.BOW_AND_ARROW;
            }
        });
    }

    public void getTransformType(ItemDisplayContext type) {
        transformType = type;
    }

    private PlayState idlePredicate(AnimationState event) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return PlayState.STOP;
        ItemStack stack = player.getMainHandItem();
        if (!stack.is(TargetModTags.Items.GUN)) return PlayState.STOP;

        if (transformType != null && transformType.firstPerson()) {
            if (this.animationProcedure.equals("empty")) {

                if (stack.getOrCreateTag().getInt("draw_time") < 16) {
                    return event.setAndContinue(RawAnimation.begin().thenLoop("animation.marlin.draw"));
                }

                if (stack.getOrCreateTag().getInt("flash_time") > 0 && stack.getOrCreateTag().getDouble("animindex") == 0) {
                    return event.setAndContinue(RawAnimation.begin().thenPlay("animation.marlin.fire"));
                }

                if (stack.getOrCreateTag().getInt("flash_time") > 0 && stack.getOrCreateTag().getDouble("animindex") == 1) {
                    return event.setAndContinue(RawAnimation.begin().thenPlay("animation.marlin.fire2"));
                }

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

                if (player.isSprinting() && player.onGround() && player.getPersistentData().getDouble("noRun") == 0) {
                    return event.setAndContinue(RawAnimation.begin().thenLoop("animation.marlin.run"));
                }

                event.getController().setAnimation(RawAnimation.begin().thenLoop("animation.marlin.idle"));
                return PlayState.CONTINUE;
            }
        }
        return PlayState.STOP;
    }

    private PlayState procedurePredicate(AnimationState event) {
        if (transformType != null && transformType.firstPerson()) {
            if (!this.animationProcedure.equals("empty") && event.getController().getAnimationState() == AnimationController.State.STOPPED) {
                event.getController().setAnimation(RawAnimation.begin().thenPlay(this.animationProcedure));
                if (event.getController().getAnimationState() == AnimationController.State.STOPPED) {
                    this.animationProcedure = "empty";
                    event.getController().forceAnimationReset();
                }
            } else if (this.animationProcedure.equals("empty")) {
                return PlayState.STOP;
            }
        }
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar data) {
        var procedureController = new AnimationController<>(this, "procedureController", 0, this::procedurePredicate);
        data.add(procedureController);
        var idleController = new AnimationController<>(this, "idleController", 3, this::idlePredicate);
        data.add(idleController);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    public void appendHoverText(ItemStack stack, Level world, List<Component> list, TooltipFlag flag) {
        TooltipTool.addGunTips(list, stack);
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
        return Set.of(TargetModSounds.MARLIN_LOOP.get(), TargetModSounds.MARLIN_PREPARE.get(), TargetModSounds.MARLIN_END.get());
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> map = super.getAttributeModifiers(slot, stack);
        UUID uuid = new UUID(slot.toString().hashCode(), 0);
        if (slot == EquipmentSlot.MAINHAND) {
            map = HashMultimap.create(map);
            map.put(Attributes.MOVEMENT_SPEED,
                    new AttributeModifier(uuid, ModUtils.ATTRIBUTE_MODIFIER, -0.04f, AttributeModifier.Operation.MULTIPLY_BASE));
        }
        return map;
    }

    public static ItemStack getGunInstance() {
        ItemStack stack = new ItemStack(TargetModItems.MARLIN.get());
        GunsTool.initCreativeGun(stack, TargetModItems.MARLIN.getId().getPath());
        return stack;
    }

    @Override
    public void setAnimationProcedure(String procedure) {
        this.animationProcedure = procedure;
    }

    @Override
    public ResourceLocation getGunIcon() {
        return new ResourceLocation(ModUtils.MODID, "textures/gun_icon/marlin_icon.png");
    }

    @Override
    public String getGunDisplayName() {
        return " MARLIN-1894";
    }
}