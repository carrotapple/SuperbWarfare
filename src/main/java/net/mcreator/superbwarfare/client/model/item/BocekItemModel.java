package net.mcreator.superbwarfare.client.model.item;

import net.mcreator.superbwarfare.init.TargetModTags;
import net.mcreator.superbwarfare.item.gun.BocekItem;
import net.mcreator.superbwarfare.network.TargetModVariables;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

public class BocekItemModel extends GeoModel<BocekItem> {
    @Override
    public ResourceLocation getAnimationResource(BocekItem animatable) {
        return new ResourceLocation("target", "animations/bocek.animation.json");
    }

    @Override
    public ResourceLocation getModelResource(BocekItem animatable) {
        return new ResourceLocation("target", "geo/bocek.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(BocekItem animatable) {
        return new ResourceLocation("target", "textures/item/bocek.png");
    }

    @Override
    public void setCustomAnimations(BocekItem animatable, long instanceId, AnimationState animationState) {
        CoreGeoBone gun = getAnimationProcessor().getBone("bone");
        CoreGeoBone jian = getAnimationProcessor().getBone("jian");
        CoreGeoBone arrow = getAnimationProcessor().getBone("arrow");
        CoreGeoBone rh = getAnimationProcessor().getBone("ys");
        CoreGeoBone lun = getAnimationProcessor().getBone("hualun1");
        CoreGeoBone shen = getAnimationProcessor().getBone("shen");
        CoreGeoBone shen_pos = getAnimationProcessor().getBone("shen_pos");
        CoreGeoBone xian = getAnimationProcessor().getBone("xian1");
        CoreGeoBone xian2 = getAnimationProcessor().getBone("xian2");
        CoreGeoBone fire = getAnimationProcessor().getBone("fire");
        CoreGeoBone deng = getAnimationProcessor().getBone("deng");
        CoreGeoBone deng2 = getAnimationProcessor().getBone("deng2");
        CoreGeoBone deng3 = getAnimationProcessor().getBone("deng3");
        CoreGeoBone holo = getAnimationProcessor().getBone("holo");
        CoreGeoBone r = getAnimationProcessor().getBone("r");

        Player player = Minecraft.getInstance().player;
        if (player == null) return;
        ItemStack stack = player.getMainHandItem();
        if (!stack.is(TargetModTags.Items.GUN)) return;

        arrow.setHidden(stack.getOrCreateTag().getInt("arrow_empty") > 0);
        jian.setHidden(stack.getOrCreateTag().getInt("max_ammo") == 0);

        double pp = player.getPersistentData().getDouble("pullpos");
        double bp = player.getPersistentData().getDouble("bowpos");
        double hp = player.getPersistentData().getDouble("handpos");

        arrow.setPosZ(9f * (float) bp);
        rh.setPosZ(9f * (float) hp);
        lun.setRotX(1.6f * (float) bp);

        xian.setRotX(0.56f * (float) bp);
        xian2.setRotX(-0.56f * (float) bp);
        xian.setScaleY(1f + (0.25f * (float) bp));
        xian2.setScaleY(1f + (0.25f * (float) bp));
        xian.setPosZ(9f * (float) bp);
        xian2.setPosZ(9f * (float) bp);

        gun.setScaleZ(1f - (0.2f * (float) pp));
        gun.setRotZ(0.2f * (float) pp);
        gun.setRotX(0.01f * (float) pp);
        gun.setPosZ(-3f * (float) pp);
        gun.setPosY(0.1f * (float) pp);
        r.setScaleZ(1f - (0.2f * (float) pp));
        deng2.setRotX(1.6f * (float) bp);
        deng2.setPosZ(0.05f * (float) bp);
        deng3.setRotX(-1.6f * (float) bp);
        deng3.setPosZ(0.05f * (float) bp);

        deng.setHidden(!(arrow.getPosZ() > 8.5));

        double p = player.getPersistentData().getDouble("zoom_pos");
        double zp = player.getPersistentData().getDouble("zoom_pos_z");

        shen_pos.setPosX(-3.4f * (float) p);
        shen_pos.setPosY(6.76f * (float) p - (float) (0.2f * zp));
        shen_pos.setPosZ(6.4f * (float) p + (float) (0.3f * zp));
        r.setScaleZ(1f - (0.31f * (float) p));
        shen.setRotZ(60 * Mth.DEG_TO_RAD * (float) p + (float) (0.05f * zp) - 0.2f);

        holo.setHidden(!(shen_pos.getPosX() < -0.7 && gun.getPosZ() < -2.5));

        double fp = player.getPersistentData().getDouble("fire_pos");
        double fr = player.getPersistentData().getDouble("fire_rot");

        if ((player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).zooming) {
            fire.setPosY(-0.01f * (float) (fp + 2 * fr));
            fire.setPosZ(3f * (float) (fp + 0.54f * fr));
            fire.setRotX(0.003f * (float) (fp + fr));
            fire.setRotZ(0f);
        } else {
            fire.setPosY(-0.03f * (float) (fp + 2 * fr));
            fire.setPosZ(4f * (float) (fp + 0.54f * fr));
            fire.setRotX(0.07f * (float) (0.18f * fp + fr));
            fire.setRotZ(-0.04f * (float) (fp + 1.3 * fr));
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
        double yaw = player.getPersistentData().getDouble("yaw");
        double pit = player.getPersistentData().getDouble("gun_pitch");
        double vy = player.getPersistentData().getDouble("vy");

        move.setPosY(-1 * (float) vy);

        move.setPosX(9.3f * (float) m);

        move.setRotX(0.5f * (float) pit);

        move.setRotZ(0.7f * (float) yaw + 2.7f * (float) m);

        move.setRotY(0.9f * (float) yaw - 1.7f * (float) m);

        CoreGeoBone camera = getAnimationProcessor().getBone("camera");

        player.getPersistentData().putDouble("camera_rot_x", Mth.RAD_TO_DEG * camera.getRotX());

        player.getPersistentData().putDouble("camera_rot_y", Mth.RAD_TO_DEG * camera.getRotY());

        player.getPersistentData().putDouble("camera_rot_z", Mth.RAD_TO_DEG * camera.getRotZ());
    }
}
