package net.mcreator.target.procedures;

import net.mcreator.target.entity.Target1Entity;
import net.mcreator.target.network.TargetModVariables;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;

public class JumppadBlockShiTiZaiFangKuaiZhongPengZhuangShiProcedure {
    public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
        if (entity == null)
            return;
        if (!(entity instanceof Target1Entity)) {
            if (entity.isShiftKeyDown()) {
                if (entity.onGround()) {
                    entity.setDeltaMovement(new Vec3((5 * entity.getLookAngle().x), 1.5, (5 * entity.getLookAngle().z)));
                    if (!(entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).zooming) {
                        entity.getPersistentData().putDouble("vy", 0.8);
                    }
                    if (world instanceof Level _level) {
                        if (!_level.isClientSide()) {
                            _level.playSound(null, BlockPos.containing(x, y, z), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("target:jump")), SoundSource.BLOCKS, 1, 1);
                        } else {
                            _level.playLocalSound(x, y, z, ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("target:jump")), SoundSource.BLOCKS, 1, 1, false);
                        }
                    }
                } else {
                    entity.setDeltaMovement(new Vec3((1.8 * entity.getLookAngle().x), 1.5, (1.8 * entity.getLookAngle().z)));
                    if (!(entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).zooming) {
                        entity.getPersistentData().putDouble("vy", 0.8);
                    }
                    if (world instanceof Level _level) {
                        if (!_level.isClientSide()) {
                            _level.playSound(null, BlockPos.containing(x, y, z), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("target:jump")), SoundSource.BLOCKS, 1, 1);
                        } else {
                            _level.playLocalSound(x, y, z, ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("target:jump")), SoundSource.BLOCKS, 1, 1, false);
                        }
                    }
                }
            } else {
                entity.setDeltaMovement(new Vec3((0.7 * entity.getDeltaMovement().x()), 1.7, (0.7 * entity.getDeltaMovement().z())));
                if (!(entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).zooming) {
                    entity.getPersistentData().putDouble("vy", 0.8);
                }
                if (world instanceof Level _level) {
                    if (!_level.isClientSide()) {
                        _level.playSound(null, BlockPos.containing(x, y, z), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("target:jump")), SoundSource.BLOCKS, 1, 1);
                    } else {
                        _level.playLocalSound(x, y, z, ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("target:jump")), SoundSource.BLOCKS, 1, 1, false);
                    }
                }
            }
            {
                boolean _setval = true;
                entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                    capability.playerDoubleJump = _setval;
                    capability.syncPlayerVariables(entity);
                });
            }
        }
    }
}
