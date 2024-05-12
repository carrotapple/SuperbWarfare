package net.mcreator.target.event;

import net.mcreator.target.network.TargetModVariables;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class LivingEntityEventHandler {
    @SubscribeEvent
    public static void onEntityAttacked(LivingHurtEvent event) {
        renderDamageIndicator(event);
    }

    private static void renderDamageIndicator(LivingHurtEvent event) {
        if (event == null || event.getEntity() == null) return;
        var damagesource = event.getSource();
        var sourceEntity = event.getEntity();

        if (damagesource == null || sourceEntity == null) return;

        if (sourceEntity instanceof Player && (damagesource.is(DamageTypes.EXPLOSION) || damagesource.is(DamageTypes.PLAYER_EXPLOSION) || damagesource.is(ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation("target:mine"))))) {
            if (sourceEntity.getServer() != null) {
                // TODO 修改为正确的音效播放方法
                sourceEntity.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, sourceEntity.position(), sourceEntity.getRotationVector(), sourceEntity.level() instanceof ServerLevel ? (ServerLevel) sourceEntity.level() : null, 4,
                        sourceEntity.getName().getString(), sourceEntity.getDisplayName(), sourceEntity.level().getServer(), sourceEntity), "playsound target:indication voice @a ~ ~ ~ 1 1");
            }
            sourceEntity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                capability.hitIndicator = 25;
                capability.syncPlayerVariables(sourceEntity);
            });
        }
    }
}
