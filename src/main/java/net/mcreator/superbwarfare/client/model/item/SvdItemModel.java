package net.mcreator.superbwarfare.client.model.item;

import net.mcreator.superbwarfare.ModUtils;
import net.mcreator.superbwarfare.event.ClientEventHandler;
import net.mcreator.superbwarfare.init.ModTags;
import net.mcreator.superbwarfare.item.gun.sniper.SvdItem;
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

public class SvdItemModel extends GeoModel<SvdItem> {
    @Override
    public ResourceLocation getAnimationResource(SvdItem animatable) {
        return new ResourceLocation(ModUtils.MODID, "animations/svd.animation.json");
    }

    @Override
    public ResourceLocation getModelResource(SvdItem animatable) {
        return new ResourceLocation(ModUtils.MODID, "geo/svd.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(SvdItem animatable) {
        return new ResourceLocation(ModUtils.MODID, "textures/item/svd.png");
    }

    @Override
    public void setCustomAnimations(SvdItem animatable, long instanceId, AnimationState animationState) {
        CoreGeoBone gun = getAnimationProcessor().getBone("bone");
        CoreGeoBone bolt = getAnimationProcessor().getBone("bolt");
        CoreGeoBone scope = getAnimationProcessor().getBone("pso1");
        CoreGeoBone bt1 = getAnimationProcessor().getBone("bullton1");
        CoreGeoBone bt2 = getAnimationProcessor().getBone("bullton2");
        CoreGeoBone glass = getAnimationProcessor().getBone("glass");
        CoreGeoBone holo = getAnimationProcessor().getBone("holo");
        CoreGeoBone flare = getAnimationProcessor().getBone("flare");

        Player player = Minecraft.getInstance().player;
        if (player == null) return;
        ItemStack stack = player.getMainHandItem();
        if (!stack.is(ModTags.Items.GUN)) return;

        if (stack.getOrCreateTag().getBoolean("HoldOpen")) {
            bolt.setPosZ(3.25f);
        }

        double zt = ClientEventHandler.getZoom_time();
        double zp = ClientEventHandler.getZoom_pos();
        double zpz = ClientEventHandler.getZoom_pos_z();

        gun.setPosX(2.02f * (float) zp);

        gun.setPosY(0.85f * (float) zp - (float) (0.6f * zpz));

        gun.setPosZ(13.2f * (float) zp + (float) (0.5f * zpz));

        gun.setRotZ((float) (0.05f * zpz));

        gun.setScaleZ(1f - (0.8f * (float) zp));

        scope.setScaleZ(1f - (0.95f * (float) zp));

        bt1.setScaleY(1f - (0.5f * (float) zp));

        bt2.setScaleX(1f - (0.5f * (float) zp));

        stack.getOrCreateTag().putBoolean("HoloHidden", !(gun.getPosX() > 1.9));

        CoreGeoBone shen = getAnimationProcessor().getBone("shen");

        double fp = player.getPersistentData().getDouble("fire_pos");
        double fr = player.getPersistentData().getDouble("fire_rot");

        if (GLFW.glfwGetMouseButton(Minecraft.getInstance().getWindow().getWindow(), GLFW.GLFW_MOUSE_BUTTON_RIGHT) == GLFW.GLFW_PRESS) {
            shen.setPosY(0.02f * (float) (fp + 2 * fr));
            shen.setPosZ(1.6f * (float) (fp + 0.54f * fr));
            shen.setRotX(0.003f * (float) (fp + fr));
            shen.setRotZ(0f);
            flare.setPosY(-2.5f);
        } else {
            shen.setPosY(0.04f * (float) (fp + 2 * fr));
            shen.setPosZ(1.8f * (float) (fp + 0.54f * fr));
            shen.setRotX(0.07f * (float) (0.18f * fp + fr));
            shen.setRotZ(-0.04f * (float) (fp + 1.3 * fr));
        }
        shen.setPosX(0.5f * (float)fr * (float)((player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables())).recoilHorizon * fp));

        holo.setPosY(0.05f + 1.1f * (float) fp);
        holo.setRotZ(-0.04f * (float) fp);
        holo.setScaleX(0.75f);
        holo.setScaleY(0.75f);

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

        glass.setPosX(0.25f * -PosX);

        glass.setPosY(0.2f * (float) fp + 0.5f * (float) vy + (float) y + PosY);

        CoreGeoBone camera = getAnimationProcessor().getBone("camera");
        CoreGeoBone main = getAnimationProcessor().getBone("0");

        float numR = (float) (1 - 0.94 * zt);
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
