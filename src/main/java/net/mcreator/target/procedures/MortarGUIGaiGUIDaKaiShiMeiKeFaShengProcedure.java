package net.mcreator.target.procedures;

import net.mcreator.target.init.TargetModAttributes;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.phys.*;

import java.util.HashMap;

public class MortarGUIGaiGUIDaKaiShiMeiKeFaShengProcedure {
    public static void execute(Entity entity, HashMap guistate) {
        if (entity == null || guistate == null)
            return;
        if (!((new Object() {
            public Entity func(Entity player, double entityReach) {
                double distance = entityReach * entityReach;
                Vec3 eyePos = player.getEyePosition(1.0f);
                HitResult hitResult = entity.pick(entityReach, 1.0f, false);
                if (hitResult.getType() != HitResult.Type.MISS) {
                    distance = hitResult.getLocation().distanceToSqr(eyePos);
                    double blockReach = 5;
                    if (distance > blockReach * blockReach) {
                        Vec3 pos = hitResult.getLocation();
                        hitResult = BlockHitResult.miss(pos, Direction.getNearest(eyePos.x, eyePos.y, eyePos.z), BlockPos.containing(pos));
                    }
                }
                Vec3 viewVec = player.getViewVector(1.0F);
                Vec3 toVec = eyePos.add(viewVec.x * entityReach, viewVec.y * entityReach, viewVec.z * entityReach);
                AABB aabb = entity.getBoundingBox().expandTowards(viewVec.scale(entityReach)).inflate(1.0D, 1.0D, 1.0D);
                EntityHitResult entityhitresult = ProjectileUtil.getEntityHitResult(player, eyePos, toVec, aabb, (p_234237_) -> !p_234237_.isSpectator(), distance);
                if (entityhitresult != null) {
                    Vec3 targetPos = entityhitresult.getLocation();
                    double distanceToTarget = eyePos.distanceToSqr(targetPos);
                    if (distanceToTarget > distance || distanceToTarget > entityReach * entityReach) {
                        hitResult = BlockHitResult.miss(targetPos, Direction.getNearest(viewVec.x, viewVec.y, viewVec.z), BlockPos.containing(targetPos));
                    } else if (distanceToTarget < distance) {
                        hitResult = entityhitresult;
                    }
                }
                if (hitResult.getType() == HitResult.Type.ENTITY) {
                    return ((EntityHitResult) hitResult).getEntity();
                }
                return null;
            }
        }.func(entity, 6)) == null)) {
            if (20 <= new Object() {
                double convert(String s) {
                    try {
                        return Double.parseDouble(s.trim());
                    } catch (Exception ignored) {
                    }
                    return 0;
                }
            }.convert(guistate.containsKey("text:pitch") ? ((EditBox) guistate.get("text:pitch")).getValue() : "") && new Object() {
                double convert(String s) {
                    try {
                        return Double.parseDouble(s.trim());
                    } catch (Exception ignored) {
                    }
                    return 0;
                }
            }.convert(guistate.containsKey("text:pitch") ? ((EditBox) guistate.get("text:pitch")).getValue() : "") <= 90) {
                {
                    Entity _ent = (new Object() {
                        public Entity func(Entity player, double entityReach) {
                            double distance = entityReach * entityReach;
                            Vec3 eyePos = player.getEyePosition(1.0f);
                            HitResult hitResult = entity.pick(entityReach, 1.0f, false);
                            if (hitResult.getType() != HitResult.Type.MISS) {
                                distance = hitResult.getLocation().distanceToSqr(eyePos);
                                double blockReach = 5;
                                if (distance > blockReach * blockReach) {
                                    Vec3 pos = hitResult.getLocation();
                                    hitResult = BlockHitResult.miss(pos, Direction.getNearest(eyePos.x, eyePos.y, eyePos.z), BlockPos.containing(pos));
                                }
                            }
                            Vec3 viewVec = player.getViewVector(1.0F);
                            Vec3 toVec = eyePos.add(viewVec.x * entityReach, viewVec.y * entityReach, viewVec.z * entityReach);
                            AABB aabb = entity.getBoundingBox().expandTowards(viewVec.scale(entityReach)).inflate(1.0D, 1.0D, 1.0D);
                            EntityHitResult entityhitresult = ProjectileUtil.getEntityHitResult(player, eyePos, toVec, aabb, (p_234237_) -> !p_234237_.isSpectator(), distance);
                            if (entityhitresult != null) {
                                Vec3 targetPos = entityhitresult.getLocation();
                                double distanceToTarget = eyePos.distanceToSqr(targetPos);
                                if (distanceToTarget > distance || distanceToTarget > entityReach * entityReach) {
                                    hitResult = BlockHitResult.miss(targetPos, Direction.getNearest(viewVec.x, viewVec.y, viewVec.z), BlockPos.containing(targetPos));
                                } else if (distanceToTarget < distance) {
                                    hitResult = entityhitresult;
                                }
                            }
                            if (hitResult.getType() == HitResult.Type.ENTITY) {
                                return ((EntityHitResult) hitResult).getEntity();
                            }
                            return null;
                        }
                    }.func(entity, 6));
                    _ent.setYRot((new Object() {
                        public Entity func(Entity player, double entityReach) {
                            double distance = entityReach * entityReach;
                            Vec3 eyePos = player.getEyePosition(1.0f);
                            HitResult hitResult = entity.pick(entityReach, 1.0f, false);
                            if (hitResult.getType() != HitResult.Type.MISS) {
                                distance = hitResult.getLocation().distanceToSqr(eyePos);
                                double blockReach = 5;
                                if (distance > blockReach * blockReach) {
                                    Vec3 pos = hitResult.getLocation();
                                    hitResult = BlockHitResult.miss(pos, Direction.getNearest(eyePos.x, eyePos.y, eyePos.z), BlockPos.containing(pos));
                                }
                            }
                            Vec3 viewVec = player.getViewVector(1.0F);
                            Vec3 toVec = eyePos.add(viewVec.x * entityReach, viewVec.y * entityReach, viewVec.z * entityReach);
                            AABB aabb = entity.getBoundingBox().expandTowards(viewVec.scale(entityReach)).inflate(1.0D, 1.0D, 1.0D);
                            EntityHitResult entityhitresult = ProjectileUtil.getEntityHitResult(player, eyePos, toVec, aabb, (p_234237_) -> !p_234237_.isSpectator(), distance);
                            if (entityhitresult != null) {
                                Vec3 targetPos = entityhitresult.getLocation();
                                double distanceToTarget = eyePos.distanceToSqr(targetPos);
                                if (distanceToTarget > distance || distanceToTarget > entityReach * entityReach) {
                                    hitResult = BlockHitResult.miss(targetPos, Direction.getNearest(viewVec.x, viewVec.y, viewVec.z), BlockPos.containing(targetPos));
                                } else if (distanceToTarget < distance) {
                                    hitResult = entityhitresult;
                                }
                            }
                            if (hitResult.getType() == HitResult.Type.ENTITY) {
                                return ((EntityHitResult) hitResult).getEntity();
                            }
                            return null;
                        }
                    }.func(entity, 6)).getYRot());
                    _ent.setXRot((float) (-((LivingEntity) (new Object() {
                        public Entity func(Entity player, double entityReach) {
                            double distance = entityReach * entityReach;
                            Vec3 eyePos = player.getEyePosition(1.0f);
                            HitResult hitResult = entity.pick(entityReach, 1.0f, false);
                            if (hitResult.getType() != HitResult.Type.MISS) {
                                distance = hitResult.getLocation().distanceToSqr(eyePos);
                                double blockReach = 5;
                                if (distance > blockReach * blockReach) {
                                    Vec3 pos = hitResult.getLocation();
                                    hitResult = BlockHitResult.miss(pos, Direction.getNearest(eyePos.x, eyePos.y, eyePos.z), BlockPos.containing(pos));
                                }
                            }
                            Vec3 viewVec = player.getViewVector(1.0F);
                            Vec3 toVec = eyePos.add(viewVec.x * entityReach, viewVec.y * entityReach, viewVec.z * entityReach);
                            AABB aabb = entity.getBoundingBox().expandTowards(viewVec.scale(entityReach)).inflate(1.0D, 1.0D, 1.0D);
                            EntityHitResult entityhitresult = ProjectileUtil.getEntityHitResult(player, eyePos, toVec, aabb, (p_234237_) -> !p_234237_.isSpectator(), distance);
                            if (entityhitresult != null) {
                                Vec3 targetPos = entityhitresult.getLocation();
                                double distanceToTarget = eyePos.distanceToSqr(targetPos);
                                if (distanceToTarget > distance || distanceToTarget > entityReach * entityReach) {
                                    hitResult = BlockHitResult.miss(targetPos, Direction.getNearest(viewVec.x, viewVec.y, viewVec.z), BlockPos.containing(targetPos));
                                } else if (distanceToTarget < distance) {
                                    hitResult = entityhitresult;
                                }
                            }
                            if (hitResult.getType() == HitResult.Type.ENTITY) {
                                return ((EntityHitResult) hitResult).getEntity();
                            }
                            return null;
                        }
                    }.func(entity, 6))).getAttribute(TargetModAttributes.MOTARPITCH.get()).getBaseValue()));
                    _ent.setYBodyRot(_ent.getYRot());
                    _ent.setYHeadRot(_ent.getYRot());
                    _ent.yRotO = _ent.getYRot();
                    _ent.xRotO = _ent.getXRot();
                    if (_ent instanceof LivingEntity _entity) {
                        _entity.yBodyRotO = _entity.getYRot();
                        _entity.yHeadRotO = _entity.getYRot();
                    }
                }
                {
                    if (!entity.level().isClientSide() && entity.getServer() != null) {
                        entity.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, entity.position(), entity.getRotationVector(), entity.level() instanceof ServerLevel ? (ServerLevel) entity.level() : null, 4,
                                entity.getName().getString(), entity.getDisplayName(), entity.level().getServer(), entity), "playsound minecraft:entity.arrow.hit_player player @s ~ ~ ~ 1 1");
                    }
                }
            } else {
                {
                    if (!entity.level().isClientSide() && entity.getServer() != null) {
                        entity.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, entity.position(), entity.getRotationVector(), entity.level() instanceof ServerLevel ? (ServerLevel) entity.level() : null, 4,
                                entity.getName().getString(), entity.getDisplayName(), entity.level().getServer(), entity), "playsound minecraft:block.note_block.bass player @s ~ ~ ~ 1 1");
                    }
                }
            }
        }
    }
}
