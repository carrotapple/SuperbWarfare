package net.mcreator.target.item.gun;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.mcreator.target.TargetMod;
import net.mcreator.target.client.renderer.item.VectorItemRenderer;
import net.mcreator.target.init.TargetModItems;
import net.mcreator.target.init.TargetModSounds;
import net.mcreator.target.init.TargetModTags;
import net.mcreator.target.item.AnimatedItem;
import net.mcreator.target.procedures.WeaponDrawLightProcedure;
import net.mcreator.target.tools.GunReload;
import net.mcreator.target.tools.GunsTool;
import net.mcreator.target.tools.TooltipTool;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
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
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
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

@Mod.EventBusSubscriber
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

    private PlayState idlePredicate(AnimationState<VectorItem> event) {
        LocalPlayer player = Minecraft.getInstance().player;
        ItemStack stack = player.getMainHandItem();

        if (this.animationProcedure.equals("empty")) {
            if (stack.getOrCreateTag().getDouble("drawtime") < 11) {
                return event.setAndContinue(RawAnimation.begin().thenLoop("animation.vec.draw"));
            }

            if (stack.getOrCreateTag().getDouble("fireanim") > 0) {
                return event.setAndContinue(RawAnimation.begin().thenPlay("animation.vec.fire"));
            }

            if (stack.getOrCreateTag().getDouble("reloading") == 1 && stack.getOrCreateTag().getDouble("emptyreload") == 1) {
                return event.setAndContinue(RawAnimation.begin().thenPlay("animation.vec.reload"));
            }

            if (stack.getOrCreateTag().getDouble("reloading") == 1 && stack.getOrCreateTag().getDouble("emptyreload") == 0) {
                return event.setAndContinue(RawAnimation.begin().thenPlay("animation.vec.reload2"));
            }

            if (stack.getOrCreateTag().getDouble("firemode") == 0 && stack.getOrCreateTag().getDouble("cg") > 0) {
                return event.setAndContinue(RawAnimation.begin().thenPlay("animation.vec.changefirerate3"));
            }

            if (stack.getOrCreateTag().getDouble("firemode") == 1 && stack.getOrCreateTag().getDouble("cg") > 0) {
                return event.setAndContinue(RawAnimation.begin().thenPlay("animation.vec.changefirerate2"));
            }

            if (stack.getOrCreateTag().getDouble("firemode") == 2 && stack.getOrCreateTag().getDouble("cg") > 0) {
                return event.setAndContinue(RawAnimation.begin().thenPlay("animation.vec.changefirerate"));
            }

            if (player.isSprinting() && player.onGround() && player.getPersistentData().getDouble("noRun") == 0) {
                return event.setAndContinue(RawAnimation.begin().thenLoop("animation.vec.run"));
            }

            return event.setAndContinue(RawAnimation.begin().thenLoop("animation.vec.idle"));
        }
        return PlayState.STOP;
    }

    private PlayState procedurePredicate(AnimationState<VectorItem> event) {
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

    @Override
    public void inventoryTick(ItemStack itemStack, Level world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(itemStack, world, entity, slot, selected);

        CompoundTag tag = itemStack.getOrCreateTag();
        double id = tag.getDouble("id");
        var mainHandItem = entity instanceof LivingEntity living ? living.getMainHandItem() : ItemStack.EMPTY;
        if (mainHandItem.getOrCreateTag().getDouble("id") != tag.getDouble("id")) {
            tag.putDouble("emptyreload", 0);
            tag.putDouble("reloading", 0);
            tag.putDouble("reloadtime", 0);
        }
        if (tag.getDouble("reloading") == 1 && tag.getDouble("ammo") == 0) {
            if (tag.getDouble("reloadtime") == 61) {
                entity.getPersistentData().putDouble("id", id);
                entity.level().playSound(null, entity.blockPosition(), TargetModSounds.VECTOR_RELOAD_EMPTY.get(), SoundSource.PLAYERS, 100, 1);
            }
            if (mainHandItem.getItem() == itemStack.getItem()
                    && mainHandItem.getOrCreateTag().getDouble("id") == id
                    && tag.getDouble("reloadtime") > 0) {
                tag.putDouble("reloadtime", tag.getDouble("reloadtime") - 1);
            } else {
                tag.putDouble("reloading", 0);
                tag.putDouble("emptyreload", 0);
                tag.putDouble("reloadtime", 0);
            }
            if (tag.getDouble("reloadtime") == 1 && mainHandItem.getOrCreateTag().getDouble("id") == id) {
                GunReload.reload(entity, GunReload.GunType.HANDGUN);
            }
        } else if (tag.getDouble("reloading") == 1 && tag.getDouble("ammo") > 0) {
            if (tag.getDouble("reloadtime") == 47) {
                entity.getPersistentData().putDouble("id", id);
                entity.level().playSound(null, entity.blockPosition(), TargetModSounds.VECTOR_RELOAD_NORMAL.get(), SoundSource.PLAYERS, 100, 1);
            }
            if (mainHandItem.getItem() == itemStack.getItem()
                    && mainHandItem.getOrCreateTag().getDouble("id") == id
                    && tag.getDouble("reloadtime") > 0) {
                tag.putDouble("reloadtime", (tag.getDouble("reloadtime") - 1));
            } else {
                tag.putDouble("reloading", 0);
                tag.putDouble("emptyreload", 0);
                tag.putDouble("reloadtime", 0);
            }
            if (tag.getDouble("reloadtime") == 1 && mainHandItem.getOrCreateTag().getDouble("id") == id) {
                GunReload.reload(entity, GunReload.GunType.HANDGUN, true);
            }
        }

        WeaponDrawLightProcedure.execute(entity, itemStack);
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

    public static ItemStack getGunInstance() {
        ItemStack stack = new ItemStack(TargetModItems.VECTOR.get());
        GunsTool.initCreativeGun(stack, TargetModItems.VECTOR.getId().getPath());
        return stack;
    }

    @Override
    public void setAnimationProcedure(String procedure) {
        this.animationProcedure = procedure;
    }

    @SubscribeEvent
    public static void handleBurstFire(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        var player = event.player;
        ItemStack mainHandItem = player.getMainHandItem();
        CompoundTag tag = mainHandItem.getOrCreateTag();
        if (mainHandItem.is(TargetModTags.Items.GUN)) {
            if (tag.getDouble("firemode") == 1) {
                player.getPersistentData().putDouble("firing", 0);
            }
            if (tag.getDouble("ammo") == 0) {
                tag.putDouble("burst", 0);
            }
        }
        Item item = mainHandItem.getItem();
        if (item == TargetModItems.VECTOR.get()
                && tag.getDouble("reloading") == 0
                && tag.getDouble("ammo") > 0
                && !player.getCooldowns().isOnCooldown(item)
                && tag.getDouble("burst") > 0
        ) {
            player.getCooldowns().addCooldown(item, tag.getDouble("burst") == 1 ? 5 : 1);
            tag.putDouble("burst", tag.getDouble("burst") - 1);
            tag.putDouble("fireanim", 2);
            tag.putDouble("ammo", (tag.getDouble("ammo") - 1));

            GunsTool.spawnBullet(player);

            player.level().playSound(null, player.blockPosition(), TargetModSounds.VECTOR_FIRE_1P.get(), SoundSource.PLAYERS, 2, 1);
            player.level().playSound(null, player.blockPosition(), TargetModSounds.VECTOR_FIRE_1P.get(), SoundSource.PLAYERS, 4, 1);
            player.level().playSound(null, player.blockPosition(), TargetModSounds.VECTOR_FAR.get(), SoundSource.PLAYERS, 6, 1);
            player.level().playSound(null, player.blockPosition(), TargetModSounds.VECTOR_VERYFAR.get(), SoundSource.PLAYERS, 12, 1);
        }
    }
}