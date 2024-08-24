package net.mcreator.superbwarfare.client.model.item;

import net.mcreator.superbwarfare.ModUtils;
import net.mcreator.superbwarfare.init.ModTags;
import net.mcreator.superbwarfare.item.gun.Glock18Item;
import net.mcreator.superbwarfare.network.ModVariables;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

public class Glock18ItemModel extends GeoModel<Glock18Item> {
    @Override
    public ResourceLocation getAnimationResource(Glock18Item animatable) {
        return new ResourceLocation(ModUtils.MODID, "animations/glock17.animation.json");
    }

    @Override
    public ResourceLocation getModelResource(Glock18Item animatable) {
        return new ResourceLocation(ModUtils.MODID, "geo/glock18.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Glock18Item animatable) {
        return new ResourceLocation(ModUtils.MODID, "textures/item/glock17.png");
    }

    @Override
    public void setCustomAnimations(Glock18Item animatable, long instanceId, AnimationState animationState) {
        CoreGeoBone gun = getAnimationProcessor().getBone("bone");
        CoreGeoBone shen = getAnimationProcessor().getBone("shen");
        CoreGeoBone slide = getAnimationProcessor().getBone("huatao");
        CoreGeoBone bullet = getAnimationProcessor().getBone("bullet");
        CoreGeoBone switch_ = getAnimationProcessor().getBone("kuaimanji");

        Player player = Minecraft.getInstance().player;
        if (player == null) return;
        ItemStack stack = player.getMainHandItem();
        if (!stack.is(ModTags.Items.GUN)) return;

        if (stack.getOrCreateTag().getInt("fire_mode") == 0) {
            switch_.setRotX(35 * Mth.DEG_TO_RAD);
        }
        if (stack.getOrCreateTag().getInt("fire_mode") == 2) {
            switch_.setRotX(0);
        }

        double p = player.getPersistentData().getDouble("zoom_pos");
        double zp = player.getPersistentData().getDouble("zoom_pos_z");

        gun.setPosX(-1.34f * (float) p);

        gun.setPosY(5.05f * (float) p - (float) (0.2f * zp));

        gun.setPosZ(5f * (float) p + (float) (0.3f * zp));

        gun.setScaleZ(1f - (0.35f * (float) p));

        gun.setRotZ(-11 * Mth.DEG_TO_RAD * (float) p + (float) (0.05f * zp));

        double fp = player.getPersistentData().getDouble("fire_pos");
        double fr = player.getPersistentData().getDouble("fire_rot");

        if ((player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables())).zooming) {
            shen.setPosY(0.03f * (float) (fp + 2 * fr));
            shen.setPosZ(2.6f * (float) (fp + 0.84f * fr));
            shen.setRotY(0.05f * (float) ((player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables())).recoilHorizon * fp));
            shen.setRotX(0.005f * (float) (fp + fr));
        } else {
            shen.setPosY(0.08f * (float) (fp + 2 * fr));
            shen.setPosZ(1.9f * (float) (fp + 0.54f * fr));
            shen.setRotX(0.17f * (float) (0.18f * fp + fr));
            shen.setRotY(0.1f * (float) ((player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables())).recoilHorizon * fp));
            shen.setRotZ(-0.01f * (float) (fp + 1.3 * fr));
        }
        shen.setPosX(0.7f * (float) fr * (float) ((player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables())).recoilHorizon * fp));

        slide.setPosZ(1.5f * (float) fp);

        if (stack.getOrCreateTag().getBoolean("HoldOpen")) {
            slide.setPosZ(1.5f);
            bullet.setScaleX(0);
            bullet.setScaleY(0);
            bullet.setScaleZ(0);
        } else {
            bullet.setScaleX(1);
            bullet.setScaleY(1);
            bullet.setScaleZ(1);
        }

        CoreGeoBone root = getAnimationProcessor().getBone("root");

        float PosX = (float) player.getPersistentData().getDouble("gun_move_posX");
        float PosY = (float) player.getPersistentData().getDouble("gun_move_posY");

        double y = player.getPersistentData().getDouble("y");
        double x = player.getPersistentData().getDouble("x");

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

        move.setRotX(0.7f * Mth.DEG_TO_RAD * (float) xRot - 0.15f * (float) vy);

        move.setRotY(0.7f * Mth.DEG_TO_RAD * (float) yRot);

        move.setRotZ(2.7f * (float) m + Mth.DEG_TO_RAD * (float) zRot);

        CoreGeoBone camera = getAnimationProcessor().getBone("camera");
        CoreGeoBone main = getAnimationProcessor().getBone("0");
        CoreGeoBone body = getAnimationProcessor().getBone("gun");
        var data = player.getPersistentData();
        float numR = (float) (1 - 0.12 * data.getDouble("zoom_time"));
        float numP = (float) (1 - 0.68 * data.getDouble("zoom_time"));

        if (stack.getOrCreateTag().getInt("gun_reloading_time") > 0) {
            main.setRotX(numR * main.getRotX());
            main.setRotY(numR * main.getRotY());
            main.setRotZ(numR * main.getRotZ());
            main.setPosX(numP * main.getPosX());
            main.setPosY(numP * main.getPosY());
            main.setPosZ(numP * main.getPosZ());
            body.setRotX(numR * body.getRotX());
            body.setRotY(numR * body.getRotY());
            body.setRotZ(numR * body.getRotZ());
            body.setPosX(numP * body.getPosX());
            body.setPosY(numP * body.getPosY());
            body.setPosZ(numP * body.getPosZ());
            camera.setRotX(numR * camera.getRotX());
            camera.setRotY(numR * camera.getRotY());
            camera.setRotZ(numR * camera.getRotZ());
        }

        player.getPersistentData().putDouble("camera_rot_x", Mth.RAD_TO_DEG * camera.getRotX());

        player.getPersistentData().putDouble("camera_rot_y", Mth.RAD_TO_DEG * camera.getRotY());

        player.getPersistentData().putDouble("camera_rot_z", Mth.RAD_TO_DEG * camera.getRotZ());
    }
}
