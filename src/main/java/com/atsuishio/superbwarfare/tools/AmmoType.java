package com.atsuishio.superbwarfare.tools;

import com.atsuishio.superbwarfare.network.ModVariables;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public enum AmmoType {
    HANDGUN("item.superbwarfare.ammo.handgun", "HandgunAmmo"),
    RIFLE("item.superbwarfare.ammo.rifle", "RifleAmmo"),
    SHOTGUN("item.superbwarfare.ammo.shotgun", "ShotgunAmmo"),
    SNIPER("item.superbwarfare.ammo.sniper", "SniperAmmo"),
    HEAVY("item.superbwarfare.ammo.heavy", "HeavyAmmo");
    public final String translatableKey;
    public final String name;

    AmmoType(String translatableKey, String name) {
        this.translatableKey = translatableKey;
        this.name = name;
    }

    public static AmmoType getType(String name) {
        for (AmmoType type : values()) {
            if (type.name.equals(name)) {
                return type;
            }
        }
        return null;
    }

    public int getCount(ItemStack stack) {
        return getCount(stack.getOrCreateTag());
    }

    public void setCount(ItemStack stack, int count) {
        setCount(stack.getOrCreateTag(), count);
    }

    public int getCount(CompoundTag tag) {
        return tag.getInt(this.name);
    }

    public void setCount(CompoundTag tag, int count) {
        tag.putInt(this.name, count);
    }

    public int getCount(ModVariables.PlayerVariables variable) {
        return switch (this) {
            case HANDGUN -> variable.handgunAmmo;
            case RIFLE -> variable.rifleAmmo;
            case SHOTGUN -> variable.shotgunAmmo;
            case SNIPER -> variable.sniperAmmo;
            case HEAVY -> variable.heavyAmmo;
        };
    }

    public void setCount(ModVariables.PlayerVariables variable, int count) {
        switch (this) {
            case HANDGUN -> variable.handgunAmmo = count;
            case RIFLE -> variable.rifleAmmo = count;
            case SHOTGUN -> variable.shotgunAmmo = count;
            case SNIPER -> variable.sniperAmmo = count;
            case HEAVY -> variable.heavyAmmo = count;
        }
    }

    @Override
    public String toString() {
        return this.name;
    }
}
