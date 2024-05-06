package net.mcreator.target.client.model.item;

import net.mcreator.target.item.Sentinel;
import net.mcreator.target.network.TargetModVariables;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

public class SentinelItemModel extends GeoModel<Sentinel> {
    @Override
    public ResourceLocation getAnimationResource(Sentinel animatable) {
        return new ResourceLocation("target", "animations/sentinel.animation.json");
    }

    @Override
    public ResourceLocation getModelResource(Sentinel animatable) {
        return new ResourceLocation("target", "geo/sentinel.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Sentinel animatable) {
        return new ResourceLocation("target", "textures/item/sentinel.png");
    }

    @Override
    public void setCustomAnimations(Sentinel animatable, long instanceId, AnimationState animationState) {
        CoreGeoBone gun = getAnimationProcessor().getBone("bone");
        CoreGeoBone shen = getAnimationProcessor().getBone("shen");
        CoreGeoBone scope = getAnimationProcessor().getBone("scope2");
        CoreGeoBone ammo = getAnimationProcessor().getBone("ammobar");
        CoreGeoBone cb = getAnimationProcessor().getBone("chamber2");
        CoreGeoBone b1 = getAnimationProcessor().getBone("b1");
        CoreGeoBone b2 = getAnimationProcessor().getBone("b2");
        CoreGeoBone b3 = getAnimationProcessor().getBone("b3");
        CoreGeoBone b4 = getAnimationProcessor().getBone("b4");
        CoreGeoBone b5 = getAnimationProcessor().getBone("b5");

        Player player = Minecraft.getInstance().player;
        ItemStack stack = player.getMainHandItem();

        double p = 0;
        p = player.getPersistentData().getDouble("zoompos");

        double zp = 0;
        zp = player.getPersistentData().getDouble("zoomposz");

        gun.setPosX(3.08f * (float) p);

        gun.setPosY(-0.94f * (float) p - (float) (0.2f * zp));

        gun.setPosZ(10f * (float) p + (float) (0.5f * zp));

        gun.setRotZ((float) (0.05f * zp));

        gun.setScaleZ(1f - (0.7f * (float) p));

        scope.setScaleZ(1f - (0.8f * (float) p));

        float fps = Minecraft.getInstance().getFps();
        if (fps <= 0) {
            fps = 1f;
        }
        float times = 250f / fps;

        cb.setRotZ(cb.getRotZ() + times * 0.03f * (float) (stack.getOrCreateTag().getDouble("crot")));

        CoreGeoBone holo = getAnimationProcessor().getBone("holo");
        if (gun.getPosX() > 1.8) {
            holo.setScaleX(1);
            holo.setScaleY(1);
        } else {
            holo.setScaleX(0);
            holo.setScaleY(0);
        }

        double fp = 0;
        fp = player.getPersistentData().getDouble("firepos");

        if ((player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).zooming) {
            shen.setPosZ(3f * (float) fp);
        } else {
            shen.setPosZ(5f * (float) fp);
        }

        if ((player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).zooming) {
            shen.setPosY(0.3f * (float) fp);
        } else {
            shen.setPosY(1.3f * (float) fp);
        }

        shen.setRotX(0.1f * (float) fp);

        if ((player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).recoilhorizon == 1) {
            shen.setRotZ(0.003f * (float) fp);
        } else {
            shen.setRotZ(-0.003f * (float) fp);
        }

        CoreGeoBone charge = getAnimationProcessor().getBone("charge");

        charge.setRotZ(charge.getRotZ() + times * 0.03f);

        if ((stack.getOrCreateTag().getDouble("power") > 0)) {
            charge.setScaleX(1);
            charge.setScaleY(1);
        } else {
            charge.setScaleX(0);
            charge.setScaleY(0);
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

        if ((player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).zooming) {

            move.setPosY(-0.2f * (float) vy);

            move.setPosX(0.3f * (float) m);

            move.setRotX(0.5f * (float) pit);

            move.setRotZ(0.7f * (float) yaw + 0.2f * (float) m);

            move.setRotY(0.9f * (float) yaw - 0.7f * (float) m);

        } else {

            move.setPosY(-1.3f * (float) vy);

            move.setPosX(9.3f * (float) m);

            move.setRotX(2.0f * (float) pit);

            move.setRotZ(3.7f * (float) yaw + 2.7f * (float) m);

            move.setRotY(1.9f * (float) yaw - 1.7f * (float) m);
        }

        CoreGeoBone flare = getAnimationProcessor().getBone("flare");

        if (stack.getOrCreateTag().getDouble("fireanim") > 0) {
            flare.setScaleX((float) (1.0 + 0.5 * (Math.random() - 0.5)));
            flare.setScaleY((float) (1.0 + 0.5 * (Math.random() - 0.5)));
            flare.setRotZ((float) (0.5 * (Math.random() - 0.5)));
        } else {
            flare.setScaleX(0);
            flare.setScaleY(0);
            flare.setRotZ(0);
        }

        if ((stack.getOrCreateTag().getDouble("ammo") <= 5)) {
            ammo.setScaleX((float) (stack.getOrCreateTag().getDouble("ammo") / 5));
        }
    }
}
