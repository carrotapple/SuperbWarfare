package net.mcreator.target.item.common.ammo;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.mcreator.target.client.renderer.item.RocketItemRenderer;
import net.mcreator.target.item.AnimatedItem;
import net.mcreator.target.tools.ParticleTool;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
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

import java.util.function.Consumer;

public class Rocket extends Item implements GeoItem, AnimatedItem {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    public String animationProcedure = "empty";
    public static ItemDisplayContext transformType;

    public Rocket() {
        super(new Item.Properties().stacksTo(16));
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        super.initializeClient(consumer);
        consumer.accept(new IClientItemExtensions() {
            private final BlockEntityWithoutLevelRenderer renderer = new RocketItemRenderer();

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return renderer;
            }
        });
    }

    public void getTransformType(ItemDisplayContext type) {
        transformType = type;
    }

    private PlayState idlePredicate(AnimationState<Rocket> event) {
        if (transformType != null && transformType.firstPerson()) {
            if (this.animationProcedure.equals("empty")) {
                event.getController().setAnimation(RawAnimation.begin().thenLoop("animation.rpg.idle"));
                return PlayState.CONTINUE;
            }
        }
        return PlayState.STOP;
    }

    private PlayState procedurePredicate(AnimationState<Rocket> event) {
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
        var idleController = new AnimationController<>(this, "idleController", 0, this::idlePredicate);
        data.add(idleController);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> map = super.getAttributeModifiers(slot, stack);
        if (slot == EquipmentSlot.MAINHAND) {
            map = HashMultimap.create(map);
            map.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Item modifier", 6d, AttributeModifier.Operation.ADDITION));
            map.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Item modifier", -2.4, AttributeModifier.Operation.ADDITION));
        }
        return map;
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity entity, LivingEntity source) {
        if (entity.level() instanceof ServerLevel level && Math.random() < 0.25) {
            level.explode(source, source.getX(), source.getY() + 1, source.getZ(), 6, Level.ExplosionInteraction.NONE);
            level.explode(null, source.getX(), source.getY() + 1, source.getZ(), 6, Level.ExplosionInteraction.NONE);

            if (!source.level().isClientSide() && source.getServer() != null) {
                ParticleTool.spawnMediumExplosionParticles(source.level(), source.getPosition(0));
            }

            stack.shrink(1);
        }

        return super.hurtEnemy(stack, entity, source);
    }

    @Override
    public void setAnimationProcedure(String procedure) {
        this.animationProcedure = procedure;
    }
}