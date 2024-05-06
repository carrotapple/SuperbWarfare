package net.mcreator.target.procedures;

import net.mcreator.target.init.TargetModMobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class TaserBulletDangTouZhiWuJiZhongShiTiShiProcedure {
    public static void execute(Entity entity, Entity immediatesourceentity, Entity sourceentity) {
        if (entity == null || immediatesourceentity == null || sourceentity == null)
            return;
        (sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getOrCreateTag().putDouble("hitcount",
                ((sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getOrCreateTag().getDouble("hitcount") + 1));
        if (entity instanceof Player player && !player.isCreative()) {
            if (!player.level().isClientSide())
                player.addEffect(new MobEffectInstance(TargetModMobEffects.SHOCK.get(), 100, 0));
        }
        if (!immediatesourceentity.level().isClientSide())
            immediatesourceentity.discard();
    }
}
