package net.mcreator.superbwarfare.client.model.item;

import net.mcreator.superbwarfare.ModUtils;
import net.mcreator.superbwarfare.event.ClientEventHandler;
import net.mcreator.superbwarfare.init.ModTags;
import net.mcreator.superbwarfare.item.gun.sniper.MosinNagantItem;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.lwjgl.glfw.GLFW;
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


        double zt = ClientEventHandler.zoomTime;
        double zp = ClientEventHandler.zoomPos;
        double zpz = ClientEventHandler.zoomPosZ;
        double swayX = ClientEventHandler.swayX;
        double swayY = ClientEventHandler.swayY;
        float moveRotZ = (float) ClientEventHandler.moveRotZ;
        float movePosX = (float) ClientEventHandler.movePosX;
        float movePosY = (float) ClientEventHandler.movePosY;
        double mph = ClientEventHandler.movePosHorizon;
        double vY = ClientEventHandler.velocityY;
        double turnRotX = ClientEventHandler.turnRot[0];
        double turnRotY = ClientEventHandler.turnRot[1];
        double turnRotZ = ClientEventHandler.turnRot[2];
        double fp = ClientEventHandler.firePos;
        double fr = ClientEventHandler.fireRot;

        gun.setPosX(2.105f * (float) zp);
        gun.setPosY(0.766f * (float) zp - (float) (0.2f * zpz));
        gun.setPosZ(12.95f * (float) zp + (float) (0.3f * zpz));
        gun.setScaleZ(1f - (0.9f * (float) zp));

        pu.setScaleZ(1f - (0.5f * (float) zp));
        bone16.setScaleZ(1f - (0.93f * (float) zp));
        bone15.setScaleX(1f - (0.2f * (float) zp));

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

        if (GLFW.glfwGetMouseButton(Minecraft.getInstance().getWindow().getWindow(), GLFW.GLFW_MOUSE_BUTTON_RIGHT) == GLFW.GLFW_PRESS) {
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
        shen.setPosX(0.2f * (float) (ClientEventHandler.recoilHorizon * (0.5 + 0.4 * ClientEventHandler.fireSpread)));

        rex.setPosY(0.05f + 0.1f * (float) fp);
        rex.setRotZ((float) (-0.08f * fp * ClientEventHandler.recoilHorizon * fp));


        CoreGeoBone root = getAnimationProcessor().getBone("root");
        root.setPosX((float) (movePosX + 20 *  ClientEventHandler.drawTime + 9.3f * mph));
        root.setPosY((float) (swayY + movePosY - 40 * ClientEventHandler.drawTime - 2f * vY));
        root.setRotX((float) (swayX - Mth.DEG_TO_RAD * 60 * ClientEventHandler.drawTime + Mth.DEG_TO_RAD * turnRotX - 0.15f * vY));
        root.setRotY((float) (0.2f * movePosX + Mth.DEG_TO_RAD * 300 * ClientEventHandler.drawTime + Mth.DEG_TO_RAD * turnRotY));
        root.setRotZ((float) (0.2f * movePosX + moveRotZ + Mth.DEG_TO_RAD * 90 * ClientEventHandler.drawTime + 2.7f * mph + Mth.DEG_TO_RAD * turnRotZ));

        CoreGeoBone camera = getAnimationProcessor().getBone("camera");
        CoreGeoBone main = getAnimationProcessor().getBone("0");
        CoreGeoBone body = getAnimationProcessor().getBone("roll");

        float numR = (float) (1 - 0.97 * zt);
        float numP = (float) (1 - 0.81 * zt);

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
        ClientEventHandler.shake(Mth.RAD_TO_DEG * camera.getRotX(),Mth.RAD_TO_DEG * camera.getRotY(), Mth.RAD_TO_DEG * camera.getRotZ());
    }
}
