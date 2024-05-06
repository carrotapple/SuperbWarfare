package net.mcreator.target.procedures;

import net.mcreator.target.init.ItemRegistry;
import net.mcreator.target.network.TargetModVariables;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class PressFireProcedure {
    public static void execute(Entity entity) {
        if (entity == null)
            return;
        TracheliumfireProcedure.execute(entity);
        TaserfireProcedure.execute(entity);
        HrfireProcedure.execute(entity);
        M79fireProcedure.execute(entity);
        AbkrfireProcedure.execute(entity);
        M98bfireProcedure.execute(entity);
        DevotiongfireProcedure.execute(entity);
        RpgfireProcedure.execute(entity);
        M4fireProcedure.execute(entity);
        Aa12fireProcedure.execute(entity);
        Hk416fireProcedure.execute(entity);
        RpkfireProcedure.execute(entity);
        SksfireProcedure.execute(entity);
        KraberfireProcedure.execute(entity);
        VecfireProcedure.execute(entity);
        MinigunfireProcedure.execute(entity);
        Mk14fireProcedure.execute(entity);
        SentinelFireProcedure.execute(entity);
        M60fireProcedure.execute(entity);
        SvdfireProcedure.execute(entity);
        MarlinfireProcedure.execute(entity);
        M870fireProcedure.execute(entity);
        AKfireProcedure.execute(entity);
        if ((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).is(ItemTags.create(new ResourceLocation("target:gun")))
                && !((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() == ItemRegistry.BOCEK.get())
                && !((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() == ItemRegistry.MINIGUN.get())
                && (entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getOrCreateTag().getDouble("ammo") == 0
                && (entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getOrCreateTag().getDouble("reloading") != 1) {
            {
                Entity _ent = entity;
                if (!_ent.level().isClientSide() && _ent.getServer() != null) {
                    _ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4,
                            _ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), "playsound target:triggerclick player @s ~ ~ ~ 10 1");
                }
            }
        }
        if ((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() == ItemRegistry.MINIGUN.get()
                && (entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).rifleammo == 0) {
            {
                Entity _ent = entity;
                if (!_ent.level().isClientSide() && _ent.getServer() != null) {
                    _ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4,
                            _ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), "playsound target:triggerclick player @s ~ ~ ~ 10 1");
                }
            }
        }
        {
            boolean _setval = true;
            entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                capability.bowpullhold = _setval;
                capability.syncPlayerVariables(entity);
            });
        }
        if ((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getOrCreateTag().getDouble("ammo") == 0) {
            PlayerReloadProcedure.execute(entity);
        }
    }
}
