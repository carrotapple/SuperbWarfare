package net.mcreator.target.procedures;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber
public class HurtcancelProcedure {
    @SubscribeEvent
    public static void onEntityAttacked(LivingAttackEvent event) {
        if (event != null && event.getEntity() != null) {
            execute(event, event.getSource(), event.getSource().getEntity());
        }
    }

    public static void execute(DamageSource damagesource, Entity sourceentity) {
        execute(null, damagesource, sourceentity);
    }

    private static void execute(@Nullable Event event, DamageSource damagesource, Entity sourceentity) {
        if (damagesource == null || sourceentity == null)
            return;
        if (sourceentity instanceof Player player && (!sourceentity.isAlive() || player.isSpectator()) && (damagesource.is(ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation("target:arrow_in_brain"))) || damagesource.is(ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation("target:mine"))) || damagesource.is(DamageTypes.ARROW))) {
            if (event != null && event.isCancelable()) {
                event.setCanceled(true);
            }
        }
    }
}
