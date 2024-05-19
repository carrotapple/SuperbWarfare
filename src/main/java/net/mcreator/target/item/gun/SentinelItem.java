package net.mcreator.target.item.gun;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.mcreator.target.TargetMod;
import net.mcreator.target.client.renderer.item.SentinelItemRenderer;
import net.mcreator.target.init.TargetModItems;
import net.mcreator.target.init.TargetModSounds;
import net.mcreator.target.item.AnimatedItem;
import net.mcreator.target.tools.*;
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
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;

public class SentinelItem extends GunItem implements GeoItem, AnimatedItem {
    private static final String TAG_POWER = "power";
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    public String animationProcedure = "empty";
    public static ItemDisplayContext transformType;

    public SentinelItem() {
        super(new Item.Properties().stacksTo(1).rarity(RarityTool.LEGENDARY));
    }

    @Override
    public boolean isBarVisible(ItemStack pStack) {
        return ItemNBTTool.getInt(pStack, TAG_POWER, 0) != 0;
    }

    @Override
    public int getBarWidth(ItemStack pStack) {
        return Math.round((float) ItemNBTTool.getInt(pStack, TAG_POWER, 0) * 13.0F / 100F);
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

    private PlayState idlePredicate(AnimationState event) {
        LocalPlayer player = Minecraft.getInstance().player;
        ItemStack stack = player.getMainHandItem();

        if (this.animationProcedure.equals("empty")) {

            if (stack.getOrCreateTag().getInt("draw_time") < 16) {
                return event.setAndContinue(RawAnimation.begin().thenLoop("animation.sentinel.draw"));
            }

            if (stack.getOrCreateTag().getBoolean("zoom_fire") && stack.getOrCreateTag().getDouble("bolt_action_anim") > 0) {
                return event.setAndContinue(RawAnimation.begin().thenPlay("animation.sentinel.shift2"));
            }

            if (!stack.getOrCreateTag().getBoolean("zoom_fire") && stack.getOrCreateTag().getDouble("bolt_action_anim") > 0) {
                return event.setAndContinue(RawAnimation.begin().thenPlay("animation.sentinel.shift"));
            }

            if (stack.getOrCreateTag().getInt("fire_animation") > 0) {
                return event.setAndContinue(RawAnimation.begin().thenPlay("animation.sentinel.fire"));
            }

            if (stack.getOrCreateTag().getBoolean("reloading") && stack.getOrCreateTag().getBoolean("empty_reload")) {
                return event.setAndContinue(RawAnimation.begin().thenPlay("animation.sentinel.reload"));
            }

            if (stack.getOrCreateTag().getBoolean("reloading") && !stack.getOrCreateTag().getBoolean("empty_reload")) {
                return event.setAndContinue(RawAnimation.begin().thenPlay("animation.sentinel.reload2"));
            }

            if (stack.getOrCreateTag().getDouble("charging_time") > 127 && stack.getOrCreateTag().getDouble("charging") == 1) {
                return event.setAndContinue(RawAnimation.begin().thenPlay("animation.sentinel.chargep"));
            }

            if (stack.getOrCreateTag().getDouble("charging_time") < 127 && stack.getOrCreateTag().getDouble("charging_time") > 0 && stack.getOrCreateTag().getDouble("charging") == 1) {
                return event.setAndContinue(RawAnimation.begin().thenPlay("animation.sentinel.charge"));
            }

            if (player.isSprinting() && player.onGround() && player.getPersistentData().getDouble("noRun") == 0) {
                return event.setAndContinue(RawAnimation.begin().thenLoop("animation.sentinel.run"));
            }

            return event.setAndContinue(RawAnimation.begin().thenLoop("animation.sentinel.idle"));
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
        if (entity instanceof Player player) {
            double cid;
            var tag = itemStack.getOrCreateTag();
            double id = tag.getDouble("id");
            if (player.getMainHandItem().getOrCreateTag().getDouble("id") != tag.getDouble("id")) {
                tag.putBoolean("empty_reload", false);
                tag.putBoolean("reloading", false);
                tag.putDouble("reload_time", 0);
            }
            if (tag.getBoolean("reloading") && tag.getInt("ammo") == 0) {
                if (tag.getDouble("reload_time") == 73) {
                    entity.getPersistentData().putDouble("id", id);
                    if (entity instanceof ServerPlayer serverPlayer) {
                        SoundTool.playLocalSound(serverPlayer, TargetModSounds.SENTINEL_RELOAD_EMPTY.get(), 100, 1);
                    }
                }
                if (player.getMainHandItem().getItem() == itemStack.getItem()
                        && player.getMainHandItem().getOrCreateTag().getDouble("id") == id) {
                    if (tag.getDouble("reload_time") > 0) {
                        tag.putDouble("reload_time", (tag.getDouble("reload_time") - 1));
                    }
                } else {
                    tag.putBoolean("reloading", false);
                    tag.putBoolean("empty_reload", false);
                    tag.putDouble("reload_time", 0);
                }
                if (tag.getDouble("reload_time") == 1 && player.getMainHandItem().getOrCreateTag().getDouble("id") == id) {
                    GunReload.reload(entity, GunInfo.Type.SNIPER);
                }
            } else if (tag.getBoolean("reloading") && tag.getInt("ammo") > 0) {
                if (tag.getDouble("reload_time") == 53) {
                    entity.getPersistentData().putDouble("id", id);
                    if (entity instanceof ServerPlayer serverPlayer) {
                        SoundTool.playLocalSound(serverPlayer, TargetModSounds.SENTINEL_RELOAD_NORMAL.get(), 100, 1);
                    }
                }
                if (player.getMainHandItem().getItem() == itemStack.getItem()
                        && player.getMainHandItem().getOrCreateTag().getDouble("id") == id) {
                    if (tag.getDouble("reload_time") > 0) {
                        tag.putDouble("reload_time", (tag.getDouble("reload_time") - 1));
                    }
                } else {
                    tag.putBoolean("reloading", false);
                    tag.putBoolean("empty_reload", false);
                    tag.putDouble("reload_time", 0);
                }
                if (tag.getDouble("reload_time") == 1 && player.getMainHandItem().getOrCreateTag().getDouble("id") == id) {
                    GunReload.reload(entity, GunInfo.Type.SNIPER, true);
                }
            }
            if (tag.getDouble("firing") > 0) {
                tag.putDouble("firing", tag.getDouble("firing") - 1);
            }
            if (tag.getDouble("zoom_firing") > 0) {
                tag.putDouble("zoom_firing", tag.getDouble("zoom_firing") - 1);
            }

            cid = tag.getDouble("cid");
            if (player.getMainHandItem().getOrCreateTag().getDouble("cid") != tag.getDouble("cid")) {
                tag.putDouble("charging", 0);
                tag.putDouble("charging_time", 0);
            }
            if (tag.getDouble("charging") == 1) {
                if (tag.getDouble("charging_time") == 127) {
                    entity.getPersistentData().putDouble("cid", cid);
                    if (entity instanceof ServerPlayer serverPlayer) {
                        SoundTool.playLocalSound(serverPlayer, TargetModSounds.SENTINEL_CHARGE.get(), 100, 1);
                    }
                }
                if (player.getMainHandItem().getItem() == itemStack.getItem()
                        && player.getMainHandItem().getOrCreateTag().getDouble("cid") == cid) {
                    if (tag.getDouble("charging_time") > 0) {
                        tag.putDouble("charging_time", tag.getDouble("charging_time") - 1);
                    }
                } else {
                    tag.putDouble("charging", 0);
                    tag.putDouble("charging_time", 0);
                }
                if (tag.getDouble("charging_time") == 16 && player.getMainHandItem().getOrCreateTag().getDouble("cid") == cid) {
                    tag.putDouble("power", 100);
                }
                if (tag.getDouble("charging_time") == 1 && player.getMainHandItem().getOrCreateTag().getDouble("cid") == cid) {
                    tag.putDouble("charging", 0);
                }
            }
            if (tag.getDouble("power") > 0) {
                tag.putDouble("add_damage", 10);
                tag.putDouble("power", tag.getDouble("power") - 0.025);
            } else {
                tag.putDouble("add_damage", 0);
            }
            if (tag.getDouble("crot") > 0) {
                tag.putDouble("crot", tag.getDouble("crot") - 1);
            }
        }
    }

    @Override
    public Set<SoundEvent> getReloadSound() {
        return Set.of(
                TargetModSounds.SENTINEL_RELOAD_EMPTY.get(),
                TargetModSounds.SENTINEL_RELOAD_NORMAL.get(),
                TargetModSounds.SENTINEL_CHARGE.get(),
                TargetModSounds.SENTINEL_BOLT.get()
        );
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
                    new AttributeModifier(uuid, TargetMod.ATTRIBUTE_MODIFIER, -0.06f, AttributeModifier.Operation.MULTIPLY_BASE));
        }
        return map;
    }

    public static ItemStack getGunInstance() {
        ItemStack stack = new ItemStack(TargetModItems.SENTINEL.get());
        GunsTool.initCreativeGun(stack, TargetModItems.SENTINEL.getId().getPath());
        return stack;
    }

    @Override
    public void setAnimationProcedure(String procedure) {
        this.animationProcedure = procedure;
    }

    @Override
    public ResourceLocation getGunIcon() {
        return new ResourceLocation(TargetMod.MODID, "textures/gun_icon/sentinel_icon.png");
    }

    @Override
    public String getGunDisplayName() {
        return "SENTINEL";
    }
}