package com.atsuishio.superbwarfare.client.model.item;

import com.atsuishio.superbwarfare.ModUtils;
import com.atsuishio.superbwarfare.event.PlayerEventHandler;
import com.atsuishio.superbwarfare.tools.GunsTool;
import com.atsuishio.superbwarfare.client.AnimationHelper;
import com.atsuishio.superbwarfare.event.ClientEventHandler;
import com.atsuishio.superbwarfare.init.ModTags;
import com.atsuishio.superbwarfare.item.gun.machinegun.M60Item;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

public class M60ItemModel extends GeoModel<M60Item> {

    @Override
    public ResourceLocation getAnimationResource(M60Item animatable) {
        return ModUtils.loc("animations/m60.animation.json");
    }

    @Override
    public ResourceLocation getModelResource(M60Item animatable) {
        return ModUtils.loc("geo/m60.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(M60Item animatable) {
        return ModUtils.loc("textures/item/m60.png");
    }

    @Override
    public void setCustomAnimations(M60Item animatable, long instanceId, AnimationState animationState) {
        CoreGeoBone gun = getAnimationProcessor().getBone("bone");
        CoreGeoBone shen = getAnimationProcessor().getBone("shen");
        CoreGeoBone tiba = getAnimationProcessor().getBone("tiba");
        CoreGeoBone b1 = getAnimationProcessor().getBone("b1");
        CoreGeoBone b2 = getAnimationProcessor().getBone("b2");
        CoreGeoBone b3 = getAnimationProcessor().getBone("b3");
        CoreGeoBone b4 = getAnimationProcessor().getBone("b4");
        CoreGeoBone b5 = getAnimationProcessor().getBone("b5");
        CoreGeoBone l = getAnimationProcessor().getBone("l");
        CoreGeoBone r = getAnimationProcessor().getBone("r");

        Player player = Minecraft.getInstance().player;
        if (player == null) return;
        ItemStack stack = player.getMainHandItem();
        if (!stack.is(ModTags.Items.GUN)) return;

        if (PlayerEventHandler.isProne(player)) {
            l.setRotX(1.5f);
            r.setRotX(1.5f);
        }

        if (stack.getOrCreateTag().getInt("ammo") < 5 && stack.getOrCreateTag().getBoolean("HideBulletChain")) {
            b5.setScaleX(0);
            b5.setScaleY(0);
            b5.setScaleZ(0);
        }

        if (stack.getOrCreateTag().getInt("ammo") < 4 && stack.getOrCreateTag().getBoolean("HideBulletChain")) {
            b4.setScaleX(0);
            b4.setScaleY(0);
            b4.setScaleZ(0);
        }

        if (stack.getOrCreateTag().getInt("ammo") < 3 && stack.getOrCreateTag().getBoolean("HideBulletChain")) {
            b3.setScaleX(0);
            b3.setScaleY(0);
            b3.setScaleZ(0);
        }

        if (stack.getOrCreateTag().getInt("ammo") < 2 && stack.getOrCreateTag().getBoolean("HideBulletChain")) {
            b2.setScaleX(0);
            b2.setScaleY(0);
            b2.setScaleZ(0);
        }

        if (stack.getOrCreateTag().getInt("ammo") < 1 && stack.getOrCreateTag().getBoolean("HideBulletChain")) {
            b1.setScaleX(0);
            b1.setScaleY(0);
            b1.setScaleZ(0);
        }

        float times = 0.6f * (float) Math.min(Minecraft.getInstance().getDeltaFrameTime(), 0.8);
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
        double fpz = ClientEventHandler.firePosZ * 13 * times;
        double fp = ClientEventHandler.firePos;
        double fr = ClientEventHandler.fireRot;

        gun.setPosX(3.74f * (float) zp);

        gun.setPosY(-0.1f * (float) zp - (float) (0.1f * zpz));

        gun.setPosZ(3.24f * (float) zp + (float) (0.3f * zpz));

        gun.setRotZ(-0.087f * (float) zp + (float) (0.05f * zpz));

        shen.setPosX((float) (0.95f * ClientEventHandler.recoilHorizon * fpz * fp));
        shen.setPosY((float) (0.15f * fp + 0.18f * fr));
        shen.setPosZ((float) (0.325 * fp + 0.34f * fr + 0.75 * fpz));
        shen.setRotX((float) (0.01f * fp + 0.05f * fr + 0.01f * fpz));
        shen.setRotY((float) (0.04f * ClientEventHandler.recoilHorizon * fpz));
        shen.setRotZ((float) ((0.08f + 0.1 * fr) * ClientEventHandler.recoilHorizon));

        shen.setPosX((float) (shen.getPosX() * (1 - 0.5 * zt)));
        shen.setPosY((float) (shen.getPosY() * (-1 + 0.4 * zt)));
        shen.setPosZ((float) (shen.getPosZ() * (1 - 0.6 * zt)));
        shen.setRotX((float) (shen.getRotX() * (1 - 0.9 * zt)));
        shen.setRotY((float) (shen.getRotY() * (1 - 0.9 * zt)));
        shen.setRotZ((float) (shen.getRotZ() * (1 - 0.9 * zt)));

        tiba.setRotZ((float) (-0.25f * fp + 0.4 * fr));

        CoreGeoBone root = getAnimationProcessor().getBone("root");
        root.setPosX((float) (movePosX + 20 * ClientEventHandler.drawTime + 9.3f * mph));
        root.setPosY((float) (swayY + movePosY - 40 * ClientEventHandler.drawTime - 2f * vY));
        root.setRotX((float) (swayX - Mth.DEG_TO_RAD * 60 * ClientEventHandler.drawTime + Mth.DEG_TO_RAD * turnRotX - 0.15f * vY));
        root.setRotY((float) (0.2f * movePosX + Mth.DEG_TO_RAD * 300 * ClientEventHandler.drawTime + Mth.DEG_TO_RAD * turnRotY));
        root.setRotZ((float) (0.2f * movePosX + moveRotZ + Mth.DEG_TO_RAD * 90 * ClientEventHandler.drawTime + 2.7f * mph + Mth.DEG_TO_RAD * turnRotZ));

        CoreGeoBone camera = getAnimationProcessor().getBone("camera");
        CoreGeoBone main = getAnimationProcessor().getBone("0");

        float numR = (float) (1 - 0.88 * zt);
        float numP = (float) (1 - 0.28 * zt);

        AnimationHelper.handleShellsAnimation(getAnimationProcessor(), 1f, 0.45f);
        CoreGeoBone shell = getAnimationProcessor().getBone("shell");

        if (GunsTool.getGunIntTag(stack, "ReloadTime") > 0) {
            main.setRotX(numR * main.getRotX());
            main.setRotY(numR * main.getRotY());
            main.setRotZ(numR * main.getRotZ());
            main.setPosX(numP * main.getPosX());
            main.setPosY(numP * main.getPosY());
            main.setPosZ(numP * main.getPosZ());
            camera.setRotX(numR * camera.getRotX());
            camera.setRotY(numR * camera.getRotY());
            camera.setRotZ(numR * camera.getRotZ());
            shell.setScaleX(0);
            shell.setScaleY(0);
            shell.setScaleZ(0);
        } else {
            shell.setScaleX(1);
            shell.setScaleY(1);
            shell.setScaleZ(1);
        }
        ClientEventHandler.shake(Mth.RAD_TO_DEG * camera.getRotX(), Mth.RAD_TO_DEG * camera.getRotY(), Mth.RAD_TO_DEG * camera.getRotZ());
    }
}
