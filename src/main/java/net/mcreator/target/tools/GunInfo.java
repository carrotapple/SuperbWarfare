package net.mcreator.target.tools;

public class GunInfo {
    public enum Type {
        HANDGUN("item.target.ammo.handgun"),
        RIFLE("item.target.ammo.rifle"),
        SHOTGUN("item.target.ammo.shotgun"),
        SNIPER("item.target.ammo.sniper");
        public final String translatableKey;

        Type(String translatableKey) {
            this.translatableKey = translatableKey;
        }
    }
}
