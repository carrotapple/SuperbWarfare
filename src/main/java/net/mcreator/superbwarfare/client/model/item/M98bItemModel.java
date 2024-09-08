package net.mcreator.superbwarfare.client.model.item;

import net.mcreator.superbwarfare.ModUtils;
import net.mcreator.superbwarfare.init.ModTags;
import net.mcreator.superbwarfare.item.gun.sniper.M98bItem;
import net.mcreator.superbwarfare.network.ModVariables;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

public class M98bItemModel extends GeoModel<M98bItem> {
    @Override
    public ResourceLocation getAnimationResource(M98bItem animatable) {
        return new ResourceLocation(ModUtils.MODID, "animations/m98b.animation.json");
    }

    @Override
    public ResourceLocation getModelResource(M98bItem animatable) {
        return new ResourceLocation(ModUtils.MODID, "geo/m98b.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(M98bItem animatable) {
        return new ResourceLocation(ModUtils.MODID, "textures/item/m98b.png");
    }

    @Override
    public void setCustomAnimations(M98bItem animatable, long instanceId, AnimationState animationState) {
        CoreGeoBone gun = getAnimationProcessor().getBone("bone");
        CoreGeoBone shen = getAnimationProcessor().getBone("shen");
        CoreGeoBone shi = getAnimationProcessor().getBone("shi");

        Player player = Minecraft.getInstance().player;
        if (player == null) return;
        ItemStack stack = player.getMainHandItem();
        if (!stack.is(ModTags.Items.GUN)) return;

        double fp = player.getPersistentData().getDouble("fire_pos");
        double fr = player.getPersistentData().getDouble("fire_rot");

        if ((player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables())).zooming) {
            shen.setPosY(0.4f * (float) (fp + 2 * fr));
            shen.setPosZ(3.6f * (float) (fp + 0.54f * fr));
            shen.setRotX(0.18f * (float) (fp + fr));
            shen.setRotZ(0f);
        } else {
            shen.setPosY(0.7f * (float) (fp + 2 * fr));
            shen.setPosZ(4.2f * (float) (fp + 0.54f * fr));
            shen.setRotX(0.25f * (float) (0.18f * fp + fr));
            shen.setRotZ(-0.01f * (float) (fp + 1.3 * fr));
        }
        shen.setPosX(0.5f * (float)fr * (float)((player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables())).recoilHorizon * fp));

        double p = player.getPersistentData().getDouble("zoom_pos");
        double zp = player.getPersistentData().getDouble("zoom_pos_z");

        gun.setPosX(2.245f * (float) p);

        gun.setPosY(0.3f * (float) p - (float) (0.2f * zp));

        gun.setPosZ(4.2f * (float) p + (float) (0.3f * zp));

        gun.setRotZ((float) (0.02f * zp));

//        CoreGeoBone holo = getAnimationProcessor().getBone("scope2");
//        CoreGeoBone qiang = getAnimationProcessor().getBone("qiang");

        stack.getOrCreateTag().putBoolean("HoloHidden", !(gun.getPosX() > 1.9));

//        if (gun.getPosX() > 1.9) {
//            holo.setHidden(false);
//            qiang.setHidden(true);
//        } else {
//            holo.setHidden(true);
//            qiang.setHidden(false);
//        }

        CoreGeoBone root = getAnimationProcessor().getBone("root");
        CoreGeoBone zhunxing = getAnimationProcessor().getBone("shi");

        float PosX = (float)player.getPersistentData().getDouble("gun_move_posX");
        float PosY = (float)player.getPersistentData().getDouble("gun_move_posY");

        double y = player.getPersistentData().getDouble("y");
        double x = player.getPersistentData().getDouble("x");

        root.setPosX(PosX);

        root.setPosY((float) y + PosY);

        root.setRotX((float) x);

        float RotZ = (float) player.getPersistentData().getDouble("gun_move_rotZ");

        root.setRotY(0.2f * PosX);

        root.setRotZ(0.2f * PosX + RotZ);

        zhunxing.setPosX(75 * PosX);

        zhunxing.setPosY(75 * PosY);

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

        CoreGeoBone camera = getAnimationProcessor().getBone("camera");
        CoreGeoBone main = getAnimationProcessor().getBone("0");
        CoreGeoBone scope = getAnimationProcessor().getBone("scope2");

        var data = player.getPersistentData();
        float numR = (float) (1 - 0.88 * data.getDouble("zoom_time"));
        float numP = (float) (1 - 0.68 * data.getDouble("zoom_time"));

        if (stack.getOrCreateTag().getInt("gun_reloading_time") > 0) {
            main.setRotX(numR * main.getRotX());
            main.setRotY(numR * main.getRotY());
            main.setRotZ(numR * main.getRotZ());
            main.setPosX(numP * main.getPosX());
            main.setPosY(numP * main.getPosY());
            main.setPosZ(numP * main.getPosZ());
            camera.setRotX(numR * camera.getRotX());
            camera.setRotY(numR * camera.getRotY());
            camera.setRotZ(numR * camera.getRotZ());

            scope.setRotX(numR * scope.getRotX());
            scope.setRotY(numR * scope.getRotY());
            scope.setRotZ(numR * scope.getRotZ());
            scope.setPosX(numP * scope.getPosX());
            scope.setPosY(numP * scope.getPosY());
            scope.setPosZ(numP * scope.getPosZ());
        }

        player.getPersistentData().putDouble("camera_rot_x", Mth.RAD_TO_DEG * camera.getRotX());

        player.getPersistentData().putDouble("camera_rot_y", Mth.RAD_TO_DEG * camera.getRotY());

        player.getPersistentData().putDouble("camera_rot_z", Mth.RAD_TO_DEG * camera.getRotZ());
    }
}
