package net.mcreator.superbwarfare.client.model.item;

import net.mcreator.superbwarfare.ModUtils;
import net.mcreator.superbwarfare.event.ClientEventHandler;
import net.mcreator.superbwarfare.init.ModTags;
import net.mcreator.superbwarfare.item.gun.rifle.SksItem;
import net.mcreator.superbwarfare.network.ModVariables;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.lwjgl.glfw.GLFW;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

public class SksItemModel extends GeoModel<SksItem> {
    @Override
    public ResourceLocation getAnimationResource(SksItem animatable) {
        return new ResourceLocation(ModUtils.MODID, "animations/sks.animation.json");
    }

    @Override
    public ResourceLocation getModelResource(SksItem animatable) {
        return new ResourceLocation(ModUtils.MODID, "geo/sks.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(SksItem animatable) {
        return new ResourceLocation(ModUtils.MODID, "textures/item/sks.png");
    }


    @Override
    public void setCustomAnimations(SksItem animatable, long instanceId, AnimationState animationState) {
        CoreGeoBone gun = getAnimationProcessor().getBone("bone");
        CoreGeoBone bolt = getAnimationProcessor().getBone("bolt");
        CoreGeoBone shuan = getAnimationProcessor().getBone("bolt2");

        Player player = Minecraft.getInstance().player;
        if (player == null) return;
        ItemStack stack = player.getMainHandItem();
        if (!stack.is(ModTags.Items.GUN)) return;

        if (stack.getOrCreateTag().getBoolean("HoldOpen")) {
            bolt.setPosZ(2.5f);
        }

        double zt = ClientEventHandler.getZoomTime();
        double zp = ClientEventHandler.getZoomPos();
        double zpz = ClientEventHandler.getZoomPosZ();

        gun.setPosX(1.53f * (float) zp);

        gun.setPosY(0.34f * (float) zp - (float) (0.6f * zpz));

        gun.setPosZ(2.5f * (float) zp + (float) (0.5f * zpz));

        gun.setRotZ((float) (0.05f * zpz));

        CoreGeoBone holo = getAnimationProcessor().getBone("holo");

        stack.getOrCreateTag().putBoolean("HoloHidden", !(gun.getPosX() > 1.2));

        CoreGeoBone shen = getAnimationProcessor().getBone("shen");

        double fp = player.getPersistentData().getDouble("fire_pos");
        double fr = player.getPersistentData().getDouble("fire_rot");

        if (GLFW.glfwGetMouseButton(Minecraft.getInstance().getWindow().getWindow(), GLFW.GLFW_MOUSE_BUTTON_RIGHT) == GLFW.GLFW_PRESS) {
            shen.setPosY(0.04f * (float) (fp + 2 * fr));
            holo.setPosY(-0.43f * (float) (fp + 2.3 * fr));
            shen.setPosZ(0.6f * (float) (fp + 0.54f * fr));
            shen.setRotX(0.015f * (float) (fp + fr));
        } else {
            shen.setPosY(0.08f * (float) (fp + 2 * fr));
            shen.setPosZ(1.2f * (float) (fp + 0.54f * fr));
            shen.setRotX(0.07f * (float) (0.18f * fp + fr));
            shen.setRotZ(-0.04f * (float) (fp + 1.3 * fr));
        }
        shen.setPosX(0.5f * (float)fr * (float)((player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables())).recoilHorizon * fp));

        shuan.setPosZ(2f * (float) fp);

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

        float numR = (float) (1 - 0.92 * zt);
        float numP = (float) (1 - 0.88 * zt);

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
        }

        player.getPersistentData().putDouble("camera_rot_x", Mth.RAD_TO_DEG * camera.getRotX());

        player.getPersistentData().putDouble("camera_rot_y", Mth.RAD_TO_DEG * camera.getRotY());

        player.getPersistentData().putDouble("camera_rot_z", Mth.RAD_TO_DEG * camera.getRotZ());
    }
}
