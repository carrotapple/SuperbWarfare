package net.mcreator.target.item.gun;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.mcreator.target.TargetMod;
import net.mcreator.target.client.renderer.item.SvdItemRenderer;
import net.mcreator.target.init.TargetModItems;
import net.mcreator.target.init.TargetModSounds;
import net.mcreator.target.item.AnimatedItem;
import net.mcreator.target.tools.GunInfo;
import net.mcreator.target.tools.GunReload;
import net.mcreator.target.tools.GunsTool;
import net.mcreator.target.tools.TooltipTool;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
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

public class SvdItem extends GunItem implements GeoItem, AnimatedItem {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    public String animationProcedure = "empty";
    public static ItemDisplayContext transformType;

    public SvdItem() {
        super(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC));
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        super.initializeClient(consumer);
        consumer.accept(new IClientItemExtensions() {
            private final BlockEntityWithoutLevelRenderer renderer = new SvdItemRenderer();

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
            var tag = stack.getOrCreateTag();
            if (tag.getInt("draw_time") < 16) {
                return event.setAndContinue(RawAnimation.begin().thenLoop("animation.svd.draw"));
            }

            if (tag.getInt("fire_animation") > 0) {
                return event.setAndContinue(RawAnimation.begin().thenPlay("animation.svd.fire"));
            }

            if (tag.getBoolean("reloading") && tag.getBoolean("empty_reload")) {
                return event.setAndContinue(RawAnimation.begin().thenPlay("animation.svd.reload"));
            }

            if (tag.getBoolean("reloading") && !tag.getBoolean("empty_reload")) {
                return event.setAndContinue(RawAnimation.begin().thenPlay("animation.svd.reload2"));
            }

            if (player.isSprinting() && player.onGround() && player.getPersistentData().getDouble("noRun") == 0) {
                return event.setAndContinue(RawAnimation.begin().thenLoop("animation.svd.run"));
            }

            return event.setAndContinue(RawAnimation.begin().thenLoop("animation.svd.idle"));
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
        TooltipTool.addGunTips(list, stack);
    }

    @Override
    public void inventoryTick(ItemStack itemStack, Level world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(itemStack, world, entity, slot, selected);
        if (entity instanceof Player player) {
            var tag = itemStack.getOrCreateTag();
            double id = tag.getDouble("id");
            if (player.getMainHandItem().getOrCreateTag().getDouble("id") != tag.getDouble("id")) {
                tag.putBoolean("empty_reload", false);
                tag.putBoolean("reloading", false);
                tag.putDouble("reload_time", 0);
            }
            if (tag.getBoolean("reloading") && tag.getInt("ammo") == 0) {
                if (tag.getDouble("reload_time") == 66) {
                    entity.getPersistentData().putDouble("id", id);
                    if (!entity.level().isClientSide() && entity.getServer() != null) {
                        entity.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, entity.position(), entity.getRotationVector(), (ServerLevel) entity.level(), 4,
                                entity.getName().getString(), entity.getDisplayName(), entity.getServer(), entity), "playsound target:svd_reload_empty player @s ~ ~ ~ 100 1");
                    }
                }
                if (player.getMainHandItem().getItem() == itemStack.getItem()
                        && player.getMainHandItem().getOrCreateTag().getDouble("id") == id) {
                    if (tag.getDouble("reload_time") > 0) {
                        tag.putDouble("reload_time", (tag.getDouble("reload_time") - 1));
                    }
                } else {
                    tag.putBoolean("reloading", false);
                    tag.putDouble("reload_time", 0);
                    tag.putBoolean("empty_reload", false);
                }
                if (tag.getDouble("reload_time") == 14 && player.getMainHandItem().getOrCreateTag().getDouble("id") == id) {
                    tag.putDouble("gj", 0);
                }
                if (tag.getDouble("reload_time") == 1 && player.getMainHandItem().getOrCreateTag().getDouble("id") == id) {
                    GunReload.reload(entity, GunInfo.Type.SNIPER);
                }
            } else if (tag.getBoolean("reloading") && tag.getInt("ammo") > 0) {
                if (tag.getDouble("reload_time") == 55) {
                    entity.getPersistentData().putDouble("id", id);
                    if (!entity.level().isClientSide() && entity.getServer() != null) {
                        entity.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, entity.position(), entity.getRotationVector(), (ServerLevel) entity.level(), 4,
                                entity.getName().getString(), entity.getDisplayName(), entity.getServer(), entity), "playsound target:svd_reload_normal player @s ~ ~ ~ 100 1");
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
        }
    }

    @Override
    public Set<SoundEvent> getReloadSound() {
        return Set.of(TargetModSounds.SVD_RELOAD_EMPTY.get(), TargetModSounds.SVD_RELOAD_NORMAL.get());
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
        ItemStack stack = new ItemStack(TargetModItems.SVD.get());
        GunsTool.initCreativeGun(stack, TargetModItems.SVD.getId().getPath());
        return stack;
    }

    @Override
    public void setAnimationProcedure(String procedure) {
        this.animationProcedure = procedure;
    }

    @Override
    public ResourceLocation getGunIcon() {
        return new ResourceLocation(TargetMod.MODID, "textures/gun_icon/svd_icon.png");
    }

    @Override
    public String getGunDisplayName() {
        return "   SVD";
    }
}