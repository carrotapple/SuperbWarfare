package net.mcreator.target.client.model.item;

import net.mcreator.target.item.gun.BocekItem;
import net.mcreator.target.network.TargetModVariables;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
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
        CoreGeoBone xian = getAnimationProcessor().getBone("xian1");
        CoreGeoBone xian2 = getAnimationProcessor().getBone("xian2");
        CoreGeoBone fire = getAnimationProcessor().getBone("fire");
        CoreGeoBone deng = getAnimationProcessor().getBone("deng");
        CoreGeoBone deng2 = getAnimationProcessor().getBone("deng2");
        CoreGeoBone deng3 = getAnimationProcessor().getBone("deng3");
        CoreGeoBone holo = getAnimationProcessor().getBone("holo");

        Player player = Minecraft.getInstance().player;
        ItemStack stack = player.getMainHandItem();

        if (stack.getOrCreateTag().getDouble("arrowempty") > 0) {
            arrow.setScaleX(0);
            arrow.setScaleY(0);
            arrow.setScaleZ(0);
        }

        if (stack.getOrCreateTag().getDouble("maxammo") == 0) {
            jian.setScaleX(0);
            jian.setScaleY(0);
            jian.setScaleZ(0);
        }

        double pp = 0;
        pp = player.getPersistentData().getDouble("pullpos");

        double bp = 0;
        bp = player.getPersistentData().getDouble("bowpos");

        double hp = 0;
        hp = player.getPersistentData().getDouble("handpos");

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
        gun.setRotZ(0.48f * (float) pp);
        gun.setRotX(0.01f * (float) pp);
        gun.setPosZ(-3f * (float) pp);
        gun.setPosY(0f * (float) pp);
        deng2.setRotX(1.6f * (float) bp);
        deng2.setPosZ(0.05f * (float) bp);
        deng3.setRotX(-1.6f * (float) bp);
        deng3.setPosZ(0.05f * (float) bp);

        if (arrow.getPosZ() > 8.5) {
            deng.setScaleX(1);
            deng.setScaleY(1);
        } else {
            deng.setScaleX(0);
            deng.setScaleY(0);
        }

        double p = 0;
        p = player.getPersistentData().getDouble("zoompos");

        double zp = 0;
        zp = player.getPersistentData().getDouble("zoomposz");

        shen.setPosX(3.08f * (float) p);

        shen.setPosY(4.38f * (float) p - (float) (0.2f * zp));

        shen.setPosZ(3f * (float) p + (float) (0.3f * zp));

        shen.setRotZ(0.478f * (float) p + (float) (0.05f * zp));

        double FirePosZ = 0;
        double FireRotX = 0;

        double fp = 0;
        fp = player.getPersistentData().getDouble("firepos");

        fire.setPosZ(4f * (float) fp);

        fire.setRotX(0.02f * (float) fp);

        if ((player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).recoilHorizon == 1) {
            fire.setRotZ(0.003f * (float) fp);
        } else {
            fire.setRotZ(-0.003f * (float) fp);
        }

        CoreGeoBone root = getAnimationProcessor().getBone("root");

        double y = 0;
        double x = 0;
        y = player.getPersistentData().getDouble("y");
        x = player.getPersistentData().getDouble("x");

        root.setPosY((float) y);
        root.setRotX((float) x);

        CoreGeoBone move = getAnimationProcessor().getBone("move");

        double m = 0;
        m = player.getPersistentData().getDouble("move");

        double yaw = 0;
        yaw = player.getPersistentData().getDouble("yaw");

        double pit = 0;
        pit = player.getPersistentData().getDouble("gunpitch");

        double vy = 0;
        vy = player.getPersistentData().getDouble("vy");

        move.setPosY(-1 * (float) vy);

        move.setPosX(9.3f * (float) m);

        move.setRotX(0.5f * (float) pit);

        move.setRotZ(3.7f * (float) yaw + 2.7f * (float) m);

        move.setRotY(1.9f * (float) yaw - 1.7f * (float) m);

        if (shen.getPosX() > 2.9 && gun.getRotZ() > 0.42) {
            holo.setScaleX(1);
            holo.setScaleY(1);
        } else {
            holo.setScaleX(0);
            holo.setScaleY(0);
        }
    }
}
