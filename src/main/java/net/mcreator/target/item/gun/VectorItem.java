package net.mcreator.target.item.gun;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.mcreator.target.TargetMod;
import net.mcreator.target.client.renderer.item.VectorItemRenderer;
import net.mcreator.target.init.TargetModItems;
import net.mcreator.target.init.TargetModSounds;
import net.mcreator.target.init.TargetModTags;
import net.mcreator.target.item.AnimatedItem;
import net.mcreator.target.tools.GunInfo;
import net.mcreator.target.tools.GunsTool;
import net.mcreator.target.tools.TooltipTool;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
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
import net.minecraftforge.registries.ForgeRegistries;
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

public class VectorItem extends GunItem implements GeoItem, AnimatedItem {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    public String animationProcedure = "empty";
    public static ItemDisplayContext transformType;

    public VectorItem() {
        super(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC));
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        super.initializeClient(consumer);
        consumer.accept(new IClientItemExtensions() {
            private final BlockEntityWithoutLevelRenderer renderer = new VectorItemRenderer();

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

        if (this.animationProcedure.equals("empty")) {
            if (stack.getOrCreateTag().getInt("draw_time") < 11) {
                return event.setAndContinue(RawAnimation.begin().thenLoop("animation.vec.draw"));
            }

            if (stack.getOrCreateTag().getInt("fire_animation") > 0) {
                return event.setAndContinue(RawAnimation.begin().thenPlay("animation.vec.fire"));
            }

            if (stack.getOrCreateTag().getBoolean("reloading") && stack.getOrCreateTag().getBoolean("empty_reload")) {
                return event.setAndContinue(RawAnimation.begin().thenPlay("animation.vec.reload_empty"));
            }

            if (stack.getOrCreateTag().getBoolean("reloading") && !stack.getOrCreateTag().getBoolean("empty_reload")) {
                return event.setAndContinue(RawAnimation.begin().thenPlay("animation.vec.reload_normal"));
            }

            if (stack.getOrCreateTag().getInt("fire_mode") == 0 && stack.getOrCreateTag().getDouble("cg") > 0) {
                return event.setAndContinue(RawAnimation.begin().thenPlay("animation.vec.changefirerate3"));
            }

            if (stack.getOrCreateTag().getInt("fire_mode") == 1 && stack.getOrCreateTag().getDouble("cg") > 0) {
                return event.setAndContinue(RawAnimation.begin().thenPlay("animation.vec.changefirerate2"));
            }

            if (stack.getOrCreateTag().getInt("fire_mode") == 2 && stack.getOrCreateTag().getDouble("cg") > 0) {
                return event.setAndContinue(RawAnimation.begin().thenPlay("animation.vec.changefirerate"));
            }

            if (player.isSprinting() && player.onGround() && player.getPersistentData().getDouble("noRun") == 0) {
                return event.setAndContinue(RawAnimation.begin().thenLoop("animation.vec.run"));
            }

            return event.setAndContinue(RawAnimation.begin().thenLoop("animation.vec.idle"));
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
        AnimationController<VectorItem> procedureController = new AnimationController<>(this, "procedureController", 0, this::procedurePredicate);
        data.add(procedureController);
        AnimationController<VectorItem> idleController = new AnimationController<>(this, "idleController", 4, this::idlePredicate);
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

    public static ItemStack getGunInstance() {
        ItemStack stack = new ItemStack(TargetModItems.VECTOR.get());
        GunsTool.initCreativeGun(stack, TargetModItems.VECTOR.getId().getPath());
        return stack;
    }

    @Override
    public Set<SoundEvent> getReloadSound() {
        return Set.of(TargetModSounds.VECTOR_RELOAD_NORMAL.get(), TargetModSounds.VECTOR_RELOAD_EMPTY.get());
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
                    new AttributeModifier(uuid, TargetMod.ATTRIBUTE_MODIFIER, -0.03f, AttributeModifier.Operation.MULTIPLY_BASE));
        }
        return map;
    }

    @Override
    public void setAnimationProcedure(String procedure) {
        this.animationProcedure = procedure;
    }

    @Override
    public void inventoryTick(ItemStack itemStack, Level world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(itemStack, world, entity, slot, selected);

        CompoundTag tag = itemStack.getOrCreateTag();
        double id = tag.getDouble("id");
        var mainHandItem = entity instanceof LivingEntity living ? living.getMainHandItem() : ItemStack.EMPTY;
        if (mainHandItem.getOrCreateTag().getDouble("id") != tag.getDouble("id")) {
            tag.putBoolean("empty_reload", false);
            tag.putBoolean("reloading", false);
            tag.putDouble("reload_time", 0);
        }
        if (tag.getBoolean("reloading") && tag.getInt("ammo") == 0) {
            if (tag.getDouble("reload_time") == 61) {
                entity.getPersistentData().putDouble("id", id);
                if (entity instanceof ServerPlayer serverPlayer) {
                    serverPlayer.connection.send(new ClientboundSoundPacket(new Holder.Direct<>(ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("target:vector_reload_empty"))),
                            SoundSource.PLAYERS, serverPlayer.getX(), serverPlayer.getY(), serverPlayer.getZ(), 10f, 1f, serverPlayer.level().random.nextLong()));
                }
//                entity.level().playSound(null, entity.blockPosition(), TargetModSounds.VECTOR_RELOAD_EMPTY.get(), SoundSource.PLAYERS, 100, 1);
            }
            if (mainHandItem.getItem() == itemStack.getItem()
                    && mainHandItem.getOrCreateTag().getDouble("id") == id
                    && tag.getDouble("reload_time") > 0) {
                tag.putDouble("reload_time", tag.getDouble("reload_time") - 1);
            } else {
                tag.putBoolean("reloading", false);
                tag.putBoolean("empty_reload", false);
                tag.putDouble("reload_time", 0);
            }
            if (tag.getDouble("reload_time") == 1 && mainHandItem.getOrCreateTag().getDouble("id") == id) {
                GunsTool.reload(entity, GunInfo.Type.HANDGUN);
            }
        } else if (tag.getBoolean("reloading") && tag.getInt("ammo") > 0) {
            if (tag.getDouble("reload_time") == 43) {
                entity.getPersistentData().putDouble("id", id);
                if (entity instanceof ServerPlayer serverPlayer) {
                    serverPlayer.connection.send(new ClientboundSoundPacket(new Holder.Direct<>(ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("target:vector_reload_normal"))),
                            SoundSource.PLAYERS, serverPlayer.getX(), serverPlayer.getY(), serverPlayer.getZ(), 10f, 1f, serverPlayer.level().random.nextLong()));
                }
//                entity.level().playSound(null, entity.blockPosition(), TargetModSounds.VECTOR_RELOAD_NORMAL.get(), SoundSource.PLAYERS, 100, 1);
            }
            if (mainHandItem.getItem() == itemStack.getItem()
                    && mainHandItem.getOrCreateTag().getDouble("id") == id
                    && tag.getDouble("reload_time") > 0) {
                tag.putDouble("reload_time", (tag.getDouble("reload_time") - 1));
            } else {
                tag.putBoolean("reloading", false);
                tag.putBoolean("empty_reload", false);
                tag.putDouble("reload_time", 0);
            }
            if (tag.getDouble("reload_time") == 1 && mainHandItem.getOrCreateTag().getDouble("id") == id) {
                GunsTool.reload(entity, GunInfo.Type.HANDGUN, true);
            }
        }
    }

    @Override
    public ResourceLocation getGunIcon() {
        return new ResourceLocation(TargetMod.MODID, "textures/gun_icon/vector_icon.png");
    }

    @Override
    public String getGunDisplayName() {
        return "VECTOR";
    }
}