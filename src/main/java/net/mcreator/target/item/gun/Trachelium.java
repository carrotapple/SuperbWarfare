package net.mcreator.target.item.gun;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.mcreator.target.TargetMod;
import net.mcreator.target.client.renderer.item.TracheliumItemRenderer;
import net.mcreator.target.init.TargetModItems;
import net.mcreator.target.procedures.ReloadingProcedure;
import net.mcreator.target.tools.RarityTool;
import net.mcreator.target.tools.TooltipTool;
import net.minecraft.ChatFormatting;
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
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
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
import java.util.UUID;
import java.util.function.Consumer;

public class Trachelium extends GunItem implements GeoItem {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    public String animationprocedure = "empty";
    public static ItemDisplayContext transformType;

    public Trachelium() {
        super(new Item.Properties().stacksTo(1).rarity(RarityTool.LEGENDARY));
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        super.initializeClient(consumer);
        consumer.accept(new IClientItemExtensions() {
            private final BlockEntityWithoutLevelRenderer renderer = new TracheliumItemRenderer();

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

    private PlayState idlePredicate(AnimationState<Trachelium> event) {
        LocalPlayer player = Minecraft.getInstance().player;
        ItemStack stack = player.getMainHandItem();
        if (this.animationprocedure.equals("empty")) {

            if (stack.getOrCreateTag().getDouble("drawtime") < 11) {
                return event.setAndContinue(RawAnimation.begin().thenLoop("animation.trachelium.draw"));
            }

            if (stack.getOrCreateTag().getDouble("fireanim") > 1) {
                return event.setAndContinue(RawAnimation.begin().thenPlay("animation.trachelium.fire"));
            }

            if (stack.getOrCreateTag().getDouble("reloading") == 1 && stack.getOrCreateTag().getDouble("emptyreload") == 1) {
                return event.setAndContinue(RawAnimation.begin().thenPlay("animation.trachelium.reload"));
            }

            if (player.isSprinting() && player.onGround() && player.getPersistentData().getDouble("noRun") == 0) {
                return event.setAndContinue(RawAnimation.begin().thenLoop("animation.trachelium.run"));
            }

            return event.setAndContinue(RawAnimation.begin().thenLoop("animation.trachelium.idle"));
        }
        return PlayState.STOP;
    }

    private PlayState procedurePredicate(AnimationState<Trachelium> event) {
        if (transformType != null && transformType.firstPerson()) {
            if (!(this.animationprocedure.equals("empty")) && event.getController().getAnimationState() == AnimationController.State.STOPPED) {
                event.getController().setAnimation(RawAnimation.begin().thenPlay(this.animationprocedure));
                if (event.getController().getAnimationState() == AnimationController.State.STOPPED) {
                    this.animationprocedure = "empty";
                    event.getController().forceAnimationReset();
                }
            }
        }
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar data) {
        AnimationController<Trachelium> procedureController = new AnimationController<>(this, "procedureController", 0, this::procedurePredicate);
        data.add(procedureController);
        AnimationController<Trachelium> idleController = new AnimationController<>(this, "idleController", 6, this::idlePredicate);
        data.add(idleController);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entity) {
        return true;
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> map = super.getAttributeModifiers(slot, stack);
        UUID uuid = new UUID(slot.toString().hashCode(), 0);
        if (slot == EquipmentSlot.MAINHAND) {
            map = HashMultimap.create(map);
            map.put(Attributes.MOVEMENT_SPEED,
                    new AttributeModifier(uuid, TargetMod.ATTRIBUTE_MODIFIER, -0.02f, AttributeModifier.Operation.MULTIPLY_BASE));
        }
        return map;
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return slotChanged;
    }

    @Override
    public void inventoryTick(ItemStack itemstack, Level world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(itemstack, world, entity, slot, selected);

        // TODO 把这坨procedure删了
        ReloadingProcedure.execute(entity, itemstack);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
        list.add(Component.translatable("des.target.trachelium_1").withStyle(ChatFormatting.GRAY).withStyle(ChatFormatting.ITALIC));
        list.add(Component.translatable("des.target.trachelium_2").withStyle(ChatFormatting.GRAY));

        TooltipTool.addGunTips(list, stack);
    }

    public static ItemStack getGunInstance() {
        ItemStack stack = new ItemStack(TargetModItems.TRACHELIUM.get());
        //GunsTool.initGun(stack, TargetModItems.TRACHELIUM.getId().getPath());
        stack.getOrCreateTag().putDouble("ammo", stack.getOrCreateTag().getDouble("mag"));
        return stack;
    }
}
