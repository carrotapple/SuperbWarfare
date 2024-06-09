package net.mcreator.target.mobeffect;

import net.mcreator.target.TargetMod;
import net.mcreator.target.init.TargetModDamageTypes;
import net.mcreator.target.init.TargetModMobEffects;
import net.mcreator.target.init.TargetModSounds;
import net.mcreator.target.network.message.ClientIndicatorMessage;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ShockMobEffect extends MobEffect {
    public ShockMobEffect() {
        super(MobEffectCategory.BENEFICIAL, -256);
        addAttributeModifier(Attributes.MOVEMENT_SPEED, "7107DE5E-7CE8-4030-940E-514C1F160890", -2.0F, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }

    @Override
    public void addAttributeModifiers(LivingEntity entity, AttributeMap attributeMap, int amplifier) {
        if (!entity.level().isClientSide()) {
            entity.level().playSound(null, BlockPos.containing(entity.getX(), entity.getY(), entity.getZ()), TargetModSounds.SHOCK.get(), SoundSource.HOSTILE, 1, 1);
        } else {
            entity.level().playLocalSound(entity.getX(), entity.getY(), entity.getZ(), TargetModSounds.SHOCK.get(), SoundSource.HOSTILE, 1, 1, false);
        }
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        entity.setXRot((float) Mth.nextDouble(RandomSource.create(), -23, -36));
        entity.xRotO = entity.getXRot();
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }

    @SubscribeEvent
    public static void onEffectAdded(MobEffectEvent.Added event) {
        LivingEntity living = event.getEntity();
        if (event.getEffectSource() instanceof LivingEntity source) {
            MobEffectInstance instance = event.getEffectInstance();

            if (!instance.getEffect().equals(TargetModMobEffects.SHOCK.get())) {
                return;
            }

            living.getPersistentData().putInt("TargetShockAttacker", source.getId());

            if (living.hasEffect(TargetModMobEffects.SHOCK.get())) {
                System.out.println(instance.getDuration());
                if (instance.getDuration() % 10 == 0) {
                    living.hurt(TargetModDamageTypes.causeShockDamage(living.level().registryAccess(),
                            source), 5.0f);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onEffectExpired(MobEffectEvent.Expired event) {
        LivingEntity living = event.getEntity();

        MobEffectInstance instance = event.getEffectInstance();
        if (instance == null) {
            return;
        }

        if (instance.getEffect().equals(TargetModMobEffects.SHOCK.get())) {
            living.getPersistentData().remove("TargetShockAttacker");
        }
    }

    @SubscribeEvent
    public static void onEffectRemoved(MobEffectEvent.Remove event) {
        LivingEntity living = event.getEntity();

        MobEffectInstance instance = event.getEffectInstance();
        if (instance == null) {
            return;
        }

        if (instance.getEffect().equals(TargetModMobEffects.SHOCK.get())) {
            living.getPersistentData().remove("TargetShockAttacker");
        }
    }

    @SubscribeEvent
    public static void onLivingTick(LivingEvent.LivingTickEvent event) {
        LivingEntity living = event.getEntity();

        if (living.hasEffect(TargetModMobEffects.SHOCK.get())) {
            MobEffectInstance instance = living.getEffect(TargetModMobEffects.SHOCK.get());

            if (instance == null) {
                return;
            }

            Entity entity;
            if (!living.getPersistentData().contains("TargetShockAttacker")) {
                entity = null;
            } else {
                entity = living.level().getEntity(living.getPersistentData().getInt("TargetShockAttacker"));
            }

            if (instance.getDuration() % 20 == 0) {
                living.hurt(TargetModDamageTypes.causeShockDamage(living.level().registryAccess(), entity), 5.0f);

                if (entity instanceof ServerPlayer player) {
                    player.level().playSound(null, player.blockPosition(), TargetModSounds.INDICATION.get(), SoundSource.VOICE, 1, 1);
                    TargetMod.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> player), new ClientIndicatorMessage(0, 5));
                }
            }
        }
    }
}
