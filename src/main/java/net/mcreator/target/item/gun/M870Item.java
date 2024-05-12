package net.mcreator.target.item.gun;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.mcreator.target.TargetMod;
import net.mcreator.target.client.renderer.item.M870ItemRenderer;
import net.mcreator.target.init.TargetModItems;
import net.mcreator.target.init.TargetModSounds;
import net.mcreator.target.item.AnimatedItem;
import net.mcreator.target.procedures.M870WuPinZaiBeiBaoZhongShiMeiKeFaShengProcedure;
import net.mcreator.target.tools.GunsTool;
import net.mcreator.target.tools.TooltipTool;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.network.chat.Component;
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
import org.jetbrains.annotations.Nullable;
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

public class M870Item extends GunItem implements GeoItem, AnimatedItem {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    public String animationProcedure = "empty";
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
        ItemStack stack = player.getMainHandItem();

        if (transformType != null && transformType.firstPerson()) {
            if (this.animationProcedure.equals("empty")) {

                if (stack.getOrCreateTag().getDouble("drawtime") < 16) {
                    return event.setAndContinue(RawAnimation.begin().thenLoop("animation.m870.draw"));
                }

                if (stack.getOrCreateTag().getDouble("firing") > 0 && stack.getOrCreateTag().getDouble("firing") < 15) {
                    return event.setAndContinue(RawAnimation.begin().thenPlay("animation.m870.shift"));
                }

                if (stack.getOrCreateTag().getDouble("emptyreload") == 1 && stack.getOrCreateTag().getDouble("prepare") > 0) {
                    return event.setAndContinue(RawAnimation.begin().thenPlay("animation.m870.preparealt"));
                }

                if (stack.getOrCreateTag().getDouble("reloading") == 1 && stack.getOrCreateTag().getDouble("prepare") > 0) {
                    return event.setAndContinue(RawAnimation.begin().thenPlay("animation.m870.prepare"));
                }

                if (stack.getOrCreateTag().getDouble("loadindex") == 0 && stack.getOrCreateTag().getDouble("loading") > 0) {
                    return event.setAndContinue(RawAnimation.begin().thenPlay("animation.m870.iterativeload"));
                }

                if (stack.getOrCreateTag().getDouble("loadindex") == 1 && stack.getOrCreateTag().getDouble("loading") > 0) {
                    return event.setAndContinue(RawAnimation.begin().thenPlay("animation.m870.iterativeload2"));
                }

                if (stack.getOrCreateTag().getDouble("finish") > 0) {
                    return event.setAndContinue(RawAnimation.begin().thenPlay("animation.m870.finish"));
                }

                if (player.isSprinting() && player.onGround() && player.getPersistentData().getDouble("noRun") == 0) {
                    return event.setAndContinue(RawAnimation.begin().thenLoop("animation.m870.run"));
                }

                event.getController().setAnimation(RawAnimation.begin().thenLoop("animation.m870.idle"));
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
        AnimationController procedureController = new AnimationController(this, "procedureController", 0, this::procedurePredicate);
        data.add(procedureController);
        AnimationController idleController = new AnimationController(this, "idleController", 4, this::idlePredicate);
        data.add(idleController);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
        TooltipTool.addShotgunTips(list, stack, 12);
    }

    @Override
    public void inventoryTick(ItemStack itemstack, Level world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(itemstack, world, entity, slot, selected);
        M870WuPinZaiBeiBaoZhongShiMeiKeFaShengProcedure.execute(entity, itemstack);
    }

    @Override
    public Set<SoundEvent> getReloadSound() {
        return Set.of(TargetModSounds.M_870_PREPAREALT.get(), TargetModSounds.M_870_RELOADLOOP.get());
    }

    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entity) {
        return true;
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return false;
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> map = super.getAttributeModifiers(slot, stack);
        UUID uuid = new UUID(slot.toString().hashCode(), 0);
        if (slot == EquipmentSlot.MAINHAND) {
            map = HashMultimap.create(map);
            map.put(Attributes.MOVEMENT_SPEED,
                    new AttributeModifier(uuid, TargetMod.ATTRIBUTE_MODIFIER, -0.04f, AttributeModifier.Operation.MULTIPLY_BASE));
        }
        return map;
    }

    public static ItemStack getGunInstance() {
        ItemStack stack = new ItemStack(TargetModItems.M_870.get());
        GunsTool.initCreativeGun(stack, TargetModItems.M_870.getId().getPath());
        return stack;
    }

    @Override
    public void setAnimationProcedure(String procedure) {
        this.animationProcedure = procedure;
    }
}