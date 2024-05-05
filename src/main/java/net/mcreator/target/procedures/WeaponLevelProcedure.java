package net.mcreator.target.procedures;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber
public class WeaponLevelProcedure {
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
        ItemStack stack = ItemStack.EMPTY;
        stack = (entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY);
        if (stack.is(ItemTags.create(new ResourceLocation("target:gun")))) {
            if (stack.getOrCreateTag().getDouble("level") == 0) {
                stack.getOrCreateTag().putDouble("exp2", 20);
            } else {
                stack.getOrCreateTag().putDouble("exp2", (stack.getOrCreateTag().getDouble("exp1") + stack.getOrCreateTag().getDouble("level") * 500));
            }
            if (stack.getOrCreateTag().getDouble("damagetotal") >= stack.getOrCreateTag().getDouble("exp2")) {
                stack.getOrCreateTag().putDouble("exp1", (stack.getOrCreateTag().getDouble("exp2")));
                stack.getOrCreateTag().putDouble("level", (stack.getOrCreateTag().getDouble("level") + 1));
            }
            stack.getOrCreateTag().putDouble("damagenow", (stack.getOrCreateTag().getDouble("damagetotal") - stack.getOrCreateTag().getDouble("exp1")));
            stack.getOrCreateTag().putDouble("damageneed", (stack.getOrCreateTag().getDouble("exp2") - stack.getOrCreateTag().getDouble("exp1")));
        }
    }
}
