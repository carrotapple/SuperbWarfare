package net.mcreator.target.client.model.item;

import net.mcreator.target.item.gun.Hk416Item;
import net.mcreator.target.network.TargetModVariables;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

public class Hk416ItemModel extends GeoModel<Hk416Item> {
    @Override
    public ResourceLocation getAnimationResource(Hk416Item animatable) {
        return new ResourceLocation("target", "animations/hk416.animation.json");
    }

    @Override
    public ResourceLocation getModelResource(Hk416Item animatable) {
        return new ResourceLocation("target", "geo/hk416.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Hk416Item animatable) {
        return new ResourceLocation("target", "textures/item/hk416.png");
    }

    @Override
    public void setCustomAnimations(Hk416Item animatable, long instanceId, AnimationState animationState) {
        CoreGeoBone gun = getAnimationProcessor().getBone("bone");
        CoreGeoBone shen = getAnimationProcessor().getBone("shen");
        CoreGeoBone holo = getAnimationProcessor().getBone("holo");
        CoreGeoBone scope = getAnimationProcessor().getBone("eotech");
        CoreGeoBone flare = getAnimationProcessor().getBone("flare");

        Player player = Minecraft.getInstance().player;
        ItemStack stack = player.getMainHandItem();

        if (stack.getOrCreateTag().getDouble("flash_time") > 0) {
            flare.setScaleX((float) (1.0 + 0.5 * (Math.random() - 0.5)));
            flare.setScaleY((float) (1.0 + 0.5 * (Math.random() - 0.5)));
            flare.setRotZ((float) (0.5 * (Math.random() - 0.5)));
        } else {
            flare.setScaleX(0);
            flare.setScaleY(0);
            flare.setRotZ(0);
        }

        double p = 0;
        p = player.getPersistentData().getDouble("zoompos");

        double zp = 0;
        zp = player.getPersistentData().getDouble("zoomposz");

        gun.setPosX(3.28f * (float) p);

        gun.setPosY(1.04f * (float) p - (float) (0.2f * zp));

        gun.setPosZ(3f * (float) p + (float) (0.3f * zp));

        gun.setRotZ((float) (0.05f * zp));

        scope.setScaleZ(1f - (0.5f * (float) p));

        if (gun.getPosX() > 3.1) {

            holo.setScaleX(1);
            holo.setScaleY(1);
        } else {
            holo.setScaleX(0);
            holo.setScaleY(0);
        }

        double fp = 0;
        fp = player.getPersistentData().getDouble("firepos");

        shen.setPosZ(0.8f * (float) fp);

        if ((player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).zooming) {
            shen.setRotX(0.002f * (float) fp);
        } else {
            shen.setRotX(0.02f * (float) fp);
        }

        if ((player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).recoilHorizon == 1) {
            shen.setRotZ(0.003f * (float) fp);
        } else {
            shen.setRotZ(-0.003f * (float) fp);
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

        move.setRotX(2.0f * (float) pit);

        move.setRotZ(3.7f * (float) yaw + 2.7f * (float) m);

        move.setRotY(1.9f * (float) yaw - 1.7f * (float) m);
    }
}
