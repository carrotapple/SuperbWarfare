package net.mcreator.target.init;

import net.mcreator.target.entity.AnimatedEntity;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class EntityAnimationFactory {
    @SubscribeEvent
    public static void onEntityTick(LivingEvent.LivingTickEvent event) {
        if (event == null || event.getEntity() == null) return;

        if (event.getEntity() instanceof AnimatedEntity entity) {
            String animation = entity.getSyncedAnimation();
            if (!animation.equals("undefined")) {
                entity.setAnimation("undefined");
                entity.setAnimationProcedure(animation);
            }
        }
    }
}
