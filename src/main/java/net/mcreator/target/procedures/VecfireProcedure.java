package net.mcreator.target.procedures;

import net.mcreator.target.init.TargetModItems;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class VecfireProcedure {
    public static void execute(Entity entity) {
        if (entity == null) return;
        if (entity instanceof Player player && !player.isSpectator()) {
            ItemStack usehand = player.getMainHandItem();
            if (usehand.getItem() == TargetModItems.VECTOR.get()) {
                if (usehand.getOrCreateTag().getDouble("firemode") == 0) {
                    if (usehand.getOrCreateTag().getDouble("reloading") == 0 && usehand.getOrCreateTag().getDouble("ammo") > 0 && !(entity instanceof Player _plrCldCheck6 && _plrCldCheck6.getCooldowns().isOnCooldown(usehand.getItem()))) {
                        player.getCooldowns().addCooldown(usehand.getItem(), 1);
                        BulletFireNormalProcedure.execute(entity);

                        if (!entity.level().isClientSide() && entity.getServer() != null) {
                            entity.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, entity.position(), entity.getRotationVector(), entity.level() instanceof ServerLevel ? (ServerLevel) entity.level() : null, 4,
                                    entity.getName().getString(), entity.getDisplayName(), entity.level().getServer(), entity), "playsound target:vec3 player @a ~ ~ ~ 2 1");
                        }
                        usehand.getOrCreateTag().putDouble("ammo", (usehand.getOrCreateTag().getDouble("ammo") - 1));
                        usehand.getOrCreateTag().putDouble("fireanim", 2);
                    }
                } else if (usehand.getOrCreateTag().getDouble("firemode") == 1 && usehand.getOrCreateTag().getDouble("burst") == 0) {
                    usehand.getOrCreateTag().putDouble("burst", 3);
                } else if (usehand.getOrCreateTag().getDouble("firemode") == 2) {
                    entity.getPersistentData().putDouble("firing", 1);
                }
            }
        }
    }
}
