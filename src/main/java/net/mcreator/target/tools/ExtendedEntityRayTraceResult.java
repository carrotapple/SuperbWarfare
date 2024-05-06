package net.mcreator.target.tools;

import net.mcreator.target.entity.ProjectileEntity;
import net.minecraft.world.phys.EntityHitResult;

/**
 * Author: MrCrayFish
 */
public class ExtendedEntityRayTraceResult extends EntityHitResult {
    private final boolean headshot;

    public ExtendedEntityRayTraceResult(ProjectileEntity.EntityResult result) {
        super(result.getEntity(), result.getHitPos());
        this.headshot = result.isHeadshot();
    }

    public boolean isHeadshot() {
        return this.headshot;
    }
}
