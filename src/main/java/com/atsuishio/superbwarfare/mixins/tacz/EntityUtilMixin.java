package com.atsuishio.superbwarfare.mixins.tacz;

import com.atsuishio.superbwarfare.entity.OBBEntity;
import com.tacz.guns.entity.EntityKineticBullet;
import com.tacz.guns.util.EntityUtil;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(EntityUtil.class)
public class EntityUtilMixin {

    @Inject(method = "getHitResult(Lnet/minecraft/world/entity/projectile/Projectile;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/phys/Vec3;)Lcom/tacz/guns/entity/EntityKineticBullet$EntityResult;",
            at = @At("HEAD"), cancellable = true, remap = false)
    private static void getHitResult(Projectile bulletEntity, Entity entity, Vec3 startVec, Vec3 endVec, CallbackInfoReturnable<EntityKineticBullet.EntityResult> cir) {
        if (entity instanceof OBBEntity obbEntity) {
            var obbList = obbEntity.getOBBs();
            for (var obb : obbList) {
                Optional<Vector3f> optional = obb.clip(startVec.toVector3f(), endVec.toVector3f());
                if (optional.isPresent()) {
                    cir.setReturnValue(new EntityKineticBullet.EntityResult(entity, new Vec3(optional.get()), false));
                    return;
                }
            }
        }
    }
}
