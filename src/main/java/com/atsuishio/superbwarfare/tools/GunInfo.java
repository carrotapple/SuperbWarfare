package com.atsuishio.superbwarfare.tools;

public class GunInfo {
    public enum Type {
        HANDGUN("item.superbwarfare.ammo.handgun"),
        RIFLE("item.superbwarfare.ammo.rifle"),
        SHOTGUN("item.superbwarfare.ammo.shotgun"),
        SNIPER("item.superbwarfare.ammo.sniper");
        public final String translatableKey;

        Type(String translatableKey) {
            this.translatableKey = translatableKey;
        }
    }
}
