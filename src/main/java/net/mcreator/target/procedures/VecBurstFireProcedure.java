package net.mcreator.target.procedures;

import net.mcreator.target.init.TargetModItems;
import net.mcreator.target.tools.GunsTool;
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

    private static void execute(Player player) {
        ItemStack usehand;
        usehand = player.getMainHandItem();
        if (usehand.is(ItemTags.create(new ResourceLocation("target:gun")))) {
            if (usehand.getOrCreateTag().getDouble("firemode") == 1) {
                player.getPersistentData().putDouble("firing", 0);
            }
            if (usehand.getOrCreateTag().getDouble("ammo") == 0) {
                usehand.getOrCreateTag().putDouble("burst", 0);
            }
        }
        if (usehand.getItem() == TargetModItems.VECTOR.get() && usehand.getOrCreateTag().getDouble("reloading") == 0 && usehand.getOrCreateTag().getDouble("ammo") > 0
                && !(player.getCooldowns().isOnCooldown(usehand.getItem())) && usehand.getOrCreateTag().getDouble("burst") > 0) {

            player.getCooldowns().addCooldown(usehand.getItem(), usehand.getOrCreateTag().getDouble("burst") == 1 ? 5 : 1);
            usehand.getOrCreateTag().putDouble("burst", (usehand.getOrCreateTag().getDouble("burst") - 1));
            GunsTool.spawnBullet(player);
            if (!player.level().isClientSide() && player.getServer() != null) {
                player.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, player.position(), player.getRotationVector(), (ServerLevel) player.level(), 4,
                        player.getName().getString(), player.getDisplayName(), player.level().getServer(), player), "playsound target:vector_fire_1p player @a ~ ~ ~ 2 1");
                player.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, player.position(), player.getRotationVector(), (ServerLevel) player.level(), 4,
                        player.getName().getString(), player.getDisplayName(), player.level().getServer(), player), "playsound target:vector_fire_1p player @s ~ ~ ~ 4 1");
                player.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, player.position(), player.getRotationVector(), (ServerLevel) player.level(), 4,
                        player.getName().getString(), player.getDisplayName(), player.level().getServer(), player), "playsound target:vector_far player @a ~ ~ ~ 6 1");
                player.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, player.position(), player.getRotationVector(), (ServerLevel) player.level(), 4,
                        player.getName().getString(), player.getDisplayName(), player.level().getServer(), player), "playsound target:vector_veryfar player @a ~ ~ ~ 12 1");
            }
            usehand.getOrCreateTag().putDouble("fireanim", 2);
            usehand.getOrCreateTag().putDouble("ammo", (usehand.getOrCreateTag().getDouble("ammo") - 1));
        }
    }
}