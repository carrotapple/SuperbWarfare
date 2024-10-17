package net.mcreator.superbwarfare.client.model.item;

import net.mcreator.superbwarfare.ModUtils;
import net.mcreator.superbwarfare.event.ClientEventHandler;
import net.mcreator.superbwarfare.init.ModTags;
import net.mcreator.superbwarfare.item.gun.sniper.SvdItem;
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
        double fpz = ClientEventHandler.firePosZ;
        double fp = ClientEventHandler.firePos;
        double fr = ClientEventHandler.fireRot;

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

        if (GLFW.glfwGetMouseButton(Minecraft.getInstance().getWindow().getWindow(), GLFW.GLFW_MOUSE_BUTTON_RIGHT) == GLFW.GLFW_PRESS) {
            flare.setPosY(-2.5f);
        }

        shen.setPosX((float) (0.95f * ClientEventHandler.recoilHorizon * fpz * fp));
        shen.setPosY((float) (0.4f * fp + 0.44f * fr));
        shen.setPosZ((float) (1.325 * fp + 0.34f * fr + 2.35 * fpz));
        shen.setRotX((float) (0.01f * fp + 0.15f * fr + 0.01f * fpz));
        shen.setRotY((float) (0.1f * ClientEventHandler.recoilHorizon * fpz));
        shen.setRotZ((float) ((0.08f + 0.1 * fr) * ClientEventHandler.recoilHorizon));

        shen.setPosX((float) (shen.getPosX() * (1 - 0.4 * zt)));
        shen.setPosY((float) (shen.getPosY() * (1 - 0.5 * zt)));
        shen.setPosZ((float) (shen.getPosZ() * (1 - 0.6 * zt)));
        shen.setRotX((float) (shen.getRotX() * (1 - 0.87 * zt)));
        shen.setRotY((float) (shen.getRotY() * (1 - 0.7 * zt)));
        shen.setRotZ((float) (shen.getRotZ() * (1 - 0.65 * zt)));

        holo.setPosY(0.05f + 1.1f * (float) fp);
        holo.setRotZ(-0.04f * (float) fp);
        holo.setScaleX(0.75f);
        holo.setScaleY(0.75f);

        CoreGeoBone root = getAnimationProcessor().getBone("root");
        root.setPosX((float) (movePosX + 20 *  ClientEventHandler.drawTime + 9.3f * mph));
        root.setPosY((float) (swayY + movePosY - 40 * ClientEventHandler.drawTime - 2f * vY));
        root.setRotX((float) (swayX - Mth.DEG_TO_RAD * 60 * ClientEventHandler.drawTime + Mth.DEG_TO_RAD * turnRotX - 0.15f * vY));
        root.setRotY((float) (0.2f * movePosX + Mth.DEG_TO_RAD * 300 * ClientEventHandler.drawTime + Mth.DEG_TO_RAD * turnRotY));
        root.setRotZ((float) (0.2f * movePosX + moveRotZ + Mth.DEG_TO_RAD * 90 * ClientEventHandler.drawTime + 2.7f * mph + Mth.DEG_TO_RAD * turnRotZ));

        glass.setPosX(0.25f * -movePosX);
        glass.setPosY(0.2f * (float) fp + 0.5f * (float) vY + (float) swayY + movePosY);

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
        ClientEventHandler.shake(Mth.RAD_TO_DEG * camera.getRotX(),Mth.RAD_TO_DEG * camera.getRotY(), Mth.RAD_TO_DEG * camera.getRotZ());

        CoreGeoBone shell1 = getAnimationProcessor().getBone("shell1");
        CoreGeoBone shell2 = getAnimationProcessor().getBone("shell2");
        CoreGeoBone shell3 = getAnimationProcessor().getBone("shell3");
        CoreGeoBone shell4 = getAnimationProcessor().getBone("shell4");
        CoreGeoBone shell5 = getAnimationProcessor().getBone("shell5");

        ClientEventHandler.handleShells(1f, 0.65f, shell1, shell2, shell3, shell4, shell5);
    }
}
