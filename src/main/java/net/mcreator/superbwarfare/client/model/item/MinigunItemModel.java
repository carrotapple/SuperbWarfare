package net.mcreator.superbwarfare.client.model.item;

import net.mcreator.superbwarfare.ModUtils;
import net.mcreator.superbwarfare.event.ClientEventHandler;
import net.mcreator.superbwarfare.init.ModTags;
import net.mcreator.superbwarfare.item.gun.machinegun.MinigunItem;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

public class MinigunItemModel extends GeoModel<MinigunItem> {

    @Override
    public ResourceLocation getAnimationResource(MinigunItem animatable) {
        return ModUtils.loc("animations/minigun.animation.json");
    }

    @Override
    public ResourceLocation getModelResource(MinigunItem animatable) {
        return ModUtils.loc("geo/minigun.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(MinigunItem animatable) {
        return ModUtils.loc("textures/item/minigun.png");
    }

    @Override
    public void setCustomAnimations(MinigunItem animatable, long instanceId, AnimationState animationState) {
        CoreGeoBone gun = getAnimationProcessor().getBone("barrel");
        CoreGeoBone shen = getAnimationProcessor().getBone("shen");
        CoreGeoBone heat_barrels = getAnimationProcessor().getBone("heatbarrels");

        Player player = Minecraft.getInstance().player;
        if (player == null) return;
        ItemStack stack = player.getMainHandItem();
        if (!stack.is(ModTags.Items.GUN)) return;

        float times = 0.6f * (float) Math.min(Minecraft.getInstance().getDeltaFrameTime(), 0.8);

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

        float heat = (float) stack.getOrCreateTag().getDouble("heat");

        heat_barrels.setScaleZ(4 * heat);

        gun.setRotZ(gun.getRotZ() + times * -0.18f * ClientEventHandler.miniGunRot);

        shen.setPosX((float) (0.75f * ClientEventHandler.recoilHorizon * fpz * fp));
        shen.setPosY((float) (-0.03f * fp - 0.06f * fr));
        shen.setPosZ((float) (0.625 * fp + 0.34f * fr + 1.15 * fpz));
        shen.setRotX((float) (0.02f * fp + 0.02f * fr + 0.02f * fpz));
        shen.setRotY((float) (0.02f * ClientEventHandler.recoilHorizon * fpz));
        shen.setRotZ((float) ((0.02f + 0.02 * fr) * ClientEventHandler.recoilHorizon));

        CoreGeoBone root = getAnimationProcessor().getBone("root");
        root.setPosX((float) (movePosX + 20 * ClientEventHandler.drawTime + 9.3f * mph));
        root.setPosY((float) (swayY + movePosY - 40 * ClientEventHandler.drawTime - 2f * vY));
        root.setRotX((float) (swayX - Mth.DEG_TO_RAD * 60 * ClientEventHandler.drawTime + Mth.DEG_TO_RAD * turnRotX - 0.15f * vY));
        root.setRotY((float) (0.2f * movePosX + Mth.DEG_TO_RAD * 300 * ClientEventHandler.drawTime + Mth.DEG_TO_RAD * turnRotY));
        root.setRotZ((float) (0.2f * movePosX + moveRotZ + Mth.DEG_TO_RAD * 90 * ClientEventHandler.drawTime + 2.7f * mph + Mth.DEG_TO_RAD * turnRotZ));

        CoreGeoBone camera = getAnimationProcessor().getBone("camera");
        ClientEventHandler.shake(Mth.RAD_TO_DEG * camera.getRotX(), Mth.RAD_TO_DEG * camera.getRotY(), Mth.RAD_TO_DEG * camera.getRotZ());
    }
}
