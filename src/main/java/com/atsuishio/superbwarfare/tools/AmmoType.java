package com.atsuishio.superbwarfare.tools;

public enum AmmoType {
    HANDGUN("item.superbwarfare.ammo.handgun"),
    RIFLE("item.superbwarfare.ammo.rifle"),
    SHOTGUN("item.superbwarfare.ammo.shotgun"),
    SNIPER("item.superbwarfare.ammo.sniper"),
    HEAVY("item.superbwarfare.ammo.heavy");
    public final String translatableKey;

    AmmoType(String translatableKey) {
        this.translatableKey = translatableKey;
    }
}
