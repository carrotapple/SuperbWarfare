package com.atsuishio.superbwarfare.mixins.tacz;

import com.atsuishio.superbwarfare.entity.OBBEntity;
import com.atsuishio.superbwarfare.init.ModParticleTypes;
import com.atsuishio.superbwarfare.init.ModSounds;
import com.tacz.guns.entity.EntityKineticBullet;
import com.tacz.guns.util.EntityUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

import static com.atsuishio.superbwarfare.tools.ParticleTool.sendParticle;

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
                    if (bulletEntity.level() instanceof ServerLevel serverLevel && bulletEntity.getDeltaMovement().lengthSqr() > 0.01) {
                        Vec3 hitPos = new Vec3(optional.get());
                        bulletEntity.level().playSound(null, BlockPos.containing(hitPos), ModSounds.HIT.get(), SoundSource.PLAYERS, 1, 1);
                        sendParticle(serverLevel, ModParticleTypes.FIRE_STAR.get(), hitPos.x, hitPos.y, hitPos.z, 2, 0, 0, 0, 0.2, false);
                        sendParticle(serverLevel, ParticleTypes.SMOKE, hitPos.x, hitPos.y, hitPos.z, 2, 0, 0, 0, 0.01, false);
                    }
                    return;
                }
            }
        }
    }
}
