package net.mcreator.target.item.gun;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.mcreator.target.TargetMod;
import net.mcreator.target.client.renderer.item.DevotionItemRenderer;
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
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;

public class Devotion extends GunItem implements GeoItem, AnimatedItem {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    public String animationProcedure = "empty";
    public static ItemDisplayContext transformType;

    public Devotion() {
        super(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC));
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        super.initializeClient(consumer);
        consumer.accept(new IClientItemExtensions() {
            private final BlockEntityWithoutLevelRenderer renderer = new DevotionItemRenderer();

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
                return event.setAndContinue(RawAnimation.begin().thenLoop("animation.devotion.draw"));
            }

            if (stack.getOrCreateTag().getInt("fire_animation") > 0) {
                return event.setAndContinue(RawAnimation.begin().thenPlay("animation.devotion.fire"));
            }

            if (stack.getOrCreateTag().getBoolean("reloading") && stack.getOrCreateTag().getBoolean("empty_reload")) {
                return event.setAndContinue(RawAnimation.begin().thenPlay("animation.devotion.reload_empty"));
            }

            if (stack.getOrCreateTag().getBoolean("reloading") && !stack.getOrCreateTag().getBoolean("empty_reload")) {
                return event.setAndContinue(RawAnimation.begin().thenPlay("animation.devotion.reload_normal"));
            }

            if (player.isSprinting() && player.onGround() && player.getPersistentData().getDouble("noRun") == 0) {
                return event.setAndContinue(RawAnimation.begin().thenLoop("animation.devotion.run"));
            }

            return event.setAndContinue(RawAnimation.begin().thenLoop("animation.devotion.idle"));
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
                    new AttributeModifier(uuid, TargetMod.ATTRIBUTE_MODIFIER, -0.05f, AttributeModifier.Operation.MULTIPLY_BASE));
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

        var itemTag = itemstack.getOrCreateTag();
        var id = itemTag.getDouble("id");
        var mainHandItem = entity instanceof LivingEntity living ? living.getMainHandItem() : ItemStack.EMPTY;
        var mainHandItemTag = mainHandItem.getOrCreateTag();

        if (mainHandItemTag.getDouble("id") != itemTag.getDouble("id")) {
            itemTag.putBoolean("empty_reload", false);
            itemTag.putBoolean("reloading", false);
            itemTag.putDouble("reload_time", 0);
        }
        if (itemTag.getBoolean("reloading") && itemTag.getInt("ammo") == 0) {
            if (itemTag.getDouble("reload_time") == 92) {
                entity.getPersistentData().putDouble("id", id);
                if (entity instanceof ServerPlayer player) {
                    SoundTool.playLocalSound(player, TargetModSounds.DEVOTION_RELOAD_EMPTY.get(), 100, 1);
                }
            }
            if (mainHandItem.getItem() == itemstack.getItem()
                    && mainHandItemTag.getDouble("id") == id) {
                if (itemTag.getDouble("reload_time") > 0) {
                    itemTag.putDouble("reload_time", itemTag.getDouble("reload_time") - 1);
                }
            } else {
                itemTag.putBoolean("reloading", false);
                itemTag.putBoolean("empty_reload", false);
                itemTag.putDouble("reload_time", 0);
            }
            if (itemTag.getDouble("reload_time") == 1 && mainHandItemTag.getDouble("id") == id) {
                GunsTool.reload(entity, GunInfo.Type.RIFLE);
            }
        } else if (itemTag.getBoolean("reloading") && itemTag.getInt("ammo") > 0) {
            if (itemTag.getDouble("reload_time") == 70) {
                entity.getPersistentData().putDouble("id", id);
                if (entity instanceof ServerPlayer player) {
                    SoundTool.playLocalSound(player, TargetModSounds.DEVOTION_RELOAD_NORMAL.get(), 100, 1);
                }
            }
            if (mainHandItem.getItem() == itemstack.getItem()
                    && mainHandItemTag.getDouble("id") == id) {
                if (itemTag.getDouble("reload_time") > 0) {
                    itemTag.putDouble("reload_time", (itemTag.getDouble("reload_time") - 1));
                }
            } else {
                itemTag.putBoolean("reloading", false);
                itemTag.putBoolean("empty_reload", false);
                itemTag.putDouble("reload_time", 0);
            }
            if (itemTag.getDouble("reload_time") == 1 && mainHandItemTag.getDouble("id") == id) {
                GunsTool.reload(entity, GunInfo.Type.RIFLE, true);
            }
        }
    }

    @Override
    public Set<SoundEvent> getReloadSound() {
        return Set.of(TargetModSounds.DEVOTION_RELOAD_EMPTY.get(), TargetModSounds.DEVOTION_RELOAD_NORMAL.get());
    }

    public static ItemStack getGunInstance() {
        ItemStack stack = new ItemStack(TargetModItems.DEVOTION.get());
        GunsTool.initCreativeGun(stack, TargetModItems.DEVOTION.getId().getPath());
        return stack;
    }

    @Override
    public void setAnimationProcedure(String procedure) {
        this.animationProcedure = procedure;
    }

    @Override
    public ResourceLocation getGunIcon() {
        return new ResourceLocation(TargetMod.MODID, "textures/gun_icon/devotion_icon.png");
    }

    @Override
    public String getGunDisplayName() {
        return "  Devotion";
    }
}