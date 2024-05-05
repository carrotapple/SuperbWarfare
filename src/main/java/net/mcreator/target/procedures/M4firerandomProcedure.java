package net.mcreator.target.procedures;

import net.mcreator.target.init.TargetModItems;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber
public class M4firerandomProcedure {
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            execute(event, event.player);
        }
    }

    public static void execute(Entity entity) {
        execute(null, entity);
    }

    private static void execute(@Nullable Event event, Entity entity) {
        if (entity == null)
            return;
        ItemStack usehand;
        usehand = (entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY);
        if (entity.getPersistentData().getDouble("firing") == 1) {
            entity.getPersistentData().putDouble("m4fire", (entity.getPersistentData().getDouble("m4fire") + 1));
        } else {
            entity.getPersistentData().putDouble("m4fire", 0);
        }
        if (entity.getPersistentData().getDouble("firing") == 1) {
            if (usehand.getItem() == TargetModItems.M_4.get() && usehand.getOrCreateTag().getDouble("reloading") == 0 && usehand.getOrCreateTag().getDouble("ammo") > 0 && entity.getPersistentData().getDouble("m4fire") == 1) {
                M4autofireProcedure.execute(entity);
            }
            if (usehand.getItem() == TargetModItems.M_4.get() && usehand.getOrCreateTag().getDouble("reloading") == 0 && usehand.getOrCreateTag().getDouble("ammo") > 0 && entity.getPersistentData().getDouble("m4fire") == 3) {
                M4autofireProcedure.execute(entity);
            }
            if (usehand.getItem() == TargetModItems.M_4.get() && usehand.getOrCreateTag().getDouble("reloading") == 0 && usehand.getOrCreateTag().getDouble("ammo") > 0 && entity.getPersistentData().getDouble("m4fire") == 5) {
                M4autofireProcedure.execute(entity);
            }
            if (entity.getPersistentData().getDouble("m4fire") >= 5) {
                entity.getPersistentData().putDouble("m4fire", 0);
            }
        }
    }
}
