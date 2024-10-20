package net.mcreator.superbwarfare.client.model.item;

import net.mcreator.superbwarfare.ModUtils;
import net.mcreator.superbwarfare.event.ClientEventHandler;
import net.mcreator.superbwarfare.init.ModTags;
import net.mcreator.superbwarfare.item.gun.smg.VectorItem;
import net.mcreator.superbwarfare.client.AnimationHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

public class VectorItemModel extends GeoModel<VectorItem> {
    @Override
    public ResourceLocation getAnimationResource(VectorItem animatable) {
        return new ResourceLocation(ModUtils.MODID, "animations/vector.animation.json");
    }

    @Override
    public ResourceLocation getModelResource(VectorItem animatable) {
        return new ResourceLocation(ModUtils.MODID, "geo/vector.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(VectorItem animatable) {
        return new ResourceLocation(ModUtils.MODID, "textures/item/vector.png");
    }

    @Override
    public void setCustomAnimations(VectorItem animatable, long instanceId, AnimationState animationState) {
        CoreGeoBone gun = getAnimationProcessor().getBone("bone");
        CoreGeoBone shen = getAnimationProcessor().getBone("shen");
        CoreGeoBone scope = getAnimationProcessor().getBone("scope");
        CoreGeoBone kmj = getAnimationProcessor().getBone("kuaimanji");

        Player player = Minecraft.getInstance().player;
        if (player == null) return;
        ItemStack stack = player.getMainHandItem();
        if (!stack.is(ModTags.Items.GUN)) return;

        if (stack.getOrCreateTag().getInt("fire_mode") == 0) {
            kmj.setRotX(-120 * Mth.DEG_TO_RAD);
        }
        if (stack.getOrCreateTag().getInt("fire_mode") == 1) {
            kmj.setRotX(-60 * Mth.DEG_TO_RAD);
        }
        if (stack.getOrCreateTag().getInt("fire_mode") == 2) {
            kmj.setRotX(0);
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

        gun.setPosX(2.35f * (float) zp);

        gun.setPosY(0.74f * (float) zp - (float) (0.2f * zpz));

        gun.setPosZ(5f * (float) zp + (float) (0.3f * zpz));

        gun.setScaleZ(1f - (0.5f * (float) zp));

        scope.setScaleZ(1f - (0.2f * (float) zp));

        stack.getOrCreateTag().putBoolean("HoloHidden", !(gun.getPosX() > 2));

        shen.setPosX((float) (0.95f * ClientEventHandler.recoilHorizon * fpz * fp));
        shen.setPosY((float) (-0.05f * fp - 0.04f * fr));
        shen.setPosZ(0.25f * (float) (7.825 * fp + 0.64f * fr + 2.35 * fpz));
        shen.setRotX((float) (0.02f * fp + 0.07f * fr + 0.01f * fpz));
        shen.setRotY((float) (0.1f * ClientEventHandler.recoilHorizon * fpz));
        shen.setRotZ((float) ((0.08f + 0.1 * fr) * ClientEventHandler.recoilHorizon));

        shen.setPosX((float) (shen.getPosX() * (1 - 0.5 * zt)));
        shen.setPosY((float) (shen.getPosY() * (1 + 0.4 * zt)));
        shen.setPosZ((float) (shen.getPosZ() * (1 - 0.3 * zt)));
        shen.setRotX((float) (shen.getRotX() * (1 - 0.9 * zt)));
        shen.setRotY((float) (shen.getRotY() * (1 - 0.9 * zt)));
        shen.setRotZ((float) (shen.getRotZ() * (1 - 0.9 * zt)));

        CoreGeoBone root = getAnimationProcessor().getBone("root");
        root.setPosX((float) (movePosX + 20 * ClientEventHandler.drawTime + 9.3f * mph));
        root.setPosY((float) (swayY + movePosY - 40 * ClientEventHandler.drawTime - 2f * vY));
        root.setRotX((float) (swayX - Mth.DEG_TO_RAD * 60 * ClientEventHandler.drawTime + Mth.DEG_TO_RAD * turnRotX - 0.15f * vY));
        root.setRotY((float) (0.2f * movePosX + Mth.DEG_TO_RAD * 300 * ClientEventHandler.drawTime + Mth.DEG_TO_RAD * turnRotY));
        root.setRotZ((float) (0.2f * movePosX + moveRotZ + Mth.DEG_TO_RAD * 90 * ClientEventHandler.drawTime + 2.7f * mph + Mth.DEG_TO_RAD * turnRotZ));

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
        ClientEventHandler.shake(Mth.RAD_TO_DEG * camera.getRotX(), Mth.RAD_TO_DEG * camera.getRotY(), Mth.RAD_TO_DEG * camera.getRotZ());

        ClientEventHandler.shake(Mth.RAD_TO_DEG * camera.getRotX(), Mth.RAD_TO_DEG * camera.getRotY(), Mth.RAD_TO_DEG * camera.getRotZ());

        AnimationHelper.handleShellsAnimation(getAnimationProcessor(), 1.2f, 0.45f);
    }
}
