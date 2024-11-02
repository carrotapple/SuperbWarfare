package net.mcreator.superbwarfare.entity.model;

import net.mcreator.superbwarfare.ModUtils;
import net.mcreator.superbwarfare.entity.Mk42Entity;
import net.mcreator.superbwarfare.event.ClientEventHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class Mk42Model extends GeoModel<Mk42Entity> {

    @Override
    public ResourceLocation getAnimationResource(Mk42Entity entity) {
        return ModUtils.loc("animations/mk_42.animation.json");
    }

    @Override
    public ResourceLocation getModelResource(Mk42Entity entity) {
        Player player = Minecraft.getInstance().player;

        int distance = 0;

        if (player != null) {
            distance = (int) player.position().distanceTo(entity.position());
        }

        if (distance < 32) {
            return ModUtils.loc("geo/sherman.geo.json");
        } else if (distance < 64) {
            return ModUtils.loc("geo/sherman_lod1.geo.json");
        } else {
            return ModUtils.loc("geo/sherman_lod2.geo.json");
        }
    }

    @Override
    public ResourceLocation getTextureResource(Mk42Entity entity) {
        return ModUtils.loc("textures/entity/sherman.png");
    }

    @Override
    public void setCustomAnimations(Mk42Entity animatable, long instanceId, AnimationState<Mk42Entity> animationState) {
        CoreGeoBone bone = getAnimationProcessor().getBone("maingun");
        EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
        bone.setRotX((entityData.headPitch()) * Mth.DEG_TO_RAD);

        CoreGeoBone camera = getAnimationProcessor().getBone("camera");

        if (animatable.getFirstPassenger() == null) return;
        ClientEventHandler.shake(Mth.RAD_TO_DEG * camera.getRotX(), Mth.RAD_TO_DEG * camera.getRotY(), Mth.RAD_TO_DEG * camera.getRotZ());
    }
}
