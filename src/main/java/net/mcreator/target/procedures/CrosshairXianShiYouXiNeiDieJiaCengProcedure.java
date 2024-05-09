package net.mcreator.target.procedures;

import net.mcreator.target.init.TargetModItems;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class CrosshairXianShiYouXiNeiDieJiaCengProcedure {
    public static boolean execute(Entity entity) {
        if (entity == null) return false;

        if (entity instanceof Player player) {
            if (player.isSpectator()) return false;
            if (!player.getMainHandItem().is(ItemTags.create(new ResourceLocation("target:gun")))
                    || !(entity.getPersistentData().getDouble("zoom_time") < 7)
            ) return false;

            return !(player.getMainHandItem().getItem() == TargetModItems.M_79.get())
                    && Minecraft.getInstance().options.getCameraType() == CameraType.FIRST_PERSON;
        }
        return false;
    }
}
