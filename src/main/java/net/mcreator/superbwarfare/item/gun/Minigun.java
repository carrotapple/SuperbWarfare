package net.mcreator.superbwarfare.item.gun;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.mcreator.superbwarfare.ModUtils;
import net.mcreator.superbwarfare.client.renderer.item.MinigunItemRenderer;
import net.mcreator.superbwarfare.init.ModItems;
import net.mcreator.superbwarfare.init.ModTags;
import net.mcreator.superbwarfare.item.AnimatedItem;
import net.mcreator.superbwarfare.tools.GunsTool;
import net.mcreator.superbwarfare.tools.ItemNBTTool;
import net.mcreator.superbwarfare.tools.RarityTool;
import net.mcreator.superbwarfare.tools.TooltipTool;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
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

public class Minigun extends GunItem implements GeoItem, AnimatedItem {
    private static final String TAG_HEAT = "heat";
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    public String animationProcedure = "empty";
    public static ItemDisplayContext transformType;

    public Minigun() {
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

    private PlayState idlePredicate(AnimationState<Minigun> event) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return PlayState.STOP;
        ItemStack stack = player.getMainHandItem();
        if (!stack.is(ModTags.Items.GUN)) return PlayState.STOP;

        if (this.animationProcedure.equals("empty")) {

            if (stack.getOrCreateTag().getInt("draw_time") < 29) {
                return event.setAndContinue(RawAnimation.begin().thenLoop("animation.minigun.draw"));
            }

            if (player.isSprinting() && player.onGround() && player.getPersistentData().getDouble("noRun") == 0) {
                if (player.hasEffect(MobEffects.MOVEMENT_SPEED)) {
                    return event.setAndContinue(RawAnimation.begin().thenLoop("animation.minigun.run_fast"));
                } else {
                    return event.setAndContinue(RawAnimation.begin().thenLoop("animation.minigun.run"));
                }
            }
            return event.setAndContinue(RawAnimation.begin().thenLoop("animation.minigun.idle"));
        }
        return PlayState.STOP;
    }

    private PlayState procedurePredicate(AnimationState<Minigun> event) {
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
        var idleController = new AnimationController<>(this, "idleController", 6, this::idlePredicate);
        data.add(idleController);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> map = super.getAttributeModifiers(slot, stack);
        UUID uuid = new UUID(slot.toString().hashCode(), 0);
        if (slot == EquipmentSlot.MAINHAND) {
            map = HashMultimap.create(map);
            map.put(Attributes.MOVEMENT_SPEED,
                    new AttributeModifier(uuid, ModUtils.ATTRIBUTE_MODIFIER, -0.2f, AttributeModifier.Operation.MULTIPLY_BASE));
        }
        return map;
    }

    @Override
    public void appendHoverText(ItemStack stack, Level world, List<Component> list, TooltipFlag flag) {
        TooltipTool.addGunTips(list, stack);
    }

    @Override
    public void inventoryTick(ItemStack itemstack, Level world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(itemstack, world, entity, slot, selected);

        double cooldown = 0;
        if (entity.wasInPowderSnow) {
            cooldown = 0.75;
        } else if (entity.isInWaterOrRain()) {
            cooldown = 0.2;
        } else if (entity.isOnFire() || entity.isInLava()) {
            cooldown = -0.5;
        }

        itemstack.getOrCreateTag().putDouble("heat", Mth.clamp(itemstack.getOrCreateTag().getDouble("heat") - 0.25 - cooldown, 0, 55));

        if (itemstack.getOrCreateTag().getDouble("overheat") > 0) {
            itemstack.getOrCreateTag().putDouble("overheat", (itemstack.getOrCreateTag().getDouble("overheat") - 1));
        }
    }

    public static ItemStack getGunInstance() {
        ItemStack stack = new ItemStack(ModItems.MINIGUN.get());
        GunsTool.initCreativeGun(stack, ModItems.MINIGUN.getId().getPath());
        return stack;
    }

    @Override
    public void setAnimationProcedure(String procedure) {
        this.animationProcedure = procedure;
    }

    @Override
    public ResourceLocation getGunIcon() {
        return new ResourceLocation(ModUtils.MODID, "textures/gun_icon/minigun_icon.png");
    }

    @Override
    public String getGunDisplayName() {
        return "M134 MINIGUN";
    }
}