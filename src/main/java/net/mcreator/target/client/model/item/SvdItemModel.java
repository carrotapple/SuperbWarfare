package net.mcreator.target.client.model.item;

import net.mcreator.target.init.TargetModTags;
import net.mcreator.target.item.gun.SvdItem;
import net.mcreator.target.network.TargetModVariables;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

public class SvdItemModel extends GeoModel<SvdItem> {
    @Override
    public ResourceLocation getAnimationResource(SvdItem animatable) {
        return new ResourceLocation("target", "animations/svd.animation.json");
    }

    @Override
    public ResourceLocation getModelResource(SvdItem animatable) {
        return new ResourceLocation("target", "geo/svd.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(SvdItem animatable) {
        return new ResourceLocation("target", "textures/item/svd.png");
    }

    @Override
    public void setCustomAnimations(SvdItem animatable, long instanceId, AnimationState animationState) {
        CoreGeoBone gun = getAnimationProcessor().getBone("bone");
        CoreGeoBone flare = getAnimationProcessor().getBone("flare");
        CoreGeoBone bolt = getAnimationProcessor().getBone("bolt");
        CoreGeoBone scope = getAnimationProcessor().getBone("pso1");
        CoreGeoBone sight = getAnimationProcessor().getBone("handguard");
        CoreGeoBone bt1 = getAnimationProcessor().getBone("bullton1");
        CoreGeoBone bt2 = getAnimationProcessor().getBone("bullton2");
        CoreGeoBone shuan = getAnimationProcessor().getBone("shuan");
        CoreGeoBone glass = getAnimationProcessor().getBone("glass");

        Player player = Minecraft.getInstance().player;
        if (player == null) return;
        ItemStack stack = player.getMainHandItem();
        if (!stack.is(TargetModTags.Items.GUN)) return;

        if (stack.getOrCreateTag().getDouble("HoldOpen") == 1) {
            bolt.setPosZ(3.25f);
        }

        double p = 0;
        p = player.getPersistentData().getDouble("zoom_pos");

        double zp = 0;
        zp = player.getPersistentData().getDouble("zoom_pos_z");

        gun.setPosX(2.02f * (float) p);

        gun.setPosY(0.85f * (float) p - (float) (0.6f * zp));

        gun.setPosZ(13.2f * (float) p + (float) (0.5f * zp));

        gun.setRotZ((float) (0.05f * zp));

        gun.setScaleZ(1f - (0.8f * (float) p));

        scope.setScaleZ(1f - (0.95f * (float) p));

        bt1.setScaleY(1f - (0.5f * (float) p));

        bt2.setScaleX(1f - (0.5f * (float) p));

        CoreGeoBone holo = getAnimationProcessor().getBone("holo");
        if (gun.getPosX() > 1.9) {
            holo.setPosY(0.05f);
            holo.setScaleX(0.45f);
            holo.setScaleY(0.45f);
            sight.setScaleX(0f);
            sight.setScaleY(0f);
        } else {
            holo.setPosY(0);
            holo.setScaleX(0);
            holo.setScaleY(0);
            sight.setScaleX(1f);
            sight.setScaleY(1f);
            glass.setScaleX(0);
            glass.setScaleY(0);
        }

        CoreGeoBone shen = getAnimationProcessor().getBone("shen");

        double fp = player.getPersistentData().getDouble("fire_pos");
        double fr = player.getPersistentData().getDouble("fire_rot");

        if ((player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).zooming) {
            shen.setPosY(0.02f * (float) (fp + 2 * fr));
            shen.setPosZ(1.6f * (float) (fp + 0.54f * fr));
            shen.setRotX(0.003f * (float) (fp + fr));
            shen.setRotZ(0f);
        } else {
            shen.setPosY(0.04f * (float) (fp + 2 * fr));
            shen.setPosZ(1.8f * (float) (fp + 0.54f * fr));
            shen.setRotX(0.07f * (float) (0.18f * fp + fr));
            shen.setRotZ(-0.04f * (float) (fp + 1.3 * fr));
        }
        shen.setPosX(0.5f * (float)fr * (float)((player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).recoilHorizon * fp));

        holo.setPosY(1.1f * (float) fp);

        holo.setRotZ(-0.04f * (float) fp);

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

        move.setPosY(-2f * (float) vy);

        double xRot = player.getPersistentData().getDouble("xRot");

        double yRot = player.getPersistentData().getDouble("yRot");

        double zRot = player.getPersistentData().getDouble("zRot");

        move.setRotX(Mth.DEG_TO_RAD * (float) xRot - 0.15f * (float) vy);

        move.setRotY(Mth.DEG_TO_RAD * (float) yRot);

        move.setRotZ(2.7f * (float) m + Mth.DEG_TO_RAD * (float) zRot);

        glass.setPosX(0.25f * -PosX);

        glass.setPosY(0.2f * (float) fp + 0.5f * (float) vy + (float) y + PosY);

        CoreGeoBone camera = getAnimationProcessor().getBone("camera");

        player.getPersistentData().putDouble("camera_rot_x", Mth.RAD_TO_DEG * camera.getRotX());

        player.getPersistentData().putDouble("camera_rot_y", Mth.RAD_TO_DEG * camera.getRotY());

        player.getPersistentData().putDouble("camera_rot_z", Mth.RAD_TO_DEG * camera.getRotZ());
    }
}
