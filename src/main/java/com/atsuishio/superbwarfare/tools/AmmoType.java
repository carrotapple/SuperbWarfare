package com.atsuishio.superbwarfare.tools;

import com.atsuishio.superbwarfare.network.ModVariables;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
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

    // ItemStack
    public int get(ItemStack stack) {
        return get(stack.getOrCreateTag());
    }

    public void set(ItemStack stack, int count) {
        set(stack.getOrCreateTag(), count);
    }

    public void add(ItemStack stack, int count) {
        add(stack.getOrCreateTag(), count);
    }

    // NBTTag
    public int get(CompoundTag tag) {
        return tag.getInt(this.name);
    }

    public void set(CompoundTag tag, int count) {
        if (count < 0) count = 0;
        tag.putInt(this.name, count);
    }

    public void add(CompoundTag tag, int count) {
        set(tag, safeAdd(get(tag), count));
    }

    public int get(Player player) {
        return player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY)
                .map(this::get)
                .orElse(0);
    }

    public void set(Player player, int count) {
        player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY)
                .ifPresent(c -> set(c, Math.max(0, count)));
    }

    public void add(Player player, int count) {
        player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY)
                .ifPresent(c -> add(c, count));
    }


    // PlayerVariables
    public int get(ModVariables.PlayerVariables variable) {
        return switch (this) {
            case HANDGUN -> variable.handgunAmmo;
            case RIFLE -> variable.rifleAmmo;
            case SHOTGUN -> variable.shotgunAmmo;
            case SNIPER -> variable.sniperAmmo;
            case HEAVY -> variable.heavyAmmo;
        };
    }

    public void set(ModVariables.PlayerVariables variable, int count) {
        if (count < 0) count = 0;

        switch (this) {
            case HANDGUN -> variable.handgunAmmo = count;
            case RIFLE -> variable.rifleAmmo = count;
            case SHOTGUN -> variable.shotgunAmmo = count;
            case SNIPER -> variable.sniperAmmo = count;
            case HEAVY -> variable.heavyAmmo = count;
        }
    }

    public void add(ModVariables.PlayerVariables variable, int count) {
        set(variable, safeAdd(get(variable), count));
    }

    private int safeAdd(int a, int b) {
        var newCount = (long) a + (long) b;

        if (newCount > Integer.MAX_VALUE) {
            newCount = Integer.MAX_VALUE;
        } else if (newCount < 0) {
            newCount = 0;
        }

        return (int) newCount;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
