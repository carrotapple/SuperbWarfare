package net.mcreator.target.procedures;

import net.mcreator.target.init.TargetModItems;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.ItemTags;
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
                Entity _ent = entity;
                if (!_ent.level().isClientSide() && _ent.getServer() != null) {
                    _ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4,
                            _ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), "playsound target:vec3 player @a ~ ~ ~ 2 1");
                }
            }
            usehand.getOrCreateTag().putDouble("fireanim", 2);
            usehand.getOrCreateTag().putDouble("ammo", (usehand.getOrCreateTag().getDouble("ammo") - 1));
        }
    }
}
