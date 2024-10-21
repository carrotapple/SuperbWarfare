package net.mcreator.superbwarfare.item.gun.rifle;

import net.mcreator.superbwarfare.ModUtils;
import net.mcreator.superbwarfare.client.PoseTool;
import net.mcreator.superbwarfare.client.renderer.item.AK12ItemRenderer;
import net.mcreator.superbwarfare.event.ClientEventHandler;
import net.mcreator.superbwarfare.init.ModItems;
import net.mcreator.superbwarfare.init.ModSounds;
import net.mcreator.superbwarfare.init.ModTags;
import net.mcreator.superbwarfare.item.AnimatedItem;
import net.mcreator.superbwarfare.item.gun.GunItem;
import net.mcreator.superbwarfare.network.ModVariables;
import net.mcreator.superbwarfare.perk.Perk;
import net.mcreator.superbwarfare.perk.PerkHelper;
import net.mcreator.superbwarfare.tools.GunsTool;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
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

import java.util.Set;
import java.util.function.Consumer;

public class AK12Item extends GunItem implements GeoItem, AnimatedItem {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    public static ItemDisplayContext transformType;

    @Override
    public Set<SoundEvent> getReloadSound() {
        return Set.of(ModSounds.AK_12_RELOAD_EMPTY.get(), ModSounds.AK_12_RELOAD_NORMAL.get());
    }

    public AK12Item() {
        super(new Properties().stacksTo(1).rarity(Rarity.EPIC));
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        super.initializeClient(consumer);
        consumer.accept(new IClientItemExtensions() {
            private final BlockEntityWithoutLevelRenderer renderer = new AK12ItemRenderer();

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

    private PlayState idlePredicate(AnimationState<AK12Item> event) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return PlayState.STOP;
        ItemStack stack = player.getMainHandItem();
        if (!stack.is(ModTags.Items.GUN)) return PlayState.STOP;

        if (stack.getOrCreateTag().getBoolean("is_empty_reloading")) {
            return event.setAndContinue(RawAnimation.begin().thenPlay("animation.ak12.reload_empty"));
        }

        if (stack.getOrCreateTag().getBoolean("is_normal_reloading")) {
            if (GunsTool.getAttachmentType(stack, GunsTool.AttachmentType.MAGAZINE) == 2) {
                return event.setAndContinue(RawAnimation.begin().thenPlay("animation.ak12.reload_normal_drum"));
            } else {
                return event.setAndContinue(RawAnimation.begin().thenPlay("animation.ak12.reload_normal"));
            }
        }

        if (player.isSprinting() && player.onGround() && player.getPersistentData().getDouble("noRun") == 0 && ClientEventHandler.drawTime < 0.01) {
            if (player.hasEffect(MobEffects.MOVEMENT_SPEED)) {
                return event.setAndContinue(RawAnimation.begin().thenLoop("animation.ak12.run_fast"));
            } else {
                return event.setAndContinue(RawAnimation.begin().thenLoop("animation.ak12.run"));
            }
        }

        return event.setAndContinue(RawAnimation.begin().thenLoop("animation.ak12.idle"));
    }

    private PlayState editPredicate(AnimationState<AK12Item> event) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return PlayState.STOP;
        ItemStack stack = player.getMainHandItem();
        if (!stack.is(ModTags.Items.GUN)) return PlayState.STOP;

        if (player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables()).edit) {
            return event.setAndContinue(RawAnimation.begin().thenPlay("animation.ak12.edit"));
        }

        return event.setAndContinue(RawAnimation.begin().thenLoop("animation.ak12.idle"));
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar data) {
        var idleController = new AnimationController<>(this, "idleController", 3, this::idlePredicate);
        data.add(idleController);
        var editController = new AnimationController<>(this, "editController", 1, this::editPredicate);
        data.add(editController);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    public static ItemStack getGunInstance() {
        ItemStack stack = new ItemStack(ModItems.AK_12.get());
        GunsTool.initCreativeGun(stack, ModItems.AK_12.getId().getPath());
        return stack;
    }

    @Override
    public void setAnimationProcedure(String procedure) {
    }

    @Override
    public void inventoryTick(ItemStack stack, Level world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);

        int scopeType = GunsTool.getAttachmentType(stack, GunsTool.AttachmentType.SCOPE);
        int barrelType = GunsTool.getAttachmentType(stack, GunsTool.AttachmentType.BARREL);
        int magType = GunsTool.getAttachmentType(stack, GunsTool.AttachmentType.MAGAZINE);
        int stockType = GunsTool.getAttachmentType(stack, GunsTool.AttachmentType.STOCK);

        int customMag = switch (magType) {
            case 1 -> 15;
            case 2 -> 45;
            default -> 0;
        };

        double customZoom = switch (scopeType) {
            case 0, 1 -> 0;
            case 2 -> 2.15;
            default -> stack.getOrCreateTag().getDouble("CustomZoom");
        };

        stack.getOrCreateTag().putBoolean("CanAdjustZoomFov", scopeType == 3);

        stack.getOrCreateTag().putDouble("CustomZoom", customZoom);

        stack.getOrCreateTag().putInt("customMag", customMag);
    }

    @Override
    public ResourceLocation getGunIcon() {
        return new ResourceLocation(ModUtils.MODID, "textures/gun_icon/ak12_icon.png");
    }

    @Override
    public String getGunDisplayName() {
        return "   AK-12";
    }

    @Override
    public boolean canApplyPerk(Perk perk) {
        return PerkHelper.RIFLE_PERKS.test(perk) || PerkHelper.MAGAZINE_PERKS.test(perk);
    }
}