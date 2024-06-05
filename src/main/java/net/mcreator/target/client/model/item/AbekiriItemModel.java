package net.mcreator.target.client.model.item;

import net.mcreator.target.item.gun.Abekiri;
import net.mcreator.target.network.TargetModVariables;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

public class AbekiriItemModel extends GeoModel<Abekiri> {
    @Override
    public ResourceLocation getAnimationResource(Abekiri animatable) {
        return new ResourceLocation("target", "animations/abekiri.animation.json");
    }

    @Override
    public ResourceLocation getModelResource(Abekiri animatable) {
        return new ResourceLocation("target", "geo/abekiri.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Abekiri animatable) {
        return new ResourceLocation("target", "textures/item/abekiri.png");
    }

    @Override
    public void setCustomAnimations(Abekiri animatable, long instanceId, AnimationState animationState) {
        CoreGeoBone gun = getAnimationProcessor().getBone("bone");
        CoreGeoBone shen = getAnimationProcessor().getBone("shen");


        Player player = Minecraft.getInstance().player;
        ItemStack stack = player.getMainHandItem();

        double p = 0;
        p = player.getPersistentData().getDouble("zoom_pos");

        double zp = 0;
        zp = player.getPersistentData().getDouble("zoom_pos_z");

        gun.setPosX(2.5f * (float) p);

        gun.setPosY(1.7f * (float) p - (float) (0.4f * zp));

        gun.setPosZ(2f * (float) p + (float) (0.3f * zp));

        gun.setRotZ((float) (0.05f * zp));

        double fp = player.getPersistentData().getDouble("fire_pos");
        double fr = player.getPersistentData().getDouble("fire_rot");

        shen.setPosX(-0.2f * (float) (fp + 2 * fr));
        shen.setPosY(0.2f * (float) (fp + 2 * fr));
        shen.setPosZ(4.2f * (float) (1.3 * fp + 0.54f * fr));
        shen.setRotX(0.25f * (float) (1.28f * fp + fr));
        shen.setRotZ(-0.1f * (float) (fp + 1.3 * fr));
        shen.setRotY(-0.15f * (float) fr);

        CoreGeoBone flare = getAnimationProcessor().getBone("flare");

        if (stack.getOrCreateTag().getDouble("flash_time") > 0) {
            flare.setScaleX((float) (1.0 + 0.5 * (Math.random() - 0.5)));
            flare.setScaleY((float) (1.0 + 0.5 * (Math.random() - 0.5)));
            flare.setRotZ((float) (0.5 * (Math.random() - 0.5)));
        } else {
            flare.setScaleX(0);
            flare.setScaleY(0);
            flare.setRotZ(0);
        }

        CoreGeoBone root = getAnimationProcessor().getBone("root");

        float PosX = (float)player.getPersistentData().getDouble("gun_move_posX");
        float PosY = (float)player.getPersistentData().getDouble("gun_move_posY");

        double y = 0;
        double x = 0;
        y = player.getPersistentData().getDouble("y");
        x = player.getPersistentData().getDouble("x");

        root.setPosX(PosX);

        root.setPosY((float) y + PosY);

        root.setRotX((float) x);

        float RotZ = (float) player.getPersistentData().getDouble("gun_move_rotZ");

        root.setRotY(0.2f * PosX);

        root.setRotZ(0.2f * PosX + RotZ);

        CoreGeoBone move = getAnimationProcessor().getBone("move");

        double m = player.getPersistentData().getDouble("move");

        double vy = player.getPersistentData().getDouble("vy");

        move.setPosX(9.3f * (float) m);

        move.setPosY(-0.95f * (float) vy);

        double xRot = player.getPersistentData().getDouble("xRot");

        double yRot = player.getPersistentData().getDouble("yRot");

        double zRot = player.getPersistentData().getDouble("zRot");

        move.setRotX(Mth.DEG_TO_RAD * (float) xRot);

        move.setRotY(Mth.DEG_TO_RAD * (float) yRot);

        move.setRotZ(2.7f * (float) m + Mth.DEG_TO_RAD * (float) zRot);
    }
}
