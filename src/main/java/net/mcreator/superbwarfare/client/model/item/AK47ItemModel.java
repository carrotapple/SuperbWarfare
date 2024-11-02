package net.mcreator.superbwarfare.client.model.item;

import net.mcreator.superbwarfare.ModUtils;
import net.mcreator.superbwarfare.client.AnimationHelper;
import net.mcreator.superbwarfare.event.ClientEventHandler;
import net.mcreator.superbwarfare.init.ModTags;
import net.mcreator.superbwarfare.item.gun.rifle.AK47Item;
import net.mcreator.superbwarfare.tools.GunsTool;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

public class AK47ItemModel extends GeoModel<AK47Item> {
    public static float fireRotY = 0f;
    public static float fireRotZ = 0f;

    @Override
    public ResourceLocation getAnimationResource(AK47Item animatable) {
        return new ResourceLocation(ModUtils.MODID, "animations/ak.animation.json");
    }

    @Override
    public ResourceLocation getModelResource(AK47Item animatable) {
        return new ResourceLocation(ModUtils.MODID, "geo/ak.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(AK47Item animatable) {
        return new ResourceLocation(ModUtils.MODID, "textures/item/ak47.png");
    }

    @Override
    public void setCustomAnimations(AK47Item animatable, long instanceId, AnimationState animationState) {
        CoreGeoBone gun = getAnimationProcessor().getBone("bone");
        CoreGeoBone scope = getAnimationProcessor().getBone("Scope1");
        CoreGeoBone scope2 = getAnimationProcessor().getBone("Scope2");
        CoreGeoBone scope3 = getAnimationProcessor().getBone("Scope3");
        CoreGeoBone cross3 = getAnimationProcessor().getBone("Cross3");
        CoreGeoBone shuan = getAnimationProcessor().getBone("shuan");

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

        int type = GunsTool.getAttachmentType(stack, GunsTool.AttachmentType.SCOPE);

        float posYAlt = switch (type) {
            case 2 -> 0.45f;
            case 3 -> 0.5f;
            default -> 0f;
        };

        float posX = switch (type) {
            case 0, 1, 3 -> 1.962f;
            case 2 -> 1.852f;
            default -> 0f;
        };
        float posY = switch (type) {
            case 0 -> 1.071f;
            case 1 -> 0.261f;
            case 2 -> 0.162f + posYAlt;
            case 3 -> 0.099f + posYAlt;
            default -> 0f;
        };
        float scaleZ = switch (type) {
            case 0 -> 0.55f;
            case 1 -> 0.2f;
            case 2 -> 0.87f;
            case 3 -> 0.78f;
            default -> 0f;
        };
        float posZ = switch (type) {
            case 0, 1 -> 2.8f;
            case 2 -> 4.2f;
            case 3 -> 3.9f;
            default -> 0f;
        };

        gun.setPosX(posX * (float) zp);
        gun.setPosY((posY) * (float) zp - (float) (0.2f * zpz) - posYAlt);
        gun.setPosZ(posZ * (float) zp + (float) (0.5f * zpz));
        gun.setScaleZ(1f - (scaleZ * (float) zp));
        scope.setScaleZ(1f - (0.4f * (float) zp));
        scope2.setScaleZ(1f - (0.3f * (float) zp));
        scope3.setScaleZ(1f - (0.7f * (float) zp));

        stack.getOrCreateTag().putBoolean("HoloHidden", !(gun.getPosX() > 1.8));

        CoreGeoBone shen;
        if (zt < 0.5) {
            shen = getAnimationProcessor().getBone("fireRootNormal");
        } else {
            shen = switch (type) {
                case 0 -> getAnimationProcessor().getBone("fireRoot0");
                case 1 -> getAnimationProcessor().getBone("fireRoot1");
                case 2 -> getAnimationProcessor().getBone("fireRoot2");
                case 3 -> getAnimationProcessor().getBone("fireRoot3");
                default -> getAnimationProcessor().getBone("fireRootNormal");
            };
        }

        fireRotY = (float) Mth.lerp(0.3f * times, fireRotY, 0.2f * ClientEventHandler.recoilHorizon * fpz);
        fireRotZ = (float) Mth.lerp(2f * times, fireRotZ, (0.4f + 0.5 * fpz) * ClientEventHandler.recoilHorizon);

        shen.setPosX(-0.4f * (float) (ClientEventHandler.recoilHorizon * (0.5 + 0.4 * ClientEventHandler.fireSpread)));
        shen.setPosY((float) (0.15f * fp + 0.18f * fr));
        shen.setPosZ((float) (0.365 * fp + 0.42f * fr + 0.95 * fpz));
        shen.setRotX((float) (0.01f * fp + 0.08f * fr + 0.01f * fpz));
        shen.setRotY(fireRotY);
        shen.setRotZ(fireRotZ);

        shen.setPosX((float) (shen.getPosX() * (1 - 0.4 * zt)));
        shen.setPosY((float) (shen.getPosY() * (-1 + 0.8 * zt)));
        shen.setPosZ((float) (shen.getPosZ() * (1 - 0.6 * zt)));
        shen.setRotX((float) (shen.getRotX() * (1 - 0.9 * zt)));
        shen.setRotY((float) (shen.getRotY() * (1 - 0.85 * zt)));
        shen.setRotZ((float) (shen.getRotZ() * (1 - 0.4 * zt)));

        shuan.setPosZ(2.4f * (float) fp);

        cross3.setRotZ(0.01f * (float) (ClientEventHandler.recoilHorizon * fp));
        cross3.setPosY(-0.23f * (float) (fp + 2.3 * fr));

        CoreGeoBone root = getAnimationProcessor().getBone("root");

        root.setPosX((float) (movePosX + 20 * ClientEventHandler.drawTime + 9.3f * mph));
        root.setPosY((float) (swayY + movePosY - 40 * ClientEventHandler.drawTime - 2f * vY));
        root.setRotX((float) (swayX - Mth.DEG_TO_RAD * 60 * ClientEventHandler.drawTime + Mth.DEG_TO_RAD * turnRotX - 0.15f * vY));
        root.setRotY((float) (0.2f * movePosX + Mth.DEG_TO_RAD * 300 * ClientEventHandler.drawTime + Mth.DEG_TO_RAD * turnRotY));
        root.setRotZ((float) (0.2f * movePosX + moveRotZ + Mth.DEG_TO_RAD * 90 * ClientEventHandler.drawTime + 2.7f * mph + Mth.DEG_TO_RAD * turnRotZ));

        CoreGeoBone camera = getAnimationProcessor().getBone("camera");
        CoreGeoBone main = getAnimationProcessor().getBone("0");

        float numR = (float) (1 - 0.98 * zt);
        float numP = (float) (1 - 0.92 * zt);

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
        ClientEventHandler.shake(Mth.RAD_TO_DEG * camera.getRotX(), Mth.RAD_TO_DEG * camera.getRotY(), Mth.RAD_TO_DEG * camera.getRotZ());

        AnimationHelper.handleShellsAnimation(getAnimationProcessor(), 1f, 0.35f);
    }
}
