package net.mcreator.target.procedures;

import net.mcreator.target.entity.ClaymoreEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber
public class QuxiaoshanghaiProcedure {
    @SubscribeEvent
    public static void onEntityAttacked(LivingAttackEvent event) {
        if (event != null && event.getEntity() != null) {
            execute(event, event.getSource(), event.getEntity(), event.getSource().getEntity());
        }
    }

    public static void execute(DamageSource damagesource, Entity entity, Entity sourceentity) {
        execute(null, damagesource, entity, sourceentity);
    }

    private static void execute(@Nullable Event event, DamageSource damagesource, Entity entity, Entity sourceentity) {
        if (damagesource == null || entity == null || sourceentity == null)
            return;
        if (entity instanceof ClaymoreEntity && !(sourceentity == null) && (entity instanceof TamableAnimal _tamEnt ? (Entity) _tamEnt.getOwner() : null) == sourceentity && !(new Object() {
            public boolean checkGamemode(Entity _ent) {
                if (_ent instanceof ServerPlayer _serverPlayer) {
                    return _serverPlayer.gameMode.getGameModeForPlayer() == GameType.CREATIVE;
                } else if (_ent.level().isClientSide() && _ent instanceof Player _player) {
                    return Minecraft.getInstance().getConnection().getPlayerInfo(_player.getGameProfile().getId()) != null && Minecraft.getInstance().getConnection().getPlayerInfo(_player.getGameProfile().getId()).getGameMode() == GameType.CREATIVE;
                }
                return false;
            }
        }.checkGamemode((entity instanceof TamableAnimal _tamEnt ? (Entity) _tamEnt.getOwner() : null)))) {
            if (entity instanceof ClaymoreEntity && damagesource.is(ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation("target:deleted_mod_element")))) {
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
            }
            if (event != null && event.isCancelable()) {
                event.setCanceled(true);
            }
        }
    }
}
