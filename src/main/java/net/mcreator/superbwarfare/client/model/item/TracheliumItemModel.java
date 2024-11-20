package net.mcreator.superbwarfare.client.model.item;

import net.mcreator.superbwarfare.ModUtils;
import net.mcreator.superbwarfare.client.AnimationHelper;
import net.mcreator.superbwarfare.event.ClientEventHandler;
import net.mcreator.superbwarfare.init.ModTags;
import net.mcreator.superbwarfare.item.gun.handgun.Trachelium;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

public class TracheliumItemModel extends GeoModel<Trachelium> {

    @Override
    public ResourceLocation getAnimationResource(Trachelium animatable) {
        return ModUtils.loc("animations/trachelium.animation.json");
    }

    @Override
    public ResourceLocation getModelResource(Trachelium animatable) {
        return ModUtils.loc("geo/trachelium.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Trachelium animatable) {
        return ModUtils.loc("textures/item/trachelium_texture.png");
    }

    @Override
    public void setCustomAnimations(Trachelium animatable, long instanceId, AnimationState animationState) {
        CoreGeoBone gun = getAnimationProcessor().getBone("bone");
        CoreGeoBone hammer = getAnimationProcessor().getBone("jichui");
        CoreGeoBone lun = getAnimationProcessor().getBone("lun");

        Player player = Minecraft.getInstance().player;
        if (player == null) return;
        ItemStack stack = player.getMainHandItem();
        if (!stack.is(ModTags.Items.GUN)) return;

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

        gun.setPosX(1.26f * (float) zp);
        gun.setPosY(1.1f * (float) zp - (float) (0.6f * zpz));
        gun.setPosZ((float) zp + (float) (0.2f * zpz));
        gun.setScaleZ(1f - (0.2f * (float) zp));

        CoreGeoBone shen;
        if (zt < 0.5) {
            shen = getAnimationProcessor().getBone("fireRootNormal");
        } else {
            shen = getAnimationProcessor().getBone("fireRoot0");
//            shen = switch (type) {
//                case 0 -> getAnimationProcessor().getBone("fireRoot0");
//                case 1 -> getAnimationProcessor().getBone("fireRoot1");
//                case 2 -> getAnimationProcessor().getBone("fireRoot2");
//                default -> getAnimationProcessor().getBone("fireRootNormal");
//            };
        }

        shen.setPosX((float) (0.95f * ClientEventHandler.recoilHorizon * fpz * fp));
        shen.setPosY((float) (0.2f * fp + 0.24f * fr));
        shen.setPosZ((float) (1.225 * fp + 0.1f * fr + 0.55 * fpz));
        shen.setRotX((float) (0.2f * fp + 0.2f * fr + 0.2f * fpz));
        shen.setRotY((float) (0.1f * ClientEventHandler.recoilHorizon * fpz));
        shen.setRotZ((float) ((0.08f + 0.1 * fr) * ClientEventHandler.recoilHorizon));

        shen.setPosX((float) (shen.getPosX() * (1 - 0.4 * zt)));
        shen.setPosY((float) (shen.getPosY() * (1 - 0.5 * zt)));
        shen.setPosZ((float) (shen.getPosZ() * (1 - 0.7 * zt)));
        shen.setRotX((float) (shen.getRotX() * (1 - 0.27 * zt)));
        shen.setRotY((float) (shen.getRotY() * (1 - 0.7 * zt)));
        shen.setRotZ((float) (shen.getRotZ() * (1 - 0.65 * zt)));

        hammer.setRotX(50 * Mth.DEG_TO_RAD * (float) ClientEventHandler.revolverPreTime);
        lun.setRotZ(-60 * Mth.DEG_TO_RAD * (float) ClientEventHandler.revolverWheelPreTime);
        CoreGeoBone ammo = getAnimationProcessor().getBone("ammo");
        CoreGeoBone ammohole = getAnimationProcessor().getBone("ammohole");
        ammo.setRotZ(60 * Mth.DEG_TO_RAD * (float) ClientEventHandler.revolverWheelPreTime);
        ammohole.setRotZ(-60 * Mth.DEG_TO_RAD * (float) ClientEventHandler.revolverWheelPreTime);

        CoreGeoBone root = getAnimationProcessor().getBone("root");
        root.setPosX((float) (movePosX + 20 * ClientEventHandler.drawTime + 9.3f * mph));
        root.setPosY((float) (swayY + movePosY - 40 * ClientEventHandler.drawTime - 2f * vY));
        root.setPosZ(!stack.getOrCreateTag().getBoolean("DA") && !stack.getOrCreateTag().getBoolean("canImmediatelyShoot")? 0.2f * (float) ClientEventHandler.revolverPreTime : 0);
        root.setRotX((float) (swayX - Mth.DEG_TO_RAD * 60 * ClientEventHandler.drawTime + Mth.DEG_TO_RAD * turnRotX - 0.15f * vY) + (!stack.getOrCreateTag().getBoolean("DA") && !stack.getOrCreateTag().getBoolean("canImmediatelyShoot") ? 2 * Mth.DEG_TO_RAD * (float) ClientEventHandler.revolverPreTime : 0));
        root.setRotY((float) (0.2f * movePosX + Mth.DEG_TO_RAD * 300 * ClientEventHandler.drawTime + Mth.DEG_TO_RAD * turnRotY));
        root.setRotZ((float) (0.2f * movePosX + moveRotZ + Mth.DEG_TO_RAD * 90 * ClientEventHandler.drawTime + 2.7f * mph + Mth.DEG_TO_RAD * turnRotZ));

        CoreGeoBone camera = getAnimationProcessor().getBone("camera");
        CoreGeoBone main = getAnimationProcessor().getBone("0");

        float numR = (float) (1 - 0.22 * zt);
        float numP = (float) (1 - 0.48 * zt);

        AnimationHelper.handleReloadShakeAnimation(stack, main, camera, numR, numP);
        ClientEventHandler.shake(Mth.RAD_TO_DEG * camera.getRotX(), Mth.RAD_TO_DEG * camera.getRotY(), Mth.RAD_TO_DEG * camera.getRotZ());
    }
}
