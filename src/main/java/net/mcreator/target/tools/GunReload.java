package net.mcreator.target.tools;

import net.mcreator.target.network.TargetModVariables;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class GunReload {
    public static void reload(Entity entity, GunInfo.Type type) {
        reload(entity, type, false);
    }

    public static void reload(Entity entity, GunInfo.Type type, boolean extraOne) {
        if (!(entity instanceof LivingEntity living)) return;

        CompoundTag tag = living.getMainHandItem().getOrCreateTag();

        int mag = tag.getInt("mag");
        int ammo = tag.getInt("ammo");
        int ammoToAdd = mag - ammo + (extraOne ? 1 : 0);
        /**
         * 空仓换弹的栓动武器应该在换单后取消待上膛标记
         */
        if (ammo ==0 && tag.getDouble("bolt_action_time") > 0) {
            tag.putDouble("need_bolt_action", 0);
        }

        int playerAmmo = entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).map(c -> switch (type) {
            case RIFLE -> c.rifleAmmo;
            case HANDGUN -> c.handgunAmmo;
            case SHOTGUN -> c.shotgunAmmo;
            case SNIPER -> c.sniperAmmo;
        }).orElse(0);

        entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
            var newAmmoCount = Math.max(0, playerAmmo - ammoToAdd);
            switch (type) {
                case RIFLE -> capability.rifleAmmo = newAmmoCount;
                case HANDGUN -> capability.handgunAmmo = newAmmoCount;
                case SHOTGUN -> capability.shotgunAmmo = newAmmoCount;
                case SNIPER -> capability.sniperAmmo = newAmmoCount;
            }

            capability.syncPlayerVariables(entity);
        });
        tag.putInt("ammo", ammo + Math.min(ammoToAdd, playerAmmo));

        tag.putBoolean("reloading", false);
        tag.putBoolean("empty_reload", false);
    }
}
