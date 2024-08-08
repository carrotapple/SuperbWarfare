package net.mcreator.superbwarfare.perk;

import net.minecraft.util.Mth;

public class AmmoPerk extends Perk {
    public float bypassArmorRate;
    public float[] rgb;

    public AmmoPerk(AmmoPerk.Builder builder) {
        super(builder.descriptionId, builder.type);
        this.bypassArmorRate = builder.bypassArmorRate;
        this.rgb = builder.rgb;
    }

    public static class Builder {
        String descriptionId;
        Type type;
        float bypassArmorRate = 0.0f;
        float[] rgb = {1, 222 / 255f, 39 / 255f};

        public Builder(String descriptionId, Type type) {
            this.descriptionId = descriptionId;
            this.type = type;
        }

        public AmmoPerk.Builder bypassArmorRate(float bypassArmorRate) {
            this.bypassArmorRate = Mth.clamp(bypassArmorRate, -1, 1);
            return this;
        }

        public AmmoPerk.Builder rgb(int r, int g, int b) {
            this.rgb[0] = r / 255f;
            this.rgb[1] = g / 255f;
            this.rgb[2] = b / 255f;
            return this;
        }
    }
}
