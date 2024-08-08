package net.mcreator.superbwarfare.perk;

import net.minecraft.util.Mth;

public class AmmoPerk extends Perk {
    public float bypassArmorRate = 0.0f;

    public AmmoPerk(String descriptionId, Type type) {
        super(descriptionId, type);
    }

    public AmmoPerk(String descriptionId, Type type, float bypassArmorRate) {
        super(descriptionId, type);
        this.bypassArmorRate = Mth.clamp(bypassArmorRate, -1, 1);
    }
}
