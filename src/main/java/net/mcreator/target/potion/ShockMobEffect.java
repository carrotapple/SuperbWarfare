
package net.mcreator.target.potion;

import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffect;

import net.mcreator.target.procedures.ShockZaiXiaoGuoChiXuShiMeiKeFaShengProcedure;
import net.mcreator.target.procedures.ShockDangYaoShuiXiaoGuoKaiShiYingYongShiProcedure;

public class ShockMobEffect extends MobEffect {
	public ShockMobEffect() {
		super(MobEffectCategory.BENEFICIAL, -256);
	}

	@Override
	public String getDescriptionId() {
		return "effect.target.shock";
	}

	@Override
	public void addAttributeModifiers(LivingEntity entity, AttributeMap attributeMap, int amplifier) {
		ShockDangYaoShuiXiaoGuoKaiShiYingYongShiProcedure.execute(entity.level(), entity.getX(), entity.getY(), entity.getZ());
	}

	@Override
	public void applyEffectTick(LivingEntity entity, int amplifier) {
		ShockZaiXiaoGuoChiXuShiMeiKeFaShengProcedure.execute(entity);
	}

	@Override
	public boolean isDurationEffectTick(int duration, int amplifier) {
		return true;
	}
}
