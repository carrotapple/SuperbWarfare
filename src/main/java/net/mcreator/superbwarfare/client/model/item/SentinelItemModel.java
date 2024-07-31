package net.mcreator.superbwarfare.client.model.item;

import net.mcreator.superbwarfare.ModUtils;
import net.mcreator.superbwarfare.init.ModTags;
import net.mcreator.superbwarfare.item.gun.SentinelItem;
import net.mcreator.superbwarfare.network.ModVariables;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

import java.util.concurrent.atomic.AtomicBoolean;

public class SentinelItemModel extends GeoModel<SentinelItem> {
    @Override
    public ResourceLocation getAnimationResource(SentinelItem animatable) {
        return new ResourceLocation(ModUtils.MODID, "animations/sentinel.animation.json");
    }

    @Override
    public ResourceLocation getModelResource(SentinelItem animatable) {
        return new ResourceLocation(ModUtils.MODID, "geo/sentinel.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(SentinelItem animatable) {
        return new ResourceLocation(ModUtils.MODID, "textures/item/sentinel.png");
    }

    @Override
    public void setCustomAnimations(SentinelItem animatable, long instanceId, AnimationState animationState) {
        CoreGeoBone gun = getAnimationProcessor().getBone("bone");
        CoreGeoBone shen = getAnimationProcessor().getBone("shen");
        CoreGeoBone scope = getAnimationProcessor().getBone("scope2");
        CoreGeoBone ammo = getAnimationProcessor().getBone("ammobar");
        CoreGeoBone cb = getAnimationProcessor().getBone("chamber2");

        Player player = Minecraft.getInstance().player;
        if (player == null) return;
        ItemStack stack = player.getMainHandItem();
        if (!stack.is(ModTags.Items.GUN)) return;

        double p = player.getPersistentData().getDouble("zoom_pos");

        double zp = player.getPersistentData().getDouble("zoom_pos_z");

        gun.setPosX(2.928f * (float) p);

        gun.setPosY(-0.062f * (float) p - (float) (0.1f * zp));

        gun.setPosZ(10f * (float) p + (float) (0.3f * zp));

        gun.setRotZ((float) (0.05f * zp));

        gun.setScaleZ(1f - (0.7f * (float) p));

        scope.setScaleZ(1f - (0.8f * (float) p));

        float fps = Minecraft.getInstance().getFps();
        if (fps <= 0) {
            fps = 1f;
        }
        float times = 250f / fps;

        cb.setRotZ(cb.getRotZ() + times * 0.03f * (float) (stack.getOrCreateTag().getDouble("chamber_rot")));

        CoreGeoBone holo = getAnimationProcessor().getBone("holo");
        holo.setPosY(0.09f);
        holo.setHidden(!(gun.getPosX() > 1.8));

        double fp = player.getPersistentData().getDouble("fire_pos");
        double fr = player.getPersistentData().getDouble("fire_rot");

        if ((player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables())).zooming) {
            shen.setPosY(0.4f * (float) (fp + 2 * fr));
            shen.setPosZ(3.6f * (float) (fp + 0.54f * fr));
            shen.setRotX(0.12f * (float) (fp + fr));
            shen.setRotZ(0f);
        } else {
            shen.setPosY(0.7f * (float) (fp + 2 * fr));
            shen.setPosZ(4.2f * (float) (fp + 0.54f * fr));
            shen.setRotX(0.15f * (float) (0.18f * fp + fr));
            shen.setRotZ(-0.01f * (float) (fp + 1.3 * fr));
        }
        shen.setPosX(0.5f * (float) fr * (float) ((player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables())).recoilHorizon * fp));

        CoreGeoBone charge = getAnimationProcessor().getBone("charge");

        charge.setRotZ(charge.getRotZ() + times * 0.05f);

        AtomicBoolean flag = new AtomicBoolean(false);
        stack.getCapability(ForgeCapabilities.ENERGY).ifPresent(
                iEnergyStorage -> flag.set(iEnergyStorage.getEnergyStored() > 0)
        );

        charge.setHidden(!flag.get());

        CoreGeoBone root = getAnimationProcessor().getBone("root");

        float PosX = (float) player.getPersistentData().getDouble("gun_move_posX");
        float PosY = (float) player.getPersistentData().getDouble("gun_move_posY");

        double y = player.getPersistentData().getDouble("y");
        double x = player.getPersistentData().getDouble("x");

        root.setPosX(PosX);

        root.setPosY((float) y + PosY);

        root.setRotX((float) x);

        float RotZ = (float) player.getPersistentData().getDouble("gun_move_rotZ");

        root.setRotY(0.2f * PosX);

        root.setRotZ(0.2f * PosX + RotZ);

        CoreGeoBone move = getAnimationProcessor().getBone("move");

        double m = player.getPersistentData().getDouble("move");

        double vy = player.getPersistentData().getDouble("vy");

        move.setPosX(9.3f * (float) m);

        move.setPosY(-2f * (float) vy);

        double xRot = player.getPersistentData().getDouble("xRot");

        double yRot = player.getPersistentData().getDouble("yRot");

        double zRot = player.getPersistentData().getDouble("zRot");

        move.setRotX(1.4f * Mth.DEG_TO_RAD * (float) xRot - 0.15f * (float) vy);

        move.setRotY(1.4f * Mth.DEG_TO_RAD * (float) yRot);

        move.setRotZ(2.7f * (float) m + Mth.DEG_TO_RAD * (float) zRot);

        CoreGeoBone flare = getAnimationProcessor().getBone("flare");

        if (stack.getOrCreateTag().getDouble("flash_time") > 0) {
            flare.setHidden(false);
            flare.setScaleX((float) (0.75 + 0.5 * (Math.random() - 0.5)));
            flare.setScaleY((float) (0.75 + 0.5 * (Math.random() - 0.5)));
            flare.setRotZ((float) (0.5 * (Math.random() - 0.5)));
        } else {
            flare.setHidden(true);
        }

        if ((stack.getOrCreateTag().getDouble("ammo") <= 5)) {
            ammo.setScaleX((float) (stack.getOrCreateTag().getDouble("ammo") / 5));
        }

        CoreGeoBone camera = getAnimationProcessor().getBone("camera");

        player.getPersistentData().putDouble("camera_rot_x", Mth.RAD_TO_DEG * camera.getRotX());

        player.getPersistentData().putDouble("camera_rot_y", Mth.RAD_TO_DEG * camera.getRotY());

        player.getPersistentData().putDouble("camera_rot_z", Mth.RAD_TO_DEG * camera.getRotZ());
    }
}
