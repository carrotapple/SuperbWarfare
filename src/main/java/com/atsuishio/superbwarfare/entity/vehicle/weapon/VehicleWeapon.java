package com.atsuishio.superbwarfare.entity.vehicle.weapon;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.ItemStack;

public class VehicleWeapon {

    // 武器的图标
    public ResourceLocation icon;
    // 武器的名称
    public Component name;
    // 武器使用的弹药类型
    public ItemStack ammo;
    // 装弹类型
    public AmmoType ammoType = AmmoType.INDIRECT;
    // 最大装弹量（对直接读取备弹的武器无效）
    public int maxAmmo;
    // 当前弹药量（对直接读取备弹的武器无效）
    public int currentAmmo;
    // 备弹量
    public int backupAmmo;

    public SoundEvent sound;

    public VehicleWeapon icon(ResourceLocation icon) {
        this.icon = icon;
        return this;
    }

    public VehicleWeapon name(Component name) {
        this.name = name;
        return this;
    }

    public VehicleWeapon name(String name) {
        this.name = Component.literal(name);
        return this;
    }

    public VehicleWeapon ammo(ItemStack ammo) {
        this.ammo = ammo;
        return this;
    }

    public VehicleWeapon direct() {
        this.ammoType = AmmoType.DIRECT;
        this.maxAmmo = 0;
        this.currentAmmo = 0;
        return this;
    }

    /**
     * 切换到该武器时的音效
     *
     * @param sound 音效
     */
    public VehicleWeapon sound(SoundEvent sound) {
        this.sound = sound;
        return this;
    }

    /**
     * 载具武器的装弹类型
     * INDIRECT - 需要先进行上弹，再发射
     * DIRECT - 直接读取载具存储的弹药
     */
    public enum AmmoType {
        INDIRECT,
        DIRECT,
    }

    public VehicleWeapon maxAmmo(int maxAmmo) {
        this.maxAmmo = maxAmmo;
        return this;
    }
}
