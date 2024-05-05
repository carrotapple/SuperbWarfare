package net.mcreator.target.procedures;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber
public class DistantRangeProcedure {
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
        if ((entity instanceof LivingEntity _entUseItem0 ? _entUseItem0.getUseItem() : ItemStack.EMPTY).getItem() == Items.SPYGLASS) {
            if ((entity.position()).distanceTo((Vec3
                    .atLowerCornerOf(entity.level().clip(new ClipContext(entity.getEyePosition(), entity.getEyePosition().add(entity.getLookAngle().scale(1024)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, entity)).getBlockPos()))) <= 512) {
                if (entity instanceof Player _player && !_player.level().isClientSide())
                    _player.displayClientMessage(
                            Component.literal((new java.text.DecimalFormat("##.#")
                                    .format((entity.position()).distanceTo((Vec3.atLowerCornerOf(
                                            entity.level().clip(new ClipContext(entity.getEyePosition(), entity.getEyePosition().add(entity.getLookAngle().scale(768)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, entity)).getBlockPos()))))
                                    + "M")),
                            true);
            } else {
                if (entity instanceof Player _player && !_player.level().isClientSide())
                    _player.displayClientMessage(Component.literal("---M"), true);
            }
        }
    }
}
