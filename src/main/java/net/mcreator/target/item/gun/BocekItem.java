package net.mcreator.target.item.gun;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.mcreator.target.client.renderer.item.BocekItemRenderer;
import net.mcreator.target.init.TargetModItems;
import net.mcreator.target.procedures.BocekreloadProcedure;
import net.mcreator.target.tools.ItemNBTTool;
import net.mcreator.target.tools.RarityTool;
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
import net.minecraft.world.entity.player.Player;
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

public class BocekItem extends GunItem implements GeoItem {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    public String animationprocedure = "empty";
    public static ItemDisplayContext transformType;

    public BocekItem() {
        super(new Item.Properties().stacksTo(1).rarity(RarityTool.SPECIAL));
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        super.initializeClient(consumer);
        consumer.accept(new IClientItemExtensions() {
            private final BlockEntityWithoutLevelRenderer renderer = new BocekItemRenderer();

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
        if (transformType != null && transformType.firstPerson()) {
            LocalPlayer player = Minecraft.getInstance().player;
            ItemStack stack = player.getMainHandItem();

            if (stack.getOrCreateTag().getDouble("drawtime") < 16) {
                return event.setAndContinue(RawAnimation.begin().thenLoop("animation.bocek.draw"));
            }

            if (this.animationprocedure.equals("empty")) {
                event.getController().setAnimation(RawAnimation.begin().thenLoop("animation.bocek.idle"));
                return PlayState.CONTINUE;
            }
        }
        return PlayState.STOP;
    }

    private PlayState procedurePredicate(AnimationState event) {
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
        AnimationController procedureController = new AnimationController(this, "procedureController", 0, this::procedurePredicate);
        data.add(procedureController);
        AnimationController idleController = new AnimationController(this, "idleController", 4, this::idlePredicate);
        data.add(idleController);
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
                    new AttributeModifier(uuid, "henghengaaa", -0.03f, AttributeModifier.Operation.MULTIPLY_BASE));
        }
        return map;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    public void appendHoverText(ItemStack itemstack, Level world, List<Component> list, TooltipFlag flag) {
        super.appendHoverText(itemstack, world, list, flag);
    }

    @Override
    public void inventoryTick(ItemStack itemstack, Level world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(itemstack, world, entity, slot, selected);
        if (entity instanceof Player player) {
            itemstack.getOrCreateTag().putDouble("maxammo", getAmmoCount(player));
        }
        BocekreloadProcedure.execute(entity, itemstack);

        if (!ItemNBTTool.getBoolean(itemstack, "init", false)) {
            initGun(itemstack, false);
        }
    }

    public static double getAmmoCount(Player player) {
        double sum = 0.0;
        for (int i = 0; i < player.getInventory().getContainerSize(); ++i) {
            ItemStack itemstack = player.getInventory().getItem(i);
            if (check(itemstack)) {
                sum += itemstack.getCount();
            }
        }
        return sum;
    }

    protected static boolean check(ItemStack stack) {
        return stack.getItem() == Items.ARROW;
    }

    public static ItemStack getGunInstance() {
        ItemStack stack = new ItemStack(TargetModItems.BOCEK.get());

        initGun(stack, true);
        return stack;
    }

    private static void initGun(ItemStack stack, boolean isCreative) {
        stack.getOrCreateTag().putDouble("zoomspeed", 1);
        stack.getOrCreateTag().putDouble("zoom", 2);
        stack.getOrCreateTag().putDouble("autorifle", 1);
        stack.getOrCreateTag().putDouble("dev", 4);
        stack.getOrCreateTag().putDouble("recoilx", 0.005);
        stack.getOrCreateTag().putDouble("recoily", 0.003);
        stack.getOrCreateTag().putDouble("headshot", 1.5);
        stack.getOrCreateTag().putDouble("damage", 2.4);
        stack.getOrCreateTag().putBoolean("init", true);

        if (isCreative) {
            stack.getOrCreateTag().putDouble("ammo", stack.getOrCreateTag().getDouble("mag"));
        }
    }
}
