package com.atsuishio.superbwarfare.client.model.item;

import com.atsuishio.superbwarfare.Mod;
import com.atsuishio.superbwarfare.item.gun.handgun.AureliaSceptre;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

public class AureliaSceptreModel extends GeoModel<AureliaSceptre> {

    public static float fireRotY = 0f;
    public static float fireRotZ = 0f;

    @Override
    public ResourceLocation getAnimationResource(AureliaSceptre animatable) {
        return Mod.loc("animations/aurelia_sceptre.animation.json");
    }

    @Override
    public ResourceLocation getModelResource(AureliaSceptre animatable) {
        return Mod.loc("geo/aurelia_sceptre.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(AureliaSceptre animatable) {
        return Mod.loc("textures/item/aurelia_sceptre.png");
    }

    @Override
    public void setCustomAnimations(AureliaSceptre animatable, long instanceId, AnimationState animationState) {
        // TODO 动画
//        GeoBone gun = getAnimationProcessor().getBone("bone");
//        GeoBone slide = getAnimationProcessor().getBone("huatao");
//        GeoBone bullet = getAnimationProcessor().getBone("bullet");
//
//        Player player = Minecraft.getInstance().player;
//        if (player == null) return;
//        ItemStack stack = player.getMainHandItem();
//        if (!(stack.getItem() instanceof GunItem)) return;
//
//        float times = 0.6f * (float) Math.min(Minecraft.getInstance().getTimer().getRealtimeDeltaTicks(), 0.8);
//        double zt = ClientEventHandler.zoomTime;
//        double zp = ClientEventHandler.zoomPos;
//        double zpz = ClientEventHandler.zoomPosZ;
//        double fpz = ClientEventHandler.firePosZ * 13 * times;
//        double fp = ClientEventHandler.firePos;
//        double fr = ClientEventHandler.fireRot;
//
//        gun.setPosX(1.23f * (float) zp);
//        gun.setPosY(1.43f * (float) zp - (float) (0.2f * zpz));
//        gun.setPosZ(7f * (float) zp + (float) (0.3f * zpz));
//        gun.setScaleZ(1f - (0.55f * (float) zp));
//
//        GeoBone body = getAnimationProcessor().getBone("gun");
//
//        fireRotY = (float) Mth.lerp(0.3f * times, fireRotY, 0.6f * ClientEventHandler.recoilHorizon * fpz);
//        fireRotZ = (float) Mth.lerp(2f * times, fireRotZ, (0.4f + 0.5 * fpz) * ClientEventHandler.recoilHorizon);
//
//        body.setPosX(-0.4f * (float) (ClientEventHandler.recoilHorizon * (0.5 + 0.4 * ClientEventHandler.fireSpread)));
//        body.setPosY((float) (0.15f * fp + 0.18f * fr));
//        body.setPosZ((float) (1.935 * fp + 0.16f * fr + 0.925 * fpz));
//        body.setRotX((float) (0.08f * fp + 0.1f * fr + 0.35f * fpz));
//        body.setRotY(fireRotY);
//        body.setRotZ(fireRotZ);
//
//        body.setPosX((float) (body.getPosX() * (1 - 0.4 * zt)));
//        body.setPosY((float) (body.getPosY() * (-1 + 0.5 * zt)));
//        body.setPosZ((float) (body.getPosZ() * (1 - 0.3 * zt)));
//        body.setRotX((float) (body.getRotX() * (1 - 0.8 * zt)));
//        body.setRotY((float) (body.getRotY() * (1 - 0.7 * zt)));
//        body.setRotZ((float) (body.getRotZ() * (1 - 0.65 * zt)));
//
//        CrossHairOverlay.gunRot = body.getRotZ();
//
//        slide.setPosZ(1.5f * (float) fp);
//
//        ClientEventHandler.gunRootMove(getAnimationProcessor());
//
//        GeoBone camera = getAnimationProcessor().getBone("camera");
//        GeoBone main = getAnimationProcessor().getBone("0");
//
//        float numR = (float) (1 - 0.12 * zt);
//        float numP = (float) (1 - 0.68 * zt);
//
//        var data = GunData.from(stack);
//
//        if (data.reload.time() > 0) {
//            main.setRotX(numR * main.getRotX());
//            main.setRotY(numR * main.getRotY());
//            main.setRotZ(numR * main.getRotZ());
//            main.setPosX(numP * main.getPosX());
//            main.setPosY(numP * main.getPosY());
//            main.setPosZ(numP * main.getPosZ());
//            camera.setRotX(numR * camera.getRotX());
//            camera.setRotY(numR * camera.getRotY());
//            camera.setRotZ(numR * camera.getRotZ());
//        }
//
//        ClientEventHandler.handleReloadShake(Mth.RAD_TO_DEG * camera.getRotX(), Mth.RAD_TO_DEG * camera.getRotY(), Mth.RAD_TO_DEG * camera.getRotZ());
//        AnimationHelper.handleShellsAnimation(getAnimationProcessor(), 0.7f, 1f);
//
//        GeoBone shell = getAnimationProcessor().getBone("shell");
//        GeoBone barrel = getAnimationProcessor().getBone("guan");
//        if (data.holdOpen.get()) {
//            slide.setPosZ(1.5f);
//            barrel.setRotX(4 * Mth.DEG_TO_RAD);
//            bullet.setScaleX(0);
//            bullet.setScaleY(0);
//            bullet.setScaleZ(0);
//
//            shell.setScaleX(0);
//            shell.setScaleY(0);
//            shell.setScaleZ(0);
//        } else {
//            barrel.setRotX(0);
//            bullet.setScaleX(1);
//            bullet.setScaleY(1);
//            bullet.setScaleZ(1);
//
//            shell.setScaleX(1);
//            shell.setScaleY(1);
//            shell.setScaleZ(1);
//        }
    }
}
