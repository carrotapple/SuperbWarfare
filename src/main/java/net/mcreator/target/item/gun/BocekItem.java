package net.mcreator.target.item.gun;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.mcreator.target.TargetMod;
import net.mcreator.target.client.renderer.item.BocekItemRenderer;
import net.mcreator.target.init.TargetModItems;
import net.mcreator.target.init.TargetModTags;
import net.mcreator.target.item.AnimatedItem;
import net.mcreator.target.tools.GunsTool;
import net.mcreator.target.tools.RarityTool;
import net.mcreator.target.tools.TooltipTool;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
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

public class BocekItem extends GunItem implements GeoItem, AnimatedItem {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    public String animationProcedure = "empty";
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

    private PlayState idlePredicate(AnimationState<BocekItem> event) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return PlayState.STOP;
        ItemStack stack = player.getMainHandItem();
        if (!stack.is(TargetModTags.Items.GUN)) return PlayState.STOP;

        if (stack.getOrCreateTag().getInt("draw_time") < 16) {
            return event.setAndContinue(RawAnimation.begin().thenLoop("animation.bocek.draw"));
        }

        if (player.isSprinting() && player.onGround() && player.getPersistentData().getDouble("noRun") == 0) {
            return event.setAndContinue(RawAnimation.begin().thenLoop("animation.bocek.run"));
        }

        if (this.animationProcedure.equals("empty")) {
            event.getController().setAnimation(RawAnimation.begin().thenLoop("animation.bocek.idle"));
            return PlayState.CONTINUE;
        }

        return PlayState.STOP;
    }

    private PlayState procedurePredicate(AnimationState<BocekItem> event) {
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

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    public void appendHoverText(ItemStack stack, Level world, List<Component> list, TooltipFlag flag) {
        TooltipTool.addBocekTips(list, stack);
    }

    public static int getAmmoCount(Player player) {
        int sum = 0;
        for (int i = 0; i < player.getInventory().getContainerSize(); ++i) {
            ItemStack itemstack = player.getInventory().getItem(i);
            if (check(itemstack)) {
                sum += itemstack.getCount();
            }
        }
        return sum;
    }

    @Override
    public void inventoryTick(ItemStack itemstack, Level world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(itemstack, world, entity, slot, selected);
        if (entity instanceof Player player) {
            itemstack.getOrCreateTag().putInt("max_ammo", getAmmoCount(player));
        }

        CompoundTag tag = itemstack.getOrCreateTag();
        if (tag.getInt("arrow_empty") > 0) {
            tag.putInt("arrow_empty", tag.getInt("arrow_empty") - 1);
        }
    }

    protected static boolean check(ItemStack stack) {
        return stack.getItem() == Items.ARROW;
    }

    public static ItemStack getGunInstance() {
        ItemStack stack = new ItemStack(TargetModItems.BOCEK.get());
        GunsTool.initCreativeGun(stack, TargetModItems.BOCEK.getId().getPath());
        return stack;
    }

    @Override
    public void setAnimationProcedure(String procedure) {
        this.animationProcedure = procedure;
    }

    @Override
    public ResourceLocation getGunIcon() {
        return new ResourceLocation(TargetMod.MODID, "textures/gun_icon/bocek_icon.png");
    }

    @Override
    public String getGunDisplayName() {
        return "   Bocek";
    }
}