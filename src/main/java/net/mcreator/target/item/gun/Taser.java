package net.mcreator.target.item.gun;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.mcreator.target.TargetMod;
import net.mcreator.target.client.renderer.item.TaserItemRenderer;
import net.mcreator.target.init.TargetModItems;
import net.mcreator.target.init.TargetModSounds;
import net.mcreator.target.item.AnimatedItem;
import net.mcreator.target.tools.EnchantmentCategoryTool;
import net.mcreator.target.tools.GunsTool;
import net.mcreator.target.tools.SoundTool;
import net.mcreator.target.tools.TooltipTool;
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
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantment;
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

public class Taser extends GunItem implements GeoItem, AnimatedItem {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    public String animationProcedure = "empty";
    public static ItemDisplayContext transformType;

    public Taser() {
        super(new Item.Properties().stacksTo(1).rarity(Rarity.COMMON));
    }


    @Override
    public Set<SoundEvent> getReloadSound() {
        return Set.of(TargetModSounds.TASER_RELOAD.get());
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        super.initializeClient(consumer);
        consumer.accept(new IClientItemExtensions() {
            private final BlockEntityWithoutLevelRenderer renderer = new TaserItemRenderer();

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

    private PlayState idlePredicate(AnimationState<Taser> event) {
        LocalPlayer player = Minecraft.getInstance().player;
        ItemStack stack = player.getMainHandItem();

        if (this.animationProcedure.equals("empty")) {

            if (stack.getOrCreateTag().getInt("draw_time") < 11) {
                return event.setAndContinue(RawAnimation.begin().thenLoop("animation.taser.draw"));
            }

            if (stack.getOrCreateTag().getInt("fire_animation") > 1) {
                return event.setAndContinue(RawAnimation.begin().thenPlay("animation.taser.fire"));
            }

            if (stack.getOrCreateTag().getBoolean("reloading")) {
                return event.setAndContinue(RawAnimation.begin().thenPlay("animation.taser.reload"));
            }

            if (player.isSprinting() && player.onGround() && player.getPersistentData().getDouble("noRun") == 0) {
                return event.setAndContinue(RawAnimation.begin().thenLoop("animation.taser.run"));
            }

            return event.setAndContinue(RawAnimation.begin().thenLoop("animation.taser.idle"));
        }
        return PlayState.STOP;
    }

    private PlayState procedurePredicate(AnimationState<Taser> event) {
        if (transformType != null && transformType.firstPerson()) {
            if (!(this.animationProcedure.equals("empty")) && event.getController().getAnimationState() == AnimationController.State.STOPPED) {
                event.getController().setAnimation(RawAnimation.begin().thenPlay(this.animationProcedure));
                if (event.getController().getAnimationState() == AnimationController.State.STOPPED) {
                    this.animationProcedure = "empty";
                    event.getController().forceAnimationReset();
                }
            }
        }
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar data) {
        AnimationController<Taser> procedureController = new AnimationController<>(this, "procedureController", 0, this::procedurePredicate);
        data.add(procedureController);
        AnimationController<Taser> idleController = new AnimationController<>(this, "idleController", 3, this::idlePredicate);
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
                    new AttributeModifier(uuid, TargetMod.ATTRIBUTE_MODIFIER, -0.01f, AttributeModifier.Operation.MULTIPLY_BASE));
        }
        return map;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    public void appendHoverText(ItemStack stack, Level world, List<Component> list, TooltipFlag flag) {
        TooltipTool.addGunTips(list, stack);
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
    public void inventoryTick(ItemStack stack, Level world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);

        if (entity instanceof Player player) {
            stack.getOrCreateTag().putInt("max_ammo", getAmmoCount(player));

            ItemStack heldItem = player.getMainHandItem();

            double id = stack.getOrCreateTag().getDouble("id");
            if (heldItem.getOrCreateTag().getDouble("id") != stack.getOrCreateTag().getDouble("id")) {
                stack.getOrCreateTag().putBoolean("empty_reload", false);
                stack.getOrCreateTag().putBoolean("reloading", false);
                stack.getOrCreateTag().putDouble("reload_time", 0);
            }

            if (stack.getOrCreateTag().getBoolean("reloading")) {
                if (stack.getOrCreateTag().getDouble("reload_time") == 54) {
                    player.getPersistentData().putDouble("id", id);

                    if (!player.level().isClientSide()) {
                        SoundTool.playLocalSound(player, TargetModSounds.TASER_RELOAD.get(), 100, 1);
                    }
                }
                if (heldItem.getItem() == stack.getItem() && heldItem.getOrCreateTag().getDouble("id") == id) {
                    if (stack.getOrCreateTag().getDouble("reload_time") > 0) {
                        stack.getOrCreateTag().putDouble("reload_time", (stack.getOrCreateTag().getDouble("reload_time") - 1));
                    }
                } else {
                    stack.getOrCreateTag().putBoolean("reloading", false);
                    stack.getOrCreateTag().putDouble("reload_time", 0);
                    stack.getOrCreateTag().putBoolean("empty_reload", false);
                }

                if (stack.getOrCreateTag().getDouble("reload_time") == 1 && heldItem.getOrCreateTag().getDouble("id") == id) {
                    if (stack.getOrCreateTag().getInt("max_ammo") >= 1) {
                        stack.getOrCreateTag().putInt("ammo", 1);

                        player.getInventory().clearOrCountMatchingItems(p -> p.getItem() == TargetModItems.TASER_ELECTRODE.get(), 1, player.inventoryMenu.getCraftSlots());
                        stack.getOrCreateTag().putBoolean("reloading", false);
                        stack.getOrCreateTag().putBoolean("empty_reload", false);
                    }
                }
            }
        }
    }

    protected static boolean check(ItemStack stack) {
        return stack.getItem() == TargetModItems.TASER_ELECTRODE.get();
    }

    public static ItemStack getGunInstance() {
        ItemStack stack = new ItemStack(TargetModItems.TASER.get());
        GunsTool.initCreativeGun(stack, TargetModItems.TASER.getId().getPath());
        return stack;
    }

    @Override
    public void setAnimationProcedure(String procedure) {
        this.animationProcedure = procedure;
    }

    @Override
    public ResourceLocation getGunIcon() {
        return new ResourceLocation(TargetMod.MODID, "textures/gun_icon/taser_icon.png");
    }

    @Override
    public String getGunDisplayName() {
        return "TASER";
    }

    @Override
    public int getEnchantmentValue(ItemStack stack) {
        return 15;
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return true;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return enchantment.category == EnchantmentCategoryTool.TASER;
    }
}
