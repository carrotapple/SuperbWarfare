package net.mcreator.superbwarfare.client.model.item;

import net.mcreator.superbwarfare.ModUtils;
import net.mcreator.superbwarfare.init.ModTags;
import net.mcreator.superbwarfare.item.gun.Taser;
import net.mcreator.superbwarfare.tools.ItemNBTTool;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

public class TaserItemModel extends GeoModel<Taser> {

    public static final String TAG_POWER = "Power";

    @Override
    public ResourceLocation getAnimationResource(Taser animatable) {
        return new ResourceLocation(ModUtils.MODID, "animations/taser.animation.json");
    }

    @Override
    public ResourceLocation getModelResource(Taser animatable) {
        return new ResourceLocation(ModUtils.MODID, "geo/taser.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Taser animatable) {
        return new ResourceLocation(ModUtils.MODID, "textures/item/tasergun.png");
    }

    @Override
    public void setCustomAnimations(Taser animatable, long instanceId, AnimationState animationState) {
        CoreGeoBone gun = getAnimationProcessor().getBone("bone");
        CoreGeoBone shen = getAnimationProcessor().getBone("shen");
        CoreGeoBone bar = getAnimationProcessor().getBone("bar");
        CoreGeoBone bluecover = getAnimationProcessor().getBone("bluecover");
        CoreGeoBone redcover = getAnimationProcessor().getBone("redcover");

        Player player = Minecraft.getInstance().player;
        if (player == null) return;
        ItemStack stack = player.getMainHandItem();
        if (!stack.is(ModTags.Items.GUN)) return;

        bar.setScaleX((float) ItemNBTTool.getInt(stack, TAG_POWER, 1200) / 1200);

        if (ItemNBTTool.getInt(stack, TAG_POWER, 1200) >= 400) {
            bluecover.setHidden(false);
            redcover.setHidden(true);
        } else {
            bluecover.setHidden(true);
            redcover.setHidden(false);
        }

        double fp = player.getPersistentData().getDouble("fire_pos");
        double fr = player.getPersistentData().getDouble("fire_rot");

        shen.setPosX(-0.05f * (float) (fp + 0.2 * fr));
        shen.setPosY(0.1f * (float) (fp + 2 * fr));
        shen.setPosZ(1.2f * (float) (1.3 * fp + 0.54f * fr));
        shen.setRotX(0.04f * (float) (1.28f * fp + fr));
        shen.setRotY(0.02f * (float) fr);
        shen.setRotZ(-0.02f * (float) (fp + 1.3 * fr));

        double p = player.getPersistentData().getDouble("zoom_pos");
        double zp = player.getPersistentData().getDouble("zoom_pos_z");

        gun.setPosX(1.82f * (float) p);

        gun.setPosY(1.3f * (float) p - (float) (0.3f * zp));

        gun.setPosZ((float) p + (float) (0.5f * zp));

        gun.setRotZ((float) (0.05f * zp));

        CoreGeoBone root = getAnimationProcessor().getBone("root");

        float PosX = (float) player.getPersistentData().getDouble("gun_move_posX");
        float PosY = (float) player.getPersistentData().getDouble("gun_move_posY");

        double y = player.getPersistentData().getDouble("y");
        double x = player.getPersistentData().getDouble("x");

        float RotZ = (float) player.getPersistentData().getDouble("gun_move_rotZ");

        root.setPosX(PosX);

        root.setPosY((float) y + PosY);

        root.setRotX((float) x);

        root.setRotY(0.2f * PosX);

        root.setRotZ(0.2f * PosX + RotZ);

        CoreGeoBone move = getAnimationProcessor().getBone("move");

        double m = player.getPersistentData().getDouble("move");

        double vy = player.getPersistentData().getDouble("vy");

        move.setPosX(9.3f * (float) m);

        move.setPosY(-2 * (float) vy);

        double xRot = player.getPersistentData().getDouble("xRot");

        double yRot = player.getPersistentData().getDouble("yRot");

        double zRot = player.getPersistentData().getDouble("zRot");

        move.setRotX(Mth.DEG_TO_RAD * (float) xRot - 0.15f * (float) vy);

        move.setRotY(Mth.DEG_TO_RAD * (float) yRot);

        move.setRotZ(2.7f * (float) m + Mth.DEG_TO_RAD * (float) zRot);

        CoreGeoBone camera = getAnimationProcessor().getBone("camera");

        player.getPersistentData().putDouble("camera_rot_x", Mth.RAD_TO_DEG * camera.getRotX());

        player.getPersistentData().putDouble("camera_rot_y", Mth.RAD_TO_DEG * camera.getRotY());

        player.getPersistentData().putDouble("camera_rot_z", Mth.RAD_TO_DEG * camera.getRotZ());
    }
}
