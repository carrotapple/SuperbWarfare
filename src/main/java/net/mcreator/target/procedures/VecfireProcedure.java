package net.mcreator.target.procedures;

import net.minecraft.world.level.GameType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.client.Minecraft;

import net.mcreator.target.init.TargetModItems;

public class VecfireProcedure {
    public static void execute(Entity entity) {
        if (entity == null)
            return;
        ItemStack usehand = ItemStack.EMPTY;
        if (!(new Object() {
            public boolean checkGamemode(Entity _ent) {
                if (_ent instanceof ServerPlayer _serverPlayer) {
                    return _serverPlayer.gameMode.getGameModeForPlayer() == GameType.SPECTATOR;
                } else if (_ent.level().isClientSide() && _ent instanceof Player _player) {
                    return Minecraft.getInstance().getConnection().getPlayerInfo(_player.getGameProfile().getId()) != null && Minecraft.getInstance().getConnection().getPlayerInfo(_player.getGameProfile().getId()).getGameMode() == GameType.SPECTATOR;
                }
                return false;
            }
        }.checkGamemode(entity))) {
            usehand = (entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY);
            if (usehand.getItem() == TargetModItems.VECTOR.get()) {
                if (usehand.getOrCreateTag().getDouble("firemode") == 1 && usehand.getOrCreateTag().getDouble("burst") == 0) {
                    usehand.getOrCreateTag().putDouble("burst", 3);
                }
            }
        }
    }
}

