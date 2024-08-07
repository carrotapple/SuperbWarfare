package net.mcreator.superbwarfare.perk;

public class Perk {
    public String descriptionId;
    public Type type;

    public Perk(String descriptionId, Type type) {
        this.descriptionId = descriptionId;
        this.type = type;
    }

    public enum Type {
        AMMO(0),
        FUNCTIONAL(1),
        DAMAGE(2);
        private final int slot;

        Type(int slot) {
            this.slot = slot;
        }

        public int getSlot() {
            return slot;
        }
    }
}
