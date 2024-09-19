package net.mcreator.superbwarfare.client.model.item;

import net.mcreator.superbwarfare.ModUtils;
import net.mcreator.superbwarfare.event.ClientEventHandler;
import net.mcreator.superbwarfare.init.ModPerks;
import net.mcreator.superbwarfare.init.ModTags;
import net.mcreator.superbwarfare.item.gun.special.TaserItem;
import net.mcreator.superbwarfare.perk.PerkHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

import java.util.concurrent.atomic.AtomicInteger;

public class TaserItemModel extends GeoModel<TaserItem> {

    @Override
    public ResourceLocation getAnimationResource(TaserItem animatable) {
        return new ResourceLocation(ModUtils.MODID, "animations/taser.animation.json");
    }

    @Override
    public ResourceLocation getModelResource(TaserItem animatable) {
        return new ResourceLocation(ModUtils.MODID, "geo/taser.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(TaserItem animatable) {
        return new ResourceLocation(ModUtils.MODID, "textures/item/tasergun.png");
    }

    @Override
    public void setCustomAnimations(TaserItem animatable, long instanceId, AnimationState animationState) {
        CoreGeoBone gun = getAnimationProcessor().getBone("bone");
        CoreGeoBone shen = getAnimationProcessor().getBone("shen");
        CoreGeoBone bar = getAnimationProcessor().getBone("bar");
        CoreGeoBone bluecover = getAnimationProcessor().getBone("bluecover");
        CoreGeoBone redcover = getAnimationProcessor().getBone("redcover");

        Player player = Minecraft.getInstance().player;
        if (player == null) return;
        ItemStack stack = player.getMainHandItem();
        if (!stack.is(ModTags.Items.GUN)) return;

        AtomicInteger energy = new AtomicInteger(0);
        stack.getCapability(ForgeCapabilities.ENERGY).ifPresent(
                e -> energy.set(e.getEnergyStored())
        );

        int perkLevel = PerkHelper.getItemPerkLevel(ModPerks.VOLT_OVERLOAD.get(), stack);

        bar.setScaleX((float) energy.get() / 12000);

        if (energy.get() >= 2000 + 200 * perkLevel) {
            bluecover.setScaleX(1);
            bluecover.setScaleY(1);
            bluecover.setScaleZ(1);
            redcover.setScaleX(0);
            redcover.setScaleY(0);
            redcover.setScaleZ(0);
        } else {
            bluecover.setScaleX(0);
            bluecover.setScaleY(0);
            bluecover.setScaleZ(0);
            redcover.setScaleX(1);
            redcover.setScaleY(1);
            redcover.setScaleZ(1);
        }

        double fp = player.getPersistentData().getDouble("fire_pos");
        double fr = player.getPersistentData().getDouble("fire_rot");

        shen.setPosX(-0.05f * (float) (fp + 0.2 * fr));
        shen.setPosY(0.1f * (float) (fp + 2 * fr));
        shen.setPosZ(1.2f * (float) (1.3 * fp + 0.54f * fr));
        shen.setRotX(0.04f * (float) (1.28f * fp + fr));
        shen.setRotY(0.02f * (float) fr);
        shen.setRotZ(-0.02f * (float) (fp + 1.3 * fr));

        double zt = ClientEventHandler.getZoom_time();
        double zp = ClientEventHandler.getZoom_pos();
        double zpz = ClientEventHandler.getZoom_pos_z();

        gun.setPosX(1.82f * (float) zp);

        gun.setPosY(1.3f * (float) zp - (float) (0.3f * zpz));

        gun.setPosZ((float) zp + (float) (0.5f * zpz));

        gun.setRotZ((float) (0.05f * zpz));

        CoreGeoBone root = getAnimationProcessor().getBone("root");

        float PosX = (float) player.getPersistentData().getDouble("gun_move_posX");
        float PosY = (float) player.getPersistentData().getDouble("gun_move_posY");

        double y = player.getPersistentData().getDouble("y");
        double x = player.getPersistentData().getDouble("x");

        float RotZ = (float) player.getPersistentData().getDouble("gun_move_rotZ");

        root.setPosX(PosX);

        root.setPosY((float) y + PosY);

        root.setRotX((float) x);

        root.setRotY(0.2f * PosX);

        root.setRotZ(0.2f * PosX + RotZ);

        CoreGeoBone move = getAnimationProcessor().getBone("move");

        double m = player.getPersistentData().getDouble("move");

        double vy = player.getPersistentData().getDouble("vy");

        move.setPosX(9.3f * (float) m);

        move.setPosY(-2 * (float) vy);

        double xRot = player.getPersistentData().getDouble("xRot");

        double yRot = player.getPersistentData().getDouble("yRot");

        double zRot = player.getPersistentData().getDouble("zRot");

        move.setRotX(Mth.DEG_TO_RAD * (float) xRot - 0.15f * (float) vy);

        move.setRotY(Mth.DEG_TO_RAD * (float) yRot);

        move.setRotZ(2.7f * (float) m + Mth.DEG_TO_RAD * (float) zRot);

        CoreGeoBone camera = getAnimationProcessor().getBone("camera");
        CoreGeoBone main = getAnimationProcessor().getBone("0");

        float numR = (float) (1 - 0.72 * zt);
        float numP = (float) (1 - 0.68 * zt);

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

        player.getPersistentData().putDouble("camera_rot_x", Mth.RAD_TO_DEG * camera.getRotX());

        player.getPersistentData().putDouble("camera_rot_y", Mth.RAD_TO_DEG * camera.getRotY());

        player.getPersistentData().putDouble("camera_rot_z", Mth.RAD_TO_DEG * camera.getRotZ());
    }
}
