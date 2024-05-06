package net.mcreator.target.procedures;

import net.mcreator.target.entity.ClaymoreEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
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
        if (entity instanceof ClaymoreEntity tamEnt && tamEnt.getOwner() == sourceentity) {
            if (tamEnt.getOwner() instanceof Player player && player.isCreative()) {
                if (entity instanceof ClaymoreEntity _entity && damagesource.is(ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation("target:deleted_mod_element")))) {
                    entity.setYRot(sourceentity.getYRot());
                    entity.setXRot(entity.getXRot());
                    entity.setYBodyRot(entity.getYRot());
                    entity.setYHeadRot(entity.getYRot());
                    entity.yRotO = entity.getYRot();
                    entity.xRotO = entity.getXRot();
                    _entity.yBodyRotO = _entity.getYRot();
                    _entity.yHeadRotO = _entity.getYRot();
                }
                if (event != null && event.isCancelable()) {
                    event.setCanceled(true);
                }
            }
        }
    }
}
