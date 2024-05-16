package net.mcreator.target.client.model.item;

import net.mcreator.target.item.gun.M60Item;
import net.mcreator.target.network.TargetModVariables;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

public class M60ItemModel extends GeoModel<M60Item> {
    @Override
    public ResourceLocation getAnimationResource(M60Item animatable) {
        return new ResourceLocation("target", "animations/m60.animation.json");
    }

    @Override
    public ResourceLocation getModelResource(M60Item animatable) {
        return new ResourceLocation("target", "geo/m60.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(M60Item animatable) {
        return new ResourceLocation("target", "textures/item/m60.png");
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
        ItemStack stack = player.getMainHandItem();

        if (player.getPersistentData().getDouble("prone") > 0) {
            l.setRotX(1.5f);
            r.setRotX(1.5f);
        }

        if (stack.getOrCreateTag().getInt("ammo") < 5 && stack.getOrCreateTag().getDouble("empty") == 1) {
            b5.setScaleX(0);
            b5.setScaleY(0);
            b5.setScaleZ(0);
        }

        if (stack.getOrCreateTag().getInt("ammo") < 4 && stack.getOrCreateTag().getDouble("empty") == 1) {
            b4.setScaleX(0);
            b4.setScaleY(0);
            b4.setScaleZ(0);
        }

        if (stack.getOrCreateTag().getInt("ammo") < 3 && stack.getOrCreateTag().getDouble("empty") == 1) {
            b3.setScaleX(0);
            b3.setScaleY(0);
            b3.setScaleZ(0);
        }

        if (stack.getOrCreateTag().getInt("ammo") < 2 && stack.getOrCreateTag().getDouble("empty") == 1) {
            b2.setScaleX(0);
            b2.setScaleY(0);
            b2.setScaleZ(0);
        }

        if (stack.getOrCreateTag().getInt("ammo") < 1 && stack.getOrCreateTag().getDouble("empty") == 1) {
            b1.setScaleX(0);
            b1.setScaleY(0);
            b1.setScaleZ(0);
        }

        double p = 0;
        p = player.getPersistentData().getDouble("zoompos");

        double zp = 0;
        zp = player.getPersistentData().getDouble("zoomposz");

        gun.setPosX(3.69f * (float) p);

        gun.setPosY(0.62f * (float) p - (float) (0.2f * zp));

        gun.setPosZ((float) p + (float) (0.6f * zp));

        gun.setRotZ(-0.087f * (float) p + (float) (0.05f * zp));

        double fp = 0;
        fp = player.getPersistentData().getDouble("firepos");

        shen.setPosZ(1.25f * (float) fp);

        if ((player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).zooming) {
            shen.setRotX(0.008f * (float) fp);
        } else {
            shen.setRotX(0.02f * (float) fp);
        }

        if ((player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).recoilHorizon == 1) {
            shen.setRotZ(0.003f * (float) fp);
            tiba.setRotZ(-0.2f * (float) fp);
        } else {
            shen.setRotZ(-0.003f * (float) fp);
            tiba.setRotZ(-0.25f * (float) fp);
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

        move.setPosY(-1.6f * (float) vy);

        move.setPosX(9.3f * (float) m);

        move.setRotX(2.0f * (float) pit);

        move.setRotZ(3.7f * (float) yaw + 2.7f * (float) m);

        move.setRotY(1.9f * (float) yaw - 1.7f * (float) m);
    }
}
