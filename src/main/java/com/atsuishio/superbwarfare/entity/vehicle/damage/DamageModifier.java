package com.atsuishio.superbwarfare.entity.vehicle.damage;

import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class DamageModifier {

    private final List<DamageModify> list = new ArrayList<>();

    /**
     * 免疫所有伤害
     */
    public DamageModifier immuneTo() {
        list.add(new DamageModify(DamageModify.ReduceType.IMMUNITY, 0, (TagKey<DamageType>) null));
        return this;
    }

    /**
     * 免疫指定类型的伤害
     *
     * @param sourceTagKey 伤害类型
     */
    public DamageModifier immuneTo(TagKey<DamageType> sourceTagKey) {
        list.add(new DamageModify(DamageModify.ReduceType.IMMUNITY, 0, sourceTagKey));
        return this;
    }

    /**
     * 免疫指定类型的伤害
     *
     * @param sourceKey 伤害类型
     */
    public DamageModifier immuneTo(ResourceKey<DamageType> sourceKey) {
        list.add(new DamageModify(DamageModify.ReduceType.IMMUNITY, 0, sourceKey));
        return this;
    }

    /**
     * 免疫指定类型的伤害
     *
     * @param condition 伤害来源判定条件
     */
    public DamageModifier immuneTo(Function<DamageSource, Boolean> condition) {
        list.add(new DamageModify(DamageModify.ReduceType.IMMUNITY, 0, condition));
        return this;
    }

    /**
     * 固定减少所有伤害一定数值
     *
     * @param value 要减少的数值
     */
    public DamageModifier reduce(float value) {
        list.add(new DamageModify(DamageModify.ReduceType.REDUCE, value, (TagKey<DamageType>) null));
        return this;
    }

    /**
     * 固定减少指定类型的伤害一定数值
     *
     * @param value        要减少的数值
     * @param sourceTagKey 伤害类型
     */
    public DamageModifier reduce(float value, TagKey<DamageType> sourceTagKey) {
        list.add(new DamageModify(DamageModify.ReduceType.REDUCE, value, sourceTagKey));
        return this;
    }

    /**
     * 固定减少指定类型的伤害一定数值
     *
     * @param value     要减少的数值
     * @param sourceKey 伤害类型
     */
    public DamageModifier reduce(float value, ResourceKey<DamageType> sourceKey) {
        list.add(new DamageModify(DamageModify.ReduceType.REDUCE, value, sourceKey));
        return this;
    }

    /**
     * 固定减少指定类型的伤害一定数值
     *
     * @param value     要减少的数值
     * @param condition 伤害来源判定条件
     */
    public DamageModifier reduce(float value, Function<DamageSource, Boolean> condition) {
        list.add(new DamageModify(DamageModify.ReduceType.REDUCE, value, condition));
        return this;
    }

    /**
     * 将所有类型的伤害乘以指定数值
     *
     * @param value 要乘以的数值
     */
    public DamageModifier multiply(float value) {
        list.add(new DamageModify(DamageModify.ReduceType.MULTIPLY, value, (TagKey<DamageType>) null));
        return this;
    }

    /**
     * 将指定类型的伤害乘以指定数值
     *
     * @param value        要乘以的数值
     * @param sourceTagKey 伤害类型
     */
    public DamageModifier multiply(float value, TagKey<DamageType> sourceTagKey) {
        list.add(new DamageModify(DamageModify.ReduceType.MULTIPLY, value, sourceTagKey));
        return this;
    }

    /**
     * 将指定类型的伤害乘以指定数值
     *
     * @param value     要乘以的数值
     * @param sourceKey 伤害类型
     */
    public DamageModifier multiply(float value, ResourceKey<DamageType> sourceKey) {
        list.add(new DamageModify(DamageModify.ReduceType.MULTIPLY, value, sourceKey));
        return this;
    }

    /**
     * 将指定类型的伤害乘以指定数值
     *
     * @param value     要乘以的数值
     * @param condition 伤害来源判定条件
     */
    public DamageModifier multiply(float value, Function<DamageSource, Boolean> condition) {
        list.add(new DamageModify(DamageModify.ReduceType.MULTIPLY, value, condition));
        return this;
    }

    /**
     * 计算减伤后的伤害值
     *
     * @param source 伤害来源
     * @param damage 原伤害值
     * @return 减伤后的伤害值
     */
    public float compute(DamageSource source, float damage) {
        for (DamageModify damageModify : list) {
            if (damageModify.match(source)) {
                damage = damageModify.compute(damage);

                if (damage <= 0) return 0;
            }
        }
        return damage;
    }
}
