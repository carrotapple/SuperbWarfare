package net.mcreator.superbwarfare.tools;

import net.mcreator.superbwarfare.entity.ProjectileEntity;
import net.minecraft.world.phys.EntityHitResult;

/**
 * Author: MrCrayFish
 */
public class ExtendedEntityRayTraceResult extends EntityHitResult {
    private final boolean headshot;
    private final boolean legshot;

    public ExtendedEntityRayTraceResult(ProjectileEntity.EntityResult result) {
        super(result.getEntity(), result.getHitPos());
        this.headshot = result.isHeadshot();
        this.legshot = result.isLegShot();
    }

    public boolean isHeadshot() {
        return this.headshot;
    }
    public boolean isLegshot() {
        return this.legshot;
    }
}
