package net.mcreator.target.tools;

import net.mcreator.target.init.TargetModParticleTypes;
import net.mcreator.target.init.TargetModSounds;
import net.minecraft.world.level.block.Blocks;
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
            sendParticle(level, particle, x, y, z, count, xOffset, yOffset, zOffset, speed, force, serverPlayer);
        }
    }

    public static <T extends ParticleOptions> void sendParticle(ServerLevel level, T particle, double x, double y, double z, int count,
                                                                double xOffset, double yOffset, double zOffset, double speed, boolean force, ServerPlayer viewer) {
        level.sendParticles(viewer, particle, force, x, y, z, count, xOffset, yOffset, zOffset, speed);
    }

    public static void spawnMediumExplosionParticles(Level level, Vec3 pos) {
        double x = pos.x;
        double y = pos.y;
        double z = pos.z;

        if (!level.isClientSide()) {
            if ((level.getBlockState(BlockPos.containing(x, y, z))).getBlock() == Blocks.WATER) {
                level.playSound(null, BlockPos.containing(x, y + 1, z), TargetModSounds.EXPLOSION_WATER.get(), SoundSource.BLOCKS, 3, 1);
            }
            level.playSound(null, BlockPos.containing(x, y + 1, z), TargetModSounds.EXPLOSION_CLOSE.get(), SoundSource.BLOCKS, 8, 1);
            level.playSound(null, BlockPos.containing(x, y + 1, z), TargetModSounds.EXPLOSION_FAR.get(), SoundSource.BLOCKS, 16, 1);
            level.playSound(null, BlockPos.containing(x, y + 1, z), TargetModSounds.EXPLOSION_VERY_FAR.get(), SoundSource.BLOCKS, 32, 1);
        } else {
            if ((level.getBlockState(BlockPos.containing(x, y, z))).getBlock() == Blocks.WATER) {
                level.playLocalSound(x, (y + 1), z, TargetModSounds.EXPLOSION_WATER.get(), SoundSource.BLOCKS, 3, 1, false);
            }
            level.playLocalSound(x, (y + 1), z, TargetModSounds.EXPLOSION_CLOSE.get(), SoundSource.BLOCKS, 24, 1, false);
            level.playLocalSound(x, (y + 1), z, TargetModSounds.EXPLOSION_FAR.get(), SoundSource.BLOCKS, 24, 1, false);
            level.playLocalSound(x, (y + 1), z, TargetModSounds.EXPLOSION_VERY_FAR.get(), SoundSource.BLOCKS, 64, 1, false);
        }

        if (level instanceof ServerLevel serverLevel) {
            if ((level.getBlockState(BlockPos.containing(x, y, z))).getBlock() == Blocks.WATER) {
                sendParticle(serverLevel, ParticleTypes.CLOUD, x, y + 3, z, 100, 1, 3, 1, 0.01, true);
                sendParticle(serverLevel, ParticleTypes.CLOUD, x, y + 3, z, 150, 2, 1, 2, 0.01, true);
                sendParticle(serverLevel, ParticleTypes.FALLING_WATER, x, y + 3, z, 400, 1.5, 4, 1.5, 1, true);
                sendParticle(serverLevel, ParticleTypes.BUBBLE_COLUMN_UP, x, y, z, 300, 3, 0.5, 3, 0.1, true);
            }
            sendParticle(serverLevel, ParticleTypes.EXPLOSION, x, y + 1, z, 5, 0.7, 0.7, 0.7, 1, true);
            sendParticle(serverLevel, ParticleTypes.CAMPFIRE_COSY_SMOKE, x, y, z, 80, 0.4, 1, 0.4, 0.02, true);
            sendParticle(serverLevel, ParticleTypes.LARGE_SMOKE, x, y + 1, z, 80, 0.4, 1, 0.4, 0.02, true);
            sendParticle(serverLevel, ParticleTypes.CAMPFIRE_COSY_SMOKE, x, y, z, 80, 2, 0.001, 2, 0.01, true);
            sendParticle(serverLevel, TargetModParticleTypes.FIRE_STAR.get(), x, y, z, 80, 0, 0, 0, 0.2, true);
        }

    }

    public static void spawnHugeExplosionParticles(Level level, Vec3 pos) {
        double x = pos.x;
        double y = pos.y;
        double z = pos.z;

        if (!level.isClientSide()) {
            if ((level.getBlockState(BlockPos.containing(x, y, z))).getBlock() == Blocks.WATER) {
                level.playSound(null, BlockPos.containing(x, y + 1, z), TargetModSounds.EXPLOSION_WATER.get(), SoundSource.BLOCKS, 3, 1);
            }
            level.playSound(null, BlockPos.containing(x, y + 1, z), TargetModSounds.EXPLOSION_CLOSE.get(), SoundSource.BLOCKS, 8, 1);
            level.playSound(null, BlockPos.containing(x, y + 1, z), TargetModSounds.EXPLOSION_FAR.get(), SoundSource.BLOCKS, 16, 1);
            level.playSound(null, BlockPos.containing(x, y + 1, z), TargetModSounds.EXPLOSION_VERY_FAR.get(), SoundSource.BLOCKS, 32, 1);
        } else {
            if ((level.getBlockState(BlockPos.containing(x, y, z))).getBlock() == Blocks.WATER) {
                level.playLocalSound(x, (y + 1), z, TargetModSounds.EXPLOSION_WATER.get(), SoundSource.BLOCKS, 3, 1, false);
            }
            level.playLocalSound(x, (y + 1), z, TargetModSounds.EXPLOSION_CLOSE.get(), SoundSource.BLOCKS, 24, 1, false);
            level.playLocalSound(x, (y + 1), z, TargetModSounds.EXPLOSION_FAR.get(), SoundSource.BLOCKS, 24, 1, false);
            level.playLocalSound(x, (y + 1), z, TargetModSounds.EXPLOSION_VERY_FAR.get(), SoundSource.BLOCKS, 64, 1, false);
        }

        if (level instanceof ServerLevel serverLevel) {
            if ((level.getBlockState(BlockPos.containing(x, y, z))).getBlock() == Blocks.WATER) {
                sendParticle(serverLevel, ParticleTypes.CLOUD, x, y + 3, z, 100, 2, 6, 2, 0.01, true);
                sendParticle(serverLevel, ParticleTypes.CLOUD, x, y + 3, z, 200, 4, 2, 4, 0.01, true);
                sendParticle(serverLevel, ParticleTypes.FALLING_WATER, x, y + 3, z, 500, 3, 8, 3, 1, true);
                sendParticle(serverLevel, ParticleTypes.BUBBLE_COLUMN_UP, x, y, z, 350, 6, 1, 6, 0.1, true);
            }

            sendParticle(serverLevel, ParticleTypes.EXPLOSION, x, y + 1, z, 75, 2.5, 2.5, 2.5, 1, true);
            sendParticle(serverLevel, ParticleTypes.FLASH, x, y + 1, z, 200, 5, 5, 5, 20, true);
            sendParticle(serverLevel, TargetModParticleTypes.FIRE_STAR.get(), x, y + 1, z, 400, 0, 0, 0, 1.5, true);
            sendParticle(serverLevel, ParticleTypes.CAMPFIRE_COSY_SMOKE, x, y + 1, z, 75, 2, 3, 2, 0.005, true);
            sendParticle(serverLevel, ParticleTypes.CAMPFIRE_COSY_SMOKE, x, y, z, 150, 7, 0.1, 7, 0.005, true);
            sendParticle(serverLevel, ParticleTypes.CLOUD, x, y + 1, z, 200, 3, 4, 3, 0.4, true);
        }

    }

    public static void cannonHitParticles(Level level, Vec3 pos) {
        double x = pos.x;
        double y = pos.y;
        double z = pos.z;

        if (level instanceof ServerLevel serverLevel) {
            sendParticle(serverLevel, ParticleTypes.EXPLOSION, x, y, z, 2, 0.5, 0.5, 0.5, 1, true);
            sendParticle(serverLevel, ParticleTypes.FLASH, x, y, z, 2, 0.2, 0.2, 0.2, 10, true);
            sendParticle(serverLevel, TargetModParticleTypes.FIRE_STAR.get(), x, y, z, 40, 0, 0, 0, 1.5, true);
        }

    }
}
