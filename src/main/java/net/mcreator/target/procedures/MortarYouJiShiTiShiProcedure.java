package net.mcreator.target.procedures;

import io.netty.buffer.Unpooled;
import net.mcreator.target.TargetMod;
import net.mcreator.target.entity.MortarShellEntity;
import net.mcreator.target.init.TargetModEntities;
import net.mcreator.target.init.TargetModItems;
import net.mcreator.target.world.inventory.MortarGUIMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.network.NetworkHooks;

public class MortarYouJiShiTiShiProcedure {
    public static void execute(LevelAccessor world, double x, double y, double z, Entity entity, Entity sourceentity) {
        if (entity == null || sourceentity == null)
            return;
        if ((sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() == ItemStack.EMPTY.getItem()) {
            if (sourceentity.isShiftKeyDown()) {
                {
                    Entity _ent = entity;
                    _ent.setYRot(sourceentity.getYRot());
                    _ent.setXRot(entity.getXRot());
                    _ent.setYBodyRot(_ent.getYRot());
                    _ent.setYHeadRot(_ent.getYRot());
                    _ent.yRotO = _ent.getYRot();
                    _ent.xRotO = _ent.getXRot();
                    if (_ent instanceof LivingEntity _entity) {
                        _entity.yBodyRotO = _entity.getYRot();
                        _entity.yHeadRotO = _entity.getYRot();
                    }
                }
            } else {
                if (sourceentity instanceof ServerPlayer _ent) {
                    BlockPos _bpos = BlockPos.containing(x, y, z);
                    NetworkHooks.openScreen(_ent, new MenuProvider() {
                        @Override
                        public Component getDisplayName() {
                            return Component.literal("MortarGUI");
                        }

                        @Override
                        public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
                            return new MortarGUIMenu(id, inventory, new FriendlyByteBuf(Unpooled.buffer()).writeBlockPos(_bpos));
                        }
                    }, _bpos);
                }
            }
        }
        if ((sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() == TargetModItems.MORTAR_SHELLS.get()
                && !(sourceentity instanceof Player _plrCldCheck10 && _plrCldCheck10.getCooldowns().isOnCooldown(TargetModItems.MORTAR_SHELLS.get()))) {
            if (sourceentity instanceof Player _player)
                _player.getCooldowns().addCooldown(TargetModItems.MORTAR_SHELLS.get(), 30);
            if (!(new Object() {
                public boolean checkGamemode(Entity _ent) {
                    if (_ent instanceof ServerPlayer _serverPlayer) {
                        return _serverPlayer.gameMode.getGameModeForPlayer() == GameType.CREATIVE;
                    } else if (_ent.level().isClientSide() && _ent instanceof Player _player) {
                        return Minecraft.getInstance().getConnection().getPlayerInfo(_player.getGameProfile().getId()) != null
                                && Minecraft.getInstance().getConnection().getPlayerInfo(_player.getGameProfile().getId()).getGameMode() == GameType.CREATIVE;
                    }
                    return false;
                }
            }.checkGamemode(sourceentity))) {
                if (sourceentity instanceof Player _player) {
                    ItemStack _stktoremove = new ItemStack(TargetModItems.MORTAR_SHELLS.get());
                    _player.getInventory().clearOrCountMatchingItems(p -> _stktoremove.getItem() == p.getItem(), 1, _player.inventoryMenu.getCraftSlots());
                }
            }
            {
                Entity _ent = entity;
                if (!_ent.level().isClientSide() && _ent.getServer() != null) {
                    _ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4,
                            _ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), "playsound target:mortar_load player @a ~ ~ ~ 1 1");
                }
            }
            {
                Entity _ent = entity;
                if (!_ent.level().isClientSide() && _ent.getServer() != null) {
                    _ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4,
                            _ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), "playsound target:mortar_fire player @a ~ ~ ~ 8 1");
                }
            }
            {
                Entity _ent = entity;
                if (!_ent.level().isClientSide() && _ent.getServer() != null) {
                    _ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4,
                            _ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), "playsound target:mortar_distant player @a ~ ~ ~ 32 1");
                }
            }
            TargetMod.queueServerWork(20, () -> {
                {
                    Entity _shootFrom = entity;
                    Level projectileLevel = _shootFrom.level();
                    if (!projectileLevel.isClientSide()) {
                        Projectile _entityToSpawn = new Object() {
                            public Projectile getArrow(Level level, Entity shooter, float damage, int knockback) {
                                AbstractArrow entityToSpawn = new MortarShellEntity(TargetModEntities.MORTAR_SHELL.get(), level);
                                entityToSpawn.setOwner(shooter);
                                entityToSpawn.setBaseDamage(damage);
                                entityToSpawn.setKnockback(knockback);
                                entityToSpawn.setSilent(true);
                                return entityToSpawn;
                            }
                        }.getArrow(projectileLevel, sourceentity, 100, 0);
                        _entityToSpawn.setPos(_shootFrom.getX(), _shootFrom.getEyeY() - 0.1, _shootFrom.getZ());
                        _entityToSpawn.shoot(_shootFrom.getLookAngle().x, _shootFrom.getLookAngle().y, _shootFrom.getLookAngle().z, 8, (float) 0.5);
                        projectileLevel.addFreshEntity(_entityToSpawn);
                    }
                }
                if (world instanceof ServerLevel _level)
                    _level.sendParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE, (entity.getX() + 2.2 * entity.getLookAngle().x), (entity.getY() + 0.1 + 2.2 * entity.getLookAngle().y), (entity.getZ() + 2.2 * entity.getLookAngle().z), 40, 0.4, 0.4, 0.4,
                            0.015);
            });
        }
    }
}
