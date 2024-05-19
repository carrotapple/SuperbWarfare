package net.mcreator.target.tools;

import net.mcreator.target.init.TargetModParticleTypes;
import net.mcreator.target.init.TargetModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class ParticleTool {
    public static <T extends ParticleOptions> void sendParticle(ServerLevel level, T particle, double x, double y, double z, int count,
                                                                double xOffset, double yOffset, double zOffset, double speed, boolean force) {
        for (ServerPlayer serverPlayer : level.players()) {
            level.sendParticles(serverPlayer, particle, force, x, y, z, count, xOffset, yOffset, zOffset, speed);
        }
    }

    public static void spawnMediumExplosionParticles(Level level, Vec3 pos) {
        double x = pos.x;
        double y = pos.y;
        double z = pos.z;

        if (!level.isClientSide()) {
            level.playSound(null, BlockPos.containing(x, y + 1, z), TargetModSounds.EXPLOSION.get(), SoundSource.BLOCKS, 8, 1);
            level.playSound(null, BlockPos.containing(x, y + 1, z), TargetModSounds.EXPLOSION_FAR.get(), SoundSource.BLOCKS, 16, 1);
            level.playSound(null, BlockPos.containing(x, y + 1, z), TargetModSounds.EXPLOSION_VERY_FAR.get(), SoundSource.BLOCKS, 32, 1);
        } else {
            level.playLocalSound(x, (y + 1), z, TargetModSounds.EXPLOSION.get(), SoundSource.BLOCKS, 24, 1, false);
            level.playLocalSound(x, (y + 1), z, TargetModSounds.EXPLOSION_FAR.get(), SoundSource.BLOCKS, 24, 1, false);
            level.playLocalSound(x, (y + 1), z, TargetModSounds.EXPLOSION_VERY_FAR.get(), SoundSource.BLOCKS, 64, 1, false);
        }

        if (level instanceof ServerLevel serverLevel) {
            sendParticle(serverLevel, ParticleTypes.CAMPFIRE_COSY_SMOKE, x, y, z, 80, 0.4, 1, 0.4, 0.02, true);
            sendParticle(serverLevel, ParticleTypes.LARGE_SMOKE, x, y + 1, z, 80, 0.4, 1, 0.4, 0.02, true);
            sendParticle(serverLevel, ParticleTypes.CAMPFIRE_COSY_SMOKE, x, y, z, 80, 2, 0.001, 2, 0.01, true);
            sendParticle(serverLevel, TargetModParticleTypes.FIRE_STAR.get(), x, y, z, 80, 0, 0, 0, 0.2, true);
        }

    }
}
