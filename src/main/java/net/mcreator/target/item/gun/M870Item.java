package net.mcreator.target.item.gun;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.mcreator.target.TargetMod;
import net.mcreator.target.client.renderer.item.M870ItemRenderer;
import net.mcreator.target.init.TargetModItems;
import net.mcreator.target.init.TargetModSounds;
import net.mcreator.target.init.TargetModTags;
import net.mcreator.target.item.AnimatedItem;
import net.mcreator.target.network.TargetModVariables;
import net.mcreator.target.tools.GunsTool;
import net.mcreator.target.tools.SoundTool;
import net.mcreator.target.tools.TooltipTool;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
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
        if (player == null) return PlayState.STOP;
        ItemStack stack = player.getMainHandItem();
        if (!stack.is(TargetModTags.Items.GUN)) return PlayState.STOP;

        if (transformType != null && transformType.firstPerson()) {
            if (this.animationProcedure.equals("empty")) {

                if (stack.getOrCreateTag().getInt("draw_time") < 16) {
                    return event.setAndContinue(RawAnimation.begin().thenLoop("animation.m870.draw"));
                }

                if (stack.getOrCreateTag().getDouble("fire_animation") > 0 && stack.getOrCreateTag().getDouble("fire_animation") < 15) {
                    return event.setAndContinue(RawAnimation.begin().thenPlay("animation.m870.shift"));
                }

                if (stack.getOrCreateTag().getBoolean("empty_reload") && stack.getOrCreateTag().getDouble("prepare") > 0) {
                    return event.setAndContinue(RawAnimation.begin().thenPlay("animation.m870.preparealt"));
                }

                if (stack.getOrCreateTag().getBoolean("reloading") && stack.getOrCreateTag().getDouble("prepare") > 0) {
                    return event.setAndContinue(RawAnimation.begin().thenPlay("animation.m870.prepare"));
                }

                if (stack.getOrCreateTag().getDouble("load_index") == 0 && stack.getOrCreateTag().getDouble("loading") > 0) {
                    return event.setAndContinue(RawAnimation.begin().thenPlay("animation.m870.iterativeload"));
                }

                if (stack.getOrCreateTag().getDouble("load_index") == 1 && stack.getOrCreateTag().getDouble("loading") > 0) {
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
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
        TooltipTool.addShotgunTips(list, stack, 12);
    }

    @Override
    public void inventoryTick(ItemStack itemstack, Level world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(itemstack, world, entity, slot, selected);
        if (entity instanceof Player player) {
            var tag = itemstack.getOrCreateTag();
            double id = tag.getDouble("id");
            if (player.getMainHandItem().getOrCreateTag().getDouble("id") != tag.getDouble("id")) {
                tag.putDouble("finish", 0);
                tag.putBoolean("reloading", false);
                tag.putDouble("prepare", 0);
                tag.putDouble("loading", 0);
                tag.putDouble("force_stop", 0);
                tag.putDouble("stop", 0);
                tag.putBoolean("empty_reload", false);
            }
            if (tag.getDouble("prepare") > 0) {
                tag.putDouble("prepare", tag.getDouble("prepare") - 1);
            }
            if (tag.getDouble("loading") > 0) {
                tag.putDouble("loading", tag.getDouble("loading") - 1);
            }
            if (tag.getDouble("finish") > 0 && tag.getDouble("loading") == 0) {
                tag.putDouble("finish", tag.getDouble("finish") - 1);
            }
            if (player.getMainHandItem().getOrCreateTag().getDouble("id") != tag.getDouble("id")) {
                tag.putBoolean("reloading", false);
            }
            if (tag.getBoolean("reloading") && player.getMainHandItem().getOrCreateTag().getDouble("id") == id) {
                if (tag.getDouble("prepare") == 10 && tag.getBoolean("empty_reload")) {
                    tag.putInt("ammo", tag.getInt("ammo") + 1);
                    entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                        capability.shotgunAmmo = capability.shotgunAmmo - 1;
                        capability.syncPlayerVariables(entity);
                    });
                }
                if (tag.getDouble("prepare") == 0 && tag.getDouble("loading") == 0
                        && !(tag.getInt("ammo") >= 8 || entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).map(c -> c.shotgunAmmo).orElse(0) == 0)) {
                    if (tag.getDouble("force_stop") == 1) {
                        tag.putDouble("stop", 1);
                    } else {
                        tag.putDouble("loading", 16);

                        player.getCooldowns().addCooldown(itemstack.getItem(), 16);
                        if (entity instanceof ServerPlayer serverPlayer) {
                            SoundTool.playLocalSound(serverPlayer, TargetModSounds.M_870_RELOAD_LOOP.get(), 100, 1);
                        }
                        if (tag.getDouble("load_index") == 1) {
                            tag.putDouble("load_index", 0);
                        } else {
                            tag.putDouble("load_index", 1);
                        }
                    }
                }
                if (tag.getDouble("loading") == 9) {
                    tag.putInt("ammo", tag.getInt("ammo") + 1);
                    entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                        capability.shotgunAmmo = capability.shotgunAmmo - 1;
                        capability.syncPlayerVariables(entity);
                    });
                }
                if ((tag.getInt("ammo") >= 8 || entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).map(c -> c.shotgunAmmo).orElse(0) == 0)
                        && tag.getDouble("loading") == 0 || tag.getDouble("stop") == 1) {
                    tag.putDouble("force_stop", 0);
                    tag.putDouble("stop", 0);
                    tag.putDouble("finish", 12);
                    player.getCooldowns().addCooldown(itemstack.getItem(), 12);
                    tag.putBoolean("reloading", false);
                    tag.putBoolean("empty_reload", false);
                }
            }
        }
    }

    @Override
    public Set<SoundEvent> getReloadSound() {
        return Set.of(TargetModSounds.M_870_PREPARE_ALT.get(), TargetModSounds.M_870_RELOAD_LOOP.get());
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

    @Override
    public ResourceLocation getGunIcon() {
        return new ResourceLocation(TargetMod.MODID, "textures/gun_icon/m870_icon.png");
    }

    @Override
    public String getGunDisplayName() {
        return " M870 MCS";
    }
}