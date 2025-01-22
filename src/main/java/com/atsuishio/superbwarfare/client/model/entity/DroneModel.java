package com.atsuishio.superbwarfare.client.model.entity;

import com.atsuishio.superbwarfare.ModUtils;
import com.atsuishio.superbwarfare.entity.DroneEntity;
import com.atsuishio.superbwarfare.init.ModItems;
import com.atsuishio.superbwarfare.tools.EntityFindUtil;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

import java.util.UUID;

import static com.atsuishio.superbwarfare.entity.DroneEntity.*;

public class DroneModel extends GeoModel<DroneEntity> {
    @Override
    public ResourceLocation getAnimationResource(DroneEntity entity) {
        return ModUtils.loc("animations/drone.animation.json");
    }

    @Override
    public ResourceLocation getModelResource(DroneEntity entity) {
        return ModUtils.loc("geo/drone.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(DroneEntity entity) {
        return ModUtils.loc("textures/entity/drone.png");
    }

    @Override
    public void setCustomAnimations(DroneEntity animatable, long instanceId, AnimationState animationState) {
        CoreGeoBone ammo1 = getAnimationProcessor().getBone("ammo1");
        CoreGeoBone ammo2 = getAnimationProcessor().getBone("ammo2");
        CoreGeoBone ammo3 = getAnimationProcessor().getBone("ammo3");
        CoreGeoBone ammo4 = getAnimationProcessor().getBone("ammo4");
        CoreGeoBone ammo5 = getAnimationProcessor().getBone("ammo5");
        CoreGeoBone ammo6 = getAnimationProcessor().getBone("ammo6");
        CoreGeoBone shell = getAnimationProcessor().getBone("shell");
        CoreGeoBone c4 = getAnimationProcessor().getBone("c4");
        // TODO 适配C4模型

        ammo6.setHidden(animatable.getEntityData().get(AMMO) <= 5);
        ammo5.setHidden(animatable.getEntityData().get(AMMO) <= 4);
        ammo4.setHidden(animatable.getEntityData().get(AMMO) <= 3);
        ammo3.setHidden(animatable.getEntityData().get(AMMO) <= 2);
        ammo2.setHidden(animatable.getEntityData().get(AMMO) <= 1);
        ammo1.setHidden(animatable.getEntityData().get(AMMO) <= 0);
        shell.setHidden(animatable.getEntityData().get(KAMIKAZE_MODE) != 1);
        c4.setHidden(animatable.getEntityData().get(KAMIKAZE_MODE) != 2);

        CoreGeoBone weapon = getAnimationProcessor().getBone("Weapon");
        String id = animatable.getEntityData().get(CONTROLLER);
        UUID uuid;
        try {
            uuid = UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            return;
        }

        Player player = animatable.level().getPlayerByUUID(uuid);
        if (player != null) {
            ItemStack stack = player.getMainHandItem();
            DroneEntity drone = EntityFindUtil.findDrone(player.level(), stack.getOrCreateTag().getString("LinkedDrone"));

            if (stack.is(ModItems.MONITOR.get()) && stack.getOrCreateTag().getBoolean("Using") && stack.getOrCreateTag().getBoolean("Linked")) {
                if (drone != null && drone.getUUID() == animatable.getUUID()) {
                    weapon.setHidden(true);
                }
            } else {
                weapon.setHidden(false);
            }
        }
    }
}
