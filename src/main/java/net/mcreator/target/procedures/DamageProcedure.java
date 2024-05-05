package net.mcreator.target.procedures;

import net.mcreator.target.entity.Target1Entity;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber
public class DamageProcedure {
    @SubscribeEvent
    public static void onEntityDeath(LivingDeathEvent event) {
        if (event != null && event.getEntity() != null) {
            execute(event, event.getEntity(), event.getSource().getEntity());
        }
    }

    public static void execute(Entity entity, Entity sourceentity) {
        execute(null, entity, sourceentity);
    }

    private static void execute(@Nullable Event event, Entity entity, Entity sourceentity) {
        if (entity == null || sourceentity == null)
            return;
        if (entity instanceof Target1Entity entity1) {
            entity1.setHealth(entity1.getMaxHealth());
            {
                if (!sourceentity.level().isClientSide() && sourceentity.getServer() != null) {
                    sourceentity.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, sourceentity.position(), sourceentity.getRotationVector(), sourceentity.level() instanceof ServerLevel ? (ServerLevel) sourceentity.level() : null, 4,
                            sourceentity.getName().getString(), sourceentity.getDisplayName(), sourceentity.level().getServer(), sourceentity), "playsound target:targetdown player @s ~ ~ ~ 100 1");
                }
            }
            if (sourceentity instanceof Player _player && !_player.level().isClientSide())
                _player.displayClientMessage(Component.literal(("Target Down " + new java.text.DecimalFormat("##.#").format((entity.position()).distanceTo((sourceentity.position()))) + "M")), true);
            entity.getPersistentData().putDouble("targetdown", 201);
            if (event != null && event.isCancelable()) {
                event.setCanceled(true);
            }
        }
    }
}
