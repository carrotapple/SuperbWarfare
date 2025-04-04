package com.atsuishio.superbwarfare.item.gun.smg;

import com.atsuishio.superbwarfare.ModUtils;
import com.atsuishio.superbwarfare.client.PoseTool;
import com.atsuishio.superbwarfare.client.renderer.item.VectorItemRenderer;
import com.atsuishio.superbwarfare.event.ClientEventHandler;
import com.atsuishio.superbwarfare.init.ModSounds;
import com.atsuishio.superbwarfare.init.ModTags;
import com.atsuishio.superbwarfare.item.gun.GunItem;
import com.atsuishio.superbwarfare.network.ModVariables;
import com.atsuishio.superbwarfare.perk.Perk;
import com.atsuishio.superbwarfare.perk.PerkHelper;
import com.atsuishio.superbwarfare.tools.GunsTool;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
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

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Set;
import java.util.function.Consumer;

public class VectorItem extends GunItem implements GeoItem {

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
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
            public HumanoidModel.ArmPose getArmPose(LivingEntity entityLiving, InteractionHand hand, ItemStack stack) {
                return PoseTool.pose(entityLiving, hand, stack);
            }
        });
    }

    public void getTransformType(ItemDisplayContext type) {
        transformType = type;
    }

    private PlayState idlePredicate(AnimationState<VectorItem> event) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return PlayState.STOP;
        ItemStack stack = player.getMainHandItem();
        if (!stack.is(ModTags.Items.GUN)) return PlayState.STOP;
        boolean drum = GunsTool.getAttachmentType(stack, GunsTool.AttachmentType.MAGAZINE) == 2;

        if (stack.getOrCreateTag().getBoolean("is_empty_reloading")) {
            if (drum) {
                return event.setAndContinue(RawAnimation.begin().thenPlay("animation.vec.reload_empty_drum"));
            } else {
                return event.setAndContinue(RawAnimation.begin().thenPlay("animation.vec.reload_empty"));
            }
        }

        if (stack.getOrCreateTag().getBoolean("is_normal_reloading")) {
            if (drum) {
                return event.setAndContinue(RawAnimation.begin().thenPlay("animation.vec.reload_normal_drum"));
            } else {
                return event.setAndContinue(RawAnimation.begin().thenPlay("animation.vec.reload_normal"));
            }
        }

        if (player.isSprinting() && player.onGround() && player.getPersistentData().getDouble("noRun") == 0 && ClientEventHandler.drawTime < 0.01) {
            if (player.hasEffect(MobEffects.MOVEMENT_SPEED)) {
                return event.setAndContinue(RawAnimation.begin().thenLoop("animation.vec.run_fast"));
            } else {
                return event.setAndContinue(RawAnimation.begin().thenLoop("animation.vec.run"));
            }
        }

        return event.setAndContinue(RawAnimation.begin().thenLoop("animation.vec.idle"));
    }

    private PlayState editPredicate(AnimationState<VectorItem> event) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return PlayState.STOP;
        ItemStack stack = player.getMainHandItem();
        if (!stack.is(ModTags.Items.GUN)) return PlayState.STOP;

        if (player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables()).edit) {
            return event.setAndContinue(RawAnimation.begin().thenPlay("animation.vector.edit"));
        }

        return event.setAndContinue(RawAnimation.begin().thenLoop("animation.vec.idle"));
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar data) {
        AnimationController<VectorItem> idleController = new AnimationController<>(this, "idleController", 2, this::idlePredicate);
        data.add(idleController);
        var editController = new AnimationController<>(this, "editController", 1, this::editPredicate);
        data.add(editController);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    @ParametersAreNonnullByDefault
    public void inventoryTick(ItemStack stack, Level world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);

        int scopeType = GunsTool.getAttachmentType(stack, GunsTool.AttachmentType.SCOPE);

        if (scopeType == 3) {
            CompoundTag tag = stack.getOrCreateTag().getCompound("Attachments");
            tag.putInt("Scope", 0);
        }
    }

    @Override
    public double getCustomZoom(ItemStack stack) {
        int scopeType = GunsTool.getAttachmentType(stack, GunsTool.AttachmentType.SCOPE);
        if (scopeType == 2) return 0.75;
        return GunsTool.getGunDoubleTag(stack, "CustomZoom");
    }

    @Override
    public int getCustomMagazine(ItemStack stack) {
        int magType = GunsTool.getAttachmentType(stack, GunsTool.AttachmentType.MAGAZINE);
        return switch (magType) {
            case 1 -> 20;
            case 2 -> 57;
            default -> 0;
        };
    }

    @Override
    public Set<SoundEvent> getReloadSound() {
        return Set.of(ModSounds.VECTOR_RELOAD_NORMAL.get(), ModSounds.VECTOR_RELOAD_EMPTY.get());
    }

    @Override
    public ResourceLocation getGunIcon() {
        return ModUtils.loc("textures/gun_icon/vector_icon.png");
    }

    @Override
    public String getGunDisplayName() {
        return "VECTOR";
    }

    @Override
    public boolean canApplyPerk(Perk perk) {
        return PerkHelper.SMG_PERKS.test(perk) || PerkHelper.MAGAZINE_PERKS.test(perk);
    }

    @Override
    public boolean isMagazineReload(ItemStack stack) {
        return true;
    }

    @Override
    public boolean isOpenBolt(ItemStack stack) {
        return true;
    }

    @Override
    public boolean hasBulletInBarrel(ItemStack stack) {
        return true;
    }

    @Override
    public boolean isAutoWeapon(ItemStack stack) {
        return true;
    }

    @Override
    public boolean isCustomizable(ItemStack stack) {
        return true;
    }

    @Override
    public boolean hasCustomBarrel(ItemStack stack) {
        return true;
    }

    @Override
    public boolean hasCustomGrip(ItemStack stack) {
        return true;
    }

    @Override
    public boolean hasCustomMagazine(ItemStack stack) {
        return true;
    }

    @Override
    public boolean hasCustomScope(ItemStack stack) {
        return true;
    }

    @Override
    public boolean hasCustomStock(ItemStack stack) {
        return true;
    }

    @Override
    public boolean canEjectShell(ItemStack stack) {
        return true;
    }

    @Override
    public int getAvailableFireModes() {
        return FireMode.SEMI.flag + FireMode.BURST.flag + FireMode.AUTO.flag;
    }
}