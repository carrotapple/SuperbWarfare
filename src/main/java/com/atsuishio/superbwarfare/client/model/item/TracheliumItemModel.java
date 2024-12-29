package com.atsuishio.superbwarfare.client.model.item;

import com.atsuishio.superbwarfare.ModUtils;
import com.atsuishio.superbwarfare.client.AnimationHelper;
import com.atsuishio.superbwarfare.client.overlay.CrossHairOverlay;
import com.atsuishio.superbwarfare.event.ClientEventHandler;
import com.atsuishio.superbwarfare.event.PlayerEventHandler;
import com.atsuishio.superbwarfare.init.ModTags;
import com.atsuishio.superbwarfare.item.gun.handgun.Trachelium;
import com.atsuishio.superbwarfare.tools.GunsTool;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

public class TracheliumItemModel extends GeoModel<Trachelium> {

    public static float posYAlt = -0.83f;
    public static float scaleZAlt = 0.8f;
    public static float posZAlt = 13.7f;

    public static float fireRotY = 0f;
    public static float fireRotZ = 0f;
    public static float rotXBipod = 0f;

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
        CoreGeoBone barrel1 = getAnimationProcessor().getBone("Barrel1");
        CoreGeoBone barrel2 = getAnimationProcessor().getBone("Barrel2");
        CoreGeoBone flare = getAnimationProcessor().getBone("flare");
        CoreGeoBone camera = getAnimationProcessor().getBone("camera");
        CoreGeoBone main = getAnimationProcessor().getBone("0");
        CoreGeoBone scope2 = getAnimationProcessor().getBone("Scope2");

        Player player = Minecraft.getInstance().player;
        if (player == null) return;
        ItemStack stack = player.getMainHandItem();
        if (!stack.is(ModTags.Items.GUN)) return;

        float times = 0.4f * (float) Math.min(Minecraft.getInstance().getDeltaFrameTime(), 0.8);
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
        double fpz = ClientEventHandler.firePosZ * 8 * times;
        double fp = ClientEventHandler.firePos;
        double fr = ClientEventHandler.fireRot;

        int stockType = GunsTool.getAttachmentType(stack, GunsTool.AttachmentType.STOCK);
        int barrelType = GunsTool.getAttachmentType(stack, GunsTool.AttachmentType.BARREL);
        int scopeType = GunsTool.getAttachmentType(stack, GunsTool.AttachmentType.SCOPE);
        int gripType = GunsTool.getAttachmentType(stack, GunsTool.AttachmentType.GRIP);

        posYAlt = Mth.lerp(times, posYAlt, stack.getOrCreateTag().getBoolean("ScopeAlt") ? -1.98f : -0.83f);
        scaleZAlt = Mth.lerp(times, scaleZAlt, stack.getOrCreateTag().getBoolean("ScopeAlt") ? 0.4f : 0.8f);
        posZAlt = Mth.lerp(times, posZAlt, stack.getOrCreateTag().getBoolean("ScopeAlt") ? 7.5f : 13.7f);

        float posY = switch (scopeType) {
            case 0 -> 1.1f;
            case 1 -> -0.18f;
            case 2 -> posYAlt;
            case 3 -> 1.1f;
            default -> 0f;
        };
        float scaleZ = switch (scopeType) {
            case 0 -> 0.2f;
            case 1 -> 0.6f;
            case 2 -> scaleZAlt;
            case 3 -> 0.2f;
            default -> 0f;
        };
        float posZ = switch (scopeType) {
            case 0 -> 1f;
            case 1 -> 6f;
            case 2 -> posZAlt;
            case 3 -> 1f;
            default -> 0f;
        };


        float posZAlt = stockType == 2 ? 1 : 0;

        gun.setPosX((float) (3.48f * zp));
        gun.setPosY((float) (posY * zp - 0.2f * zpz));
        gun.setPosZ((float)(posZ * zp + 0.2f * zpz) + posZAlt);
        gun.setScaleZ((float) (1f - scaleZ * zp));

        scope2.setScaleZ(1f - (0.7f * (float) zp));

        CoreGeoBone shen;
        if (zt < 0.5) {
            shen = getAnimationProcessor().getBone("fireRootNormal");
        } else {
            shen = switch (scopeType) {
                case 0 -> getAnimationProcessor().getBone("fireRoot0");
                case 1 -> getAnimationProcessor().getBone("fireRoot1");
                case 2 -> getAnimationProcessor().getBone("fireRoot2");
                case 3 -> getAnimationProcessor().getBone("fireRoot3");
                default -> getAnimationProcessor().getBone("fireRootNormal");
            };
        }

        fireRotY = (float) Mth.lerp(0.2f * times, fireRotY, 0.2f * ClientEventHandler.recoilHorizon * fpz);
        fireRotZ = (float) Mth.lerp(2f * times, fireRotZ, (0.4f + 0.5 * fpz) * ClientEventHandler.recoilHorizon);

        shen.setPosX((float) (0.95f * ClientEventHandler.recoilHorizon * fpz * fp));
        shen.setPosY((float) (0.2f * fp + 0.24f * fr));
        shen.setPosZ((float) (1.225 * fp + 0.1f * fr + 0.55 * fpz));
        shen.setRotX((float) (0.14f * fp + 0.14f * fr + 0.14f * fpz));
        shen.setRotY(fireRotY);
        shen.setRotZ(fireRotZ);

        shen.setPosX((float) (shen.getPosX() * (1 - 0.4 * zt)));
        shen.setPosY((float) (shen.getPosY() * (1 - 0.5 * zt) * (PlayerEventHandler.isProne(player) ? 0.03 : 1)));
        shen.setPosZ((float) (shen.getPosZ() * (1 - 0.7 * zt) * (PlayerEventHandler.isProne(player) ? 0.4 : 1)));
        shen.setRotX((float) (shen.getRotX() * (1 - 0.27 * zt) * (barrelType == 1 ? 0.4 : 1.2) * (stockType == 2 ? 0.6 : 1.2) * (gripType == 1 ? 0.8 : 1.2) * (PlayerEventHandler.isProne(player) && gripType == 3 ? 0.03 : 1.2)));
        shen.setRotY((float) (shen.getRotY() * (1 - 0.7 * zt)));
        shen.setRotZ((float) (shen.getRotZ() * (1 - 0.65 * zt)));

        CrossHairOverlay.gunRot = shen.getRotZ();

        stack.getOrCreateTag().putBoolean("HoloHidden", (gun.getPosX() <= 3 || Mth.abs(shen.getRotX()) > (scopeType == 2 ? 3 : 1) * Mth.DEG_TO_RAD || Mth.abs(main.getRotX()) > (scopeType == 2 ? 5.7 : 1) * Mth.DEG_TO_RAD || Mth.abs(main.getRotY()) > 3 * Mth.DEG_TO_RAD));

        hammer.setRotX(50 * Mth.DEG_TO_RAD * (float) ClientEventHandler.revolverPreTime);
        lun.setRotZ(-60 * Mth.DEG_TO_RAD * (float) ClientEventHandler.revolverWheelPreTime);
        CoreGeoBone ammo = getAnimationProcessor().getBone("ammo");
        CoreGeoBone ammohole = getAnimationProcessor().getBone("ammohole");
        ammo.setRotZ(60 * Mth.DEG_TO_RAD * (float) ClientEventHandler.revolverWheelPreTime);
        ammohole.setRotZ(-60 * Mth.DEG_TO_RAD * (float) ClientEventHandler.revolverWheelPreTime);

        if (stack.getOrCreateTag().getBoolean("is_empty_reloading")) {
            lun.setRotZ(0);
            ammo.setRotZ(0);
            ammohole.setRotZ(0);
        }

        CoreGeoBone root = getAnimationProcessor().getBone("root");
        root.setPosX((float) (movePosX + 20 * ClientEventHandler.drawTime + 9.3f * mph));
        root.setPosY((float) (swayY + movePosY - 40 * ClientEventHandler.drawTime - 2f * vY));
        root.setPosZ(!stack.getOrCreateTag().getBoolean("DA") && !stack.getOrCreateTag().getBoolean("canImmediatelyShoot")? 0.2f * (float) ClientEventHandler.revolverPreTime : 0);
        root.setRotX((float) ((swayX - Mth.DEG_TO_RAD * 60 * ClientEventHandler.drawTime + Mth.DEG_TO_RAD * turnRotX - 0.15f * vY)));
        root.setRotY((float) (0.2f * movePosX + Mth.DEG_TO_RAD * 300 * ClientEventHandler.drawTime + Mth.DEG_TO_RAD * turnRotY));
        root.setRotZ((float) (0.2f * movePosX + moveRotZ + Mth.DEG_TO_RAD * 90 * ClientEventHandler.drawTime + 2.7f * mph + Mth.DEG_TO_RAD * turnRotZ));

        CoreGeoBone l = getAnimationProcessor().getBone("l");
        CoreGeoBone r = getAnimationProcessor().getBone("r");
        rotXBipod = Mth.lerp(1.5f * times, rotXBipod, PlayerEventHandler.isProne(player) ? -90 : 0);
        l.setRotX(rotXBipod * Mth.DEG_TO_RAD);
        r.setRotX(rotXBipod * Mth.DEG_TO_RAD);

        float numR = (float) (1 - 0.22 * zt);
        float numP = (float) (1 - 0.48 * zt);

        AnimationHelper.handleReloadShakeAnimation(stack, main, camera, numR, numP);
        ClientEventHandler.shake(Mth.RAD_TO_DEG * camera.getRotX(), Mth.RAD_TO_DEG * camera.getRotY(), Mth.RAD_TO_DEG * camera.getRotZ());

        barrel1.setPosZ((scopeType == 0 && gripType == 0) ? 17.9f : 0);
        barrel2.setPosZ((scopeType == 0 && gripType == 0) ? 15.3f : 3);

        float flarePosZ = 0;

        if (scopeType > 0 || gripType > 0) {
            if (barrelType == 1) {
                flarePosZ = -21;
            } else {
                flarePosZ = -18;
            }
        } else if (barrelType == 1) {
            flarePosZ = -3;
        }

        flare.setPosZ(flarePosZ);
    }
}
