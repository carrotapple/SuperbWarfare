package net.mcreator.target.mobeffect;

import net.mcreator.target.init.TargetModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.phys.Vec3;

public class ShockMobEffect extends MobEffect {
    public ShockMobEffect() {
        super(MobEffectCategory.BENEFICIAL, -256);
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
        entity.setDeltaMovement(new Vec3(0, (entity.getDeltaMovement().y()), 0));

        if (!entity.level().isClientSide()) {
            entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 10, 10));
        }

        entity.setShiftKeyDown(true);
        entity.setYRot((float) (entity.getYRot() + Mth.nextDouble(RandomSource.create(), -15, 15)));
        entity.setXRot((float) Mth.nextDouble(RandomSource.create(), -20, -23));
        entity.setYBodyRot(entity.getYRot());
        entity.setYHeadRot(entity.getYRot());
        entity.yRotO = entity.getYRot();
        entity.xRotO = entity.getXRot();
        entity.yBodyRotO = entity.getYRot();
        entity.yHeadRotO = entity.getYRot();
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }
}
