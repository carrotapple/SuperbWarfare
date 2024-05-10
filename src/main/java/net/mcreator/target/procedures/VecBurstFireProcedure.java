package net.mcreator.target.procedures;

import net.mcreator.target.init.TargetModItems;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class VecBurstFireProcedure {
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            execute(event.player);
        }
    }

    private static void execute(Player entity) {
        ItemStack usehand;
        usehand = entity.getMainHandItem();
        if (usehand.is(ItemTags.create(new ResourceLocation("target:gun")))) {
            if (usehand.getOrCreateTag().getDouble("firemode") == 1) {
                entity.getPersistentData().putDouble("firing", 0);
            }
            if (usehand.getOrCreateTag().getDouble("ammo") == 0) {
                usehand.getOrCreateTag().putDouble("burst", 0);
            }
        }
        if (usehand.getItem() == TargetModItems.VECTOR.get() && usehand.getOrCreateTag().getDouble("reloading") == 0 && usehand.getOrCreateTag().getDouble("ammo") > 0
                && !(entity.getCooldowns().isOnCooldown(usehand.getItem())) && usehand.getOrCreateTag().getDouble("burst") > 0) {

            entity.getCooldowns().addCooldown(usehand.getItem(), usehand.getOrCreateTag().getDouble("burst") == 1 ? 5 : 1);
            usehand.getOrCreateTag().putDouble("burst", (usehand.getOrCreateTag().getDouble("burst") - 1));
            BulletFireNormalProcedure.execute(entity);
            if (!entity.level().isClientSide() && entity.getServer() != null) {
                entity.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, entity.position(), entity.getRotationVector(), (ServerLevel) entity.level(), 4,
                        entity.getName().getString(), entity.getDisplayName(), entity.level().getServer(), entity), "playsound target:vector_fire_1p player @a ~ ~ ~ 2 1");
                entity.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, entity.position(), entity.getRotationVector(), (ServerLevel) entity.level(), 4,
                        entity.getName().getString(), entity.getDisplayName(), entity.level().getServer(), entity), "playsound target:vector_fire_1p player @s ~ ~ ~ 4 1");
                entity.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, entity.position(), entity.getRotationVector(), (ServerLevel) entity.level(), 4,
                        entity.getName().getString(), entity.getDisplayName(), entity.level().getServer(), entity), "playsound target:vector_far player @a ~ ~ ~ 6 1");
                entity.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, entity.position(), entity.getRotationVector(), (ServerLevel) entity.level(), 4,
                        entity.getName().getString(), entity.getDisplayName(), entity.level().getServer(), entity), "playsound target:vector_veryfar player @a ~ ~ ~ 12 1");
            }
            usehand.getOrCreateTag().putDouble("fireanim", 2);
            usehand.getOrCreateTag().putDouble("ammo", (usehand.getOrCreateTag().getDouble("ammo") - 1));
        }
    }
}