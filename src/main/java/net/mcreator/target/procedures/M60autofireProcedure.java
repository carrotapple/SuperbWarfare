package net.mcreator.target.procedures;

import net.mcreator.target.init.ItemRegistry;
import net.mcreator.target.network.TargetModVariables;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber
public class M60autofireProcedure {
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
            if (usehand.getItem() == ItemRegistry.M_60.get() && usehand.getOrCreateTag().getDouble("reloading") == 0 && usehand.getOrCreateTag().getDouble("ammo") > 0
                    && !(entity instanceof Player _plrCldCheck5 && _plrCldCheck5.getCooldowns().isOnCooldown(usehand.getItem()))) {
                if (usehand.getOrCreateTag().getDouble("animindex") == 1) {
                    usehand.getOrCreateTag().putDouble("animindex", 0);
                } else {
                    usehand.getOrCreateTag().putDouble("animindex", 1);
                }
                if (entity instanceof Player _player)
                    _player.getCooldowns().addCooldown(usehand.getItem(), 2);
                BulletFireNormalProcedure.execute(entity);
                {
                    if (!entity.level().isClientSide() && entity.getServer() != null) {
                        entity.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, entity.position(), entity.getRotationVector(), entity.level() instanceof ServerLevel ? (ServerLevel) entity.level() : null, 4,
                                entity.getName().getString(), entity.getDisplayName(), entity.level().getServer(), entity), "playsound target:m60fire player @a ~ ~ ~ 4 1");
                    }
                }
                usehand.getOrCreateTag().putDouble("ammo", (usehand.getOrCreateTag().getDouble("ammo") - 1));
                usehand.getOrCreateTag().putDouble("empty", 1);
                usehand.getOrCreateTag().putDouble("fireanim", 2);
                {
                    double _setval = 1;
                    entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                        capability.firing = _setval;
                        capability.syncPlayerVariables(entity);
                    });
                }
            }
        }
    }
}
