package net.mcreator.superbwarfare.item.gun;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.mcreator.superbwarfare.ModUtils;
import net.mcreator.superbwarfare.client.renderer.item.SentinelItemRenderer;
import net.mcreator.superbwarfare.energy.ItemEnergyProvider;
import net.mcreator.superbwarfare.init.ModItems;
import net.mcreator.superbwarfare.init.ModSounds;
import net.mcreator.superbwarfare.init.ModTags;
import net.mcreator.superbwarfare.item.AnimatedItem;
import net.mcreator.superbwarfare.network.ModVariables;
import net.mcreator.superbwarfare.tools.GunsTool;
import net.mcreator.superbwarfare.tools.RarityTool;
import net.mcreator.superbwarfare.tools.TooltipTool;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffects;
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
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
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
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class SentinelItem extends GunItem implements GeoItem, AnimatedItem {
    private final Supplier<Integer> energyCapacity;

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    public String animationProcedure = "empty";
    public static ItemDisplayContext transformType;

    public SentinelItem() {
        super(new Item.Properties().stacksTo(1).rarity(RarityTool.LEGENDARY));

        this.energyCapacity = () -> 240000;
    }

    @Override
    public boolean isBarVisible(ItemStack pStack) {
        if (!pStack.getCapability(ForgeCapabilities.ENERGY).isPresent()) {
            return false;
        }

        AtomicInteger energy = new AtomicInteger(0);
        pStack.getCapability(ForgeCapabilities.ENERGY).ifPresent(
                e -> energy.set(e.getEnergyStored())
        );
        return energy.get() != 0;
    }

    @Override
    public int getBarWidth(ItemStack pStack) {
        AtomicInteger energy = new AtomicInteger(0);
        pStack.getCapability(ForgeCapabilities.ENERGY).ifPresent(
                e -> energy.set(e.getEnergyStored())
        );

        return Math.round((float) energy.get() * 13.0F / 240000F);
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, CompoundTag tag) {
        return new ItemEnergyProvider(stack, energyCapacity.get());
    }

    @Override
    public int getBarColor(ItemStack pStack) {
        return 0x95E9FF;
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        super.initializeClient(consumer);
        consumer.accept(new IClientItemExtensions() {
            private final BlockEntityWithoutLevelRenderer renderer = new SentinelItemRenderer();

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

    private PlayState fireAnimPredicate(AnimationState event) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return PlayState.STOP;
        ItemStack stack = player.getMainHandItem();
        if (!stack.is(ModTags.Items.GUN)) return PlayState.STOP;

        if (this.animationProcedure.equals("empty")) {

            if ((player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables())).zooming && stack.getOrCreateTag().getInt("bolt_action_anim") > 0) {
                return event.setAndContinue(RawAnimation.begin().thenPlay("animation.sentinel.shift2"));
            }

            if (stack.getOrCreateTag().getInt("bolt_action_anim") > 0) {
                return event.setAndContinue(RawAnimation.begin().thenPlay("animation.sentinel.shift"));
            }

            if (stack.getOrCreateTag().getInt("fire_animation") > 0) {
                return event.setAndContinue(RawAnimation.begin().thenPlay("animation.sentinel.fire"));
            }

            if (stack.getOrCreateTag().getBoolean("is_empty_reloading")) {
                return event.setAndContinue(RawAnimation.begin().thenPlay("animation.sentinel.reload_empty"));
            }

            if (stack.getOrCreateTag().getBoolean("is_normal_reloading")) {
                return event.setAndContinue(RawAnimation.begin().thenPlay("animation.sentinel.reload_normal"));
            }

            if (stack.getOrCreateTag().getDouble("sentinel_charge_time") < 127 && stack.getOrCreateTag().getDouble("sentinel_charge_time") > 0 && stack.getOrCreateTag().getBoolean("sentinel_is_charging")) {
                return event.setAndContinue(RawAnimation.begin().thenPlay("animation.sentinel.charge"));
            }

            return event.setAndContinue(RawAnimation.begin().thenLoop("animation.sentinel.idle"));
        }
        return PlayState.STOP;
    }

    private PlayState idlePredicate(AnimationState<SentinelItem> event) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return PlayState.STOP;
        ItemStack stack = player.getMainHandItem();
        if (!stack.is(ModTags.Items.GUN)) return PlayState.STOP;

        if (this.animationProcedure.equals("empty")) {

            if (stack.getOrCreateTag().getInt("draw_time") < 16) {
                return event.setAndContinue(RawAnimation.begin().thenLoop("animation.sentinel.draw"));
            }

            if (player.isSprinting() && player.onGround()
                    && player.getPersistentData().getDouble("noRun") == 0
                    && !(stack.getOrCreateTag().getBoolean("is_normal_reloading") || stack.getOrCreateTag().getBoolean("is_empty_reloading"))
                    && !stack.getOrCreateTag().getBoolean("sentinel_is_charging")) {
                if (player.hasEffect(MobEffects.MOVEMENT_SPEED)) {
                    return event.setAndContinue(RawAnimation.begin().thenLoop("animation.sentinel.run_fast"));
                } else {
                    return event.setAndContinue(RawAnimation.begin().thenLoop("animation.sentinel.run"));
                }
            }

            return event.setAndContinue(RawAnimation.begin().thenLoop("animation.sentinel.idle"));
        }
        return PlayState.STOP;
    }

    private PlayState procedurePredicate(AnimationState<SentinelItem> event) {
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
        var fireAnimController = new AnimationController<>(this, "fireAnimController", 1, this::fireAnimPredicate);
        data.add(fireAnimController);
        var idleController = new AnimationController<>(this, "idleController", 4, this::idlePredicate);
        data.add(idleController);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    public void appendHoverText(ItemStack stack, Level world, List<Component> list, TooltipFlag flag) {
        TooltipTool.addSentinelTips(list, stack);
    }

    @Override
    public void inventoryTick(ItemStack itemStack, Level world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(itemStack, world, entity, slot, selected);

        var tag = itemStack.getOrCreateTag();

        itemStack.getCapability(ForgeCapabilities.ENERGY).ifPresent(
                energy -> {
                    int energyStored = energy.getEnergyStored();

                    if (energyStored > 0) {
                        energy.extractEnergy(5, false);

                        tag.putDouble("sentinelChargeDamage", 0.2857142857142857 * tag.getDouble("damage") * tag.getDouble("levelDamageMultiple"));
                    } else {
                        tag.putDouble("sentinelChargeDamage", 0);
                    }
                }
        );

        if (tag.getDouble("chamber_rot") > 0) {
            tag.putDouble("chamber_rot", tag.getDouble("chamber_rot") - 1);
        }
    }

    @Override
    public Set<SoundEvent> getReloadSound() {
        return Set.of(
                ModSounds.SENTINEL_RELOAD_EMPTY.get(),
                ModSounds.SENTINEL_RELOAD_NORMAL.get(),
                ModSounds.SENTINEL_CHARGE.get(),
                ModSounds.SENTINEL_BOLT.get()
        );
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> map = super.getAttributeModifiers(slot, stack);
        UUID uuid = new UUID(slot.toString().hashCode(), 0);
        if (slot == EquipmentSlot.MAINHAND) {
            map = HashMultimap.create(map);
            map.put(Attributes.MOVEMENT_SPEED,
                    new AttributeModifier(uuid, ModUtils.ATTRIBUTE_MODIFIER, -0.06f, AttributeModifier.Operation.MULTIPLY_BASE));
        }
        return map;
    }

    public static ItemStack getGunInstance() {
        ItemStack stack = new ItemStack(ModItems.SENTINEL.get());
        GunsTool.initCreativeGun(stack, ModItems.SENTINEL.getId().getPath());
        return stack;
    }

    @Override
    public void setAnimationProcedure(String procedure) {
        this.animationProcedure = procedure;
    }

    @Override
    public ResourceLocation getGunIcon() {
        return new ResourceLocation(ModUtils.MODID, "textures/gun_icon/sentinel_icon.png");
    }

    @Override
    public String getGunDisplayName() {
        return "SENTINEL";
    }
}