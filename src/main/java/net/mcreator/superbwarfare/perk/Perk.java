package net.mcreator.superbwarfare.perk;

public record Perk(net.mcreator.superbwarfare.perk.Perk.Type type) {

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
