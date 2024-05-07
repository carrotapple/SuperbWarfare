package net.mcreator.target.item.gun;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.mcreator.target.TargetMod;
import net.mcreator.target.client.renderer.item.VectorItemRenderer;
import net.mcreator.target.init.TargetModItems;
import net.mcreator.target.procedures.VectorWuPinZaiBeiBaoZhongShiMeiKeFaShengProcedure;
import net.mcreator.target.tools.ItemNBTTool;
import net.mcreator.target.tools.TooltipTool;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.network.chat.Component;
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
import java.util.UUID;
import java.util.function.Consumer;

public class VectorItem extends GunItem implements GeoItem {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    public String animationprocedure = "empty";
    public static ItemDisplayContext transformType;

    public VectorItem() {
        super(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC));
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        super.initializeClient(consumer);
        consumer.accept(new IClientItemExtensions() {
            private final BlockEntityWithoutLevelRenderer renderer = new VectorItemRenderer();

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

    private PlayState idlePredicate(AnimationState<VectorItem> event) {
        LocalPlayer player = Minecraft.getInstance().player;
        ItemStack stack = player.getMainHandItem();

        if (this.animationprocedure.equals("empty")) {

            if (stack.getOrCreateTag().getDouble("drawtime") < 11) {
                return event.setAndContinue(RawAnimation.begin().thenLoop("animation.vec.draw"));
            }

            if (stack.getOrCreateTag().getDouble("fireanim") > 0) {
                return event.setAndContinue(RawAnimation.begin().thenPlay("animation.vec.fire"));
            }

            if (stack.getOrCreateTag().getDouble("reloading") == 1 && stack.getOrCreateTag().getDouble("emptyreload") == 1) {
                return event.setAndContinue(RawAnimation.begin().thenPlay("animation.vec.reload"));
            }

            if (stack.getOrCreateTag().getDouble("reloading") == 1 && stack.getOrCreateTag().getDouble("emptyreload") == 0) {
                return event.setAndContinue(RawAnimation.begin().thenPlay("animation.vec.reload2"));
            }

            if (stack.getOrCreateTag().getDouble("firemode") == 0 && stack.getOrCreateTag().getDouble("cg") > 0) {
                return event.setAndContinue(RawAnimation.begin().thenPlay("animation.vec.changefirerate3"));
            }

            if (stack.getOrCreateTag().getDouble("firemode") == 1 && stack.getOrCreateTag().getDouble("cg") > 0) {
                return event.setAndContinue(RawAnimation.begin().thenPlay("animation.vec.changefirerate2"));
            }

            if (stack.getOrCreateTag().getDouble("firemode") == 2 && stack.getOrCreateTag().getDouble("cg") > 0) {
                return event.setAndContinue(RawAnimation.begin().thenPlay("animation.vec.changefirerate"));
            }

            if (player.isSprinting() && player.onGround() && player.getPersistentData().getDouble("unspringtable") == 0) {
                return event.setAndContinue(RawAnimation.begin().thenLoop("animation.vec.run"));
            }

            return event.setAndContinue(RawAnimation.begin().thenLoop("animation.vec.idle"));
        }
        return PlayState.STOP;
    }

    private PlayState procedurePredicate(AnimationState<VectorItem> event) {
        if (transformType != null && transformType.firstPerson()) {
            if (!this.animationprocedure.equals("empty") && event.getController().getAnimationState() == AnimationController.State.STOPPED) {
                event.getController().setAnimation(RawAnimation.begin().thenPlay(this.animationprocedure));
                if (event.getController().getAnimationState() == AnimationController.State.STOPPED) {
                    this.animationprocedure = "empty";
                    event.getController().forceAnimationReset();
                }
            } else if (this.animationprocedure.equals("empty")) {
                return PlayState.STOP;
            }
        }
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar data) {
        AnimationController<VectorItem> procedureController = new AnimationController<>(this, "procedureController", 0, this::procedurePredicate);
        data.add(procedureController);
        AnimationController<VectorItem> idleController = new AnimationController<>(this, "idleController", 4, this::idlePredicate);
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
        VectorWuPinZaiBeiBaoZhongShiMeiKeFaShengProcedure.execute(entity, itemstack);

        if (!ItemNBTTool.getBoolean(itemstack, "init", false)) {
            initGun(itemstack, false);
        }
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
                    new AttributeModifier(uuid, TargetMod.ATTRIBUTE_MODIFIER, -0.03f, AttributeModifier.Operation.MULTIPLY_BASE));
        }
        return map;
    }

    public static ItemStack getGunInstance() {
        ItemStack stack = new ItemStack(TargetModItems.VECTOR.get());

        initGun(stack, true);
        return stack;
    }

    private static void initGun(ItemStack stack, boolean isCreative) {
        stack.getOrCreateTag().putDouble("zoomspeed", 1.6);
        stack.getOrCreateTag().putDouble("zoom", 1.25);
        stack.getOrCreateTag().putDouble("autorifle", 1);
        stack.getOrCreateTag().putDouble("dev", 3.5);
        stack.getOrCreateTag().putDouble("smg", 1);
        stack.getOrCreateTag().putDouble("recoilx", 0.011);
        stack.getOrCreateTag().putDouble("recoily", 0.004);
        stack.getOrCreateTag().putDouble("damage", 4.5);
        stack.getOrCreateTag().putDouble("headshot", 1.5);
        stack.getOrCreateTag().putDouble("velocity", 22);
        stack.getOrCreateTag().putDouble("mag", 33);
        stack.getOrCreateTag().putBoolean("init", true);

        if (isCreative) {
            stack.getOrCreateTag().putDouble("ammo", stack.getOrCreateTag().getDouble("mag"));
        }
    }
}
