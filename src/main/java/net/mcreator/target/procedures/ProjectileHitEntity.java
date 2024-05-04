
package net.mcreator.target.procedures;

import net.mcreator.target.init.TargetModItems;
import net.mcreator.target.network.TargetModVariables;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.Registries;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

public class ProjectileHitEntity {
    public static void execute(LevelAccessor world, Entity entity, Entity immediatesourceentity, Entity sourceentity) {
        if (entity == null || sourceentity == null)
            return;
        {
            Entity _ent = sourceentity;
            if (!_ent.level().isClientSide() && _ent.getServer() != null) {
                _ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4,
                        _ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), "playsound target:indication voice @s ~ ~ ~ 1 1");
            }
        }
        double _setval = 25;
        sourceentity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
            capability.hitind = _setval;
            capability.syncPlayerVariables(sourceentity);
        });

        if ((sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() == TargetModItems.BOCEK.get()) {
            entity.hurt(new DamageSource(world.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation("target:gunfire"))), immediatesourceentity),
                    (0.2f * (float) ((sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getOrCreateTag().getDouble("speed"))) * (float) ((sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getOrCreateTag().getDouble("damageadd")));
        } else {
            entity.hurt(new DamageSource(world.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation("target:gunfire"))), immediatesourceentity),
                    ((float) ((sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getOrCreateTag().getDouble("adddamage")) + (float) ((sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getOrCreateTag().getDouble("damage"))) * (float) ((sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getOrCreateTag().getDouble("damageadd")));
        }
        immediatesourceentity.discard();

    }
}

