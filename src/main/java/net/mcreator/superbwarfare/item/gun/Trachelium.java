package net.mcreator.superbwarfare.item.gun;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.mcreator.superbwarfare.ModUtils;
import net.mcreator.superbwarfare.client.renderer.item.TracheliumItemRenderer;
import net.mcreator.superbwarfare.init.ModItems;
import net.mcreator.superbwarfare.init.ModSounds;
import net.mcreator.superbwarfare.init.ModTags;
import net.mcreator.superbwarfare.item.AnimatedItem;
import net.mcreator.superbwarfare.tools.GunsTool;
import net.mcreator.superbwarfare.tools.PoseTool;
import net.mcreator.superbwarfare.tools.TooltipTool;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffects;
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

public class Trachelium extends GunItem implements GeoItem, AnimatedItem {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    public String animationProcedure = "empty";
    public static ItemDisplayContext transformType;

    public Trachelium() {
        super(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC));
    }

    @Override
    public Set<SoundEvent> getReloadSound() {
        return Set.of(ModSounds.TRACHELIUM_RELOAD_EMPTY.get());
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
            public HumanoidModel.ArmPose getArmPose(LivingEntity entityLiving, InteractionHand hand, ItemStack stack) {
                return PoseTool.pose(entityLiving, hand, stack);
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
        if (!stack.is(ModTags.Items.GUN)) return PlayState.STOP;

        if (this.animationProcedure.equals("empty")) {

            if (stack.getOrCreateTag().getInt("draw_time") < 11) {
                return event.setAndContinue(RawAnimation.begin().thenLoop("animation.trachelium.draw"));
            }

            if (stack.getOrCreateTag().getInt("fire_animation") > 1) {
                return event.setAndContinue(RawAnimation.begin().thenPlay("animation.trachelium.fire"));
            }

            if (stack.getOrCreateTag().getBoolean("is_empty_reloading")) {
                return event.setAndContinue(RawAnimation.begin().thenPlay("animation.trachelium.reload"));
            }

            if (player.isSprinting() && player.onGround() && player.getPersistentData().getDouble("noRun") == 0) {
                if (player.hasEffect(MobEffects.MOVEMENT_SPEED)) {
                    return event.setAndContinue(RawAnimation.begin().thenLoop("animation.trachelium.run_fast"));
                } else {
                    return event.setAndContinue(RawAnimation.begin().thenLoop("animation.trachelium.run"));
                }
            }

            return event.setAndContinue(RawAnimation.begin().thenLoop("animation.trachelium.idle"));
        }
        return PlayState.STOP;
    }

    private PlayState procedurePredicate(AnimationState event) {
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
        AnimationController<Trachelium> procedureController = new AnimationController<>(this, "procedureController", 0, this::procedurePredicate);
        data.add(procedureController);
        AnimationController<Trachelium> idleController = new AnimationController<>(this, "idleController", 3, this::idlePredicate);
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
                    new AttributeModifier(uuid, ModUtils.ATTRIBUTE_MODIFIER, -0.02f, AttributeModifier.Operation.MULTIPLY_BASE));
        }
        return map;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
        list.add(Component.translatable("des.superbwarfare.trachelium_1").withStyle(ChatFormatting.GRAY).withStyle(ChatFormatting.ITALIC));
        list.add(Component.translatable("des.superbwarfare.trachelium_2").withStyle(ChatFormatting.GRAY));

        TooltipTool.addHideText(list, Component.literal(""));
        TooltipTool.addHideText(list, Component.translatable("des.superbwarfare.trachelium_3").withStyle(ChatFormatting.WHITE));
        TooltipTool.addHideText(list, Component.translatable("des.superbwarfare.trachelium_4").withStyle(Style.EMPTY.withColor(0xF4F0FF)));

        TooltipTool.addGunTips(list, stack);
    }

    public static ItemStack getGunInstance() {
        ItemStack stack = new ItemStack(ModItems.TRACHELIUM.get());
        GunsTool.initCreativeGun(stack, ModItems.TRACHELIUM.getId().getPath());
        return stack;
    }

    @Override
    public void setAnimationProcedure(String procedure) {
        this.animationProcedure = procedure;
    }

    @Override
    public ResourceLocation getGunIcon() {
        return new ResourceLocation(ModUtils.MODID, "textures/gun_icon/trachelium_icon.png");
    }

    @Override
    public String getGunDisplayName() {
        return "TRACHELIUM";
    }
}