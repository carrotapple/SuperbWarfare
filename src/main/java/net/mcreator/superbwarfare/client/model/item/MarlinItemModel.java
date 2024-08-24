package net.mcreator.superbwarfare.client.model.item;

import net.mcreator.superbwarfare.ModUtils;
import net.mcreator.superbwarfare.init.ModTags;
import net.mcreator.superbwarfare.item.gun.MarlinItem;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

public class MarlinItemModel extends GeoModel<MarlinItem> {
    @Override
    public ResourceLocation getAnimationResource(MarlinItem animatable) {
        return new ResourceLocation(ModUtils.MODID, "animations/marlin.animation.json");
    }

    @Override
    public ResourceLocation getModelResource(MarlinItem animatable) {
        return new ResourceLocation(ModUtils.MODID, "geo/marlin.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(MarlinItem animatable) {
        return new ResourceLocation(ModUtils.MODID, "textures/item/marlin.png");
    }

    @Override
    public void setCustomAnimations(MarlinItem animatable, long instanceId, AnimationState animationState) {
        CoreGeoBone gun = getAnimationProcessor().getBone("bone");
        CoreGeoBone shen = getAnimationProcessor().getBone("shen");
        CoreGeoBone jichui = getAnimationProcessor().getBone("jichui");

        Player player = Minecraft.getInstance().player;
        if (player == null) return;
        ItemStack stack = player.getMainHandItem();
        if (!stack.is(ModTags.Items.GUN)) return;

        double p = player.getPersistentData().getDouble("zoom_pos");
        double zp = player.getPersistentData().getDouble("zoom_pos_z");

        gun.setPosX(1.712f * (float) p);

        gun.setPosY(1.06f * (float) p - (float) (0.7f * zp));

        gun.setPosZ(4f * (float) p + (float) (0.9f * zp));

        gun.setRotZ((float) (0.02f * zp));

        gun.setScaleZ(1f - (0.5f * (float) p));

        double fp = player.getPersistentData().getDouble("fire_pos");
        double fr = player.getPersistentData().getDouble("fire_rot");

        shen.setPosX(-0.2f * (float) (fp + 2 * fr));
        shen.setPosY(0.4f * (float) (fp + 2 * fr));
        shen.setPosZ(1.9f * (float) (1.3 * fp + 0.54f * fr));
        shen.setRotX(0.085f * (float) (1.28f * fp + fr));
        shen.setRotZ(-0.03f * (float) (fp + 1.3 * fr));
        shen.setRotY(-0.05f * (float) fr);


        if (stack.getOrCreateTag().getInt("fire_animation") > 0) {
            jichui.setRotX(-0.52f);
        }

        CoreGeoBone root = getAnimationProcessor().getBone("root");

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
        var data = player.getPersistentData();
        float numR = (float) (1 - 0.55 * data.getDouble("zoom_time"));
        float numP = (float) (1 - 0.88 * data.getDouble("zoom_time"));

        if (stack.getOrCreateTag().getBoolean("reloading")) {
            main.setRotX(numR * main.getRotX());
            main.setRotY(numR * main.getRotY());
            main.setRotZ(numR * main.getRotZ());
            main.setPosX(numP * main.getPosX());
            main.setPosY(numP * main.getPosY());
            main.setPosZ(numP * main.getPosZ());
            camera.setRotX(numR * camera.getRotX());
            camera.setRotY(numR * camera.getRotY());
            camera.setRotZ(numR * camera.getRotZ());
        }

        player.getPersistentData().putDouble("camera_rot_x", Mth.RAD_TO_DEG * camera.getRotX());

        player.getPersistentData().putDouble("camera_rot_y", Mth.RAD_TO_DEG * camera.getRotY());

        player.getPersistentData().putDouble("camera_rot_z", Mth.RAD_TO_DEG * camera.getRotZ());
    }
}
