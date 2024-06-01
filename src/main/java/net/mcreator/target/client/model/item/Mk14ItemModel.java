package net.mcreator.target.client.model.item;

import net.mcreator.target.item.gun.Mk14Item;
import net.mcreator.target.network.TargetModVariables;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

public class Mk14ItemModel extends GeoModel<Mk14Item> {
    @Override
    public ResourceLocation getAnimationResource(Mk14Item animatable) {
        return new ResourceLocation("target", "animations/mk14ebr.animation.json");
    }

    @Override
    public ResourceLocation getModelResource(Mk14Item animatable) {
        return new ResourceLocation("target", "geo/mk14ebr.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Mk14Item animatable) {
        return new ResourceLocation("target", "textures/item/mk14.png");
    }

    @Override
    public void setCustomAnimations(Mk14Item animatable, long instanceId, AnimationState animationState) {
        CoreGeoBone gun = getAnimationProcessor().getBone("0");
        CoreGeoBone scope = getAnimationProcessor().getBone("elcan");
        CoreGeoBone flare = getAnimationProcessor().getBone("flare");
        CoreGeoBone rex = getAnimationProcessor().getBone("rex");
        CoreGeoBone l = getAnimationProcessor().getBone("l");
        CoreGeoBone r = getAnimationProcessor().getBone("r");
        CoreGeoBone body = getAnimationProcessor().getBone("mk14_default");
        CoreGeoBone shuan = getAnimationProcessor().getBone("shuan");

        Player player = Minecraft.getInstance().player;
        ItemStack stack = player.getMainHandItem();

        if (player.getPersistentData().getDouble("prone") > 0) {
            l.setRotX(-1.5f);
            r.setRotX(-1.5f);
        }

        double p = 0;
        p = player.getPersistentData().getDouble("zoom_pos");

        double zp = 0;
        zp = player.getPersistentData().getDouble("zoom_pos_z");

        gun.setPosX(6.372f * (float) p);

        gun.setPosY(0.59f * (float) p - (float) (0.2f * zp));

        gun.setPosZ(6.2f * (float) p + (float) (0.5f * zp));

        gun.setRotZ((float) (0.05f * zp));

        gun.setScaleZ(1f - (0.8f * (float) p));

        scope.setScaleZ(1f - (0.99f * (float) p));

        CoreGeoBone shen = getAnimationProcessor().getBone("shen");

        double fp = player.getPersistentData().getDouble("fire_pos");
        double fr = player.getPersistentData().getDouble("fire_rot");

        if ((player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).zooming) {
            shen.setPosY(0.02f * (float) (fp + 2 * fr));
            shen.setPosZ(2.8f * (float) (fp + 0.54f * fr));
            shen.setRotX(0.003f * (float) (fp + fr));
            if ((player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).recoilHorizon > 0) {
                shen.setRotY(0.015f * (float) fr);
            } else {
                shen.setRotY(-0.015f * (float) fr);
            }
        } else {
            shen.setPosY(0.04f * (float) (fp + 2 * fr));
            shen.setPosZ(3.5f * (float) (fp + 0.54f * fr));
            shen.setRotX(0.07f * (float) (0.18f * fp + fr));
            shen.setRotZ(-0.04f * (float) (fp + 1.3 * fr));
            if ((player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).recoilHorizon > 0) {
                shen.setRotY(0.03f * (float) fr);
            } else {
                shen.setRotY(-0.03f * (float) fr);
            }
        }

        if (gun.getPosX() > 5.5f) {

            rex.setScaleX(2.2f);
            rex.setScaleY(2.2f);
            body.setScaleZ(0.4f);
        } else {
            rex.setScaleX(0);
            rex.setScaleY(0);
            body.setScaleZ(1);
        }

        CoreGeoBone bolt = getAnimationProcessor().getBone("bolt");

        if (stack.getOrCreateTag().getDouble("gj") == 1) {
            bolt.setPosZ(6);
        }

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

        root.setPosY((float) y + 2.5f * PosY);

        root.setRotX((float) x);

        root.setRotZ(0.1f * PosX);

        CoreGeoBone move = getAnimationProcessor().getBone("move");

        double m = 0;
        m = player.getPersistentData().getDouble("move");

        double yaw = 0;
        yaw = player.getPersistentData().getDouble("yaw");

        double pit = 0;
        pit = player.getPersistentData().getDouble("gun_pitch");

        double vy = 0;
        vy = player.getPersistentData().getDouble("vy");

        move.setPosY(-1 * (float) vy);

        if ((player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).zooming) {

            move.setPosX(0);

            move.setRotX(0);

            move.setRotZ(0);

            move.setRotY(0);

        } else {

            move.setPosX(9.3f * (float) m);

            move.setRotX(2.0f * (float) pit);

            move.setRotZ(2.7f * (float) yaw + 2.7f * (float) m);

            move.setRotY(3.9f * (float) yaw - 1.7f * (float) m);
        }
    }
}
