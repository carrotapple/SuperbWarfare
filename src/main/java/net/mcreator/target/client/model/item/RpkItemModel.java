package net.mcreator.target.client.model.item;

import net.mcreator.target.item.gun.RpkItem;
import net.mcreator.target.network.TargetModVariables;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

public class RpkItemModel extends GeoModel<RpkItem> {
    @Override
    public ResourceLocation getAnimationResource(RpkItem animatable) {
        return new ResourceLocation("target", "animations/rpk.animation.json");
    }

    @Override
    public ResourceLocation getModelResource(RpkItem animatable) {
        return new ResourceLocation("target", "geo/rpk.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(RpkItem animatable) {
        return new ResourceLocation("target", "textures/item/rpk.png");
    }

    @Override
    public void setCustomAnimations(RpkItem animatable, long instanceId, AnimationState animationState) {
        CoreGeoBone gun = getAnimationProcessor().getBone("bone");
        CoreGeoBone shen = getAnimationProcessor().getBone("shen");
        CoreGeoBone scope = getAnimationProcessor().getBone("pka");
        CoreGeoBone flare = getAnimationProcessor().getBone("flare");
        CoreGeoBone hide = getAnimationProcessor().getBone("hide");
        CoreGeoBone button = getAnimationProcessor().getBone("button");
        CoreGeoBone shuan = getAnimationProcessor().getBone("shuan");

        Player player = Minecraft.getInstance().player;
        ItemStack stack = player.getMainHandItem();

        double p = 0;
        p = player.getPersistentData().getDouble("zoompos");

        double zp = 0;
        zp = player.getPersistentData().getDouble("zoomposz");

        gun.setPosX(1.69f * (float) p);

        gun.setPosY(-0.33f * (float) p - (float) (0.6f * zp));

        gun.setPosZ(3.2f * (float) p + (float) (0.5f * zp));

        gun.setRotZ((float) (0.05f * zp));

        gun.setScaleZ(1f - (0.55f * (float) p));

        scope.setScaleZ(1f - (0.9f * (float) p));

        button.setScaleX(1f - (0.2f * (float) p));

        button.setScaleY(1f - (0.3f * (float) p));

        button.setScaleZ(1f - (0.3f * (float) p));


        CoreGeoBone holo = getAnimationProcessor().getBone("holo");
        if (gun.getPosX() > 1.65f) {
            holo.setScaleX(0.9f);
            holo.setScaleY(0.9f);
            hide.setScaleX(0);
            hide.setScaleY(0);
            hide.setScaleZ(0);

        } else {
            holo.setScaleX(0);
            holo.setScaleY(0);
            hide.setScaleX(1);
            hide.setScaleY(1);
            hide.setScaleZ(1);

        }

        double fp = 0;
        fp = player.getPersistentData().getDouble("firepos");

        shen.setPosZ((float) fp);

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

        shuan.setPosZ(2.4f * (float) fp);

        if (stack.getOrCreateTag().getDouble("fireanim") > 0) {
            flare.setScaleX((float) (1.0 + 0.5 * (Math.random() - 0.5)));
            flare.setScaleY((float) (1.0 + 0.5 * (Math.random() - 0.5)));
            flare.setRotZ((float) (0.5 * (Math.random() - 0.5)));
        } else {
            flare.setScaleX(0);
            flare.setScaleY(0);
            flare.setRotZ(0);
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
