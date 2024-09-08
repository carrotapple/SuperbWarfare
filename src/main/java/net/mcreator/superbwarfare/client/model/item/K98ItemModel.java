package net.mcreator.superbwarfare.client.model.item;

import net.mcreator.superbwarfare.ModUtils;
import net.mcreator.superbwarfare.init.ModTags;
import net.mcreator.superbwarfare.item.gun.sniper.K98Item;
import net.mcreator.superbwarfare.network.ModVariables;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

public class K98ItemModel extends GeoModel<K98Item> {
    @Override
    public ResourceLocation getAnimationResource(K98Item animatable) {
        return new ResourceLocation(ModUtils.MODID, "animations/k98.animation.json");
    }

    @Override
    public ResourceLocation getModelResource(K98Item animatable) {
        return new ResourceLocation(ModUtils.MODID, "geo/kar98k.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(K98Item animatable) {
        return new ResourceLocation(ModUtils.MODID, "textures/item/k98.png");
    }

    @Override
    public void setCustomAnimations(K98Item animatable, long instanceId, AnimationState animationState) {
        CoreGeoBone gun = getAnimationProcessor().getBone("bone");
        CoreGeoBone shen = getAnimationProcessor().getBone("shen");
        CoreGeoBone clip = getAnimationProcessor().getBone("mag");

        Player player = Minecraft.getInstance().player;
        if (player == null) return;
        ItemStack stack = player.getMainHandItem();
        if (!stack.is(ModTags.Items.GUN)) return;


        if (stack.getOrCreateTag().getDouble("prepare") > 11 && stack.getOrCreateTag().getInt("ammo") == 1) {
            clip.setScaleX(0);
            clip.setScaleY(0);
            clip.setScaleZ(0);
        } else {
            clip.setScaleX(1);
            clip.setScaleY(1);
            clip.setScaleZ(1);
        }

        double p = player.getPersistentData().getDouble("zoom_pos");
        double zp = player.getPersistentData().getDouble("zoom_pos_z");

        gun.setPosX(2.11f * (float) p);
        gun.setPosY(1.52f * (float) p - (float) (0.2f * zp));
        gun.setPosZ(10f * (float) p + (float) (0.3f * zp));
        gun.setScaleZ(1f - (0.7f * (float) p));


        double fp = player.getPersistentData().getDouble("fire_pos");
        double fr = player.getPersistentData().getDouble("fire_rot");

        if ((player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables())).zooming) {
            shen.setPosY(0.1f * (float) (fp + 2 * fr));
            shen.setPosZ(5.6f * (float) (fp + 0.54f * fr));
            shen.setRotX(0.03f * (float) (fp + fr));
            shen.setRotZ(0f);
        } else {
            shen.setPosY(0.28f * (float) (fp + 2 * fr));
            shen.setPosZ(5.8f * (float) (fp + 0.54f * fr));
            shen.setRotX(0.17f * (float) (0.18f * fp + fr));
            shen.setRotZ(-0.04f * (float) (fp + 1.3 * fr));
        }
        shen.setPosX(0.5f * (float)fr * (float)((player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables())).recoilHorizon * fp));


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

        move.setPosY(2f * (float) vy);

        double xRot = player.getPersistentData().getDouble("xRot");

        double yRot = player.getPersistentData().getDouble("yRot");

        double zRot = player.getPersistentData().getDouble("zRot");

        move.setRotX(0.75f * Mth.DEG_TO_RAD * (float) xRot - 0.15f * (float) vy);

        move.setRotY(0.75f * Mth.DEG_TO_RAD * (float) yRot);

        move.setRotZ(2.7f * (float) m + Mth.DEG_TO_RAD * (float) zRot);

        CoreGeoBone camera = getAnimationProcessor().getBone("camera");
        CoreGeoBone main = getAnimationProcessor().getBone("0");
        CoreGeoBone body = getAnimationProcessor().getBone("roll");
        var data = player.getPersistentData();
        float numR = (float) (1 - 0.52 * data.getDouble("zoom_time"));
        float numP = (float) (1 - 0.58 * data.getDouble("zoom_time"));

        if (stack.getOrCreateTag().getInt("gun_reloading_time") > 0 || stack.getOrCreateTag().getBoolean("reloading")) {
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
