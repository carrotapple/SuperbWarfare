package net.mcreator.superbwarfare.client.model.item;

import net.mcreator.superbwarfare.ModUtils;
import net.mcreator.superbwarfare.init.ModTags;
import net.mcreator.superbwarfare.item.gun.MosinNagantItem;
import net.mcreator.superbwarfare.network.ModVariables;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

public class MosinNagantItemModel extends GeoModel<MosinNagantItem> {
    @Override
    public ResourceLocation getAnimationResource(MosinNagantItem animatable) {
        return new ResourceLocation(ModUtils.MODID, "animations/mosin_nagant.animation.json");
    }

    @Override
    public ResourceLocation getModelResource(MosinNagantItem animatable) {
        return new ResourceLocation(ModUtils.MODID, "geo/mosin_nagant.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(MosinNagantItem animatable) {
        return new ResourceLocation(ModUtils.MODID, "textures/item/mosin_nagant.png");
    }

    @Override
    public void setCustomAnimations(MosinNagantItem animatable, long instanceId, AnimationState animationState) {
        CoreGeoBone gun = getAnimationProcessor().getBone("bone");
        CoreGeoBone shen = getAnimationProcessor().getBone("shen");
        CoreGeoBone pu = getAnimationProcessor().getBone("pu");
        CoreGeoBone bone15 = getAnimationProcessor().getBone("bone15");
        CoreGeoBone bone16 = getAnimationProcessor().getBone("bone16");
        CoreGeoBone qiangshen = getAnimationProcessor().getBone("qiangshen");
        CoreGeoBone rex = getAnimationProcessor().getBone("rex");

        Player player = Minecraft.getInstance().player;
        if (player == null) return;
        ItemStack stack = player.getMainHandItem();
        if (!stack.is(ModTags.Items.GUN)) return;


        double p = player.getPersistentData().getDouble("zoom_pos");
        double zp = player.getPersistentData().getDouble("zoom_pos_z");

        gun.setPosX(2.105f * (float) p);
        gun.setPosY(0.766f * (float) p - (float) (0.2f * zp));
        gun.setPosZ(12.95f * (float) p + (float) (0.3f * zp));
        gun.setScaleZ(1f - (0.9f * (float) p));

        pu.setScaleZ(1f - (0.5f * (float) p));
        bone16.setScaleZ(1f - (0.93f * (float) p));
        bone15.setScaleX(1f - (0.2f * (float) p));

        if (gun.getPosX() > 1.4) {
            qiangshen.setScaleX(0);
            qiangshen.setScaleY(0);
            qiangshen.setScaleZ(0);
        } else {
            qiangshen.setScaleX(1);
            qiangshen.setScaleY(1);
            qiangshen.setScaleZ(1);
        }

        stack.getOrCreateTag().putBoolean("HoloHidden", !(gun.getPosX() > 1.4));

        double fp = player.getPersistentData().getDouble("fire_pos");
        double fr = player.getPersistentData().getDouble("fire_rot");

        if ((player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables())).zooming) {
            shen.setPosY(0.07f * (float) (fp + 2 * fr));
            shen.setPosZ(3.6f * (float) (fp + 0.54f * fr));
            shen.setRotX(0.02f * (float) (fp + fr));
            shen.setRotZ(0f);
        } else {
            shen.setPosY(0.28f * (float) (fp + 2 * fr));
            shen.setPosZ(3.8f * (float) (fp + 0.54f * fr));
            shen.setRotX(0.17f * (float) (0.18f * fp + fr));
            shen.setRotZ(-0.04f * (float) (fp + 1.3 * fr));
        }
        shen.setPosX((float)(0.75f * fr * (player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables())).recoilHorizon * fp));

        rex.setPosY(0.05f + 0.1f * (float) fp);
        rex.setRotZ((float) (-0.08f * fp * (player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables())).recoilHorizon));


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
        float numR = (float) (1 - 0.97 * data.getDouble("zoom_time"));
        float numP = (float) (1 - 0.81 * data.getDouble("zoom_time"));

        if (stack.getOrCreateTag().getBoolean("reloading") || stack.getOrCreateTag().getInt("bolt_action_anim") > 0) {
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
