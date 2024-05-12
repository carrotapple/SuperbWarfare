package net.mcreator.target.tools;

public class GunInfo {
    public enum Type {
        HANDGUN("Handgun"), RIFLE("Rifle"), SHOTGUN("Shotgun"), SNIPER("Sniper");
        public final String name;

        Type(String name) {
            this.name = name;
        }
    }
}
