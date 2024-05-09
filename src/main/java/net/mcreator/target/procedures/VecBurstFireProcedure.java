package net.mcreator.target.procedures;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.event.TickEvent;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.tags.ItemTags;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

import net.mcreator.target.init.TargetModItems;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber
public class VecBurstFireProcedure {
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
        ItemStack usehand = ItemStack.EMPTY;
        usehand = (entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY);
        if (usehand.is(ItemTags.create(new ResourceLocation("target:gun")))) {
            if (usehand.getOrCreateTag().getDouble("firemode") == 1) {
                entity.getPersistentData().putDouble("firing", 0);
            }
            if (usehand.getOrCreateTag().getDouble("ammo") == 0) {
                usehand.getOrCreateTag().putDouble("burst", 0);
            }
        }
        if (usehand.getItem() == TargetModItems.VECTOR.get() && usehand.getOrCreateTag().getDouble("reloading") == 0 && usehand.getOrCreateTag().getDouble("ammo") > 0
                && !(entity instanceof Player _plrCldCheck9 && _plrCldCheck9.getCooldowns().isOnCooldown(usehand.getItem())) && usehand.getOrCreateTag().getDouble("burst") > 0) {
            if (usehand.getOrCreateTag().getDouble("burst") == 1) {
                if (entity instanceof Player _player)
                    _player.getCooldowns().addCooldown(usehand.getItem(), 5);
            } else {
                if (entity instanceof Player _player)
                    _player.getCooldowns().addCooldown(usehand.getItem(), 1);
            }
            usehand.getOrCreateTag().putDouble("burst", (usehand.getOrCreateTag().getDouble("burst") - 1));
            BulletFireNormalProcedure.execute(entity);
            {
                if (!entity.level().isClientSide() && entity.getServer() != null) {
                    entity.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, entity.position(), entity.getRotationVector(), entity.level() instanceof ServerLevel ? (ServerLevel) entity.level() : null, 4,
                            entity.getName().getString(), entity.getDisplayName(), entity.level().getServer(), entity), "playsound target:vector_fire_1p player @a ~ ~ ~ 2 1");
                    entity.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, entity.position(), entity.getRotationVector(), entity.level() instanceof ServerLevel ? (ServerLevel) entity.level() : null, 4,
                            entity.getName().getString(), entity.getDisplayName(), entity.level().getServer(), entity), "playsound target:vector_fire_1p player @s ~ ~ ~ 4 1");
                    entity.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, entity.position(), entity.getRotationVector(), entity.level() instanceof ServerLevel ? (ServerLevel) entity.level() : null, 4,
                            entity.getName().getString(), entity.getDisplayName(), entity.level().getServer(), entity), "playsound target:vector_far player @a ~ ~ ~ 6 1");
                    entity.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, entity.position(), entity.getRotationVector(), entity.level() instanceof ServerLevel ? (ServerLevel) entity.level() : null, 4,
                            entity.getName().getString(), entity.getDisplayName(), entity.level().getServer(), entity), "playsound target:vector_veryfar player @a ~ ~ ~ 12 1");
                }
            }
            usehand.getOrCreateTag().putDouble("fireanim", 2);
            usehand.getOrCreateTag().putDouble("ammo", (usehand.getOrCreateTag().getDouble("ammo") - 1));
        }
    }
}

